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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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

//TODO Vytvoøení samostatné tøídy kolizních Shapù. Pozice, velikost, kdy aktivní, LinkedList shapù stejné tøídy.
//TODO Zvuky.

//TODO Základní umìlá inteligence, spoleèná pro všechny nepøátele (napø. chození k hráèi, otáèení se)
//TODO Umìlá inteligence v samostatném (možná vnoøeném) objektu - létání, palba po hráèi.

/** Main class of the Applet. */
public class MainClass extends Panel implements Runnable {
	public static int WIDTH = 800;
	public static int HEIGHT = 480;
	public static GameState state = GameState.MAIN_MENU;
	public static GameState lastState;
	public FrameClass frameClass;
	private static Color backgroundColor = new Color(102, 226, 255);
	
	/** Menu panel. */
	private MenuPanel panelMainMenu, panelGameMenu, panelControls, panelWin, panelLose;
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
	/** Stores all menu panels. */
	private static ArrayList<MenuPanel> panels;
	/** Stores every enemy that has been added to the current game.*/
	public static ArrayList<Enemy> allEnemies;
	/** Stores all buttons which are used ingame and what they are used for. */
	public static ArrayList<UsedKeys> allUsedKeys;

	public MainClass(FrameClass frameClass) {
		// Set game panel in window
		this.frameClass = frameClass;
		frameClass.setBackground(backgroundColor);
		frameClass.setSize(WIDTH, HEIGHT);
		frameClass.add(this);
		
		allUsedKeys = new ArrayList<UsedKeys>();
		
		// Listeners
		this.addComponentListener(new ClassComponentListener());
		this.addMouseListener(new ClassMouseListener());
		//this.addKeyListener(new ClassKeyListener());
		frameClass.addKeyListener(new ClassKeyListener());
		
		
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
		
		// Main panels
		panelGameMenu = new MenuPanel(GameState.GAME_MENU);
			panelGameMenu.add(new MenuButton("Continue"));
			panelGameMenu.add(new MenuButton("Controls"));
			panelGameMenu.add(new MenuButton("Exit Level"));
			panelGameMenu.add(new MenuButton("Exit Game"));
		
		panelMainMenu = new MenuPanel(GameState.MAIN_MENU);
			panelMainMenu.add(new MenuButton("New Game"));
			panelMainMenu.add(new MenuButton("Load Level"));
			panelMainMenu.add(new MenuButton("Controls"));
			panelMainMenu.add(new MenuButton("Exit"));						
			
		panelWin = new MenuPanel(GameState.WIN);
			panelWin.setBackground(Color.green);
			Label labWin = new Label("You Win!");
			labWin.setFont(new Font(Font.SERIF, Font.BOLD, 50));
			panelWin.add(labWin);
			panelWin.add(new MenuButton("To Main Menu"));
		
		panelLose = new MenuPanel(GameState.LOSE);
			panelLose.setBackground(Color.red);
			Label labLose = new Label("You Lose");
			labLose.setFont(new Font(Font.SERIF, Font.BOLD, 50));
			panelLose.add(labLose);
			panelLose.add(new MenuButton("To Main Menu"));
			
		// Sub-Menu panels
		panelControls = new MenuPanel(GameState.SUB_MENU, 2);
			for(UsedKeys key : allUsedKeys) {
				panelControls.add(new Label(key.keyName));
				panelControls.add(new Label(key.usage));
			}
			panelControls.add(new MenuButton("Back"));
		
		// Adding panels into the game
		gbc.gridx = 1;
		gbl.setConstraints(panel2, gbc);
		gbl.setConstraints(panel4, gbc);
		gbc.gridx = 2;
		gbl.setConstraints(panelMainMenu, gbc);
		gbl.setConstraints(panelGameMenu, gbc);
		gbc.gridx = 3;
		gbl.setConstraints(panel3, gbc);
		gbl.setConstraints(panel5, gbc);
		
		for(MenuPanel panel : panels) {
			this.add(panel);
		}
		
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
		player = new Player();
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
			for(MenuPanel panel : panels) {
				panel.setVisible();
			}
			this.validate();
			
			if(state == GameState.GAME_MENU || state == GameState.MAIN_MENU) {
				lastState = state;
			}
			
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
	
	private static BufferedImage toBufferedImage(Image img)
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
		/** Used to determine which sub-menu is visible. */
		private boolean submenuVisible;
		
		public MenuPanel(GameState visibleState) {
			this(visibleState, 1);
		}
		
		public MenuPanel(GameState visibleState, int colsCount) {
			super();
			this.visibleState = visibleState;
			this.setLayout(new GridLayout(0, colsCount, 10, 10));
			panels.add(this);
		}
		
		/**
		 * Sets visibility of panel based on the Game State.
		 */
		protected void setVisible() {
			if(state.equals(GameState.SUB_MENU)) {
				this.setVisible(submenuVisible);
			} else {
				if(visibleState.equals(state)) {
					this.setVisible(true);
				} else {
					this.setVisible(false);
				}
			}
		}
		
		public void setSubmenuVisible(boolean submenuVisible) {
			this.submenuVisible = submenuVisible;
		}
	}
	
