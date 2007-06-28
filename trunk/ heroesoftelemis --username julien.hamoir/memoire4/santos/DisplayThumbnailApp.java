/*
 * Created on May 31, 2005
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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class demonstrates the usage of the class DisplayThumbnail.
 * It will create an instance of DisplayThumbnail and an instance of
 * DisplayJAI controlled by it.
 * Never mind the layout stuff, I was just playing with SpringLayout.
 */
public class DisplayThumbnailApp extends JFrame implements MouseMotionListener
  {
  // The instance of DisplayThumbnail.
  private DisplayThumbnail dt;
  // The instance of DisplayJAI.
  private DisplayJAI dj;
  // Two labels to show the world (original image) and thumbnail viewport coordinates.
  private JLabel world,view;

 /**
  * The constructor for the class, which will take as arguments the original
  * image and the desired width and height of the region to be shown.
  * @param image the original image to be displayed
  * @param dWidth the desired width for the region to be shown.
  * @param dHeight the desired height for the region to be shown.
  */
  public DisplayThumbnailApp(PlanarImage image,int dWidth,int dHeight)
    {
    super("Interactive Thumbnail Example");
    SpringLayout layout = new SpringLayout();
    Container contentPane = getContentPane();
    contentPane.setLayout(layout);
    // First we create the instance of DisplayThumbnail with a 0.1 scale.
    dt = new DisplayThumbnail(image,0.05f,dWidth,dHeight);
    // We must register mouse motion listeners to it !
    dt.addMouseMotionListener(this);
    // Now we create the instance of DisplayJAI to show the region corresponding
    // to the viewport.
    dj = new DisplayJAI(dt.getImage());
    // Set it size.
    dj.setPreferredSize(new Dimension(dWidth,dHeight));
    dj.setMinimumSize(new Dimension(dWidth,dHeight));
    dj.setMaximumSize(new Dimension(dWidth,dHeight));
    // Create a JPanel so we can add a border around the instance of DisplayJAI.
    JPanel borderDisplay = new JPanel(new BorderLayout());
    borderDisplay.add(dj);
    borderDisplay.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    // Create a small panel with the labels.
    JLabel worldL = new JLabel("World: ");
    world = new JLabel("");
    JLabel viewL = new JLabel("Thumb: ");
    view = new JLabel("");
    // All set ? Let's work with the Spring Layout then.
    contentPane.add(dt);
    contentPane.add(borderDisplay);
    contentPane.add(worldL);
    contentPane.add(world);
    contentPane.add(viewL);
    contentPane.add(view);
    // West of DisplayThumbnail is glued to West of the container.
    layout.putConstraint(SpringLayout.WEST,dt,5,SpringLayout.WEST,contentPane);
    // North of DisplayThumbnail is glued to North of the container.
    layout.putConstraint(SpringLayout.NORTH,dt,5,SpringLayout.NORTH,contentPane);
    // West of DisplayJAI is glued to East of DisplayThumbnail (with some extra space).
    layout.putConstraint(SpringLayout.WEST,borderDisplay,50,SpringLayout.EAST,dt);
    // North of DisplayJAI is glued to North of the container.
    layout.putConstraint(SpringLayout.NORTH,borderDisplay,5,SpringLayout.NORTH,contentPane);
    // South of DisplayJAI is glued to South of the container.
    layout.putConstraint(SpringLayout.SOUTH,contentPane,5,SpringLayout.SOUTH,borderDisplay);
    // East of DisplayJAI is glued to East of the container.
    layout.putConstraint(SpringLayout.EAST,contentPane,5,SpringLayout.EAST,borderDisplay);
    // West of worldL is glued to West of the container.
    layout.putConstraint(SpringLayout.WEST,worldL,5,SpringLayout.WEST,contentPane);
    // North of worldL is glued to South of DisplayThumbnail.
    layout.putConstraint(SpringLayout.NORTH,worldL,5,SpringLayout.SOUTH,dt);
    // West of viewL is glued to West of the container.
    layout.putConstraint(SpringLayout.WEST,viewL,5,SpringLayout.WEST,contentPane);
    // North of viewL is glued to South of worldL.
    layout.putConstraint(SpringLayout.NORTH,viewL,5,SpringLayout.SOUTH,worldL);
    // West of world is glued to East of worldL.
    layout.putConstraint(SpringLayout.WEST,world,5,SpringLayout.EAST,worldL);
    // North of world is glued to South of DisplayThumbnail.
    layout.putConstraint(SpringLayout.NORTH,world,5,SpringLayout.SOUTH,dt);
    // West of view is glued to East of viewL.
    layout.putConstraint(SpringLayout.WEST,view,5,SpringLayout.EAST,viewL);
    // North of view is glued to South of world.
    layout.putConstraint(SpringLayout.NORTH,view,5,SpringLayout.SOUTH,world);
    // Set the closing operation so the application is finished.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // Don't let the user resize the window.
    setResizable(false);
    pack(); // set an adequated size for the frame
    setVisible(true); // show the frame.    
    }

 /**
  * This method will be called when we drag the mouse over the thumbnail.
  */
  public void mouseDragged(MouseEvent e)
    {
    // Change the image on the DisplayJAI component to correspond to the
    // viewport on the thumbnail.
    dj.set(dt.getImage());
    // Gets some information about the viewport and cropped image.
    Rectangle crop = dt.getCroppedImageBounds();
    Rectangle viewp = dt.getViewportBounds();
    // Change the labels' contents with this information.
    world.setText(""+crop.x+","+crop.y+" ("+crop.width+"x"+crop.height+")");
    view.setText(""+viewp.x+","+viewp.y+" ("+viewp.width+"x"+viewp.height+")");
    }

 /**
  * This method is here just to keep the MouseMotionListener interface happy.
  */
  public void mouseMoved(MouseEvent e) { }

 /**
  * The application entry point, which will load the image passed as the
  * command-line argument and create an instance of the application.
  */
  public static void main(String[] args)
    {
    PlanarImage image = JAI.create("fileload", "essai.bmp");
    new DisplayThumbnailApp(image,640,640);
    }

  }