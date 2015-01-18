package level;

import java.awt.Color;
import java.awt.Image;

import core.MainClass;

public class TileGrass extends Tile {
	public final static Color color = new Color(0, 255, 0);
	public final static Image texture = MainClass.getImage("data/grass.png");
	
	public TileGrass(int x, int y) {
		super(x, y, texture, color);
	}
}
