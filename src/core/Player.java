package core;

import java.awt.Color;
import java.util.ArrayList;

import projectile.Projectile;

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
	private boolean jumped = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean ducked = false;
	private MainClass mainClass;

	private static Background bg1 = MainClass.getBg1();
	private static Background bg2 = MainClass.getBg2();
	
	/** Player shooting projectiles. */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public Player(MainClass mainClass) {
		this.mainClass = mainClass;
	}

	public void update() {
		if (speedX < 0) {
			centerX += speedX;
		}

		if (speedX <= 0) {
			bg1.setSpeedX(0);
			bg2.setSpeedX(0);
		}
		if (centerX <= 200 && speedX > 0) {
			centerX += speedX;
		}
		// Scroolování pozadí je na opaènou stranu, než jde postava.
		if (speedX > 0 && centerX > 200) {
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
		if (centerX + speedX <= mainClass.getCurrentSprite().getWidth(mainClass)/2) {
			centerX = mainClass.getCurrentSprite().getWidth(mainClass)/2+1;
		}
		
		this.weaponX = centerX + 50;
		this.weaponY = centerY - 25;
	}

	public void moveRight() {
		if (ducked == false) {
			speedX = MOVESPEED;
		}
	}

	public void moveLeft() {
		if (ducked == false) {
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
		Projectile p = new Projectile(5, Color.green, 7);
		p.spawnProjectile(weaponX, weaponY);
		projectiles.add(p);
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

	public boolean isDucked() {
		return ducked;
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

	public void setDucked(boolean ducked) {
		this.ducked = ducked;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}
}
