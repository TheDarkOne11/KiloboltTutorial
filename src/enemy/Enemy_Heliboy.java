package enemy;

import java.awt.Color;

import projectile.Projectile;
import animation.Animation_Heliboy;

public class Enemy_Heliboy extends Enemy {
	final static int MAXHP = 10;
	final static int DAMAGE = 2;
	final static int WEAPON_DIFF_X = -42;
	final static int WEAPON_DIFF_Y = 8;
	final static int WEAPON_SPEED_X = -3;
	Projectile projectile;

	public Enemy_Heliboy() {
		super(MAXHP, WEAPON_DIFF_X, WEAPON_DIFF_Y);
		projectile = new Projectile(DAMAGE, Color.red, WEAPON_SPEED_X);
		//TODO Možná se pokusit pøedávání animací mezi touto a rodièovskou tøídou udìlat trochu blbuvzdornìjší.
		super.anim = new Animation_Heliboy();
		super.anim.init();
	}

	public void update() {
		super.update();
		super.anim.update();
	}

	public static int getMaxhp() {
		return MAXHP;
	}

	public Projectile getProjectile() {
		return projectile;
	}

}
