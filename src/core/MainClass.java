package core;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;

/** Main class of the Applet. */
public class MainClass extends Applet implements Runnable {
	private int updatesPerSec = 60;
	private Player player = new Player();
	private Image image;
	private Graphics second;
	private Image character;
	private URL base;

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

	}

	@Override
	public void start() {
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
		g.drawImage(character, player.getCenterX() - player.getWidth()/2, player.getCenterY() - player.getHeight()/2, this);
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
			repaint();

			try {
				Thread.sleep(1000 / this.updatesPerSec);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	class ClassKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				System.out.println("Move up");
				break;

			case KeyEvent.VK_DOWN:
				System.out.println("Move down");
				break;

			case KeyEvent.VK_LEFT:
				player.moveLeft();
				break;

			case KeyEvent.VK_RIGHT:
				player.moveRight();
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
				System.out.println("Stop moving down");
				break;

			case KeyEvent.VK_LEFT:
				player.stop();
				break;

			case KeyEvent.VK_RIGHT:
				player.stop();
				break;

			case KeyEvent.VK_SPACE:
				System.out.println("Stop jumping");
				break;

			}
		}

		@Override
		public void keyTyped(KeyEvent e) {

		}

	}

}
