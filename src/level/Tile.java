package level;

import java.awt.Color;
import java.awt.Image;

public class Tile implements Cloneable {
	private int x;
	private int y;
	private int speedX;
	private Image texture;
	private Color color;
	
	private final static int width = 32;
	private final static int height = 32;
	
	public Tile(Image texture, Color color) {
		this.texture = texture;
		this.color = color;
	}
	
	public void setPosition(int x, int y) {
		this.x = x*width;
		this.y = y*height;
	}
	
	public void update() {
		this.x += this.speedX;
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

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	
}
