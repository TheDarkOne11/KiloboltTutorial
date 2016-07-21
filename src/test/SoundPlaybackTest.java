package test;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundPlaybackTest {

	public static void main(String[] args) {
		
		try {
			// File path
			File file = new File("C:\\Users\\Petr\\JAVA_Workspace\\KiloboltTutorial\\src\\test\\soundTest.wav");
			
			Clip clip = loadClip(file);
			playClip(clip);
			
			Thread.sleep(6000);	// Program musí být aktivní po celou dobu pøehrávání zvuku.
		
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
	}

	/**
	 * Získání zvukového souboru.
	 * @param file cesta k zvuk. souboru.
	 * @return zvukový soubor
	 */
	public static Clip loadClip(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		AudioInputStream audioIn = AudioSystem.getAudioInputStream(file);
		
		Clip clip = AudioSystem.getClip();
		clip.open(audioIn);
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
	public static void stopClip(Clip clip) {
		if(clip.isRunning()) {
			clip.stop();
		}
		
		clip.setFramePosition(0);
	}
	
}
