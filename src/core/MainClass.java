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
import enemy.Enemy_Heliboy;

/** Main class of the Applet. */
public class MainClass extends Applet implements Runnable {
	private int updatesPerSec = 60;
	private Player player;
	private Enemy_Heliboy hb, hb2;
	private Image image, currentSprite, character, character2, character3,
			characterCover, characterJumped, background, heliboy, heliboy2,
			heliboy3, heliboy4, heliboy5;
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;
	private Animation anim, hanim;

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

		// Image Setups
		character = getImage(base, "data/character.png");
		character2 = getImage(base, "data/character2.png");
		character3 = getImage(base, "data/character3.png");

		characterCover = getImage(base, "data/cover.png");
		characterJumped = getImage(base, "data/jumped.png");

		heliboy = getImage(base, "data/heliboy.png");
		heliboy2 = getImage(base, "data/heliboy2.png");
		heliboy3 = getImage(base, "data/heliboy3.png");
		heliboy4 = getImage(base, "data/heliboy4.png");
		heliboy5 = getImage(base, "data/heliboy5.png");

		background = getImage(base, "data/background.png");

		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 50);
		anim.addFrame(character3, 50);
		anim.addFrame(character2, 50);

		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);

		currentSprite = anim.getImage();
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
		g.drawImage(currentSprite,
				player.getCenterX() - currentSprite.getWidth(this) / 2,
				player.getCenterY() - currentSprite.getHeight(this) / 2, this);
		g.drawImage(hanim.getImage(), hb.getCenterX() - heliboy.getWidth(this) / 2,
				hb.getCenterY() - heliboy.getHeight(this) / 2, this);
		g.drawImage(hanim.getImage(), hb2.getCenterX() - heliboy.getWidth(this) / 2,
				hb2.getCenterY() - heliboy.getHeight(this) / 2, this);

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
			player.update();
			if (player.isJumped()) {
				currentSprite = characterJumped;
			} else if (player.isJumped() == false && player.isDucked() == false) {
				currentSprite = anim.getImage();
			}

			Projectile.update(player.getProjectiles());
			// TODO Should add updating of all enemies at once.
			Projectile.update(hb.getProjectiles());
			Projectile.update(hb2.getProjectiles());
			hb.update();
			hb2.update();
			bg1.update();
			bg2.update();
			animate();
			repaint();

			try {
				Thread.sleep(1000 / this.updatesPerSec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void animate() {
		anim.update(10);
		hanim.update(50);
	}

	public static Background getBg1() {
		return bg1;
	}

	public static Background getBg2() {
		return bg2;
	}

	public Image getCurrentSprite() {
		return currentSprite;
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
				currentSprite = characterCover;
				if (player.isJumped() == false) {
					player.setDucked(true);
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
				if (player.isDucked() == false && player.isJumped() == false) {
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
				currentSprite = anim.getImage();
				player.setDucked(false);
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
