package enemy;

import java.awt.Color;
import java.awt.Rectangle;

import projectile.Projectile;
import animation.Animation;
import animation.Animation_Heliboy;
import core.MainClass;

public class Enemy_Heliboy extends Enemy {
	final static int MAXHP = 20;
	final static int DAMAGE = 2;
	final static int WEAPON_DIFF_X = -42;
	final static int WEAPON_DIFF_Y = 8;
	final static int WEAPON_SPEED_X = -3;
	final static Projectile PROJECTILE = new Projectile(DAMAGE, Color.red, WEAPON_SPEED_X);
	final static Animation ANIM = new Animation_Heliboy();
	
	// Collision rectangles
	private Rectangle recCollision;

	public Enemy_Heliboy() {
		super(MAXHP, PROJECTILE, ANIM, WEAPON_DIFF_X, WEAPON_DIFF_Y);
		recCollision = new Rectangle(this.centerX - 38, this.centerY - 28, 63, 68);
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
	}
}
