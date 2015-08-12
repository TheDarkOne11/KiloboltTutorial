package core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import animation.Animation_Player;
import level.LevelReader;
import level.Tile;
import level.TileSpawn;
import projectile.Projectile;

//TODO Pohrát si s velikostí nepøátel a hráèe vùèi Tile.
public class Player extends Entity {
	private final static float JUMPSPEED = -10f;
	private final static float MOVESPEED = 5f;
	private final static float MAXFALLSPEED = 10f;
	private final static float FALLSPEED = 0.5f;
	private final static int WIDTH = 128;
	private final static int HEIGHT = 128;
	
	public boolean dead;
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
	/** Stores time of shooting projectile. */
	private long time;
	
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
	
	
	public Player(MainClass mainClass) {
		super(30, WIDTH, HEIGHT, 60, MOVESPEED, new Projectile(5, Color.black, 7));
		this.mainClass = mainClass;
	}
	
	public void addPlayer() {
		centerX = TileSpawn.x;
		centerY = TileSpawn.y;
		this.anim = new Animation_Player();
		this.anim.init();
	}
	
	public void update() {
		collision();
		centerX += speedX;
		centerY += speedY;

		// Gravitace
		if(isFalling || jumped) {
			jumped = false;
			if(speedY <= MAXFALLSPEED) speedY += FALLSPEED;
			else speedY = MAXFALLSPEED;
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
		
		try {
			anim.update();
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	public void  collision() {
		// Projectile collision
		for(Projectile p : MainClass.getProjectiles()) {
			if(!p.entity.equals(this)) {
				if(p.getRec().intersects(recRadius)) {
					// If projectile hits any rectangle
					if(p.getRec().intersects(recBodyL) || p.getRec().intersects(recBodyU) || p.getRec().intersects(recFootL) || p.getRec().intersects(recFootR) || p.getRec().intersects(recHandL) || p.getRec().intersects(recHandR)) {
						this.hit(p);
					}
				}
			}
		}
		
		// Terrain collision
		for(Tile e : LevelReader.getAllTiles()) {
			if(recRadius.intersects(e.getRecCollision())) {
				
				// Kolize s tøídou Tile
				if(recBodyU.intersects(e.getRecCollision())) {
					centerY = e.getY()+Tile.getHeight()+recBodyU.height;
					speedY = 0;
				} 
				
				if(recBodyL.intersects(e.getRecCollision()) && !jumped) {
					speedY = 0f;
					//centerY = e.getY()-recBodyL.height+1;
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
		g.drawImage(anim.getCurrentImage(), getCenterX() - anim.getCurrentImage().getWidth(mainClass) / 2, getCenterY() - anim.getCurrentImage().getHeight(mainClass) / 2, mainClass);
		super.paintHpBar(g);
		
		/*
		g.setColor(Color.red);
		g.drawRect(recBodyL.x, recBodyL.y, recBodyL.width, recBodyL.height);
		g.drawRect(recBodyU.x, recBodyU.y, recBodyU.width, recBodyU.height);
		g.drawRect(recFootL.x, recFootL.y, recFootL.width, recFootL.height);
		g.drawRect(recFootR.x, recFootR.y, recFootR.width, recFootR.height);
		g.drawRect(recHandR.x, recHandR.y, recHandR.width, recHandR.height);
		g.drawRect(recHandL.x, recHandL.y, recHandL.width, recHandL.height);
		g.drawRect(recRadius.x, recRadius.y, recRadius.width, recRadius.height);*/
	}

	public void die() {	
		System.out.println("Player dead");
		this.dead = true;
	}

	public void attack() {
		if((this.time + (60*1000)/this.rateOfFire) < System.currentTimeMillis() | this.time == 0) {
			projectile.spawnProjectile(this, true);
			time = System.currentTimeMillis();
		}
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

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public static float getJumpspeed() {
		return JUMPSPEED;
	}

	public int getHeight() {
		return height;
	}

	public static float getFallspeed() {
		return FALLSPEED;
	}
}
