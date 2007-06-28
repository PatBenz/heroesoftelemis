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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.WindowAdapter;
//import java.awt.event.WindowEvent;
import java.io.*;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import javax.swing.*;
import javax.media.jai.JAI;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.RenderedOp;
//import javax.imageio.*;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.display.SourceImage;
public class Home extends JPanel{
	public static String premier_path="imageParDefaut.JPG";
	public static String second_path="imageParDefaut.JPG";
	public static String result_path;
	//----------------------------------------------
	public static JFrame frame;
	public static JPanel home,top,left,center;
	public static JTabbedPane right;
	public static JLabel titre,imLbl1,imLbl2,imLbl3,imageLabel1,imageLabel2;
	public static JScrollPane centerSP1,centerSP2;
	public static FilePath1 fp1;
	public static FilePath2 fp2;
	public static int done=0;
	public static BufferedImage firstIm,secondIm,resultIm;
	public static void main(String[]args){
		frame=new JFrame("Assembleur d'images");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		home= new JPanel();
		home.setLayout(new BorderLayout());
		home.setBackground(Color.black);
		//------------top--------------------------
		top=new JPanel();
		top.setLayout(new BoxLayout(top,BoxLayout.Y_AXIS));
		top.setBackground(Color.getColor("blue"));
		titre=new JLabel("Ouvrez les fichiers et utilisez les fonctionnalit�s sur le panneau de droite");
		titre.setForeground(Color.lightGray);
		top.add(titre);
		top.add(Box.createRigidArea(new Dimension(0,20)));
		home.add(top,BorderLayout.NORTH);
		///------------left--------------------------
		left=new JPanel();
		left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
		left.setBackground(Color.black);
		ImageIcon im1=new ImageIcon("e.jpg");
		ImageIcon im2=new ImageIcon("f.jpg");
		ImageIcon im3=new ImageIcon("assemblage2.JPG");
		imLbl1= new JLabel(im1);
		imLbl2= new JLabel(im2);
		imLbl3= new JLabel(im3);
		
		
		//left.add(Box.createRigidArea(new Dimension(0,20)));
		left.add(imLbl1);
		//left.add(Box.createRigidArea(new Dimension(0,20)));
		left.add(imLbl2);
		left.add(Box.createRigidArea(new Dimension(0,20)));
		left.add(imLbl3);
		home.add(left,BorderLayout.WEST);
		//------------center--------------------------
		center=new JPanel();
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		center.setBackground(Color.black);
		//browse file
		fp1= new FilePath1();
		//scroll image 1
		ImageIcon image1=new ImageIcon(premier_path);
		imageLabel1= new JLabel(image1);
		centerSP1=new JScrollPane(imageLabel1);
		centerSP1.setAutoscrolls(true);
		centerSP1.setRequestFocusEnabled(true);
		centerSP1.setPreferredSize(new Dimension(400,300));
				
		//browse file
		fp2= new FilePath2();
		//scroll image 2
		ImageIcon image2=new ImageIcon(second_path);
		imageLabel2= new JLabel(image2);
		centerSP2=new JScrollPane(imageLabel2);
		centerSP2.setPreferredSize(new Dimension(400,300));
		
		//center.add(l);
		center.add(centerSP1);
		center.add(fp1);
		center.add(Box.createRigidArea(new Dimension(0,10)));
		//center.add(m);
		center.add(centerSP2);
		center.add(fp2);
		
		home.add(center,BorderLayout.CENTER);
		/*/------------center------------------------
		JPanel center=new JPanel();
		center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
		//label
		JLabel n=new JLabel("Image r�sultante");
		n.setAlignmentX(0);
		//
		ImageIcon image3=new ImageIcon("ab.JPG");
		JLabel imageLabel3= new JLabel(image3);
		JScrollPane centerSP=new JScrollPane(imageLabel3);
		centerSP.setPreferredSize(new Dimension(450,400));
		
		center.add(n);
		center.add(centerSP);
		home.add(center,BorderLayout.CENTER);
		*///------------right------------------------
		right=new JTabbedPane();
		//right.setBackground(Color.black);
		right.addTab("Module5",new Mod5Tab());
		right.addTab("Module1",new Mod1Tab());
		right.addTab("Module2",new Mod2Tab());
		right.addTab("Module3",new Mod3Tab());
		right.addTab("Module4",new Mod4Tab());		
		//right.addTab("Aide",new IntroPanel());
		home.add(right,BorderLayout.EAST);
		/*/------------bottom-----------------------
		JPanel butt=new JPanel();
		butt.setLayout(new BoxLayout(butt,BoxLayout.Y_AXIS));
		//JButton b1=new JButton("Exit");
		//b1.addActionListener(new Mod1Listener());
		//butt.add(b1);
		butt.add(Box.createRigidArea(new Dimension(0,30)));
		home.add(butt,BorderLayout.SOUTH);
		*///-----------------------------------------
		frame.getContentPane().add(home);
		frame.pack();
		frame.show();
		
	
	}
	
	
private static class BrowseListener1 implements ActionListener{
	public void actionPerformed(ActionEvent event){
		//JFrame frame=new JFrame("Module 5: Grille et croix");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFileChooser chooser=new JFileChooser();
		int status= chooser.showOpenDialog(null);
		if(status!= JFileChooser.APPROVE_OPTION);
		else{
		File file=chooser.getSelectedFile();
		Home.premier_path=file.getPath();
		//System.out.println(Home.premier_path);
		fp1.tx.setText(Home.premier_path);
		
		}
	
	}
}
private static class BrowseListener2 implements ActionListener{
	public void actionPerformed(ActionEvent event){
		//JFrame frame=new JFrame("Module 5: Grille et croix");
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JFileChooser chooser=new JFileChooser();
		int status= chooser.showOpenDialog(null);
		if(status!= JFileChooser.APPROVE_OPTION);
		else{
		File file=chooser.getSelectedFile();
		Home.second_path=file.getPath();
		//System.out.println(Home.second_path);
		fp2.tx.setText(Home.second_path);
	
		
		}
	
	}
}
private static class OkListener1 implements ActionListener{
	public void actionPerformed(ActionEvent event){
		Home.premier_path=fp1.tx.getText();
		
		DicomInputStream dis=null;
		AttributeList list=new AttributeList();
		SourceImage si=null;
		
		 try {
        	dis= new DicomInputStream(new File(premier_path));
        	list.read(dis);
        	if(list.isImage())
        	{
        		si=new SourceImage(list);
        	}
        	
        }catch (IOException e) {
        	JOptionPane.showMessageDialog(null,"File not found");
        	done--;
        	if(done<0)done=0;
    		imageLabel1.setIcon(new ImageIcon("imageParDefaut.JPG"));
    
        	}
        catch(Exception e){e.printStackTrace();System.exit(-1);}
        
        BufferedImage im=si.getBufferedImage();
       // imageLabel1.getGraphics().drawImage(im, 0, 0, 400, 400,imageLabel1);
        // Create a ParameterBlock and specify the source and
        // parameters
        firstIm=im;
        ParameterBlock pb = new ParameterBlock();
             pb.addSource(im);                   // The source image
             pb.add(0.35F);                        // The xScale
             pb.add(0.35F);                        // The yScale
             pb.add(0.0F);                       // The x translation
             pb.add(0.0F);                       // The y translation
             pb.add(new InterpolationNearest()); // The interpolation

        // Create the scale operation
       RenderedOp imOp = JAI.create("scale", pb, null);

done++;
        //---
        ImageIcon icone=new ImageIcon(imOp.getAsBufferedImage());
        imageLabel1.setIcon(icone);
        
	}
}
public static class OkListener2 implements ActionListener{
	public void actionPerformed(ActionEvent event){
		Home.second_path=fp2.tx.getText();
		
		DicomInputStream dis=null;
		AttributeList list=new AttributeList();
		SourceImage si=null;
		
		  try {

        	dis= new DicomInputStream(new File(second_path));
        	list.read(dis);
        	if(list.isImage())
        	{
        		si=new SourceImage(list);
        	}
        	
		  }catch (IOException e) {
		  	JOptionPane.showMessageDialog(null,"File not found");
		  	done--;
        	if(done<0)done=0;
		  	imageLabel2.setIcon(new ImageIcon("imageParDefaut.JPG"));
		  	}
	        catch(Exception e){e.printStackTrace();System.exit(-1);}
	    
	    BufferedImage im=si.getBufferedImage();
	    secondIm=im;
	   // imageLabel2.getGraphics().drawImage(im, 0, 0, 400, 300,imageLabel2);
	    // Create a ParameterBlock and specify the source and
        // parameters
        ParameterBlock pb = new ParameterBlock();
             pb.addSource(im);                   // The source image
             pb.add(0.35F);                        // The xScale
             pb.add(0.35F);                        // The yScale
             pb.add(0.0F);                       // The x translation
             pb.add(0.0F);                       // The y translation
             pb.add(new InterpolationNearest()); // The interpolation

        // Create the scale operation
       RenderedOp imOp = JAI.create("scale", pb, null);

done++;
        //---
       ImageIcon icone=new ImageIcon(imOp.getAsBufferedImage());
        imageLabel2.setIcon(icone);
	
	}
}
public static class FilePath1 extends JPanel{
	public JTextField tx;
	public FilePath1(){
		setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		JLabel l =new JLabel("Path:");
		tx=new JTextField();
		tx.setMaximumSize(new Dimension(300,20));
		tx.setPreferredSize(new Dimension(300,20));
		JButton b =new JButton("Browse");
		b.addActionListener(new BrowseListener1());
		JButton bt=new JButton("Ok");
		bt.addActionListener(new OkListener1());
		add(l);
		add(tx);
		add(b);
		add(bt);
	}
}
public static class FilePath2 extends JPanel{
	public JTextField tx;
	public FilePath2(){
		setLayout(new BoxLayout(this,BoxLayout.LINE_AXIS));
		JLabel l =new JLabel("Path:");
		tx=new JTextField();
		tx.setMaximumSize(new Dimension(300,20));
		tx.setPreferredSize(new Dimension(300,20));
		JButton b =new JButton("Browse");
		b.addActionListener(new BrowseListener2());
		JButton bt=new JButton("Ok");
		bt.addActionListener(new OkListener2());
		add(l);
		add(tx);
		add(b);
		add(bt);
	}
}

}
