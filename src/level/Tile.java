package level;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

import core.MainClass;

public class Tile implements Cloneable {
	private int x;
	private int y;
	private Image texture;
	private Color color;
	
	private Rectangle recCollision = new Rectangle();
	
	private final static int width = 32;
	private final static int height = 32;
		
	public Tile(Image texture, Color color) {
		this.texture = texture;
		this.color = color;
	}
	
	public void setPosition(int x, int y) {
		this.x = x*width;
		this.y = y*height;
		this.recCollision = new Rectangle(this.x, this.y, Tile.width, Tile.height);
	}
	
	public void update() {
		this.x += MainClass.getBg1().getSpeedX();
		this.recCollision.setRect(this.x, this.y, Tile.width, Tile.height);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Image getTexture() {
		return texture;
	}

	public Color getColor() {
		return color;
	}

	public void setTexture(Image texture) {
		this.texture = texture;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Rectangle getCollisionRec() {
		return recCollision;
	}

	public Rectangle getRecCollision() {
		return recCollision;
	}
	
}
