package enemy;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import animation.Animation;
import core.Background;
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
	private Background bg = MainClass.getBg1();
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
	 * @param centerX
	 * @param centerY
	 */
	public void add(int centerX, int centerY) {
		this.setCenterX(centerX);
		this.setCenterY(centerY);
		this.anim = (Animation) anim.clone();
		this.anim.init();
		MainClass.allEnemies.add((Enemy) this.clone());
	}

	/**
	 * Updates current enemy.
	 */
	public void update() {
		centerX += speedX;
		speedX = bg.getSpeedX();
		recRadius.setRect(centerX - 112, centerY - 112, 224, 224);
		
		this.weaponX = movingRight ? this.centerX - this.weaponDiffX : this.centerX + this.weaponDiffX;
		this.weaponY = this.centerY + this.weaponDiffY;
		
		anim.update();
		updateRec();
		collision();
		AI();
	}
	
	/**
	 * Paints all enemies that have been added.
	 * @param g
	 * @param mainClass 
	 */
	public void paint(Graphics g) {
		super.paintHpBar(g);
		
		Graphics2D g2d = (Graphics2D) g;
		Image tmpImage = anim.getCurrentImage();
		
		if(movingRight) {
			int tmpX = this.getCenterX() - tmpImage.getWidth(mainClass) / 2;
			int tmpY = this.getCenterY() - tmpImage.getHeight(mainClass) / 2;
			g2d.drawImage(tmpImage, tmpX+tmpImage.getWidth(mainClass), tmpY, -tmpImage.getWidth(mainClass), tmpImage.getHeight(mainClass), mainClass);
		} else {
			g2d.drawImage(tmpImage, this.getCenterX() - tmpImage.getWidth(mainClass) / 2, this.getCenterY() - tmpImage.getHeight(mainClass) / 2, mainClass);
		}
	}
	
	public void die() {
		MainClass.allEnemies.remove(this);
		System.out.println(this.getClass().getSimpleName() + ": DEAD");
	}

	public void attack() {
		if((this.time + (60*1000)/this.rateOfFire) < System.currentTimeMillis() | this.time == 0) {
			if(this.centerX < MainClass.getPlayer().getCenterX()) {
				this.projectile.spawnProjectile(this, true);
			} else {
				this.projectile.spawnProjectile(this, false);
			}
			
			time = System.currentTimeMillis();
		}
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
