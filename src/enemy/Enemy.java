package enemy;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import projectile.Projectile;
import animation.Animation;
import core.Background;
import core.Entity;
import core.MainClass;

/** Super class for all enemies. */
public abstract class Enemy extends Entity implements Cloneable{
	protected static MainClass mainClass;
	/** Difference between center and weapon coordinates. */
	private int weaponDiffX, weaponDiffY;
	private Background bg = MainClass.getBg1();
	/** Stores time of shooting projectile. */
	private long time;
	/** Stores every enemy that has been added to the current game.*/
	public static ArrayList<Enemy> allEnemies = new ArrayList<Enemy>();
	
	/** Rectangular radius where collisions are turned on. */
	protected Rectangle recRadius = new Rectangle();

	protected Enemy(int maxHp, double rateOfFire, Projectile projectile, Animation anim, int weaponDiffX, int weaponDiffY) {
		super(maxHp);
		this.maxHp = maxHp;
		this.rateOfFire = rateOfFire;
		this.projectile = projectile;
		this.anim = anim;
		this.weaponDiffX = weaponDiffX;
		this.weaponDiffY = weaponDiffY;
		
		anim.init();
	}
	
	/**
	 * Adds an enemy to the game.
	 * @param centerX
	 * @param centerY
	 */
	public void add(int centerX, int centerY) {
		this.setCenterX(centerX);
		this.setCenterY(centerY);
		Enemy.allEnemies.add((Enemy) this.clone());
	}

	/**
	 * Updates current enemy.
	 */
	public void update() {
		centerX += speedX;
		speedX = bg.getSpeedX();
		this.weaponX = this.centerX + this.weaponDiffX;
		this.weaponY = this.centerY + this.weaponDiffY;
		recRadius.setRect(centerX - 112, centerY - 112, 224, 224);
		anim.update();
		updateRec();
		collision();
	}
	
	/**
	 * Updates collision rectangles.
	 */
	public abstract void updateRec();
	
	/**
	 * Paints all enemies that have been added.
	 * @param g
	 * @param mainClass 
	 */
	public void paint(Graphics g) {
		g.drawImage(anim.getCurrentImage(), this.getCenterX() - anim.getCurrentImage().getWidth(mainClass) / 2, this.getCenterY() - anim.getCurrentImage().getHeight(mainClass) / 2, mainClass);
	}
	
	public void die() {
		Enemy.allEnemies.remove(this);
		System.out.println("DEAD");
	}

	public void attack() {
		if((this.time + (60*1000)/this.rateOfFire) < System.currentTimeMillis() | this.time == 0) {
			this.projectile.spawnProjectile(this);
			time = System.currentTimeMillis();
		}
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

	public static void setMainClass(MainClass mainClass) {
		Enemy.mainClass = mainClass;
	}
}
