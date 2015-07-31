package test;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** GridBagLayout funguje na syst�mu m��ky. Ka�d� komponenta je um�st�na v t�to m��ce. **/

public class MyGridBagLayout extends Frame {
	GridBagLayout gbl;
	GridBagConstraints gbc;	// Prom�nn�, kter� vyu��v� gbl. Manipuluj� s komponentou (velikost, um�st�n� atd.)
	
	void makeButton(String s, int gridx, int gridy) {
		Button bt = new Button(s);
		gbc.gridx = gridx;	// Kolik bun�k m��ky zabere button na ���ku.
		gbc.gridy = gridy;  // Kolik bun�k m��ky zabere button na v��ku.
		gbl.setConstraints(bt, gbc);  // Gbc a button je p�ipojen k GridBagLayout
		System.out.println(s + ": " + gbc.gridx + "/ " + gbc.gridy);
		this.add(bt);
	}
	
	private MyGridBagLayout() {
		//okno
		this.setVisible(true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		// Program
		gbl = new GridBagLayout();
		gbc = new GridBagConstraints();
		this.setLayout(gbl);
		gbc.fill = GridBagConstraints.BOTH; // Ka�d� komponenta m� povoleno zcela vyplnit p�id�len� m�sto.
		
		gbc.weightx = 1.0;  // Pom�r, kter� ur��, kolik voln�ho m�sta m� b�t d�no ke komponent�
							// v dan�m sm�ru. Z�visl� na gbc.fill()
		
		// GridBagConstraints.REMAINDER ��k�, �e je posledn� komponentou na ��dku(�i sloupci).
		// GridBagConstraints.RELATIVE  ��k�, �e je um�st�n za posledn� komponentou na ��dku(�i sloupci)
		makeButton("Button 1", 0, 0);
		makeButton("Button 2", 1, 0);
		makeButton("Button 3", 2, 0);  
		
		gbc.weightx = 0.0;
		gbc.gridwidth = 2;
		makeButton("Button 4", 0, 1);
		gbc.weighty = 1.0;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		makeButton("Button 5", GridBagConstraints.RELATIVE, 1);
		gbc.weighty = 0.0;
		gbc.gridheight = 1;
		
		// Nastav�, �e dal�� p�idan� komponenta bude v bu�ce 0/2
		//gbc.gridx = 0;
		//gbc.gridy = 2;
		makeButton("Button 6", 0, 2);
		
		makeButton("Button 7", 1, 2);
		
		this.setSize(200, 120);
	}



	public static void main(String[] args) {
		new MyGridBagLayout();
	}

}
