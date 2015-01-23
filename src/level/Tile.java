package level;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import core.MainClass;

public abstract class Tile {
	private int x;
	private int y;
	private int speedX;
	private Image texture;
	private Color color;
	
	private final static int width = 32;
	private final static int height = 32;
	
	public Tile(int x, int y, Image texture, Color color) {
		this.x = x*width;
		this.y = y*height;
		this.texture = texture;
		this.color = color;
	}
	
	public void update() {
		this.x += this.speedX;
		
		if(MainClass.getPlayer().getCenterX() > this.x) {
			
		}
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
