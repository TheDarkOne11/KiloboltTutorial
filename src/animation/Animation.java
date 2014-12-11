package animation;

import java.awt.Image;
import java.util.ArrayList;

/** Animations for characters. */
public abstract class Animation {
	private ArrayList<AnimFrame> frames;
	private int currentFrame;
	private long animTime; // How long does parts of animation take.
	private long totalDuration; // How long does whole animation take.

	public Animation() {
		frames = new ArrayList<AnimFrame>();
		totalDuration = 0;

		synchronized (this) {
			animTime = 0;
			currentFrame = 0;
		}
	}

	public synchronized void addFrame(Image image, long duration) {
		totalDuration += duration;
		frames.add(new AnimFrame(image, totalDuration));
	}

	/**
	 * Updates currentFrame.
	 * 
	 * @param elapsedTime
	 *            is time since last update.
	 */
	public synchronized void update(long elapsedTime) {
		if (frames.size() > 1) {
			animTime += elapsedTime;
			if (animTime >= totalDuration) {
				animTime = animTime % totalDuration;
				currentFrame = 0;

			}

			if (animTime > getFrame(currentFrame).endTime) {
				currentFrame++;

			}
		}
	}

	/**
	 * Gets image that is currently animated.
	 * @return
	 */
	public synchronized Image getCurrentImage() {
		if (frames.size() == 0) {
			return null;
		} else {
			return getFrame(currentFrame).image;
		}
	}
	
	public abstract void init();
	public abstract void update();

	private AnimFrame getFrame(int i) {
		return frames.get(i);
	}

	private class AnimFrame {

		Image image;
		long endTime;

		public AnimFrame(Image image, long endTime) {
			this.image = image;
			this.endTime = endTime;
		}
	}
}
