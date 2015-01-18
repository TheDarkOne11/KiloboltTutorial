package level;

import java.awt.Color;
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
	private static ArrayList<Tile> tileTypes = new ArrayList<Tile>();
	private static ArrayList<Tile> allTiles = new ArrayList<Tile>();
	
	public static void init() {
		
	}
	
	/**
	 * Reads image RGB values.
	 */
	public static void readImage() {
		level = MainClass.getImage("data/level/demo.png");
		
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {
				int pixel = level.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				Tile.setTiles(x, y, new Color(red, green, blue));
				
			}
		}
	}
	
	public static void setTileTypes(ArrayList<Tile> tileTypes) {
		
	}
	
	/**
	 * Reads values from levelFile.
	 */
	public static void readLevelFile() {
		
	}
	
	/**
	 * Sets levelFile. 
	 * LevelFile is an alternative of image. Created after readImage values are read.
	 * LevelFile has values from Image and last edit time of Image.
	 */
	public static void setLevelFile() {
		
	}
}
