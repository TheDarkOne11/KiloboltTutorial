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

//TODO Pohr�t si s velikost� nep��tel a hr��e v��i Tile.
public class Player extends Entity {
	private final float JUMPSPEED = -10f;
	private final float MOVESPEED = 5f;
	private final float MAXFALL = 10f;

	private int weaponX = 50;
	private int weaponY = -25;
	private int backgroundStartMove = 200;
	private boolean isMovingLeft = false;
	private boolean isMovingRight = false;
	private boolean isCovered = false;
	private boolean isFalling = true;
	/** Used for animations.*/
	private boolean isJumping = false;
	/** Used to correct collision. Otherwise he wouldn't jump. */
	private boolean jumped = false;
	private MainClass mainClass;
	private Animation_Player animPlayer;
	
	// Collisions
	/** Upper body collision rectangle. */
	private Rectangle recBodyU = new Rectangle();	
	/** Lower body collision rectangle. */
	private Rectangle recBodyL = new Rectangle();
	private Rectangle recFootR = new Rectangle();
	private Rectangle recFootL = new Rectangle();
	private Rectangle recHandR = new Rectangle();
	private Rectangle recHandL = new Rectangle();
	/** Rectangular radius where collisions are turned on. */
	private Rectangle recRadius = new Rectangle();

	/** Player shooting projectiles. */
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	public Player(MainClass mainClass) {
		centerX = TileSpawn.x;
		centerY = TileSpawn.y;
		this.mainClass = mainClass;
		animPlayer = new Animation_Player();
		animPlayer.init();
	}
	
	public void update() {
		collision();
		centerX += speedX;
		centerY += speedY;

		// Gravitace
		if(isFalling || jumped) {
			jumped = false;
			if(speedY <= MAXFALL) speedY += 0.5f;
			else speedY = MAXFALL;
		}
		
		weaponX = centerX + 50;
		weaponY = centerY - 25;
		
		// Collision Rectangles
		recBodyU.setRect(centerX - 28, centerY - 64, 56, 64);
		recBodyL.setRect(centerX - 26, centerY, 53, 64);
		
		recFootR.setRect(recBodyL.x+recBodyL.width, recBodyL.y, 10, 55);
		recFootL.setRect(recBodyL.x-10, recBodyL.y, 10, 55);
		
		recHandR.setRect(centerX + 34, centerY - 32, 30, 20);
		recHandL.setRect(centerX - 64, centerY - 32, 30, 20);
		recRadius.setRect(centerX - 112, centerY - 112, 224, 224);
		animPlayer.update();
	}

	public void  collision() {
		for(Tile e : LevelReader.getAllTiles()) {
			if(recRadius.intersects(e.getRecCollision())) {
				
				// Kolize s t��dou Tile
				if(recBodyU.intersects(e.getRecCollision())) {
					centerY = e.getY()+Tile.getHeight()+recBodyU.height;
					speedY = 0;
				} 
				
				if(recBodyL.intersects(e.getRecCollision()) && !jumped) {
						speedY = 0f;
						centerY = e.getY()-recBodyL.height+1;
						isFalling = false;
						isJumping = false;
				} else {
						isFalling = true;
				}
				
				if(recFootL.intersects(e.getRecCollision())) {
					centerX += 5;					
				}
				
				if(recFootR.intersects(e.getRecCollision())) {
					centerX -= 5;
				}
				
				if(recHandL.intersects(e.getRecCollision())) {
					centerX += 5;
				}
				
				if(recHandR.intersects(e.getRecCollision())) {
					centerX -= 5;
				}
				
			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(animPlayer.getCurrentImage(), getCenterX() - animPlayer.getCurrentImage().getWidth(mainClass) / 2, getCenterY() - animPlayer.getCurrentImage().getHeight(mainClass) / 2, mainClass);
		/*
		g.drawRect(recBodyL.x, recBodyL.y, recBodyL.width, recBodyL.height);
		g.drawRect(recBodyU.x, recBodyU.y, recBodyU.width, recBodyU.height);
		g.setColor(Color.red);
		g.drawRect(recFootL.x, recFootL.y, recFootL.width, recFootL.height);
		g.drawRect(recFootR.x, recFootR.y, recFootR.width, recFootR.height);
		g.drawRect(recHandR.x, recHandR.y, recHandR.width, recHandR.height);
		g.drawRect(recHandL.x, recHandL.y, recHandL.width, recHandL.height);*/
	}

	public void die() {		
	}

	public void attack() {
		Projectile p = (Projectile) new Projectile(5, Color.black, 7).clone();
		p.spawnProjectile(this);
		projectiles.add(p);
	}

	public int getWeaponX() {
		return weaponX;
	}

	public int getWeaponY() {
		return weaponY;
	}

	public int getBackgroundStartMove() {
		return backgroundStartMove;
	}

	public boolean isMovingLeft() {
		return isMovingLeft;
	}

	public boolean isMovingRight() {
		return isMovingRight;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public boolean isJumping() {
		return isJumping;
	}

	public void setWeaponX(int weaponX) {
		this.weaponX = weaponX;
	}

	public void setWeaponY(int weaponY) {
		this.weaponY = weaponY;
	}

	public void setBackgroundStartMove(int backgroundStartMove) {
		this.backgroundStartMove = backgroundStartMove;
	}

	public void setMovingLeft(boolean isMovingLeft) {
		this.isMovingLeft = isMovingLeft;
	}

	public void setMovingRight(boolean isMovingRight) {
		this.isMovingRight = isMovingRight;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	public void setJumping(boolean isJumping) {
		this.isJumping = isJumping;
	}

	public boolean isCovered() {
		return isCovered;
	}

	public void setCovered(boolean isCovered) {
		this.isCovered = isCovered;
	}

	public boolean isJumped() {
		return jumped;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	public float getJUMPSPEED() {
		return JUMPSPEED;
	}

	public float getMOVESPEED() {
		return MOVESPEED;
	}

	public float getMAXFALL() {
		return MAXFALL;
	}
}
