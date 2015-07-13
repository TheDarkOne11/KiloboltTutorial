package core;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Okno a posluchaèe.
 * 
 * @author Petr
 * 
 */
public class FrameClass extends JFrame {
	public static void main(String[] args) {
		// Ochrana proti obèasné bílé obrazovce
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new FrameClass();
			}
		});
	}

	public FrameClass() {
		this.setTitle("KiloboltTutorial");
		this.setExtendedState(MAXIMIZED_BOTH);
		//this.setUndecorated(true);
		this.setVisible(true);
		this.setFocusable(true);
		this.addWindowListener(new CloseWindow_WindowAdapter());

		MainClass mainClass = new MainClass(this);

		this.add(mainClass);
	}

	class CloseWindow_WindowAdapter extends WindowAdapter {

		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}
}
