package enemy;

import java.util.ArrayList;

import projectile.Projectile;
import core.Background;
import core.MainClass;

/** Super class for all enemies. */
public class Enemy {
	private int maxHp;
	private int currHp, speedX, centerX, centerY, weaponX, weaponY;
	/** Difference beetween center and weapon coordinates. */
	private int weaponDiffX, weaponDiffY;
	private Background bg = MainClass.getBg1();
	protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	protected Enemy(int maxHp, int weaponDiffX, int weaponDiffY) {
		this.maxHp = maxHp;
		this.weaponDiffX = weaponDiffX;
		this.weaponDiffY = weaponDiffY;
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
		this.weaponX = this.centerX + this.weaponDiffX;
		this.weaponY = this.centerY + this.weaponDiffY;
	}

	public void die() {
	}

	public void attack(Projectile p) {
		p.spawnProjectile(this.weaponX, this.weaponY);
		projectiles.add(p);
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

	public int getWeaponX() {
		return weaponX;
	}

	public int getWeaponY() {
		return weaponY;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}
}
