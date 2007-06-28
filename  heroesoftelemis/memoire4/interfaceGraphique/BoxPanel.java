/*
 * Created on 28-mars-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package interfaceGraphique;

/**
 * @author Mbenza B. Patrick
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import javax.swing.*;

import java.awt.*;
public class BoxPanel extends JPanel{
	
public BoxPanel(){
	setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	setBackground(Color.getColor("DarkSlateGray"));
	JButton b1=new JButton("Module 1: Hardware ideal");
	b1.addActionListener(new Mod1Listener());
	JButton b2=new JButton("Module 2: Enti�rement param�tr�");
	b2.addActionListener(new Mod2Listener());
	JButton b3=new JButton("Module 3: Avec rep�re");
	b3.addActionListener(new Mod3Listener());
	JButton b4=new JButton("Module 4: Aucune donn�e");
	b4.addActionListener(new Mod4Listener());
	JButton b5=new JButton("Module 5: Grille et croix");
	b5.addActionListener(new Mod5Listener());
	
	//ImageIcon image=new ImageIcon("assemblage2.JPG");
	//JLabel imageLabel= new JLabel(image);

	add(b1);
	add(b2);
	add(b3);
	add(b4);
	add(b5);
	//add(imageLabel);
}
}
