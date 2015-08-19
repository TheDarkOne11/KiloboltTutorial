package level.tileTypes;

import java.awt.Color;
import java.awt.Image;

import core.MainClass;
import level.TileUsable;

public class TileWin extends TileUsable {
	public final static Color color = new Color(255, 200, 0);
	public final static Image texture = MainClass.getImage("\\data\\win.png");
	
	public TileWin() {
		super(texture, color);
	}

	@Override
	public void action() {
		MainClass.state = MainClass.GameState.WIN;
	}
}
