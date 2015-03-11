package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import level.LevelReader;
import level.Tile;
import level.TileSpawn;
import projectile.Projectile;
import animation.Animation_Player;

public class Player {
	final float JUMPSPEED = -10f;
	final float MOVESPEED = 5f;
	final float MAXFALL = 5f;

	private int centerX;
	private int centerY;
	
	private int weaponX = 50;
	private int weaponY = -25;
	private float speedX = 0f;
	private float speedY = 0f;
	private int backgroundStartMove = 200;
	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isCovered = false;
	private boolean isJumping = false;
	private boolean isFalling = true;
	private MainClass mainClass;
	private Animation_Player animPlayer;
	
	// Collisions
	/** Upper body collision rectangle. */
	private Rectangle recBodyU = new Rectangle();	
	/** Lower body collision rectangle. */
	private Rectangle recBodyL = new Rectangle();
	private Rectangle recHandR = new Rectangle();
	private Rectangle recHandL = new Rectangle();
	/** Rectangular radius where collisions are turned on. */
	private Rectangle recRadius = new Rectangle();

	private static Background bg1 = MainClass.getBg1();
	private static Background bg2 = MainClass.getBg2();
	
	/** Player shooting projectiles. */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public Player(MainClass mainClass) {
		this.centerX = TileSpawn.x;
		this.centerY = TileSpawn.y;
		this.mainClass = mainClass;
		this.animPlayer = new Animation_Player();
		animPlayer.init();
	}
	
	public void update() {
		collision();
		this.centerX += this.speedX;
		this.centerY += this.speedY;
		
		// Gravitace
		if(isFalling || isJumping) {
			if(speedY <= this.MAXFALL) speedY += 0.5f;
			else speedY = this.MAXFALL;
		}
		
		this.weaponX = centerX + 50;
		this.weaponY = centerY - 25;
		this.recBodyU.setRect(centerX - 37, centerY - 64, 74, 64);
		this.recBodyL.setRect(centerX - 37, centerY, 71, 64);
		this.recHandR.setRect(centerX + 34, centerY - 32, 30, 20);
		this.recHandL.setRect(centerX - 64, centerY - 32, 30, 20);
		this.recRadius.setRect(centerX - 112, centerY - 112, 224, 224);
		animPlayer.update();
	}
	
	public void collision() {
		for(Tile e : LevelReader.getAllTiles()) {
			if(this.recRadius.intersects(e.getRecCollision())) {
				
				// Kolize s tøídou Tile
				if(this.recBodyU.intersects(e.getRecCollision())) {
					centerY = e.getY()+Tile.getHeight()+recBodyU.height;
					this.setSpeedY(0);
				} 
				
				if(this.recBodyL.intersects(e.getRecCollision())) {
						speedY = 0f;
						this.centerY = e.getY()-this.recBodyL.height;
						this.isFalling = false;
						this.isJumping = false;
				} else {
						isFalling = true;
				}
				
				if(this.recHandL.intersects(e.getRecCollision())) {
					this.centerX += 5;
				}
				
				if(this.recHandR.intersects(e.getRecCollision())) {
					this.centerX -= 5;
				}
			}
		}
	}

	public void shoot() {
		Projectile p = (Projectile) new Projectile(5, Color.green, 7).clone();
		p.spawnProjectile(weaponX, weaponY);
		projectiles.add(p);
	}
	
	public void paint(Graphics g) {
		g.drawImage(animPlayer.getCurrentImage(), this.getCenterX() - animPlayer.getCurrentImage().getWidth(mainClass) / 2, this.getCenterY() - animPlayer.getCurrentImage().getHeight(mainClass) / 2, mainClass);
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.isMovingLeft = movingLeft;
	}

	public void setMovingRight(boolean movingRight) {
		this.isMovingRight = movingRight;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCovered(boolean covered) {
		this.isCovered = covered;
	}

	public boolean isCovered() {
		return isCovered;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public int getBackgroundStartMove() {
		return backgroundStartMove;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean falling) {
		this.isFalling = falling;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setJumping(boolean jumping) {
		this.isJumping = jumping;
	}

	public float getSpeedX() {
		return speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
}
