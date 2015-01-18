package level;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

public abstract class Tile {
	private int x, y;
	private Image texture;
	private Color color;
	private static ArrayList<Tile> tileTypes = new ArrayList<Tile>();
	private static ArrayList<Tile> allTiles = new ArrayList<Tile>();
	
	private final static int width = 64;
	private final static int height = 64;
	
	public Tile(int x, int y, Image texture, Color color) {
		this.x = x;
		this.y = y;
		this.texture = texture;
		this.color = color;
	}

	public static void setTiles(int x, int y, Color color) { 
		if(!color.equals(Color.white)) {
			if(color.equals(TileGrass.color)) {
				allTiles.add(new TileGrass(x*width, y*height));
				//TODO Je tøeba je ještì namalovat, zjistit správnost umístìní v allTiles.
				//TODO Poté si ujasnit, které metody mají být v Tile a které v nezávislém LevelReaderu.
			} 
			
			else {
				System.out.println("Unknown Tile, color: " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
			}
		}
	}
	
	public static void addTiles() {
		
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
	
}
