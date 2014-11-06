package core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import projectile.Projectile;
import Animation.Animation;
import Animation.Animation_Heliboy;
import Animation.Animation_Player;
import enemy.Enemy_Heliboy;

/** Main class of the Applet. */
public class MainClass extends Applet implements Runnable {
	private int updatesPerSec = 60;
	private Player player;
	private Enemy_Heliboy hb, hb2;
	private Image image;
	private Image background;
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;
	private Animation_Player animPlayer;
	private Animation animHeliboy;

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

		hb = new Enemy_Heliboy(340, 360);
		hb2 = new Enemy_Heliboy(700, 360);

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
		
		g.drawImage(animHeliboy.getCurrentImage(), hb.getCenterX() - animHeliboy.getCurrentImage().getWidth(this) / 2, hb.getCenterY() - animHeliboy.getCurrentImage().getHeight(this) / 2, this);
		g.drawImage(animHeliboy.getCurrentImage(), hb2.getCenterX() - animHeliboy.getCurrentImage().getWidth(this) / 2, hb2.getCenterY() - animHeliboy.getCurrentImage().getHeight(this) / 2, this);

		Projectile.paint(g, player.getProjectiles());
		// TODO Should add painting of all projectiles at once.
		Projectile.paint(g, hb.getProjectiles());
		Projectile.paint(g, hb2.getProjectiles());
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
			
			// TODO Should add updating of all enemies at once.
			Projectile.update(hb.getProjectiles());
			Projectile.update(hb2.getProjectiles());
			animHeliboy.update();
			hb.update();
			hb2.update();
			
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

	public URL getBase() {
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
				hb.attack(hb.getProjectile());
				hb2.attack(hb2.getProjectile());
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
