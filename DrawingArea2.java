
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
import javax.swing.UIManager;

public class DrawingArea2 extends JComponent{
	private GameClient client;
	private Image image;
	private Graphics2D graphicsObject;
	private float currBrush = 3.0f;
	
	private int oldX, oldY, newX, newY;
	private float brushSize = 3.0f;
	private Color brushColor = Color.black;

	private Boolean playing = true;

	public DrawingArea2(GameClient client){
		this.client = client;
		setDoubleBuffered(false);
		
				addMouseListener(new MouseAdapter(){
					public void mousePressed(MouseEvent e){
						
						oldX = e.getX();
						oldY = e.getY();
						newX = oldX;
						newY = oldY;
						
						if(playing == true){
							client.sendGameData("PLAYER " +client.getPlayerName()+" "+oldX+" "+oldY+" "+newX+" "+newY+" "+brushSize+" "+brushColor);
							graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
							graphicsObject.drawLine(oldX, oldY, oldX, oldY);
						} 						
							
						repaint();
												
					}
				});
				addMouseMotionListener(new MouseMotionListener(){

					@Override
					public void mouseDragged(MouseEvent e) {
						newX = e.getX();
						newY = e.getY();
						//System.out.println("brush color: " + brushColor);
						
						if(graphicsObject != null){

							if(playing == true){
							client.sendGameData("PLAYERA " +client.getPlayerName()+" "+oldX+" "+oldY+" "+newX+" "+newY+" "+brushSize+" "+brushColor);
							graphicsObject.setStroke(new BasicStroke(brushSize,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
							graphicsObject.drawLine(oldX, oldY, newX, newY);
							}
							repaint();
							oldX = newX;
							oldY = newY;
						}
					}
					@Override
					public void mouseMoved(MouseEvent e) {}	
				});
			
			
	
	}
	
	//sets up the drawing area
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
		repaint();
	}
	
	public void setBrushToRed(){
		brushSize = currBrush;
		brushColor = Color.red;
		graphicsObject.setPaint(Color.red);
	}

	public void setBrushToBlack(){
		brushSize = currBrush;
		brushColor = Color.black;
		graphicsObject.setPaint(Color.black);
	}

	public void setBrushToBlue(){
		brushSize = currBrush;
		brushColor = Color.blue;
		graphicsObject.setPaint(Color.blue);
	}

	public void setBrushToGreen(){
		brushSize = currBrush;
		brushColor = Color.green;
		graphicsObject.setPaint(Color.green);
	}

	public void setBrushToYellow(){
		brushSize = currBrush;
		brushColor = Color.yellow;
		graphicsObject.setPaint(Color.yellow);
	}
	
	public void setBrushToMagenta(){
		brushSize = currBrush;
		brushColor = Color.magenta;
		graphicsObject.setPaint(Color.magenta);
	}

	public void eraseDrawing(){
		brushSize = 20.0f;
		brushColor = Color.white;
		graphicsObject.setPaint(Color.white);
		//System.out.println("White brush: " + brushColor);
	}

	/// SET PLAYER ///
	public void setEnableDrawingArea(){
		//code to enable drawing area
		playing = true;
	}

	public void setDisableDrawingArea(){
		//code to disable drawing area
		playing = false;
	}

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
