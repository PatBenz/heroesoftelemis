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
//import java.awt.BorderLayout;
import java.awt.*;
import java.io.*;
import javax.swing.*;
public class Mod5Tab extends JPanel{
	public Mod5Tab(){
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		setBackground(Color.black);
		//-----------------------------
		JPanel help=new JPanel();
		help.setBackground(Color.blue);
		help.setLayout(new BoxLayout(help,BoxLayout.Y_AXIS));
		
		JLabel title=new JLabel("Ce module exige les pr�requis suivants:");
		JLabel t1=new JLabel("		- Une grille sur l'image");
		JLabel t2=new JLabel("		- Une croix pour le stiching");
		title.setForeground(Color.white);
		t1.setForeground(Color.white);
		t2.setForeground(Color.white);
		
		
		JTextArea ta=new JTextArea(20,30);
		File m5=new File("module5.txt");
		try{
		FileReader fr=new FileReader(m5);
		BufferedReader br=new BufferedReader(fr);
		String info="";
		String line=br.readLine();
		while(line!=null){info+=line+"\n"; line=br.readLine();}
		ta.setText(info);
		//ta.setForeground(Color.darkGray);
		ta.setBackground(Color.lightGray);
		ta.setDisabledTextColor(Color.darkGray);
		ta.disable();
		}catch(Exception e){System.out.println(""+e);}
		
		JScrollPane SP=new JScrollPane(ta);
		//SP.setPreferredSize(new Dimension(100,100));
		SP.setMaximumSize(new Dimension(1000,300));
		
		
		help.add(title);
		help.add(t1);
		help.add(t2);
		help.add(SP);
		
		JButton b=new JButton("Ajuster et Coller");
		b.addActionListener(new Mod5Listener());
		add(help);
		add(b);
		

	}
}
