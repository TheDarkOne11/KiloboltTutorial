package core;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	/*** * Získání zvukového souboru.
	 * @param fileName cesta k zvuk. souboru.
	 * @return zvukový soubor
	 */
	public static Clip loadClip(String fileName) {
		Clip clip = null;
		
		try {
			File file = new File(MainClass.gamePath + "\\data\\sound\\" + fileName + ".wav");
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
			
			clip = AudioSystem.getClip();
			clip.open(audioIn);
			
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return clip;

		
		
	}
	
	/**
	 * Pøehrání zvuk. souboru.
	 * @param clip
	 */
	public static void playClip(Clip clip) {
		
		stopClip(clip);
		clip.start();
	}
	
	
	/**
	 * Zastavení zvuk. souboru, pokud už bìží.
	 * @param clip
	 */
	private static void stopClip(Clip clip) {
		if(clip.isRunning()) {
			clip.stop();
		}
		
		clip.setFramePosition(0);
	}
	
}
