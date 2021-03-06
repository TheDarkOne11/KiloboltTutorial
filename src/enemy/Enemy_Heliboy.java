package enemy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import animation.Animation;
import animation.Animation_Heliboy;
import core.MainClass;
import core.Sound;
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
		
		// Sounds
		this.attackSound = Sound.loadClip("heliboyAttack");
		this.deathSound = Sound.loadClip("heliboyDeath");
	}
	
	public void AI() {
		
		// Pohyb k hr��i.	
			
		if(distance == 0) distance = Min + (int)(Math.random() * ((Max - Min) + 1));
		this.moveToPlayer(distance);
		
		//Palba
		if(this.getPointCenter().y > player.getPointCenter().y - player.getHeight()/2 && this.getPointCenter().y < player.getPointCenter().y + player.getHeight()/2) {
			attack();
		}
			
	}

	public void collision() {
		// Projectiles-shot-by-player collision
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
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public Rectangle getRecCollision() {
		return recCollision;
	}
}
