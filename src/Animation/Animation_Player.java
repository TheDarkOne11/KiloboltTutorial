package animation;

import java.awt.Image;

import core.MainClass;

/**
 * All of player animations are here.
 * @author Petr
 *
 */
public class Animation_Player extends Animation {
	private Image character_static, character_static2, character_static3;
	private Image currentImage, characterCover, characterJumped;
	private Image character_walk;
	private MainClass mainClass;
	
	public Animation_Player(MainClass mainclass) {
		this.mainClass = mainclass;
	}
	
	/**
	 * Initialization of all player animations.
	 */
	public void init() {
		// Image init
		character_static = mainClass.getImage(mainClass.getBase(), "data/character.png");
		character_static2 = mainClass.getImage(mainClass.getBase(), "data/character2.png");
		character_static3 = mainClass.getImage(mainClass.getBase(), "data/character3.png");
		
		characterCover = mainClass.getImage(mainClass.getBase(), "data/cover.png");
		characterJumped = mainClass.getImage(mainClass.getBase(), "data/jumped.png");
		
		// Animated when static.
		this.addFrame(character_static, 1250);
		this.addFrame(character_static2, 50);
		this.addFrame(character_static3, 50);
		this.addFrame(character_static2, 50);
		
		this.currentImage = super.getCurrentImage();
	}
	
	/**
	 * Update of all player animations.
	 */
	public void update() {
		if (mainClass.getPlayer().isJumped()) {
			currentImage = characterJumped;
		} else if(mainClass.getPlayer().isCovered() == true) {
			currentImage = characterCover;
		} else if(mainClass.getPlayer().isJumped() == false && mainClass.getPlayer().isCovered() == false) {
			currentImage = super.getCurrentImage();
		}
		this.update(10);
	
	}

	public Image getCurrentImage() {
		return currentImage;
	}
	
}
