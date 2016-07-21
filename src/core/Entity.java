package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

import animation.Animation;
import projectile.Projectile;

/**
 * Parent class for all entities.
 * @author Petr
 *
 */
public abstract class Entity {
	protected int maxHp, currHp, width, height;
	protected Point pointCenter, pointWeapon;
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
	
	public abstract void update();
	public abstract void paint(Graphics g);
	public abstract void die();
	public abstract void attack();
	public abstract void collision();
	
	/**
	 * Draws entity(currentImage in anim) on screen.
	 * Takes care of flipping the character - when character wants to turn around.
	 * @param g
	 * @param condition means under what condition should the image be flipped.
	 */
	public void drawEntity(Graphics g, boolean condition) {
		Graphics2D g2d = (Graphics2D) g;
		if(condition) {
			// Entita se ot·ËÌ
			int tmpX = pointCenter.x - anim.getCurrentImage().getWidth(null) / 2;
			int tmpY = pointCenter.y - anim.getCurrentImage().getHeight(null) / 2;
			g2d.drawImage(anim.getCurrentImage(), tmpX+anim.getCurrentImage().getWidth(null), tmpY, -anim.getCurrentImage().getWidth(null), anim.getCurrentImage().getHeight(null), null);
		} else {
			g2d.drawImage(anim.getCurrentImage(), pointCenter.x - anim.getCurrentImage().getWidth(null) / 2, pointCenter.y - anim.getCurrentImage().getHeight(null) / 2, null);
		}
	}
	
	
	public void paintHpBar(Graphics g) {
		hpCount = currHp + " HP";
		hpBar = new Rectangle(this.pointCenter.x - width/2, this.pointCenter.y + 10 - 4*height/5, width, 20);
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

	public Point getPointCenter() {
		return pointCenter;
	}

	public Point getPointWeapon() {
		return pointWeapon;
	}

	public void setPointCenter(Point centerPoint) {
		this.pointCenter = centerPoint;
	}

	public void setPointWeapon(Point weaponPoint) {
		this.pointWeapon = weaponPoint;
	}
}
