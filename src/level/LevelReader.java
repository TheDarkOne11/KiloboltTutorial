package level;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import core.MainClass;

/**
 * Reads data from image and creates level according to it.
 * @author Petr
 *
 */
public class LevelReader {
	private static BufferedImage level;
	/** Stores all subclasses of Tile (TileGrass). */
	private static ArrayList<Tile> tileClasses = new ArrayList<Tile>();
	/** Stores all tiles in the map. */
	private static ArrayList<Tile> allTiles = new ArrayList<Tile>();
	private String levelName;
	
	public LevelReader(String levelName) {
		this.levelName = levelName;
	}

	public void init() {
		// TileTypes array initialization
		tileClasses.add(new TileBrick());
		tileClasses.add(new TileGrass());
		tileClasses.add(new TileSpawn());
		tileClasses.add(new TileHeliboy());
		
		this.readImage();
	}
	
	/**
	 * Reads image RGB values.
	 */
	private void readImage() {
		level = MainClass.getImage("\\data\\level\\" + levelName + ".png");
		
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
			NotFound: {
				// Projde všechny typy
				for(Tile e : tileClasses) {
					if(color.equals(e.getColor())) {
						if(e.getTileType() == TileType.TERRAIN) {
							allTiles.add((Tile) e.clone());
							e.setPosition(x, y);
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
