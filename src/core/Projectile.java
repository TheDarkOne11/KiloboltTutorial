package core;

public class Projectile {
	private int x, y, speedX, distance;
	private boolean visible;

	
	
	public Projectile(int startX, int startY) {
		this.x = startX;
		this.y = startY;
		this.speedX = 7;
		this.visible = true;
		this.distance = 800;
	}

	public void update() {
		x += speedX;
		if (x > this.distance) {
		   visible = false;
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
