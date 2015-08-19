package level;

import java.awt.Color;

import enemy.Enemy_Heliboy;

public class TileHeliboy extends Tile {
	public final static Color color = new Color(255, 0, 0);

	public TileHeliboy() {
		super(color, TileType.ENEMY, new Enemy_Heliboy());
	}

}
