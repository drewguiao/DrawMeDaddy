
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.JComponent;

public class DrawingArea extends JComponent{
	
	private Image image;
	private Graphics2D graphicsObject;
	private float brushSize = 3.0f;
	private Color brushColor = Color.BLACK;
	public DrawingArea(){
		setDoubleBuffered(false);
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
		graphicsObject.setPaint(Color.WHITE);
		graphicsObject.fillRect(0, 0, getSize().width, getSize().height);
		graphicsObject.setPaint(Color.BLACK);
		repaint();
	}
	
	public void setBrushToRed(){
		this.brushColor = Color.RED;
		graphicsObject.setPaint(Color.RED);
	}
	
	public void setBrushToBlack(){
		this.brushColor = Color.BLACK;
		graphicsObject.setPaint(Color.BLACK);
	}
	
	public void setBrushSmall(){
		this.brushSize = 3.0f;
	}
	public void setBrushMedium(){
		this.brushSize = 5.0f;
	}
	public void setBrushLarge(){
		this.brushSize = 10.0f;
	}

	public Graphics2D getGraphicsObject(){
		return this.graphicsObject;
	}

	public Image getBuiltInImage(){
		return this.image;
	}

	public float getBrushSize(){
		return this.brushSize;
	}

	public Color getBrushColor(){
		return this.brushColor;
	}
}
