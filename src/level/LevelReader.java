package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import core.MainClass;
import level.tileTypes.TileBrick;
import level.tileTypes.TileGrass;
import level.tileTypes.TileHeliboy;
import level.tileTypes.TileSpawn;
import level.tileTypes.TileWin;

/**
 * Reads data from image and creates level according to it.
 * @author Petr
 *
 */
public class LevelReader {
	private static BufferedImage level;
	/** Stores all subclasses of Tile (TileGrass). */
	private static ArrayList<Tile> tileClasses;
	/** Stores all tiles in the map. */
	private static ArrayList<Tile> allTiles;
	private String levelName;
	
	public LevelReader(String levelName) {
		this.levelName = levelName;
	}

	public void init() {
		tileClasses = new ArrayList<Tile>();
		allTiles = new ArrayList<Tile>();
		
		if(!tileClasses.isEmpty()) tileClasses.clear();
		if(!allTiles.isEmpty()) allTiles.clear();
		
		// TileTypes array initialization
		tileClasses.add(new TileBrick());
		tileClasses.add(new TileGrass());
		tileClasses.add(new TileSpawn());
		tileClasses.add(new TileHeliboy());
		tileClasses.add(new TileWin());
		
		this.readImage();
	}
	
	/**
	 * Reads image RGB values.
	 */
	private void readImage() {
		level = MainClass.getImage("\\data\\level\\" + levelName);
				
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {				
				int pixelColor = level.getRGB(x, y);
				
				Color tmp = new Color(pixelColor);
				int red = tmp.getRed();
				int green = tmp.getGreen();
				int blue = tmp.getBlue();
				
				this.setAllTiles(x, y, new Color(red, green, blue));
				
			}
		}
	}

	/**
	 * Sets up allTiles arraylist.
	 * @param x
	 * @param y
	 * @param color
	 */
	private void setAllTiles(int x, int y, Color color) { 
		if(!color.equals(Color.white)) {
			NotFound: {
				// Projde všechny typy
				for(Tile e : tileClasses) {
					if(color.equals(e.getColor())) {
						if(e.getTileType() == TileType.TERRAIN || e.getTileType() == TileType.USABLE_TERRAIN) {
							e.setPosition(x, y);
							allTiles.add((Tile) e.clone());
						} else if(e.getTileType() == TileType.ENEMY) {
							e.getEnemyType().add(x*Tile.getWidth(), y*Tile.getHeight());
							
						} else if(e.getTileType() == TileType.PLAYER) {
							TileSpawn.x = x*Tile.getWidth();
							TileSpawn.y = y*Tile.getHeight();
						}
						break NotFound;
					} 
				}
				System.out.println("Unknown Tile, color: " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
				System.out.println("Position: " + x + ", " + y);
			}
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

	public static ArrayList<Tile> getTileClasses() {
		return tileClasses;
	}
}
