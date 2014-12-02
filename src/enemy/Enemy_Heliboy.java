package enemy;

import java.awt.Color;

import projectile.Projectile;
import core.MainClass;


public class Enemy_Heliboy extends Enemy {
	final static int MAXHP = 10;
	final static int DAMAGE = 2;
	final static int WEAPON_DIFF_X = -42;
	final static int WEAPON_DIFF_Y = 8;
	final static int WEAPON_SPEED_X = -3;
	Projectile projectile;
	MainClass mainClass;

	public Enemy_Heliboy(MainClass mainClass) {
		super(MAXHP, WEAPON_DIFF_X, WEAPON_DIFF_Y);
		this.mainClass = mainClass;
		projectile = new Projectile(DAMAGE, Color.red, WEAPON_SPEED_X);
	}

	public static int getMaxhp() {
		return MAXHP;
	}

	public Projectile getProjectile() {
		return projectile;
	}

}
