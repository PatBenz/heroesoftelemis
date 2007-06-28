/*
 * Created on July 7, 2006
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 * 
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 * 
 * STATUS: Complete.
 * 
 * Redistribution and usage conditions must be done under the
 * Creative Commons license:
 * English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
 * Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
 * More information on design and applications are on the projects' page
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */


package santos;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * This class uses an instance of DisplayJAIWhileStoringCoordinates to display an 
 * image in an application. The user may click on some of the images' pixels to get
 * their coordinates and pixels' values. When exiting, the application will print
 * the collected data.
 */ 
public class DisplayJAIWhileStoringCoordinatesApp extends JFrame implements WindowListener

  {
  private DisplayJAIWhileStoringCoordinates dj; // an instance of the display component.
  
 /**
  * The constructor of the class, which sets the frame appearance and creates an
  * instance of the modified display class.
  * @param image
  */
  public DisplayJAIWhileStoringCoordinatesApp(RenderedImage image)
    {
    setTitle("Click on some regions on the image !");
    // Get the JFrame's ContentPane.
    Container contentPane = getContentPane();
    contentPane.setLayout(new BorderLayout());
    // Create an instance of DisplayJAIWhileStoringCoordinates and adds it to the content pane.
    dj = new DisplayJAIWhileStoringCoordinates(image);
    contentPane.add(new JScrollPane(dj),BorderLayout.CENTER);
    // Add a listener for windows events, so we can print the collected data before
    // exiting the application.
    addWindowListener(this);
    // Set the closing operation so the application is finished.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(400,400); // adjust the frame size.
    setVisible(true); // show the frame.
    }

  /**
   * This method will be called when we close the window. 
   */
  public void windowClosing(WindowEvent e)
    {
    ArrayList list = dj.getClicksInformation();
    for (int i =0 ; i<list.size();i++){
    	System.out.println(list.get(i));
    	}
    }
  
  
  // These methods are here to keep the WindowListener interface happy.
  public void windowActivated(WindowEvent e) { }
  public void windowClosed(WindowEvent e) { }
  public void windowDeactivated(WindowEvent e) { } 
  public void windowDeiconified(WindowEvent e) { } 
  public void windowIconified(WindowEvent e) { } 
  public void windowOpened(WindowEvent e) { } 

 /**
  * The application entry point.
  * @param args the command line arguments.
  */
  public static void main(String[] args)
    {
    PlanarImage image = JAI.create("fileload", "essai.bmp");
    new DisplayJAIWhileStoringCoordinatesApp(image);
    } // end main
  
  } // end class
