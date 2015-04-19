package core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import level.LevelReader;
import projectile.Projectile;
import enemy.Enemy;
import enemy.Enemy_Heliboy;

//TODO Udìlat double buffering dle http://stackoverflow.com/questions/2873506/how-to-use-double-buffering-inside-a-thread-and-applet

/** Main class of the Applet. */
public class MainClass extends Applet implements Runnable {
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	
	private int updatesPerSec = 60;
	private static Entity player;
	private Image image;
	private Image background;
	private Graphics second;
	private Graphics2D g2d;
	private static URL base;
	private static Background bg1, bg2;
	private Camera cam;
	private LevelReader lvl;
	/** Stores all projectiles. */
	private static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	// Enemy types
	private Enemy_Heliboy heliboy;

	@Override
	public void init() {
		Frame frame = (Frame) this.getParent().getParent();
		cam = new Camera(0, 0);

		this.addKeyListener(new ClassKeyListener());
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				System.out.println("X/Y: " + e.getXOnScreen() + "/ " + e.getYOnScreen());
			}
		});

		frame.setTitle("KiloboltTutorial");
		this.setSize(WIDTH, HEIGHT);
		this.setBackground(new Color(102, 226, 255));
		this.setFocusable(true);
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		background = getImage(base, "data/background.png");
		Enemy.setMainClass(this);
		lvl = new LevelReader("demo");
		lvl.init();
	}

	@Override
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		
		heliboy = new Enemy_Heliboy();
		heliboy.add(400, 360);
		heliboy.add(700, 300);

		player = new Player(this);
		Thread mainThread = new Thread(this);
		mainThread.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public void destroy() {
		super.destroy();
	}
	
	public static BufferedImage getImage(String path) {
		try {
		    URL url = new URL(MainClass.getBase(), path);
		    return ImageIO.read(url);
		} catch (IOException e) {
		}
		return null;
	}

	@Override
	public void paint(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
			g2d = (Graphics2D) second;
		}
		
		g2d.translate(cam.getX(), cam.getY());
		//////////////////////////////////////////
		// Místo na kreslení
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

	@Override
	public void update(Graphics g) {
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}
	
	@Override
	public void run() {
		while (true) {
			player.update();
			cam.update((Player) player);
			
			for(int i = 0; i < Enemy.allEnemies.size(); i++) {
				Enemy.allEnemies.get(i).update();
			}
			
			Projectile.update(projectiles);
			
			bg1.update();
			bg2.update();
			lvl.update();
			
			this.WIDTH = this.getWidth();
			this.HEIGHT = this.getHeight();
			
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

	public static URL getBase() {
		return base;
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
					player.setSpeedY(((Player) player).getJUMPSPEED());
					((Player) player).setJumped(true);
				}
				break;

			case KeyEvent.VK_DOWN:
				((Player) player).setCovered(true);
				break;

			case KeyEvent.VK_LEFT:
				((Player) player).setMovingLeft(true);
				player.setSpeedX(-((Player) player).getMOVESPEED());
				break;

			case KeyEvent.VK_RIGHT:
				((Player) player).setMovingRight(true);
				player.setSpeedX(((Player) player).getMOVESPEED());
				break;

			case KeyEvent.VK_SPACE:
				// TODO Tested enemie's attacks
				for(int i = 0; i < Enemy.allEnemies.size(); i++) {
					Enemy.allEnemies.get(i).attack();
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
				break;

			}
		}

		public void keyTyped(KeyEvent e) {
			
		}

	}

}
