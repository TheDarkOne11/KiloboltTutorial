package enemy;

import java.awt.Color;

import projectile.Projectile;



public class Enemy_Heliboy extends Enemy {
	final static int MAXHP = 10;
	final static int DAMAGE = 2;
	final static int WEAPON_DIFF_X = -42;
	final static int WEAPON_DIFF_Y = 8;
	final static int WEAPON_SPEED_X = -3;
	Projectile projectile;

	public Enemy_Heliboy(int centerX, int centerY) {
		super(MAXHP, WEAPON_DIFF_X, WEAPON_DIFF_Y);
		this.add(centerX, centerY);
		projectile = new Projectile(DAMAGE, Color.red, WEAPON_SPEED_X);
	}

	public static int getMaxhp() {
		return MAXHP;
	}

	public Projectile getProjectile() {
		return projectile;
	}

}
