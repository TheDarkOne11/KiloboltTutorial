package core;

import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
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

//TODO Hráè bude støílet za pomoci myši. Jeho pistole bude støílet tam, kam myš zamíøí.
//TODO Funkènost štítù hráèe, vytvoøení samostatného objektu a obrázku pro štít.

//TODO Základní umìlá inteligence, spoleèná pro všechny nepøátele (napø. chození k hráèi, otáèení se)
//TODO Umìlá inteligence v samostatném (možná vnoøeném) objektu - létání, palba po hráèi.


/** Main class of the Applet. */
public class MainClass extends Panel implements Runnable {
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	private static GameState state = GameState.MAIN_MENU;
	private FrameClass frameClass;
	private static Color backgroundColor = new Color(102, 226, 255);
	
	/** Menu panel. */
	private Panel panelMainMenu, panelGameMenu, panelWin, panelLose;
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
	
	/** Stores all projectiles. */
	private static ArrayList<Projectile> projectiles;
	/** Stores all panels in menu. */
	private static ArrayList<MenuPanel> panels;
	/** Stores every enemy that has been added to the current game.*/
	public static ArrayList<Enemy> allEnemies;

	public MainClass(FrameClass frameClass) {
		// Set game panel in window
		this.frameClass = frameClass;
		frameClass.setBackground(backgroundColor);
		frameClass.setSize(WIDTH, HEIGHT);
		frameClass.add(this);
		
		// Menu section
		panels = new ArrayList<MenuPanel>();
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		
		Panel panel2 = new MenuPanel(GameState.MAIN_MENU);	// Empty panels used to position
		Panel panel3 = new MenuPanel(GameState.MAIN_MENU);	// full panels in the middle.
		Panel panel4 = new MenuPanel(GameState.GAME_MENU);
		Panel panel5 = new MenuPanel(GameState.GAME_MENU);
		
		panelGameMenu = new MenuPanel(GameState.GAME_MENU);
			panelGameMenu.setLayout(new GridLayout(3, 1, 10, 10));
			new MenuButton("Continue", panelGameMenu);
			new MenuButton("Exit Level", panelGameMenu);
			new MenuButton("Exit Game", panelGameMenu);
		
		panelMainMenu = new MenuPanel(GameState.MAIN_MENU);
			panelMainMenu.setLayout(new GridLayout(3, 1, 10, 10));
			new MenuButton("New Game", panelMainMenu);
			new MenuButton("Load Level", panelMainMenu);
			new MenuButton("Exit", panelMainMenu);
			
		panelWin = new MenuPanel(GameState.WIN);
			panelWin.setLayout(new GridLayout(2, 1, 10, 10));
			panelWin.setBackground(Color.green);
			Label labWin = new Label("You Win!");
			labWin.setFont(new Font(Font.SERIF, Font.BOLD, 50));
			panelWin.add(labWin);
			new MenuButton("To Main Menu", panelWin);
			
		panelLose = new MenuPanel(GameState.LOSE);
			panelLose.setLayout(new GridLayout(2, 1, 10, 10));
			panelLose.setBackground(Color.red);
			Label labLose = new Label("You Lose");
			labLose.setFont(new Font(Font.SERIF, Font.BOLD, 50));
			panelLose.add(labLose);
			new MenuButton("To Main Menu", panelLose);
		
		gbc.gridx = 1;
		gbl.setConstraints(panel2, gbc);
		gbl.setConstraints(panel4, gbc);
		gbc.gridx = 2;
		gbl.setConstraints(panelMainMenu, gbc);
		gbl.setConstraints(panelGameMenu, gbc);
		gbc.gridx = 3;
		gbl.setConstraints(panel3, gbc);
		gbl.setConstraints(panel5, gbc);
		
		this.add(panel2);
		this.add(panel4);
		this.add(panelMainMenu);
		this.add(panelGameMenu);
		this.add(panelLose);
		this.add(panelWin);
		this.add(panel3);
		this.add(panel5);
		
		// Listeners
		this.addComponentListener(new ClassComponentListener());
		this.addMouseListener(new ClassMouseListener());
		this.addKeyListener(new ClassKeyListener());
		frameClass.addKeyListener(new ClassKeyListener());
		
		// Start game
		gamePath = System.getProperty("user.dir") + "\\src";
		Thread mainThread = new Thread(this);
		mainThread.start();
	}
	
	/**
	 * Inicialization of variables needed at the start of Running state.
	 */
	private void loadLevel(String levelName) {		
		projectiles = new ArrayList<Projectile>();
		allEnemies = new ArrayList<Enemy>();
		if(!projectiles.isEmpty()) projectiles.clear();
		if(!allEnemies.isEmpty()) allEnemies.clear();
		
		cam = new Camera(0, 0);
		background = new ImageIcon(gamePath + "\\data\\background.png").getImage();
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		Enemy.setMainClass(this);
		
		lvl = new LevelReader(levelName);
		lvl.init();
		player = new Player(this);
		Enemy.setPlayer(player);;
	}

	@Override
	public void paint(Graphics g) {
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
			// Takes care of visibility of all panels
			for(MenuPanel tmp : panels) {
				tmp.setVisible();
			}
			this.validate();
			
			
			if(state == GameState.WIN) {
				frameClass.setBackground(Color.green);
			}
			
			if(state == GameState.LOSE) {
				frameClass.setBackground(Color.red);
			}
			
			if(state == GameState.RUNNING) {
				if(player.dead) state = GameState.LOSE;
				
				player.update();
				cam.update((Player) player);
				
				Projectile.update(projectiles);
				
				bg1.update();
				bg2.update();
				
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
		GameState visibleState;
		
		public MenuPanel(GameState visibleState) {
			super();
			this.visibleState = visibleState;
			panels.add(this);
		}
		
		/**
		 * Sets visibility of panel based on the Game State.
		 */
		protected void setVisible() {
			if(visibleState.equals(state)) {
				this.setVisible(true);
			} else {
				this.setVisible(false);
			}
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
			
			case KeyEvent.VK_ESCAPE:
				if(state == GameState.RUNNING) {
					state = GameState.GAME_MENU;
				} else if(state == GameState.GAME_MENU) {
					state = GameState.RUNNING;
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
			// Main Menu buttons
			if(e.getActionCommand().equals("New Game")) {
				loadLevel("demo.png");
				state = GameState.RUNNING;
				
			} else if(e.getActionCommand().equals("Load Level")) {
				FileDialog fileDialog = new FileDialog(frameClass, "Load level", FileDialog.LOAD);
				fileDialog.setDirectory(gamePath + "\\data\\level");
				fileDialog.setVisible(true);
				if(fileDialog.getFile() != null) {
				loadLevel(fileDialog.getFile());
				state = GameState.RUNNING;
				}
				
			} else if(e.getActionCommand().equals("Exit")) {
				frameClass.processEvent(new WindowEvent(frameClass, 201));
			}
			
			// Game Menu buttons
			if(e.getActionCommand().equals("Continue")) {
				state = GameState.RUNNING;
			} else if(e.getActionCommand().equals("Exit Level")) {
				state = GameState.MAIN_MENU;
			} else if(e.getActionCommand().equals("Exit Game")) {
				frameClass.processEvent(new WindowEvent(frameClass, 201));
			}
			
			// Win/Lose buttons
			if(e.getActionCommand().equals("To Main Menu")) {
				state = GameState.MAIN_MENU;
				frameClass.setBackground(backgroundColor);
			}
		}
		
	}
	
	enum GameState {
		MAIN_MENU,
		RUNNING,
		WIN,
		LOSE,
		GAME_MENU; 
	}

}