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
	private MainClass mainClass;
	
	public Animation_Heliboy(MainClass mainClass) {
		this.mainClass = mainClass;
	}

	public void init() {
		// Image init
		//TODO "Blik�n�" animace na za��tku animace m��e b�t zp�soben� t�m, �e se obr�zek se na�te p�i paintu.
		//TODO viz kniha str. 209
		heliboy = mainClass.getImage(MainClass.getBase(), "data/heliboy.png");
		heliboy2 = mainClass.getImage(MainClass.getBase(), "data/heliboy2.png");
		heliboy3 = mainClass.getImage(MainClass.getBase(), "data/heliboy3.png");
		heliboy4 = mainClass.getImage(MainClass.getBase(), "data/heliboy4.png");
		heliboy5 = mainClass.getImage(MainClass.getBase(), "data/heliboy5.png");
		
		// Rotor spinning
		this.addFrame(heliboy, 100);
		this.addFrame(heliboy2, 100);
		this.addFrame(heliboy3, 100);
		this.addFrame(heliboy4, 100);
		this.addFrame(heliboy5, 100);
		this.addFrame(heliboy4, 100);
		this.addFrame(heliboy3, 100);
		this.addFrame(heliboy2, 100);
	}

	public void update() {
		this.update(25);
	}
}
