package animation;

import java.awt.Image;

import core.MainClass;

/**
 * All of heliboy animations are here.
 * @author Petr
 *
 */
public class Animation_Heliboy extends Animation {
	private Image heliboy, heliboy2, heliboy3, heliboy4, heliboy5;
	
	public void init() {
		// Image init
		//TODO "Blikání" animace na zaèátku animace mùže být zpùsobené tím, že se obrázek se naète pøi paintu.
		//TODO viz kniha str. 209
		heliboy = MainClass.getImage("data/heliboy.png");
		heliboy2 = MainClass.getImage("data/heliboy2.png");
		heliboy3 = MainClass.getImage("data/heliboy3.png");
		heliboy4 = MainClass.getImage("data/heliboy4.png");
		heliboy5 = MainClass.getImage("data/heliboy5.png");
		
		// Rotor spinning
		this.addFrame(heliboy, 100);
		this.addFrame(heliboy2, 50);
		this.addFrame(heliboy3, 100);
		this.addFrame(heliboy4, 50);
		this.addFrame(heliboy5, 100);
		this.addFrame(heliboy4, 50);
		this.addFrame(heliboy3, 100);
		this.addFrame(heliboy2, 50);
	}

	public void update() {
		this.update(25);
	}
}
