package core;

import java.awt.Image;
import java.util.ArrayList;

/** Animations for characters. */
public class CopyOfAnimation {
	private ArrayList<AnimFrame> frames;
	private int currentFrame;
	private long animTime;
	private int duration;

	public CopyOfAnimation() {
		frames = new ArrayList<AnimFrame>();
		currentFrame = 0;

		synchronized (this) {
			animTime = 0;
			duration = 0;
		}
	}

	public synchronized void addFrame(Image image, int duration) {
		duration += duration;
		frames.add(new AnimFrame(image, duration));
	}

	public synchronized void update() {
		if(frames.size() >= 1)  {
			
		}
	}

	class AnimFrame {
		private Image image;
		private int endTime;

		public AnimFrame(Image image, int endTime) {
			this.image = image;
			this.endTime = endTime;
		}
	}
}