	class MenuButton extends Button {

		private MenuButton(String label) {
			super(label);
			this.addActionListener(new ClassMenuButtonListeners());
		}
	}
	
	class ClassKeyListener extends KeyAdapter {
		public ClassKeyListener() {
			allUsedKeys.add(new UsedKeys(KeyEvent.getKeyText(KeyEvent.VK_UP), "Jump"));
			allUsedKeys.add(new UsedKeys(KeyEvent.getKeyText(KeyEvent.VK_DOWN), "Shield"));
			allUsedKeys.add(new UsedKeys(KeyEvent.getKeyText(KeyEvent.VK_LEFT), "Go left"));
			allUsedKeys.add(new UsedKeys(KeyEvent.getKeyText(KeyEvent.VK_RIGHT), "Go right"));
			allUsedKeys.add(new UsedKeys(KeyEvent.getKeyText(KeyEvent.VK_ESCAPE), "To Menu"));
		}
		
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
	
	}
	
	/**
	 * No Ingame use. Set for testing the game only.
	 * @author Petr
	 *
	 */
	class ClassMouseListener extends MouseAdapter {
		public ClassMouseListener() {
			allUsedKeys.add(new UsedKeys("Mouse Button 1", "Shoot"));
			allUsedKeys.add(new UsedKeys("Mouse Button 3", "Teleport(Developement only)"));
		}
		
		public void mousePressed(MouseEvent e) {
			if(state == GameState.RUNNING) {
				// e.getX je x na obrazovce, tmpX je aktuální x ve høe(ovlivnìno posouváním screenu)
				int tmpX = player.getPointCenter().x - MainClass.WIDTH/2 + e.getX();
				int tmpY = player.getPointCenter().y - 3*MainClass.HEIGHT/4 + e.getY();
				
				if(e.getButton() == MouseEvent.BUTTON1) {
					player.isAttacking = true;
					player.mouse = new Point(tmpX, tmpY);
				} else if(e.getButton() == MouseEvent.BUTTON3) {
					System.out.println("X/Y: " + tmpX + "/ " + tmpY);
					player.setPointCenter(new Point(tmpX, tmpY));
				}
			}
		}

		public void mouseReleased(MouseEvent e) {
			if(state == GameState.RUNNING) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					player.isAttacking = false;
				} 
			}
		}
		
	}
	
	class ClassComponentListener extends ComponentAdapter {

		public void componentResized(ComponentEvent e) {
			MainClass.WIDTH = getWidth();
			MainClass.HEIGHT = getHeight();
		}
		
	}
	
	/**
	 * ActionListener for all MenuButtons.
	 * @author Petr
	 *
	 */
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
			
			// Sub-Menu buttons
			if(e.getActionCommand().equals("Controls")) {
				panelControls.setSubmenuVisible(true);
				state = GameState.SUB_MENU;
			}
			
			if(e.getActionCommand().equals("Back")) {
				/* Sets all sub-menus to invisible. */
				for(MenuPanel panel : panels) {
					panel.setSubmenuVisible(false);
				}
				
				state = lastState;
			}
			
			// Win/Lose buttons
			if(e.getActionCommand().equals("To Main Menu")) {
				state = GameState.MAIN_MENU;
				frameClass.setBackground(backgroundColor);
			}
		}
		
	}
	
	public enum GameState {
		MAIN_MENU,
		SUB_MENU,
		RUNNING,
		WIN,
		LOSE,
		GAME_MENU; 
	}

}