package core;

import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import enemy.Enemy;
import level.LevelReader;
import projectile.Projectile;

//TODO Udìlat double buffering dle http://stackoverflow.com/questions/2873506/how-to-use-double-buffering-inside-a-thread-and-applet

//TODO Menu, ukládání a naèítání levelù. Naèítání a ukládání udìlat pomocí FileDialog.

//TODO Základní umìlá inteligence, spoleèná pro všechny nepøátele (napø. chození k hráèi, otáèení se)
//TODO Umìlá inteligence v samostatném (možná vnoøeném) objektu - létání, palba po hráèi.
//TODO Funkènost štítù hráèe, vytvoøení samostatného objektu a obrázku pro štít.

/** Main class of the Applet. */
public class MainClass extends Panel implements Runnable {
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	public static GameState state = GameState.MENU;
	
	/** Menu panel. */
	private Panel panelMenu;
	private GridBagLayout gbl;
	private GridBagConstraints gbc;
	
	private int updatesPerSec = 60;
	private static Player player;
	public static String gamePath;
	private Image image;
	private Image background;
	private Graphics second;
	private Graphics2D g2d;
	private static Background bg1, bg2;
	private Camera cam;
	private LevelReader lvl;
	private FrameClass frameClass;
	
	/** Stores all projectiles. */
	private static ArrayList<Projectile> projectiles;
	/** Stores all panels in menu. */
	private static ArrayList<Panel> panels;
	/** Stores every enemy that has been added to the current game.*/
	public static ArrayList<Enemy> allEnemies;

	public MainClass(FrameClass frameClass) {
		// Set game panel in window
		this.frameClass = frameClass;
		frameClass.setBackground(new Color(102, 226, 255));
		frameClass.setSize(WIDTH, HEIGHT);
		frameClass.add(this);
		
		// Menu section
		panels = new ArrayList<Panel>();
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		
		Panel panel2 = new MenuPanel();	// Je zde, aby byl panelMenu uprostøed
		Panel panel3 = new MenuPanel();
		
		panelMenu = new MenuPanel();
			panelMenu.setLayout(new GridLayout(4, 1, 10, 10));
			new MenuButton("Continue", panelMenu);
			new MenuButton("New game", panelMenu);
			new MenuButton("Levels", panelMenu);
			new MenuButton("Exit", panelMenu);
		
		gbc.gridx = 1;
		gbl.setConstraints(panel2, gbc);
		gbc.gridx = 2;
		gbl.setConstraints(panelMenu, gbc);
		gbc.gridx = 3;
		gbl.setConstraints(panel3, gbc);
		
		this.add(panel2);
		this.add(panelMenu);
		this.add(panel3);
		
		// Listeners
		this.addComponentListener(new ClassComponentListener());
		this.addMouseListener(new ClassMouseListener());
		this.addKeyListener(new ClassKeyListener());
		frameClass.addKeyListener(new ClassKeyListener());
		
		// Start game
		Thread mainThread = new Thread(this);
		mainThread.start();
	}
	
	/**
	 * Inicialization of variables needed at the start of Running state.
	 */
	private void loadLevel() {		
		projectiles = new ArrayList<Projectile>();
		allEnemies = new ArrayList<Enemy>();
		if(!projectiles.isEmpty()) projectiles.clear();
		if(!allEnemies.isEmpty()) allEnemies.clear();
		
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
		// Menu
		if(state == GameState.MENU) {
			
		}
		
		// Game
		if(state == GameState.RUNNING) {			
			g2d.translate(cam.getX(), cam.getY());
			//////////////////////////////////////////
			// Místo na kreslení
			g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
			g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
			lvl.paintTiles(g);
			
			player.paint(g);
			
			Projectile.paint(g, projectiles);
			
			for(int i = 0; i < MainClass.allEnemies.size(); i++) {
				try {
					MainClass.allEnemies.get(i).paint(g);
				} catch(NullPointerException e) {
					e.printStackTrace();
					System.out.println("Enemy " + MainClass.allEnemies.get(i).getClass().getName() + " doesn't probably have animation.");
				}
			}
			
			///////////////////////////////////////////
			g2d.translate(-cam.getX(), -cam.getY());
		}
	}

	@Override
	public void update(Graphics g) {
		image = createImage(this.getWidth(), this.getHeight());
		second = image.getGraphics();
		g2d = (Graphics2D) second;
		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
		
	}
	
	@Override
	public void run() {
		while (true) {
			if(state == GameState.MENU) {
				if(!panelMenu.isVisible()) {
					for(Panel tmp : panels) {
						tmp.setVisible(true);
					}
					this.validate();
				}
			}
			
			if(state == GameState.RUNNING) {
				if(player.dead) state = GameState.MENU;
				
				if(panelMenu.isVisible()) {
					for(Panel tmp : panels) {
						tmp.setVisible(false);
					}
					this.validate();
				}
				
				player.update();
				cam.update((Player) player);
				
				Projectile.update(projectiles);
				
				bg1.update();
				bg2.update();
				lvl.update();
				
				for(int i = 0; i < MainClass.allEnemies.size(); i++) {
					MainClass.allEnemies.get(i).update();
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
	
	class MenuPanel extends Panel {
		
		public MenuPanel() {
			super();
			panels.add(this);
		}
	}
	
	class MenuButton extends Button {

		private MenuButton(String label, Panel panel) {
			super(label);
			panel.add(this);
			this.addActionListener(new ClassMenuButtonListeners());
		}
		
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
				if(state == GameState.RUNNING) {
					state = GameState.MENU;
				} else {
					loadLevel();
					state = GameState.RUNNING;
				}
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
			if(state == GameState.RUNNING) {
				int tmpX = player.getCenterX() - MainClass.WIDTH/2 + e.getX();
				int tmpY = player.getCenterY() - 3*MainClass.HEIGHT/4 + e.getY();
				System.out.println("X/Y: " + tmpX + "/ " + tmpY);
				player.setCenterX(tmpX);
				player.setCenterY(tmpY);
			}
		}
		
	}
	
	class ClassComponentListener extends ComponentAdapter {

		public void componentResized(ComponentEvent e) {
			MainClass.WIDTH = getWidth();
			MainClass.HEIGHT = getHeight();
		}
		
	}
	
	class ClassMenuButtonListeners implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Continue")) {
				loadLevel();
				state = GameState.RUNNING;
				
			} else if(e.getActionCommand().equals("New Game")) {
				
			} else if(e.getActionCommand().equals("Levels")) {
				
			} else if(e.getActionCommand().equals("Exit")) {
				frameClass.processEvent(new WindowEvent(frameClass, 201));
			}
		}
		
	}
	
	enum GameState {
		MENU,
		RUNNING; 
	}

}