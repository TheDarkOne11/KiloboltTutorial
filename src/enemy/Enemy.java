package enemy;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import animation.Animation;
import core.Entity;
import core.MainClass;
import core.Player;
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
		
		System.out.println("X/ Y / SpeedX: " + pointCenter.x + "/ " + pointCenter.y + "/ " + speedX);
	}

	/**
	 * Updates collision rectangles.
	 */
	public abstract void updateRec();

	/**
	 * Artificial Intelligence
	 */
	public abstract void AI();
	
	public static void setMainClass(MainClass mainClass) {
		Enemy.mainClass = mainClass;
	}
	
	public static void setPlayer(Player player) {
		Enemy.player = player;
	}

	/**
	 * Potøeba, jinak by nepøátelé sdíleli souøadnice.
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
