package level;

import java.awt.Color;
import java.awt.Image;

import core.MainClass;

public class TileBrick extends Tile {
	public int x, y;
	public final static Color color = new Color(255, 106, 0);
	public final static Image texture = MainClass.getImage("data/brick.png");
	
	public TileBrick() {
		super(texture, color, TileType.TERRAIN);
	}
}
