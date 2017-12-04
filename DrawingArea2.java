
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
	private float currBrush = 3.0f;
	
	private int oldX, oldY, newX, newY;
	private float brushSize = 3.0f;
	private Color brushColor = Color.BLACK;
	public DrawingArea2(GameClient client){
		this.client = client;
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				
				oldX = e.getX();
				oldY = e.getY();
				newX = oldX;
				newY = oldY;
				client.sendGameData("COORDINATE "+oldX+" "+oldY+" "+newX+" "+newY+" "+brushSize+" "+brushColor);
				graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
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

					client.sendGameData("COORDINATEB "+oldX+" "+oldY+" "+newX+" "+newY+" "+brushSize+" "+brushColor);
					graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
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

	public Graphics2D getGraphicsObject(){
		return this.graphicsObject;
	}

	public Image getImage(){
		return this.image;
	}
	
	public void clear() {
		graphicsObject.setPaint(Color.white);
		graphicsObject.fillRect(0,0,getSize().width,getSize().height);
		graphicsObject.setPaint(Color.black);
		newX = oldX;
		newY = oldY;
		repaint();
	}
	
	public void setBrushToRed(){
		//brushSize = currBrush;
		graphicsObject.setPaint(Color.red);
	}

	public void setBrushToBlack(){
		//brushSize = currBrush;
		graphicsObject.setPaint(Color.black);
	}

	public void setBrushToBlue(){
		//brushSize = currBrush;
		graphicsObject.setPaint(Color.blue);
	}

	public void setBrushToGreen(){
		//brushSize = currBrush;
		graphicsObject.setPaint(Color.green);
	}

	public void setBrushToYellow(){
		//brushSize = currBrush;
		graphicsObject.setPaint(Color.yellow);
	}
	
	public void setBrushToMagenta(){
		//brushSize = currBrush;
		graphicsObject.setPaint(Color.magenta);
	}

	// public void eraseDrawing(){
	// 	graphicsObject.setPaint(Color.white);
	// 	brushSize = 20.0f;
	// 	addMouseListener(new MouseAdapter(){
	// 		public void mousePressed(MouseEvent e){
	// 			oldX = e.getX();
	// 			oldY = e.getY();
	// 			graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
	// 			sendGameData();
	// 			graphicsObject.drawLine(oldX, oldY, oldX, oldY);
				
	// 			repaint();
	// 		}
	// 	});
	// }

	//// BRUSH SIZES SETTINGS ////

	public void setBrushSmall(){
		brushSize = 3.0f;
		currBrush = brushSize;
	}
	public void setBrushMedium(){
		brushSize = 5.0f;
		currBrush = brushSize;
	}
	public void setBrushLarge(){
		brushSize = 10.0f;
		currBrush = brushSize;
  }
}
