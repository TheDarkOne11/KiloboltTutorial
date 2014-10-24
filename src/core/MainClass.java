package core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

import enemy.Heliboy;

/** Main class of the Applet. */
public class MainClass extends Applet implements Runnable {
	private int updatesPerSec = 60;
	private Player player;
	private Heliboy hb, hb2;
	private Image image, background, currentSprite, character, characterCover, characterJumped, heliboy;
	private Graphics second;
	private URL base;
	private static Background bg1, bg2;

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
		characterCover = getImage(base, "data/cover.png");
		characterJumped = getImage(base, "data/jumped.png");
		currentSprite = character;
		heliboy = getImage(base, "data/heliboy.png");
		background = getImage(base, "data/background.png");
	}

	@Override
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		
		hb = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);
		
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
		g.drawImage(currentSprite, player.getCenterX() - currentSprite.getWidth(this)/2, player.getCenterY() - currentSprite.getHeight(this)/2, this);
		g.drawImage(heliboy, hb.getCenterX() - heliboy.getWidth(this)/2, hb.getCenterY() - heliboy.getHeight(this)/2, this);
		g.drawImage(heliboy, hb2.getCenterX() - heliboy.getWidth(this)/2, hb2.getCenterY() - heliboy.getHeight(this)/2, this);
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
			if (player.isJumped()){
				currentSprite = characterJumped;
			}else if (player.isJumped() == false && player.isDucked() == false){
				currentSprite = character;
			}
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

	public Image getCurrentSprite() {
		return currentSprite;
	}

	class ClassKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
	        case KeyEvent.VK_UP:
	            System.out.println("Move up");
	            break;

	        case KeyEvent.VK_DOWN:
	            currentSprite = characterCover;
	            if (player.isJumped() == false){
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

	        }
		}

		@Override
		public void keyReleased(KeyEvent e) {
			switch (e.getKeyCode()) {
	        case KeyEvent.VK_UP:
	            System.out.println("Stop moving up");
	            break;

	        case KeyEvent.VK_DOWN:
	            currentSprite = character;
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
