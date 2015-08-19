package level;

import java.awt.Color;
import java.awt.Image;
import java.awt.Rectangle;

import enemy.Enemy;

public class Tile implements Cloneable {
	private int x;
	private int y;
	private Image texture;
	private Color color;
	private TileType tileType;
	private Enemy enemyType;
	
	private Rectangle recCollision = new Rectangle();
	
	
	private final static int width = 32;
	private final static int height = 32;
		
	/**
	 * 
	 * @param texture is a texture of the tile.
	 * @param color is the identificator of the tile.
	 * @param tileType is used to distinguish between enemy, terrain and player.
	 */
	public Tile(Image texture, Color color, TileType tileType) {
		this.texture = texture;
		this.color = color;
		this.tileType = tileType;
	}
	
	/**
	 * 
	 * @param color is the identificator of the tile.
	 * @param tileType is used to distinguish between enemy, terrain and player.
	 * @param enemyType is the object of Enemy which is tied to this tile.
	 */
	public Tile(Color color, TileType tileType, Enemy enemyType) {
		this(null, color, tileType);
		this.enemyType = enemyType;
	}
	
	public void setPosition(int x, int y) {
		this.x = x*width;
		this.y = y*height;
		this.recCollision = new Rectangle(this.x, this.y, Tile.width, Tile.height);
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

	public TileType getTileType() {
		return tileType;
	}

	public Enemy getEnemyType() {
		return enemyType;
	}
	
}
