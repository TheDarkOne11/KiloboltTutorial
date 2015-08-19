package level;

import java.awt.Color;
import java.awt.Image;

public abstract class TileUsable extends Tile {

	public TileUsable(Image texture, Color color) {
		super(texture, color, TileType.USABLE_TERRAIN);
	}
	
	public abstract void action();

}
