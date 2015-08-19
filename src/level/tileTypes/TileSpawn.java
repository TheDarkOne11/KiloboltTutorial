package level.tileTypes;

import java.awt.Color;
import java.awt.Image;

import level.Tile;
import level.TileType;

public class TileSpawn extends Tile {
	public static int x, y;
	public final static Color color = new Color(0, 0, 255);
	public final static Image texture = null;
	
	public TileSpawn() {
		super(texture, color, TileType.PLAYER);
	}
	
	
}
