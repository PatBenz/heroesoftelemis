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
import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;
public class Main {
public static void main(String[]args){
	JFrame frame=new JFrame("Assembleur d'images");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	JPanel main= new JPanel();
	main.setLayout(new BorderLayout());
	//main.setBackground(Color.darkGray);
	/*------------north------------------
	JPanel top=new JPanel();
	top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
	JLabel titre=new JLabel("Ouvrez les fichiers et utilisez les fonctionnalit�s sur le panneau de droite");
	top.add(titre);
	top.add(Box.createRigidArea(new Dimension(0,20)));
	main.add(top,BorderLayout.NORTH);
	*///------------west-------------------
	//------------center-----------------
	JTabbedPane tp=new JTabbedPane();
	//tp.addTab("Modules",new ModuleChoicePanel());
	//tp.addTab("Aide",new IntroPanel());
	main.add(tp,BorderLayout.CENTER);
	//------------east-------------------
	//------------south------------------
	//-----------------------------------------
	frame.getContentPane().add(main);
	frame.pack();
	frame.show();
}
}
