package level.tileTypes;

import java.awt.Color;

import enemy.Enemy_Heliboy;
import level.Tile;
import level.TileType;

public class TileHeliboy extends Tile {
	public final static Color color = new Color(255, 0, 0);

	public TileHeliboy() {
		super(color, TileType.ENEMY, new Enemy_Heliboy());
	}

}
