package enemy;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.sound.sampled.Clip;

import animation.Animation;
import core.Entity;
import core.MainClass;
import core.Player;
import core.Sound;
import projectile.Projectile;

/** Super class for all enemies. */
public abstract class Enemy extends Entity implements Cloneable{
	protected static MainClass mainClass;
	protected static Player player;
	/** Difference between center and weapon coordinates. */
	private int weaponDiffX, weaponDiffY;
	/** Stores time of a projectile shooting. */
	private long time;
	/** For flipping images.*/
	protected boolean movingRight = false;
	
	/** Rectangular radius where collisions are turned on. */
	protected Rectangle recRadius = new Rectangle();
	
	/** Sound */
	protected Clip attackSound;

	protected Enemy(int maxHp, int width, int height, double rateOfFire, float movespeed, Projectile projectile, Animation anim, int weaponDiffX, int weaponDiffY) {
		super(maxHp, width, height, rateOfFire, movespeed, projectile);
		this.weaponDiffX = weaponDiffX;
		this.weaponDiffY = weaponDiffY;
		this.anim = anim;
	}
	
	/**
	 * Adds an enemy to the game.
	 * @param centerPoint.x
	 * @param centerPoint.y
	 */
	public void add(int centerX, int centerY) {
		super.setPointCenter(new Point(centerX, centerY));
		this.anim = (Animation) anim.clone();
		this.anim.init();
		MainClass.allEnemies.add((Enemy) this.clone());
	}

	/**
	 * Paints all enemies that have been added.
	 * @param g
	 * @param mainClass 
	 */
	public void paint(Graphics g) {
		super.paintHpBar(g);
		super.drawEntity(g, movingRight);
	}
	
	public void die() {
		Sound.playClip(deathSound);
		MainClass.allEnemies.remove(this);
		System.out.println(this.getClass().getSimpleName() + ": DEAD");
	}

	public void attack() {
		if((this.time + (60*1000)/this.rateOfFire) < System.currentTimeMillis() | this.time == 0) {
			if(pointCenter.x < MainClass.getPlayer().getPointCenter().x) {
				this.projectile.spawnProjectile(this, true);
			} else {
				this.projectile.spawnProjectile(this, false);
			}
			
			Sound.playClip(attackSound);
			time = System.currentTimeMillis();
		}
	}

	/**
	 * Updates current enemy.
	 */
	public void update() {
		recRadius.setRect(pointCenter.x - 112, pointCenter.y - 112, 224, 224);
		
		super.setPointWeapon(new Point(movingRight ? pointCenter.x - this.weaponDiffX : pointCenter.x + this.weaponDiffX, this.pointCenter.y + this.weaponDiffY));
		
		anim.update();
		updateRec();
		collision();
		AI();
	}

	/**
	 * Updates collision rectangles.
	 */
	public abstract void updateRec();

	/**
	 * Artificial Intelligence
	 */
	public abstract void AI();
	
	public void moveToPlayer(int distance) {
		// Pomoc� movingRight se ovl�d� ot��en�
		if(pointCenter.x < player.getPointCenter().x) movingRight = true;
		else movingRight = false;
		
		if(pointCenter.x < player.getPointCenter().x - distance) {
			pointCenter.x += this.movespeed;
		} else if(pointCenter.x > player.getPointCenter().x + distance) {
			pointCenter.x -= this.movespeed;
		}
		
		if(pointCenter.y < player.getPointCenter().y) {
			pointCenter.y += this.movespeed;
		} else if(pointCenter.y > player.getPointCenter().y) {
			pointCenter.y -= this.movespeed;
		}
		
		// Pokud jsou p��mo u hr��e.
		if(!player.isMovingLeft()&!player.isMovingRight()&!player.isJumping()) {
			if(this.movespeed >= Math.abs(player.getPointCenter().x -pointCenter.x)){
				pointCenter.x = player.getPointCenter().x;
			}
			
			if(this.movespeed >= Math.abs(player.getPointCenter().y - pointCenter.y)) {
				pointCenter.y = player.getPointCenter().y;
			}
		}
	}
	
	public static void setMainClass(MainClass mainClass) {
		Enemy.mainClass = mainClass;
	}
	
	public static void setPlayer(Player player) {
		Enemy.player = player;
	}

	/**
	 * Nezbytn� metoda, jinak by nep��tel� sd�leli sou�adnice.
	 */
	protected Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
