package enemy;


public class Heliboy extends Enemy {
	final static int MAXHP = 10;
	final static int DAMAGE = 2;

	public Heliboy(int centerX, int centerY) {
		super(MAXHP, DAMAGE);
		this.add(centerX, centerY);
	}

	public static int getMaxhp() {
		return MAXHP;
	}

	public static int getDamage() {
		return DAMAGE;
	}

}
