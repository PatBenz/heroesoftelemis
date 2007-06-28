/*
 * Created on Jun 17, 2005
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
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROIShape;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * This class uses some ImageRegions to create regions of interest on an image and plot the
 * parallel lines of the regions with instances of ParallelLinesComponent.
 */
public class ParallelLinesComponentApp
  {
  /**
   * The applications' entry point.
   * @param args the command-line arguments.
   */
  public static void main(String[] args)
    {
    // We don't need image file names, this app should run with the landsat.tif image.
    // Read the image.
    PlanarImage input = JAI.create("fileload","landsat.tif");
    // Create a displayable version of this image.
    PlanarImage view = JAI.create("bandselect",input,new int[] {3,2,1});
    // Create four shapes, then ROIShapes, then ImageRegions.
    int[] x1 = new int[]{542,509,577};
    int[] y1 = new int[]{106,167,153};
    Shape s1 = new Polygon(x1,y1,3);
    ImageRegion r1 = new ImageRegion(input,new ROIShape(s1)); 
    r1.setBorderColor(new Color(255,255,0));
    r1.setStroke(new BasicStroke(3));
    int[] x2 = new int[]{222,319,302};
    int[] y2 = new int[]{430,342,420};
    Shape s2 = new Polygon(x2,y2,3);
    ImageRegion r2 = new ImageRegion(input,new ROIShape(s2)); 
    r2.setBorderColor(new Color(255,0,200));
    r2.setStroke(new BasicStroke(3));
    Shape s3 = new Rectangle(400,657,19,34);
    ImageRegion r3 = new ImageRegion(input,new ROIShape(s3)); 
    r3.setBorderColor(new Color(50,255,255));
    r3.setStroke(new BasicStroke(3));
    Shape s4 = new Rectangle(18,404,12,103);
    ImageRegion r4 = new ImageRegion(input,new ROIShape(s4)); 
    r4.setBorderColor(new Color(20,255,20));
    r4.setStroke(new BasicStroke(3));
    // Create a JFrame for displaying the results.
    JFrame frame = new JFrame();
    frame.setTitle("Image with regions");
    // Add to the JFrame's ContentPane an instance of DisplayImageWithRegions, 
    // which will contain the viewable image and its regions.
    DisplayImageWithRegions display = new DisplayImageWithRegions(view);
    display.addImageRegion(r1);
    display.addImageRegion(r2);
    display.addImageRegion(r3);
    display.addImageRegion(r4);
    frame.getContentPane().add(new JScrollPane(display));
    // Set the closing operation so the application is finished.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack(); // adjust the frame size using preferred dimensions.
    frame.setVisible(true); // show the frame.
    // Create frames for the ParallelLinesComponents too.
    // Plantations.
    JFrame frame1 = new JFrame();
    frame1.setTitle("Plantations area");
    ParallelLinesComponent c1 = new ParallelLinesComponent(r1,"Plantations Area");
    c1.setMinimum(0); c1.setMaximum(255);
    frame1.getContentPane().add(c1);
    frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame1.pack(); // adjust the frame size using preferred dimensions.
    frame1.setVisible(true); // show the frame.
    // Urban area.
    JFrame frame2 = new JFrame();
    frame2.setTitle("Urban area");
    ParallelLinesComponent c2 = new ParallelLinesComponent(r2,"Urban Area");
    c2.setMinimum(0); c2.setMaximum(255);
    frame2.getContentPane().add(c2);
    frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame2.pack(); // adjust the frame size using preferred dimensions.
    frame2.setVisible(true); // show the frame.
    // Soil area.
    JFrame frame3 = new JFrame();
    frame3.setTitle("Soil area");
    ParallelLinesComponent c3 = new ParallelLinesComponent(r3,"Soil Area");
    c3.setMinimum(0); c3.setMaximum(255);
    frame3.getContentPane().add(c3);
    frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame3.pack(); // adjust the frame size using preferred dimensions.
    frame3.setVisible(true); // show the frame.
    // Mixed area.
    JFrame frame4 = new JFrame();
    frame4.setTitle("Mixed area");
    ParallelLinesComponent c4 = new ParallelLinesComponent(r4,"Mixed Area");
    c4.setMinimum(0); c4.setMaximum(255);
    frame4.getContentPane().add(c4);
    frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame4.pack(); // adjust the frame size using preferred dimensions.
    frame4.setVisible(true); // show the frame.
    }

  }