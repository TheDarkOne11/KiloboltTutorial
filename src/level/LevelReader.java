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
	private static ArrayList<Tile> tileTypes = new ArrayList<Tile>();
	private static ArrayList<Tile> allTiles = new ArrayList<Tile>();
	
	public void init() {
		
	}
	
	/**
	 * Reads image RGB values.
	 */
	public void readImage() {
		level = MainClass.getImage("data/level/demo.png");
		
		for(int x = 0; x < level.getWidth(); x++) {
			for(int y = 0; y < level.getHeight(); y++) {
				int pixel = level.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				this.setTiles(x, y, new Color(red, green, blue));
				
			}
		}
	}
	
	/**
	 * Reads values from levelFile.
	 */
	public void readLevelFile() {
		
	}
	
	/**
	 * Sets levelFile. 
	 * LevelFile is an alternative of image. Created after readImage values are read.
	 * LevelFile has values from Image and last edit time of Image.
	 */
	public void setLevelFile() {
		
	}

	public void setTiles(int x, int y, Color color) { 
		if(!color.equals(Color.white)) {
			if(color.equals(TileGrass.color)) {
				allTiles.add(new TileGrass(x, y));
			} 
			
			else {
				System.out.println("Unknown Tile, color: " + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue());
			}
		}
	}
	
	public void update() {
		for(int i = 0; i < allTiles.size(); i++) {
			allTiles.get(i).setSpeedX(MainClass.getBg1().getSpeedX());
			allTiles.get(i).update();
			
			// Removes tiles that player already went past.
			if(MainClass.getPlayer().getCenterX() - MainClass.getPlayer().getBackgroundStartMove() - Tile.getWidth() > allTiles.get(i).getX()) {
				allTiles.remove(i);
			}
		}
	}

	public void paintTiles(Graphics g) {
		for(Tile i : allTiles) {
			g.drawImage(i.getTexture(), i.getX(), i.getY(), null);
		}
	}

	public void setTileTypes(ArrayList<Tile> tileTypes) {
		
	}
}
