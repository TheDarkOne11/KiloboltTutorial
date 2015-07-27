package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import enemy.Enemy;
import level.LevelReader;
import projectile.Projectile;

//TODO Ud�lat double buffering dle http://stackoverflow.com/questions/2873506/how-to-use-double-buffering-inside-a-thread-and-applet

//TODO Z�kladn� um�l� inteligence, spole�n� pro v�echny nep��tele (nap�. chozen� k hr��i, ot��en� se)
//TODO Um�l� inteligence v samostatn�m (mo�n� vno�en�m) objektu - l�t�n�, palba po hr��i.
//TODO Funk�nost �t�t� hr��e, vytvo�en� samostatn�ho objektu a obr�zku pro �t�t.
//TODO Menu, ukl�d�n� a na��t�n� level�. Pokusit se vyu��t Enum.

/** Main class of the Applet. */
public class MainClass extends Panel implements Runnable {
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	
	public boolean ingame = false;
	public boolean inmenu = true;
	
	private boolean resized = true;
	private int updatesPerSec = 60;
	private static Entity player;
	public static String gamePath;
	private Image image;
	private Image background;
	private Graphics second;
	private Graphics2D g2d;
	private static Background bg1, bg2;
	private Camera cam;
	private LevelReader lvl;
	/** Stores all projectiles. */
	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	public MainClass(FrameClass frameClass) {
		this.addComponentListener(new ClassComponentListener());
		this.addMouseListener(new ClassMouseListener());
		this.addKeyListener(new ClassKeyListener());
		frameClass.addKeyListener(new ClassKeyListener());
		frameClass.setBackground(new Color(102, 226, 255));
		frameClass.setSize(WIDTH, HEIGHT);
		frameClass.add(this);
		/*
		if(ingame) {
			cam = new Camera(0, 0);
			gamePath = System.getProperty("user.dir") + "\\src";
			background = new ImageIcon(gamePath + "\\data\\background.png").getImage();
			bg1 = new Background(0, 0);
			bg2 = new Background(2160, 0);
			player = new Player(this);
			Enemy.setMainClass(this);
			lvl = new LevelReader("demo");
			lvl.init();
			((Player) player).addPlayer();	// Player init must be before lvl.init and adding Player must be behind it.
		}*/
		
		Thread mainThread = new Thread(this);
		mainThread.start();
	}
	
	private void startLevel() {
		cam = new Camera(0, 0);
		gamePath = System.getProperty("user.dir") + "\\src";
		background = new ImageIcon(gamePath + "\\data\\background.png").getImage();
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		player = new Player(this);
		Enemy.setMainClass(this);
		lvl = new LevelReader("demo");
		lvl.init();
		((Player) player).addPlayer();	// Player init must be before lvl.init and adding Player must be behind it.
	}
	
	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}
	
	public static BufferedImage getImage(String path) {
		BufferedImage buff = toBufferedImage(new ImageIcon(gamePath + path).getImage());
		return buff;
	}

	@Override
	public void paint(Graphics g) {
		if(inmenu) {
			g.fillRect(100, 100, 20, 50);
		}
		
		if(ingame) {
			g2d.translate(cam.getX(), cam.getY());
			//////////////////////////////////////////
			// M�sto na kreslen�
			g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
			g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
			lvl.paintTiles(g);
			
			player.paint(g);
			
			Projectile.paint(g, projectiles);
			
			for(int i = 0; i < Enemy.allEnemies.size(); i++) {
				try {
					Enemy.allEnemies.get(i).paint(g);
				} catch(NullPointerException e) {
					e.printStackTrace();
					System.out.println("Enemy " + Enemy.allEnemies.get(i).getClass().getName() + " doesn't probably have animation.");
				}
			}
			
			///////////////////////////////////////////
			g2d.translate(-cam.getX(), -cam.getY());
		}
	}

	@Override
	public void update(Graphics g) {
		if(ingame) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
			g2d = (Graphics2D) second;
			resized = false;
			
			second.setColor(getBackground());
			second.fillRect(0, 0, getWidth(), getHeight());
			second.setColor(getForeground());
			paint(second);
	
			g.drawImage(image, 0, 0, this);
		}
	}
	
	@Override
	public void run() {
		while (true) {
			if(inmenu) {
				
			}
			
			if(ingame) {
				player.update();
				cam.update((Player) player);
				
				Projectile.update(projectiles);
				
				bg1.update();
				bg2.update();
				lvl.update();
				
				for(int i = 0; i < Enemy.allEnemies.size(); i++) {
					Enemy.allEnemies.get(i).update();
				}
			}
			repaint();

			try {
				Thread.sleep(1000 / this.updatesPerSec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static Background getBg1() {
		return bg1;
	}

	public static Background getBg2() {
		return bg2;
	}

	public static Player getPlayer() {
		return (Player) player;
	}

	public static ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}
	
	class ClassKeyListener implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				if(!((Player) player).isJumping()) {
					((Player) player).setJumping(true);
					player.setSpeedY(Player.getJumpspeed());
					((Player) player).setJumped(true);
				}
				break;
	
			case KeyEvent.VK_DOWN:
				((Player) player).setCovered(true);
				break;
	
			case KeyEvent.VK_LEFT:
				if(!((Player) player).isCovered()) {
					((Player) player).setMovingLeft(true);
					player.setSpeedX(-((Player) player).getMovespeed());
				}
				break;
	
			case KeyEvent.VK_RIGHT:
				if(!((Player) player).isCovered()) {
					((Player) player).setMovingRight(true);
					player.setSpeedX(((Player) player).getMovespeed());
				}
				break;
	
			case KeyEvent.VK_CONTROL:
				if (((Player) player).isCovered() == false && ((Player) player).isJumped() == false) {
					player.attack();
				}
				break;
	
			}
		}
	
		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				break;
	
			case KeyEvent.VK_DOWN:
				((Player) player).setCovered(false);
				break;
	
			case KeyEvent.VK_LEFT:
				((Player) player).setMovingLeft(false);
				player.setSpeedX(0);
				break;
	
			case KeyEvent.VK_RIGHT:
				((Player) player).setMovingRight(false);
				player.setSpeedX(0);
				break;
	
			case KeyEvent.VK_SPACE:
				startLevel();
				ingame = !ingame;
				inmenu = !inmenu;
				break;
	
			}
		}
	
		public void keyTyped(KeyEvent e) {
			
		}
	
	}
	
	/**
	 * No Ingame use. Set for testing the game only.
	 * @author Petr
	 *
	 */
	class ClassMouseListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int tmpX = player.getCenterX() - MainClass.WIDTH/2 + e.getX();
			int tmpY = player.getCenterY() - 3*MainClass.HEIGHT/4 + e.getY();
			System.out.println("X/Y: " + tmpX + "/ " + tmpY);
			player.setCenterX(tmpX);
			player.setCenterY(tmpY);
		}
		
	}
	
	class ClassComponentListener extends ComponentAdapter {

		public void componentResized(ComponentEvent e) {
			resized = true;
			MainClass.WIDTH = getWidth();
			MainClass.HEIGHT = getHeight();
		}
		
	}

}
