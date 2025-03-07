/*
 * Created on May 22, 2005
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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.BorderExtender;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class demonstrates the use of the border operator. The user can
 * interactively choose the image border dimensions and the border type.
 */
public class Border extends JFrame implements ChangeListener,ActionListener
  {
  // The component and original image.
  private DisplayJAI display;
  private PlanarImage image;

  // A JComboBox for the existing border types.
  private JComboBox borderTypes;
  private String[] borderTypesLabels = {"Zero","Constant","Copy","Reflection","Wrap"};

  // JSpinners for the four border dimensions.
  private JSpinner top,left,right,bottom;

 /**
  * The constructor for the class, which sets its GUI.
  * @param image the image which will receive a border and be displayed.
  */
  public Border(PlanarImage image)
    {
    super("Border operator example");
    this.image = image;
    // Create a control panel for the combo box and spinners.
    JPanel controlPanel = new JPanel(new GridLayout(5,2));
    // Add the border type combo box.
    borderTypes = new JComboBox(borderTypesLabels);
    borderTypes.addActionListener(this);
    controlPanel.add(new JLabel("Border Type"));
    controlPanel.add(borderTypes);
    // Add the four sides spinners. We need four instances of SpinnerModel.
    SpinnerModel modelT = new SpinnerNumberModel(10,0,100,5);
    top = new JSpinner(modelT);
    top.addChangeListener(this);
    controlPanel.add(new JLabel("Top border"));
    controlPanel.add(top);
    SpinnerModel modelB = new SpinnerNumberModel(10,0,100,5);
    bottom = new JSpinner(modelB);
    bottom.addChangeListener(this);
    controlPanel.add(new JLabel("Bottom border"));
    controlPanel.add(bottom);
    SpinnerModel modelL = new SpinnerNumberModel(10,0,100,5);
    left = new JSpinner(modelL);
    left.addChangeListener(this);
    controlPanel.add(new JLabel("Left border"));
    controlPanel.add(left);
    SpinnerModel modelR = new SpinnerNumberModel(10,0,100,5);
    right = new JSpinner(modelR);
    right.addChangeListener(this);
    controlPanel.add(new JLabel("Right border"));
    controlPanel.add(right);
    // Small trick to make the control panel look better.
    JPanel positionControlPanel = new JPanel(new BorderLayout());
    positionControlPanel.add(controlPanel,BorderLayout.NORTH);
    // Create the display component.
    display = new DisplayJAI(image); // Temporarily uses the original image.
    resetImage(); // Now that the instance of DisplayJAI is created, set
                  // an image with borders.
    // Add to the JFrame's ContentPane the display and the control panel.
    getContentPane().add(new JScrollPane(display),BorderLayout.CENTER);
    getContentPane().add(positionControlPanel,BorderLayout.EAST);
    // Set the closing operation so the application is finished.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    pack(); // adjust the frame size using preferred dimensions.
    setVisible(true); // show the frame.
    }

 /**
  * This method will be called when the spinners' contents change.
  */
  public void stateChanged(ChangeEvent e)
    {
    resetImage();
    }

 /**
  * This method will be called when the combo box contents change.
  */
  public void actionPerformed(ActionEvent e)
    {
    resetImage();
    }

 /**
  * This method will create a border around the image with the values from
  * the spinners and combo box. The new image will be used for displaying.
  */
  private void resetImage()
    {
    // Gets values from the spinners and combo box.
    int topValue = Integer.parseInt(top.getValue().toString());
    int bottomValue = Integer.parseInt(bottom.getValue().toString());
    int leftValue = Integer.parseInt(left.getValue().toString());
    int rightValue = Integer.parseInt(right.getValue().toString());
    int borderType = borderTypes.getSelectedIndex();
    // Create a ParameterBlock for the border operation.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add(new Integer(leftValue));
    pb.add(new Integer(rightValue));
    pb.add(new Integer(topValue));
    pb.add(new Integer(bottomValue));
    switch(borderType)
      {
      case 0:
        pb.add(BorderExtender.createInstance(BorderExtender.BORDER_ZERO));
        break;
      case 1: // Construction of constant borders is different.
        pb.add(new BorderExtenderConstant(new double[]{255.,0.,180.}));
        break;
      case 2:
        pb.add(BorderExtender.createInstance(BorderExtender.BORDER_COPY));
        break;
      case 3:
        pb.add(BorderExtender.createInstance(BorderExtender.BORDER_REFLECT));
        break;
      case 4:
        pb.add(BorderExtender.createInstance(BorderExtender.BORDER_WRAP));
        break;
      }
    PlanarImage borderImage = JAI.create("border",pb);
    // Oops, the border will be set "outside" the original image extents,
    // which will cause problems for the display component. Let's
    // reposition it so the image origin is back at (0,0).
    pb = new ParameterBlock();
    pb.addSource(borderImage);
    pb.add(1.0f*leftValue);
    pb.add(1.0f*topValue);
    // Create the output image by translating itself.
    borderImage = JAI.create("translate",pb,null);
    display.set(borderImage);
    }

 /**
  * The application entry point.
  * @param args the command line arguments. We need one arguments, the image
  * file name.
  */
  public static void main(String[] args)
    {
    // We need one arguments: the image file name.
    /*if (args.length != 1)
      {
      System.err.println("Usage: java operators.Border image");
      System.exit(0);
      }*/
    // Read the image.
    PlanarImage input = JAI.create("fileload", "radio-frontale.jpg");
    // Create an instance of the JFrame.
    new Border(input);
    }
  
  } // end class