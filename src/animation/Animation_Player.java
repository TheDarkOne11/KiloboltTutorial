package animation;

import java.awt.Image;

import core.MainClass;
import core.Player;

/**
 * All of player animations are here.
 * @author Petr
 *
 */
public class Animation_Player extends Animation {
	private Image character_static, character_static2, character_static3;
	private Image currentImage, characterCover, characterJumped;
	
	/**
	 * Initialization of all player animations.
	 */
	public void init() {
		// Image init
		character_static = MainClass.getImage("\\data\\character.png");
		character_static2 = MainClass.getImage("\\data\\character2.png");
		character_static3 = MainClass.getImage("\\data\\character3.png");
		
		characterCover = MainClass.getImage("\\data\\cover.png");
		characterJumped = MainClass.getImage("\\data\\jumped.png");
		
		// Animated when static.
		this.addFrame(character_static, 2000);
		this.addFrame(character_static2, 50);
		this.addFrame(character_static3, 100);
		this.addFrame(character_static2, 50);
		
		this.currentImage = super.getCurrentImage();
	}
	
	/**
	 * Checks what animation should be up and updates it.
	 */
	public void update() {
		if (MainClass.getPlayer().isJumping()) {
			currentImage = characterJumped;
		} else if(Math.abs(MainClass.getPlayer().getSpeedY()) > Player.getFallspeed() ) {
			currentImage = characterJumped;
		} else if(MainClass.getPlayer().isCovered() == true) {
			currentImage = characterCover;
		} else if(MainClass.getPlayer().isJumping() == false && MainClass.getPlayer().isCovered() == false) {
			currentImage = super.getCurrentImage();
		}
		this.update(25);
	
	}

	public Image getCurrentImage() {
		return currentImage;
	}
	
}
