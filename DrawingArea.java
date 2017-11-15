
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JComponent;


public class DrawingArea extends JComponent{
	private Image image;
	private Graphics2D graphicsObject;
	private int currentX, currentY, previousX, previousY;
	private float brushSize = 3.0f;
	
	public DrawingArea(){
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){

			public void mousePressed(MouseEvent e) {
				previousX = e.getX();
				previousY = e.getY();

				graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
				graphicsObject.drawLine(previousX, previousY, previousX, previousY);
				
				repaint();
			}
			
			
		});

		addMouseMotionListener(new MouseMotionAdapter(){

			public void mouseDragged(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				
				if(graphicsObject != null){

					graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
					graphicsObject.drawLine(previousX, previousY, currentX, currentY);
					
//					graphicsObject.fillOval(currentX, currentY, brushSize, brushSize);
					repaint();
					previousX = currentX;
					previousY = currentY;
				}
			}
			
			
		});
	}
	
	protected void paintComponent(Graphics g){
		if(image == null){
			image = createImage(getSize().width, getSize().height);
			graphicsObject = (Graphics2D) image.getGraphics();
			graphicsObject.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
		
			clear();
		}
		
		g.drawImage(image, 0, 0, null);
	}
	
	public void clear(){
		graphicsObject.setPaint(Color.white);
		graphicsObject.fillRect(0,0,getSize().width,getSize().height);
		graphicsObject.setPaint(Color.black);
		repaint();
	}
	
	
	
////////////////////////////	ADD COLORS HERE //////////////////////////////
	public void setBrushToRed(){
		graphicsObject.setPaint(Color.red);
	}

	public void setBrushToBlack(){
		graphicsObject.setPaint(Color.black);
	}
	
///////////////////////////// SET BRUSH SIZE HERE /////////////////////////////
	public void setBrushSmall(){
		brushSize = 3.0f;
	}
	public void setBrushMedium(){
		brushSize = 5.0f;
	}
	public void setBrushLarge(){
		brushSize = 10.0f;
	}
}
