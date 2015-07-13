package core;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.SwingUtilities;

/**
 * Okno a poslucha�e.
 * 
 * @author Petr
 * 
 */
public class FrameClass extends Frame {
	public static void main(String[] args) {
		// Ochrana proti ob�asn� b�l� obrazovce
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FrameClass();
			}
		});
	}

	public FrameClass() {
		this.setTitle("KiloboltTutorial");
		//this.setExtendedState(MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		this.setFocusable(true);
		this.addWindowListener(new CloseWindow_WindowAdapter());

		MainClass mainClass = new MainClass(this);
	}

	class CloseWindow_WindowAdapter extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
