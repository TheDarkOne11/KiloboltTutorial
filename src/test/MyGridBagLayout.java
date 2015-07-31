package test;

import java.awt.Button;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/** GridBagLayout funguje na systému møížky. Každá komponenta je umístìna v této møížce. **/

public class MyGridBagLayout extends Frame {
	GridBagLayout gbl;
	GridBagConstraints gbc;	// Promìnné, které využívá gbl. Manipulují s komponentou (velikost, umístìní atd.)
	
	void makeButton(String s, int gridx, int gridy) {
		Button bt = new Button(s);
		gbc.gridx = gridx;	// Kolik bunìk møížky zabere button na šíøku.
		gbc.gridy = gridy;  // Kolik bunìk møížky zabere button na výšku.
		gbl.setConstraints(bt, gbc);  // Gbc a button je pøipojen k GridBagLayout
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
		gbc.fill = GridBagConstraints.BOTH; // Každá komponenta má povoleno zcela vyplnit pøidìlené místo.
		
		gbc.weightx = 1.0;  // Pomìr, který urèí, kolik volného místa má být dáno ke komponentì
							// v daném smìru. Závislý na gbc.fill()
		
		// GridBagConstraints.REMAINDER Øíká, že je poslední komponentou na øádku(èi sloupci).
		// GridBagConstraints.RELATIVE  Øíká, že je umístìn za poslední komponentou na øádku(èi sloupci)
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
		
		// Nastaví, že další pøidaná komponenta bude v buòce 0/2
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
