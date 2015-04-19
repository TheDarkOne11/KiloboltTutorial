package enemy;

import java.awt.Graphics;
import java.util.ArrayList;

import projectile.Projectile;
import animation.Animation;
import core.Background;
import core.Entity;
import core.MainClass;

/** Super class for all enemies. */
public class Enemy extends Entity implements Cloneable{
	protected static MainClass mainClass;
	private Projectile projectile;
	/** Difference between center and weapon coordinates. */
	private int weaponDiffX, weaponDiffY;
	private Background bg = MainClass.getBg1();
	protected ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	/** Stores every enemy that has been added to the current game.*/
	public static ArrayList<Enemy> allEnemies = new ArrayList<Enemy>();
	
	public Enemy(MainClass mainClass) {
		Enemy.mainClass = mainClass;
	}

	protected Enemy(int maxHp, Projectile projectile, Animation anim, int weaponDiffX, int weaponDiffY) {
		this.maxHp = maxHp;
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
		anim.update();
	}
	
	/**
	 * Paints all enemies that have been added.
	 * @param g
	 * @param mainClass 
	 */
	public void paint(Graphics g) {
		g.drawImage(anim.getCurrentImage(), this.getCenterX() - anim.getCurrentImage().getWidth(mainClass) / 2, this.getCenterY() - anim.getCurrentImage().getHeight(mainClass) / 2, mainClass);
	}
	
	public void die() {
		Enemy.allEnemies.remove(this.clone());
	}

	public void attack() {
		this.projectile.spawnProjectile(this);
		projectiles.add((Projectile) this.projectile.clone());
	}

	public void collision() {
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

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public static void setMainClass(MainClass mainClass) {
		Enemy.mainClass = mainClass;
	}
}
