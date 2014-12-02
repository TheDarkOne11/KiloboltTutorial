package test;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class TileMap extends Applet {
	public Tile[][] tiles;
	public int width;
	public int height;

	@Override
	public void init() {
		tiles = new Tile[16][16];
		this.setSize(800, 480);
		this.setName("Tiles");

		this.width = this.getWidth()/tiles.length;
		this.height = this.getHeight()/tiles[0].length;
		for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				tiles[i][j] = new Tile(i*width, j*height, this.getColor());
			}
		}
		this.addMouseListener(new MouseListen());
	}
	
	private Color getColor() {
		Random r = new Random();
		int x = r.nextInt(4);
		
		if(x == 0) {
			return Color.blue;
		} else if(x == 1) {
			return Color.black;
		} else if(x == 2) {
			return Color.yellow;
		} else {
			return Color.red;
		}
	}

	@Override
	public void paint(Graphics g) {
		/*for(int i = 0; i < tiles.length; i++) {
			for(int j = 0; j < tiles[i].length; j++) {
				g.setColor(tiles[i][j].color);
				g.fillRect(tiles[i][j].x, tiles[i][j].y, this.width, this.height);
				g.setColor(Color.white);
				g.drawRect(tiles[i][j].x, tiles[i][j].y, this.width, this.height);
			}
		}*/
		
		MyPolygon pol = new MyPolygon(50, 50, width, 6);
		g.drawPolygon(pol.posX, pol.posY, pol.n);
	}
	
	class Tile {
		public Color color;
		public int x, y;
		
		public Tile(int x, int y, Color color) {
			this.color = color;
			this.x = x;
			this.y = y;
		}
		
		
	}

	class MyPolygon {
		public int startX, startY, n, sideWidth;
		public int[] posX, posY;
		
		public MyPolygon(int startX, int startY, int sideWidth, int n) {
			this.startX = startX;
			this.startY = startY;
			this.sideWidth = sideWidth;
			this.n = n;
			posX = new int[n];
			posY = new int[n];
			setPos();
		}
		
		private void setPos() {
			for(int i = 0; i < n; i++) {
				posX[i] = (int) (startX + sideWidth * Math.cos(i * 2 * Math.PI / n));
				posY[i] = (int) (startY + sideWidth * Math.sin(i * 2 * Math.PI / n));
				System.out.println(i + ". " + posX[i] + "/ " + posY[i]);
			}
		}
		
	}
	
	class MouseListen extends MouseAdapter {

		public void mouseClicked(MouseEvent e) {
			System.out.println(e.getXOnScreen() + "/ " + e.getYOnScreen());
		}
		
	}
}
