package core;

public class Camera {
	private float x, y;
	
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void update(Player player) {
		this.x = MainClass.WIDTH/2 - player.getPointCenter().x;
		this.y = 3*MainClass.HEIGHT/4 - player.getPointCenter().y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}
	
}
