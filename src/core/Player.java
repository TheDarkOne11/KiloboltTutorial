package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import projectile.Projectile;
import animation.Animation_Player;

public class Player {
	final int JUMPSPEED = -15;
	final int MOVESPEED = 5;
	final int GROUND = 382;

	private int centerX = 100;
	private int centerY = 382;
	private int weaponX = 50;
	private int weaponY = -25;
	private int speedX = 0;
	private int speedY = 1;
	private int backgroundStartMove = 200;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean covered = false;
	private boolean jumped = false;
	private MainClass mainClass;
	private Animation_Player animPlayer;
	
	// Collisions
	/** Upper body collision rectangle. */
	private Rectangle uBody = new Rectangle();	
	/** Lower body collision rectangle. */
	private Rectangle lBody = new Rectangle();
	private Rectangle rHand = new Rectangle();
	private Rectangle lHand = new Rectangle();
	/** Rectangular radius where collisions are turned on. */
	private Rectangle collisionRadius = new Rectangle();

	private static Background bg1 = MainClass.getBg1();
	private static Background bg2 = MainClass.getBg2();
	
	/** Player shooting projectiles. */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public Player(MainClass mainClass) {
		this.mainClass = mainClass;
		this.animPlayer = new Animation_Player(mainClass);
		animPlayer.init();
	}

	public void update() {
		if (speedX < 0) {
			centerX += speedX;
		}

		if (speedX <= 0) {
			bg1.setSpeedX(0);
			bg2.setSpeedX(0);
		}
		if (centerX <= backgroundStartMove && speedX > 0) {
			centerX += speedX;
		}
		// Scroolování pozadí je na opaènou stranu, než jde postava.
		if (speedX > 0 && centerX > backgroundStartMove) {
			bg1.setSpeedX(-MOVESPEED);
			bg2.setSpeedX(-MOVESPEED);
		}

		// Updates Y Position
		centerY += speedY;
		if (centerY + speedY >= GROUND) {
			centerY = GROUND;
		}

		// Handles Jumping
		if (jumped == true) {
			speedY += 1;

			if (centerY + speedY >= GROUND) {
				centerY = GROUND;
				speedY = 0;
				jumped = false;
			}

		}

		// Prevents going beyond X coordinate of 0
		if (centerX + speedX <= animPlayer.getCurrentImage().getWidth(mainClass)/2) {
			centerX = animPlayer.getCurrentImage().getWidth(mainClass)/2+1;
		}
		
		this.weaponX = centerX + 50;
		this.weaponY = centerY - 25;
		this.uBody.setRect(centerX - 37, centerY - 0, 74, 64);
		this.lBody.setRect(centerX - 37, centerY, 71, 64);
		this.rHand.setRect(centerX + 34, centerY - 32, 30, 20);
		this.lHand.setRect(centerX - 64, centerY - 32, 30, 20);
		this.collisionRadius.setRect(centerX - 112, centerY - 112, 224, 224);
		animPlayer.update();
	}

	public void moveRight() {
		if (covered == false) {
			speedX = MOVESPEED;
		}
	}

	public void moveLeft() {
		if (covered == false) {
			speedX = -MOVESPEED;
		}
	}

	public void stopRight() {
		setMovingRight(false);
		stop();
	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (isMovingRight() == false && isMovingLeft() == false) {
			speedX = 0;
		}

		if (isMovingRight() == false && isMovingLeft() == true) {
			moveLeft();
		}

		if (isMovingRight() == true && isMovingLeft() == false) {
			moveRight();
		}

	}

	public void jump() {
		if (jumped == false) {
			speedY = JUMPSPEED;
			jumped = true;
		}

	}
	
	public void shoot() {
		Projectile p = (Projectile) new Projectile(5, Color.green, 7).clone();
		p.spawnProjectile(weaponX, weaponY);
		projectiles.add(p);
	}
	
	public void paint(Graphics g) {
		g.drawImage(animPlayer.getCurrentImage(), this.getCenterX() - animPlayer.getCurrentImage().getWidth(mainClass) / 2, this.getCenterY() - animPlayer.getCurrentImage().getHeight(mainClass) / 2, mainClass);
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public boolean isCovered() {
		return covered;
	}

	public int getSpeedX() {
		return speedX;
	}

	public boolean isJumped() {
		return jumped;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setCovered(boolean covered) {
		this.covered = covered;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public int getBackgroundStartMove() {
		return backgroundStartMove;
	}
}
