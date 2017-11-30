
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class DrawingArea2 extends JComponent{
	private GameClient client;
	private Image image;
	private Graphics2D graphicsObject;
	
	private int oldX, oldY, newX, newY;
	private float brushSize = 3.0f;
	
	public DrawingArea2(GameClient client){
		this.client = client;
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				oldX = e.getX();
				oldY = e.getY();
				graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				client.broadCastCoordinates(oldX, oldY);
				graphicsObject.drawLine(oldX, oldY, oldX, oldY);
				
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				newX = e.getX();
				newY = e.getY();
				
				if(graphicsObject != null){

					graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
					client.broadCastCoordinates(newX, newY);
					graphicsObject.drawLine(oldX, oldY, newX, newY);

					repaint();
					oldX = newX;
					oldY = newY;
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) {}	
		});
		
	}
	
	protected void paintComponent(Graphics g){
		if(image == null){
			image = createImage(getSize().width,getSize().height);
			graphicsObject = (Graphics2D) image.getGraphics();
			graphicsObject.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		
			clear();
		}
		g.drawImage(image,0,0,null);
	}

	private void clear() {
		graphicsObject.setPaint(Color.white);
		graphicsObject.fillRect(0,0,getSize().width,getSize().height);
		graphicsObject.setPaint(Color.black);
		repaint();
	}
	
	
}