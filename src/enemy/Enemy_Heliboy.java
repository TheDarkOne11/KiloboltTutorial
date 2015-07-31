package enemy;

import java.awt.Color;
import java.awt.Rectangle;

import animation.Animation;
import animation.Animation_Heliboy;
import core.MainClass;
import core.Player;
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
	
	@SuppressWarnings("unused")
	private Player player = MainClass.getPlayer();
	
	// Collision rectangles
	private Rectangle recCollision;

	public Enemy_Heliboy() {
		super(MAXHP, WIDTH, HEIGHT, RATE_OF_FIRE, MOVESPEED, PROJECTILE, ANIM, WEAPON_DIFF_X, WEAPON_DIFF_Y);
		recCollision = new Rectangle(this.centerX - 38, this.centerY - 28, 63, 68);
	}
	
	public void AI() {
		/*
		// Pohyb k hr·Ëi.
			int distance = 200;
			if(this.centerX < player.getCenterX() - distance) {
				this.centerX += this.movespeed;
				movingRight = true;
			} else if(this.centerX > player.getCenterX() + distance) {
				this.centerX -= this.movespeed;
				movingRight = false;
			}
			
			if(this.centerY < player.getCenterY()) {
				this.centerY += this.movespeed;
			} else if(this.centerY > player.getCenterY()) {
				this.centerY -= this.movespeed;
			}
			
			// Pokud jsou p¯Ìmo u hr·Ëe.
			if(!player.isMovingLeft()&!player.isMovingRight()&!player.isJumping()) {
				if(this.movespeed >= Math.abs(player.getCenterX() -this.centerX)){
					this.centerX = player.getCenterX();
				}
				
				if(this.movespeed >= Math.abs(player.getCenterY() - this.centerY)) {
					this.centerY = player.getCenterY();
				}
			}
			
			//Palba
			if(centerY > player.getCenterY() - player.getHeight()/2 && centerY < player.getCenterY() + player.getHeight()/2) {
				attack();
			}
			*/
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
		recCollision.x = this.centerX - 38;
		recCollision.y = this.centerY - 28;
		AI();
	}
}
