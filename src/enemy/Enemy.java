package enemy;

import core.Background;
import core.MainClass;

/** Super class for all enemies. */
public class Enemy {
	private int maxHp, damage;
	private int currHp, speedX, centerX, centerY;
	private Background bg = MainClass.getBg1();

	public Enemy(int maxHp, int damage) {
		this.maxHp = maxHp;
		this.damage = damage;
	}
	
	/**
	 * Adds an enemy to the game.
	 * @param centerX
	 * @param centerY
	 */
	public void add(int centerX, int centerY) {
		this.setCenterX(centerX);
		this.setCenterY(centerY);
	}

	public void update() {
		centerX += speedX;
		speedX = bg.getSpeedX();
	}

	public void die() {
	}

	public void attack() {
	}

	public int getCurrHp() {
		return currHp;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public Background getBg() {
		return bg;
	}

	public void setCurrHp(int currHp) {
		this.currHp = currHp;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setBg(Background bg) {
		this.bg = bg;
	}
}
