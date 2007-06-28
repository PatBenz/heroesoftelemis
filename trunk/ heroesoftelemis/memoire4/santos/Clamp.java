/*
 * Created on July 14, 2006
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 * 
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 * 
 * STATUS: Complete
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
import java.awt.GridLayout;
import java.awt.image.renderable.ParameterBlock;
import java.util.Hashtable;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class demonstrates the use of the clamp operator. It allows the
 * user to set the minimum and maximum values interactively.
 */
public class Clamp extends JFrame implements ChangeListener
  {
  private JSlider minSlider; // a slider to set the minimum clamping value
  private JSlider maxSlider; // a slider to set the maximum clamping value
  private PlanarImage image; // the original image
  private DisplayJAI display; // the display component

 /**
  * The constructor of the class creates the user interface and registers
  * the event listeners.
  * @param filename the file name of the image (we'll use it on the title bar)
  * @param image the PlanarImage to be rendered/rotated
  */
  public Clamp(String filename,PlanarImage image)
    {
    super("Interactive clamping of image "+filename);
    this.image = image;
    // Set the content pane's layout
    getContentPane().setLayout(new BorderLayout());
    // Create and set the image display component
    display = new DisplayJAI(image);
    getContentPane().add(new JScrollPane(display),BorderLayout.CENTER);
    // Create a small control panel with two sliders
    JPanel controlPanel = new JPanel(new GridLayout(2,1));
    // Create the minimum value slider, and set its labels using a label table
    minSlider = new JSlider(0,255,0);
    Hashtable minSliderLabels = new Hashtable();
    for(int label=0;label<=255;label+=16)
      minSliderLabels.put(new Integer(label),new JLabel(""+label));
    minSliderLabels.put(new Integer(255),new JLabel("255"));
    minSlider.setLabelTable(minSliderLabels);
    minSlider.setPaintLabels(true);
    // Registers the change listener for the slider and add it to the
    // control panel
    minSlider.addChangeListener(this);
    controlPanel.add(minSlider);
    // Create the maximum value slider, and set its labels using a label table
    maxSlider = new JSlider(0,255,255);
    Hashtable maxSliderLabels = new Hashtable();
    for(int label=0;label<=255;label+=16)
      maxSliderLabels.put(new Integer(label),new JLabel(""+label));
    maxSliderLabels.put(new Integer(255),new JLabel("255"));
    maxSlider.setLabelTable(maxSliderLabels);
    maxSlider.setPaintLabels(true);
    // Registers the change listener for the slider and add it to the
    // control panel
    maxSlider.addChangeListener(this);
    controlPanel.add(maxSlider);
    // Add the control panel to the frame
    getContentPane().add(controlPanel,BorderLayout.NORTH);
    // Set the closing operation so the application is finished.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack(); // adjust the frame size using preferred dimensions.
    setVisible(true); // show the frame.
    }

 /**
  * This method will be executed when the slider positions changes.
  */
  public void stateChanged(ChangeEvent e)
    {
    // If we're still adjusting, return.
    if (minSlider.getValueIsAdjusting() || maxSlider.getValueIsAdjusting()) return;
    // Get the values from both slides.
    int minValue = minSlider.getValue();
    int maxValue = maxSlider.getValue();
    // Ensure that min is below 255 and max is above 0
    if (minValue > 254) minSlider.setValue(254);
    if (maxValue < 1) maxSlider.setValue(1);
    // Ensure that min is below max, adjust depending on which slider generated
    // the event
    if (minValue >= maxValue) 
      {
      if (e.getSource() == minSlider) // moved this therefore range should change
        {
        maxSlider.setValue(minValue+1); 
        }
      if (e.getSource() == maxSlider) // moved this therefore range should change
        {
        minSlider.setValue(maxValue-1); 
        }
      }
    // Creates a new, clamped image and uses it on the DisplayJAI component
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add(new double[]{minValue});
    pb.add(new double[]{maxValue});
    PlanarImage clampedImage = JAI.create("clamp", pb);
    display.set(clampedImage);
    }

 /**
  * The application entry point.
  * @param args the command line arguments.
  */
  public static void main(String[] args)
    {
    // We need one argument: the image filename.
    /*if (args.length != 1)
      {
      System.err.println("Usage: java operators.Clamp image");
      System.exit(0);
      }*/
    // Read the image.
    PlanarImage image = JAI.create("fileload", "essai.bmp");
    // Create the GUI and start the application.
    new Clamp("essai.bmp",image);
    }

  } // end class