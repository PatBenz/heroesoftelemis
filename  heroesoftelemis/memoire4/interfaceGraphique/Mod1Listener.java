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
import java.awt.event.*;

import javax.swing.JFrame;


public class Mod1Listener implements ActionListener {
	public void actionPerformed(ActionEvent event){
		JFrame frame=new JFrame("Module 1:Hardware ideal");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.show();
	
	}

}
