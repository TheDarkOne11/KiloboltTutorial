package enemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import animation.Animation;
import animation.Animation_Heliboy;
import core.MainClass;
import projectile.Projectile;

public class Enemy_Heliboy extends Enemy {
	final static int MAXHP = 20;
	final static int DAMAGE = 2;
	final static double RATE_OF_FIRE = 30;
	final static float MOVESPEED = 3f;
	final static int WEAPON_DIFF_X = -42;
	final static int WEAPON_DIFF_Y = 8;
	final static int WEAPON_SPEED_X = 3;
	final static int WIDTH = 96;
	final static int HEIGHT = 96;
	final static Projectile PROJECTILE = new Projectile(DAMAGE, Color.red, WEAPON_SPEED_X);
	final static Animation ANIM = new Animation_Heliboy();
	
	// Collision rectangles
	private Rectangle recCollision;
	
	// AI
	private int Max;
	private int Min;
	private int distance = 0;

	public Enemy_Heliboy() {
		super(MAXHP, WIDTH, HEIGHT, RATE_OF_FIRE, MOVESPEED, PROJECTILE, ANIM, WEAPON_DIFF_X, WEAPON_DIFF_Y);
		Min = 100;
		Max = 300;
	}
	
	public void AI() {
		
		// Pohyb k hr·Ëi.	
			
		if(distance == 0) distance = Min + (int)(Math.random() * ((Max - Min) + 1));

		if(pointCenter.x < player.getCenterPoint().x) movingRight = true;
		else movingRight = false;
		
		if(pointCenter.x < player.getCenterPoint().x - distance) {
			pointCenter.x += this.movespeed;
		} else if(pointCenter.x > player.getCenterPoint().x + distance) {
			pointCenter.x -= this.movespeed;
		}
		
		if(pointCenter.y < player.getCenterPoint().y) {
			pointCenter.y += this.movespeed;
		} else if(pointCenter.y > player.getCenterPoint().y) {
			pointCenter.y -= this.movespeed;
		}
		
		// Pokud jsou p¯Ìmo u hr·Ëe.
		if(!player.isMovingLeft()&!player.isMovingRight()&!player.isJumping()) {
			if(this.movespeed >= Math.abs(player.getCenterPoint().x -pointCenter.x)){
				pointCenter.x = player.getCenterPoint().x;
			}
			
			if(this.movespeed >= Math.abs(player.getCenterPoint().y - pointCenter.y)) {
				pointCenter.y = player.getCenterPoint().y;
			}
		}
		/*
		//Palba
		if(centerY > player.getCenterPoint().y - player.getHeight()/2 && centerY < player.getCenterPoint().y + player.getHeight()/2) {
			attack();
		}*/
			
	}

	public void collision() {
		// Projectile collision
		for(Projectile p : MainClass.getProjectiles()) {
			if(p.entity.equals(MainClass.getPlayer())) {
				if(p.getRec().intersects(recRadius)) {
					// If projectile hits any rectangle
					if(p.getRec().intersects(recCollision)) {
						this.hit(p);
					}
				}
			}
		}
	}
	
	public void updateRec() {
		recCollision = new Rectangle(pointCenter.x - 38, pointCenter.y - 28, 63, 68);
		/*recNorth = new Rectangle(centerPoint.x - 28, centerPoint.y - 38, 53, 5);
		recSouth = new Rectangle(centerPoint.x - 28, centerPoint.y + 38, 53, 5);
		recEast = new Rectangle(centerPoint.x + 28, centerPoint.y - 28, 5, 63);
		recWest = new Rectangle(centerPoint.x - 38, centerPoint.y - 28, 5, 63);*/
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		/*g.drawRect(recNorth.x, recNorth.y, recNorth.width, recNorth.height);
		g.drawRect(recSouth.x, recSouth.y, recSouth.width, recSouth.height);
		g.drawRect(recEast.x, recEast.y, recEast.width, recEast.height);
		g.drawRect(recWest.x, recWest.y, recWest.width, recWest.height);*/
	}

	public Rectangle getRecCollision() {
		return recCollision;
	}
}
