package level.tileTypes;

import java.awt.Color;
import java.awt.Image;

import core.MainClass;
import level.Tile;
import level.TileType;

public class TileBrick extends Tile {
	public final static Color color = new Color(255, 106, 0);
	public final static Image texture = MainClass.getImage("\\data\\brick.png");
	
	public TileBrick() {
		super(texture, color, TileType.TERRAIN);
	}
}
