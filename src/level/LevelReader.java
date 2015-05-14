package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import core.MainClass;
import enemy.Enemy_Heliboy;

/**
 * Reads data from image and creates level according to it.
 * @author Petr
 *
 */
public class LevelReader {
	private static BufferedImage level;
	private static ArrayList<Tile> tileTypes = new ArrayList<Tile>();
	private static ArrayList<Tile> allTiles = new ArrayList<Tile>();
	private String levelName;
	
	public LevelReader(String levelName) {
		this.levelName = levelName;
	}

	public void init() {
		// TileTypes array initialization
		tileTypes.add(new TileBrick());
		tileTypes.add(new TileGrass());
		tileTypes.add(new TileSpawn());
		tileTypes.add(new TileHeliboy());
		
		this.readImage();
	}
	
	/**
	 * Reads image RGB values.
	 */
	private void readImage() {
		level = MainClass.getImage("data/level/" + levelName + ".png");
		
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {
				int pixel = level.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				this.setAllTiles(x, y, new Color(red, green, blue));
				
			}
		}
	}

	/**
	 * Sets allTiles arraylist.
	 * @param x
	 * @param y
	 * @param color
	 */
	private void setAllTiles(int x, int y, Color color) { 
		if(!color.equals(Color.white)) {
			if(color.equals(TileSpawn.color)) {
				TileSpawn.x = x*TileSpawn.getWidth();
				TileSpawn.y = y*TileSpawn.getHeight();
			} else if (color.equals(TileHeliboy.color)) {
				new Enemy_Heliboy().add(x*TileSpawn.getWidth(), y*TileSpawn.getHeight());
			}
			NotFound: {
				// Projde všechny typy
				for(Tile e : tileTypes) {
					if(color.equals(e.getColor())) {
						allTiles.add((Tile) e.clone());
						e.setPosition(x, y);
						break NotFound;
					} 
				}
				System.out.println("Unknown Tile, color: " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
			}
		} 
	}
	
	/**
	 * Updates tile movement and removal.
	 */
	public void update() {
		for(int i = 0; i < allTiles.size(); i++) {
			allTiles.get(i).update();
			
			// Removes tiles that player already went past.
			/*if(MainClass.getPlayer().getCenterX() - MainClass.getPlayer().getBackgroundStartMove() - Tile.getWidth() > allTiles.get(i).getX()) {
				allTiles.remove(i);
			}*/
		}
	}

	public void paintTiles(Graphics g) {
		for(Tile i : allTiles) {
			g.drawImage(i.getTexture(), i.getX(), i.getY(), null);
		}
	}

	public static ArrayList<Tile> getAllTiles() {
		return allTiles;
	}
}
