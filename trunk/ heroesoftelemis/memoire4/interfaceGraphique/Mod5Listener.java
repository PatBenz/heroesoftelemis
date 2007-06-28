/*
 * Created on 28-mars-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package interfaceGraphique;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;

/**
 * @author Mbenza B. Patrick
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Mod5Listener implements ActionListener {
	public void actionPerformed(ActionEvent event){
		if(Home.done<2){JOptionPane.showMessageDialog(null,"Please select two images");}
		else{
		Home.frame.disable();		
		JFrame frame=new JFrame("Module 5: Grille et croix");
		
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setMenuBar(new MenuBar());
	    frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                Home.frame.enable();
            }
        });
		final JPanel p= new JPanel();
		p.setLayout(new BorderLayout());
		p.setBackground(Color.lightGray);
		ImageIcon ic=new ImageIcon(Home.firstIm);
		JLabel imag=new JLabel(ic);
		p.add(imag);
		
		 frame.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
            	System.out.println("Coordonn�es du click : " + e.getX() + ";"+e.getY());
            }
            public void mouseEntered(MouseEvent e){
            	p.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            }
            public void mouseExited (MouseEvent e){
            	p.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            	}
            public void mousePressed(MouseEvent e){}
            public void mouseReleased(MouseEvent e){}
        });
		
		frame.getContentPane().add(p);
		frame.pack();
		frame.show();
	
	}
	}
}
