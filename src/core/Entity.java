package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import animation.Animation;
import projectile.Projectile;

/**
 * Parent class for all entities.
 * @author Petr
 *
 */
public abstract class Entity {
	protected int maxHp, currHp, centerX, centerY, width, height, weaponX, weaponY;
	protected double rateOfFire;
	protected float speedX, speedY, movespeed;
	protected String hpCount;
	protected Rectangle hpBar;
	protected Animation anim;
	protected Projectile projectile;
	
	public Entity(int maxHp, int width, int height, double rateOfFire, float movespeed, Projectile projectile) {
		this.maxHp = maxHp;
		this.width = width;
		this.height = height;
		this.currHp = maxHp;
		this.rateOfFire = rateOfFire;
		this.movespeed = movespeed;
		this.projectile = projectile;
	}
	
	public void paintHpBar(Graphics g) {
		hpCount = currHp + " HP";
		hpBar = new Rectangle(this.centerX - width/2, this.centerY + 10 - 4*height/5, width, 20);
		g.setColor(Color.black);
		g.drawString(hpCount, hpBar.x+hpBar.width/2 - g.getFontMetrics().stringWidth(hpCount)/2, hpBar.y + 2*hpBar.height/3);
		
		if(currHp*100/maxHp <= 25){
			g.setColor(new Color(255, 0, 0, 99));
		} else if(currHp*100/maxHp <= 75) {
			g.setColor(new Color(255, 140, 0, 99));
		} else {
			g.setColor(new Color(0, 255, 0, 99));
		}
		
		g.fillRect(hpBar.x, hpBar.y, hpBar.width, hpBar.height);		
	}
	
	public abstract void update();
	public abstract void paint(Graphics g);
	public abstract void die();
	public abstract void attack();
	public abstract void collision();
	
	public void hit(Projectile p) {
		this.currHp -= p.getDamage();
		p.setVisible(false);
		//System.out.println(this.getClass().getSimpleName() + " hit for " + p.getDamage() + " HP. Remaining HP: " + this.currHp);
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

	public float getMovespeed() {
		return movespeed;
	}

	public void setMovespeed(float movespeed) {
		this.movespeed = movespeed;
	}
}
