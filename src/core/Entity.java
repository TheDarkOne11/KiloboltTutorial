package core;

import java.awt.Graphics;

import projectile.Projectile;
import animation.Animation;

/**
 * Parent class for all entities.
 * @author Petr
 *
 */
public abstract class Entity {
	protected int maxHp, currHp, centerX, centerY, weaponX, weaponY;
	protected float speedX, speedY;
	protected Animation anim;
	protected Projectile projectile;
	
	public Entity(int maxHp) {
		this.maxHp = maxHp;
		this.currHp = maxHp;
	}
	
	public abstract void update();
	public abstract void paint(Graphics g);
	public abstract void die();
	public abstract void attack();
	public abstract void collision();
	
	public void hit(Projectile p) {
		this.currHp -= p.getDamage();
		p.setVisible(false);
		System.out.println(this.getClass().getSimpleName() + " hit for " + p.getDamage() + " HP. Remaining HP: " + this.currHp);
		if(this.currHp <= 0) {
			die();
		}
	}
	
	public int getMaxHp() {
		return maxHp;
	}
	public int getCurrHp() {
		return currHp;
	}
	public int getCenterX() {
		return centerX;
	}
	public int getCenterY() {
		return centerY;
	}
	public int getWeaponX() {
		return weaponX;
	}
	public int getWeaponY() {
		return weaponY;
	}
	public float getSpeedX() {
		return speedX;
	}
	public float getSpeedY() {
		return speedY;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public void setCurrHp(int currHp) {
		this.currHp = currHp;
	}
	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}
	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}
	public void setWeaponX(int weaponX) {
		this.weaponX = weaponX;
	}
	public void setWeaponY(int weaponY) {
		this.weaponY = weaponY;
	}
	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}
	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
}
