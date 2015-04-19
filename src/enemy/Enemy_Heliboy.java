package enemy;

import java.awt.Color;

import projectile.Projectile;
import animation.Animation;
import animation.Animation_Heliboy;

public class Enemy_Heliboy extends Enemy {
	final static int MAXHP = 10;
	final static int DAMAGE = 2;
	final static int WEAPON_DIFF_X = -42;
	final static int WEAPON_DIFF_Y = 8;
	final static int WEAPON_SPEED_X = -3;
	final static Projectile PROJECTILE = new Projectile(DAMAGE, Color.red, WEAPON_SPEED_X);
	final static Animation ANIM = new Animation_Heliboy();

	public Enemy_Heliboy() {
		super(MAXHP, PROJECTILE, ANIM, WEAPON_DIFF_X, WEAPON_DIFF_Y);
	}
}
