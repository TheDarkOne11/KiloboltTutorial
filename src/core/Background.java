package core;

public class Background {
	/** Coordinats of background's upper left corner. */
	private int bgX, bgY;
	private int speedX;

	public Background(int bgX, int bgY) {
		this.bgX = bgX;
		this.bgY = bgY;
		this.speedX = 0;
	}

	public void update() {
		bgX += speedX;
		
		// Obrázky pozadí jsou 2160 x 48, jsou 2 po sobì se støídající.
		// Scroolování obrázkù, pokud dojdu na hranici obrazovky, obrázek se objeví na druhé stranì.
		if (bgX <= -2160) {
			bgX += 4320;
		}
	}

	public int getBgX() {
		return bgX;
	}

	public int getBgY() {
		return bgY;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setBgX(int bgX) {
		this.bgX = bgX;
	}

	public void setBgY(int bgY) {
		this.bgY = bgY;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
}
