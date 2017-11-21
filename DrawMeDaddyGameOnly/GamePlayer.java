
import java.awt.Color;
import java.net.InetAddress;

public class GamePlayer {
	private InetAddress address;
	private int port;
	private String name;
	private int x,y,newX,newY;
	private float brushSize;
	private Color brushColor;
	
	public GamePlayer(String name, InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.name = name;
	}
	
	public InetAddress getAddress(){
		return address;
	}

	/**
	 * Returns the port number
	 * @return
	 */
	public int getPort(){
		return port;
	}

	/**
	 * Returns the name of the player
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Sets the X coordinate of the player
	 * @param x
	 */
	public void setX(int x){
		this.x=x;
	}
	
	
	/**
	 * Returns the X coordinate of the player
	 * @return
	 */
	public int getX(){
		return x;
	}
	
	
	/**
	 * Returns the y coordinate of the player
	 * @return
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Sets the y coordinate of the player
	 * @param y
	 */
	public void setY(int y){
		this.y=y;		
	}

	public void setNewX(int newX){
		this.newX = newX;
	}
	public void setNewY(int newY){
		this.newY = newY;
	}

	public int getNewX(){
		return this.newX;
	}
	public int getNewY(){
		return this.newY;
	}

	/**
	 * String representation. used for transfer over the network
	 */
	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=x+" ";
		retval+=y+" ";
		retval+=newX+" ";
		retval+=newY+" ";
		retval+=brushSize+" ";
		retval+=brushColor+" ";
		return retval;
	}

	public Color getBrushColor() {
		return brushColor;
	}

	public void setBrushColor(Color brushColor) {
		this.brushColor = brushColor;
	}

	public float getBrushSize() {
		return brushSize;
	}

	public void setBrushSize(float brushSize) {
		this.brushSize = brushSize;
	}
}