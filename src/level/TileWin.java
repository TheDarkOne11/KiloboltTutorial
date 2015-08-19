package level;

import java.awt.Color;
import java.awt.Image;

import core.MainClass;

public class TileWin extends Tile {
	public final static Color color = new Color(255, 200, 0);
	public final static Image texture = MainClass.getImage("\\data\\win.png");
	
	public TileWin() {
		super(texture, color, TileType.TERRAIN);
	}
}
