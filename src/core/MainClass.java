package core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import animation.Animation;
import animation.Animation_Heliboy;
import animation.Animation_Player;

import projectile.Projectile;
import enemy.Enemy;
import enemy.Enemy_Heliboy;

/** Main class of the Applet. */
public class MainClass extends Applet implements Runnable {
	private int updatesPerSec = 60;
	private Player player;
	private Image image;
	private Image background;
	private Graphics second;
	private static URL base;
	private static Background bg1, bg2;
	private Animation_Player animPlayer;
	private Animation animHeliboy;
	
	// Enemy types
	private Enemy_Heliboy heliboy;

	@Override
	public void init() {
		Frame frame = (Frame) this.getParent().getParent();

		this.addKeyListener(new ClassKeyListener());

		frame.setTitle("KiloboltTutorial");
		this.setSize(800, 480);
		this.setBackground(Color.black);
		this.setFocusable(true);
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		background = getImage(base, "data/background.png");
		
		animPlayer = new Animation_Player(this);
		animPlayer.init();
		
		animHeliboy = new Animation_Heliboy(this);
		animHeliboy.init();
		
	}

	@Override
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);

		/*hb = new Enemy_Heliboy(340, 360);
		hb2 = new Enemy_Heliboy(700, 360);*/
		
		heliboy = new Enemy_Heliboy(this);
		heliboy.add(340, 360);
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

	@Override
	public void paint(Graphics g) {
		g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
		
		g.drawImage(animPlayer.getCurrentImage(), player.getCenterX() - animPlayer.getCurrentImage().getWidth(this) / 2, player.getCenterY() - animPlayer.getCurrentImage().getHeight(this) / 2, this);
		
		Projectile.paint(g, player.getProjectiles());
		
		for(int i = 0; i < Enemy.allEnemies.size(); i++) {
			//TODO Pøesunout animHeliboy do Enemy_Heliboy
			// Všechny typy nepøátel musí mít správnou animaci!!!!
			Enemy.allEnemies.get(i).paint(g, animHeliboy.getCurrentImage(), this);
			Projectile.paint(g, Enemy.allEnemies.get(i).getProjectiles());
		}
	}

	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);
	}
	
	@Override
	public void run() {
		while (true) {
			animPlayer.update();
			player.update();
			Projectile.update(player.getProjectiles());
			
			animHeliboy.update();
			for(int i = 0; i < Enemy.allEnemies.size(); i++) {
				Projectile.update(Enemy.allEnemies.get(i).getProjectiles());
				Enemy.allEnemies.get(i).update();
			}
			
			bg1.update();
			bg2.update();
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

	public Player getPlayer() {
		return player;
	}

	public Animation_Player getAnim() {
		return animPlayer;
	}

	class ClassKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				System.out.println("Move up");
				// TODO Tested enemie's attacks
				for(int i = 0; i < Enemy.allEnemies.size(); i++) {
					Enemy.allEnemies.get(i).attack(heliboy.getProjectile());
				}
				
				break;

			case KeyEvent.VK_DOWN:
				if (player.isJumped() == false) {
					player.setCovered(true);
					player.setSpeedX(0);
				}
				break;

			case KeyEvent.VK_LEFT:
				player.moveLeft();
				player.setMovingLeft(true);
				break;

			case KeyEvent.VK_RIGHT:
				player.moveRight();
				player.setMovingRight(true);
				break;

			case KeyEvent.VK_SPACE:
				player.jump();
				break;

			case KeyEvent.VK_CONTROL:
				if (player.isCovered() == false && player.isJumped() == false) {
					player.shoot();
				}
				break;

			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				System.out.println("Stop moving up");
				break;

			case KeyEvent.VK_DOWN:
				player.setCovered(false);
				break;

			case KeyEvent.VK_LEFT:
				player.stopLeft();
				break;

			case KeyEvent.VK_RIGHT:
				player.stopRight();
				break;

			case KeyEvent.VK_SPACE:
				break;

			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	}

}
