/*
 * Created on Jul 19, 2006
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
import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
import java.awt.image.renderable.ParameterBlock;
import java.util.Hashtable;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.BorderFactory;
//import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class is a simple image viewer with options to load and save images and to
 * change zoom and adjust brightness and contrast. 
 * In order to compile and run this application, you'll need the simplecontrast
 * operator (look for it in http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */
public class AdjustImage extends JFrame implements ActionListener, ChangeListener
  {
  // Image being processed and its display component
  private PlanarImage image = null;
  private DisplayJAI display;
  // List of menu itens that may fire events.
  //private JMenuItem fOpen,fSave,fExit; // on the "File" menu
  private JMenuItem[] zZoom; // on the "Zoom" menu
  private int[] zoomValues = {-32,-16,-8,-4,-2,1,2,4,8,16,32}; // minus means zoom out
  private float zoomFactor = 1; // need to store it separatedly
  private JMenuItem hAbout; // on the "About" menu 
  // Sliders that will also fire events
  private JSlider brightnessSlider; // a slider to set the brightness
  private JSlider contrastSlider; // a slider to set the brightness
  private float[] contrastValues = 
    {0, 0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.9f,
     1f, 1.2f, 1.4f, 1.6f, 1.8f, 2f, 2.5f, 3f, 3.5f, 4f, 4.5f, 
     5f, 6f, 7f, 8f, 9f, 10f};
  // When events are fired, those variables will be set.
  private boolean needsRescaling=false,needsAdjusting=false;
  
 /**
  * The constructor for the class, which creates its user interface.
  */
  public AdjustImage()
    {
    super("Adjust Image Brightness and Contrast");
    // Add a menu bar
    setJMenuBar(buildMenuBar());
    getContentPane().setLayout(new BorderLayout());
    // Add a control panel    
    getContentPane().add(buildControlPanel(),BorderLayout.SOUTH);
    // Set the display component
    display = new DisplayJAI();
    getContentPane().add(new JScrollPane(display),BorderLayout.CENTER);
    // Set the closing operation so the application is finished.
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(800,600); // adjust the frame size using specific dimensions.
    setVisible(true); // show the frame.
    }
  
 /**
  * This method constructs the menu bar for this applications' user interface.
  */
  private JMenuBar buildMenuBar()
    {
    JMenuBar menuBar = new JMenuBar();
    // Create a "File" menu.
    //JMenu fileMenu = new JMenu("Fichier");
    //fileMenu.setMnemonic(KeyEvent.VK_F);
    // Add a "Open" item.
    //fOpen = new JMenuItem("Open");
    //fOpen.setMnemonic(KeyEvent.VK_O);
    //fOpen.addActionListener(this);
    //fileMenu.add(fOpen);
    // Add a "Save" item.
    //fSave = new JMenuItem("Save");
    //fSave.setMnemonic(KeyEvent.VK_S);
    //fSave.addActionListener(this);
    //fileMenu.add(fSave);
    //fSave.setEnabled(false); // we don't have anything to save yet!
    // Add a "Exit" item.
    //fileMenu.addSeparator();
    //fExit = new JMenuItem("Exit");
    //fExit.setMnemonic(KeyEvent.VK_X);
    //fExit.addActionListener(this);
    //fileMenu.add(fExit);
    // Create a "Zoom" menu.
    JMenu zoomMenu = new JMenu("Zoom");
    //zoomMenu.setMnemonic(KeyEvent.VK_Z);
    // With several zoom options.
    zZoom = new JMenuItem[zoomValues.length]; 
    boolean zoomOut = true;
    for(int z=0;z<zoomValues.length;z++)
      {
      // Check if we need to change the zoom from out to in.
      if (zoomValues[z] == 1) zoomOut = false; 
      String label;
      if (zoomOut) label = "1/"+-zoomValues[z]+"x";
      else label = zoomValues[z]+"x";
      zZoom[z] = new JMenuItem(label);
      if (zoomValues[z] == 1) // add separators before and after the "1" value.
        {
        zoomMenu.addSeparator();
        zoomMenu.add(zZoom[z]);
        zoomMenu.addSeparator();
        }
      else // no separators
        zoomMenu.add(zZoom[z]);
      zZoom[z].addActionListener(this);
      zZoom[z].setEnabled(false); // nothing to zoom in yet!
      }
    // Create a "Help" menu.
    JMenu helpMenu = new JMenu("Aide");
    //helpMenu.setMnemonic(KeyEvent.VK_H);
    // Add a "About" item.
    hAbout = new JMenuItem("Mode d'emploi");
    //hAbout.setMnemonic(KeyEvent.VK_A);
    hAbout.addActionListener(this);
    helpMenu.add(hAbout);
    // Add the "File", "View" and "Help" menus to the menu bar.
    //menuBar.add(fileMenu);
    menuBar.add(zoomMenu);
    menuBar.add(helpMenu);
    return menuBar;    
    }
  
 /**
  * This method constructs the brightness and contrast control panel for this
  * applications' user interface.
  */
  private JPanel buildControlPanel()
    {
    JPanel controlPanel = new JPanel(new GridLayout(2,1));
    // Create the brightness slider, and set its labels using a label table
    brightnessSlider = new JSlider(-127,127,0); 
    Hashtable bSliderLabels = new Hashtable();
    int[] ints = new int[]{-127,-100,-70,-50,-35,-25,-15,-5,0,5,15,25,35,50,70,100,127};
    for (int i=0; i<ints.length; i++){
    	int indice = ints[i];
    	bSliderLabels.put(new Integer(indice),new JLabel(""+indice));
    }
    brightnessSlider.setLabelTable(bSliderLabels);
    brightnessSlider.setPaintLabels(true);
    // Registers the change listener for the slider and add it to the control panel.
    brightnessSlider.addChangeListener(this);
    brightnessSlider.setBorder(BorderFactory.createTitledBorder("Luminosité"));
    brightnessSlider.setEnabled(false);
    controlPanel.add(brightnessSlider);
    // Create the contrast slider, and set its labels using a label table
    contrastSlider = new JSlider(0,contrastValues.length-1,10); 
    Hashtable cSliderLabels = new Hashtable();
    for(int i=0;i<contrastValues.length;i++)
      cSliderLabels.put(new Integer(i),new JLabel(""+contrastValues[i]));
    contrastSlider.setLabelTable(cSliderLabels);
    contrastSlider.setPaintLabels(true);
    contrastSlider.setMinorTickSpacing(1);
    contrastSlider.setPaintTicks(true);
    // Registers the change listener for the slider and add it to the control panel.
    contrastSlider.addChangeListener(this);
    contrastSlider.setBorder(BorderFactory.createTitledBorder("Contraste"));
    contrastSlider.setEnabled(false);
    controlPanel.add(contrastSlider);
    return controlPanel;
    }
  
 /**
  * This method will be called when an action event occurs (e.g. a menu being selected)
  */
  public void actionPerformed(ActionEvent e)
    {
    /*if (e.getSource() == fOpen) // open an image
      {    
      JFileChooser fc = new JFileChooser();
      // Let the user try and open all types of images...
      fc.setAcceptAllFileFilterUsed(true);
      // .. but with a default filter on the dialog.
      fc.setFileFilter(new ImageFileFilter());
      int returnVal = fc.showOpenDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION)
        {
        image = JAI.create("fileload",fc.getSelectedFile().toString());
        display.set(image);
        // Now we can save and zoom and manipulate it!
        //fSave.setEnabled(true);
        for (int i = 0; i<zZoom.length; i++){
        	((JMenuItem) zZoom[i]).setEnabled(true);
        	
        }
        brightnessSlider.setEnabled(true);
        contrastSlider.setEnabled(true);
        needsRescaling = false; needsAdjusting = false;
        }
      }*//*
    else if (e.getSource() == fSave) // save the image
      {    
      JFileChooser fc = new JFileChooser();
      // Let the user try and open all types of images...
      fc.setAcceptAllFileFilterUsed(true);
      // .. but with a default filter on the dialog.
      fc.setFileFilter(new ImageFileFilter());
      int returnVal = fc.showSaveDialog(this);
      if (returnVal == JFileChooser.APPROVE_OPTION)
        {
        // Get the name for saving the file.
        String oName = fc.getSelectedFile().toString();
        // Get an extension that *may* be the correct output image type.
        String extension = oName.substring(oName.indexOf('.')+1).toUpperCase();
        if (extension.equals("JPG")) extension = "JPEG";
        else if (extension.equals("TIF")) extension = "TIFF";
        // Save it.
        JAI.create("filestore",display.getSource(),oName,extension);
        }
      }*/
    /*else if (e.getSource() == fExit) // close the application
      {
      dispose();
      System.exit(0);
      }
    else */if (e.getSource() == hAbout)
      {
      // Create the "About" dialog
      JOptionPane.showMessageDialog(this,
          "<html>A l'aide de ce pannel, l'utilisateur peut zoomer et dézoomer<br>"+
          "sur l'image et ajuster le contraste et la luminosité afin de mieux<br>" +
          "visualiser l'image.<br><br>"+
		  "L'utilisateur peut alors sélectionner la croix sur l'image, ainsi que<br>"+
		  "les coins d'un rectangle de la grille en arrière-plan de l'image.</html>",
          "Mode d'emploi",
          JOptionPane.PLAIN_MESSAGE);
      }
    else // Assume zoom?
      {
      for(int z=0;z<zZoom.length;z++)
        if (e.getSource() == zZoom[z])
          {
          // Get the zoom factor
          zoomFactor = zoomValues[z];
          if (zoomFactor < 0) zoomFactor = -1f/zoomFactor;
          needsRescaling = (zoomFactor!=1);
          resetImage();
          }
      } // the event was supposedly zoom
    }
  
 /**
  * This method will be called when a change event occurs (e.g. a slider changes)
  */
  public void stateChanged(ChangeEvent e)
    {
    // Let the adjusting stop before proceeding.
    if (brightnessSlider.getValueIsAdjusting() || contrastSlider.getValueIsAdjusting()) return;
    // Do we need to adjust the images' brightness and/or contrast?
    int brightness = brightnessSlider.getValue();
    float contrast = contrastValues[contrastSlider.getValue()];
    needsAdjusting = (brightness!=0) || (contrast!=1);
    resetImage();
    }
  
 /**
  * This method will redisplay the image with a zoom, brightness and contrast adjust
  */
  private void resetImage()
    {
    display.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    // Create a copy of the original image.
    PlanarImage newImage = image.createSnapshot();
    // Do we need to adjust the contrast/brightness?
    if (needsAdjusting)
      {    
      int brightness = brightnessSlider.getValue();
      float contrast = contrastValues[contrastSlider.getValue()];
      // Let's do the manipulation!
      ParameterBlock p = new ParameterBlock();
      p.addSource(newImage);
      p.add(new Float(contrast));
      p.add(new Float(brightness));
      p.add(new Boolean(true)); // independent bands
      // Threshold the image with the simplecontrast operator.
      newImage = JAI.create("simplecontrast",p);
      }
    // Do we need to rescale the image?
    if (needsRescaling)
      {
      // Scales the image.
      ParameterBlock pb = new ParameterBlock();
      pb.addSource(newImage);
      pb.add(zoomFactor);
      pb.add(zoomFactor);
      pb.add(0.0F);
      pb.add(0.0F);
      pb.add(new InterpolationNearest());
      // Creates a new, scaled image.
      newImage = JAI.create("scale", pb);
      }
    display.set(newImage);
    display.setCursor(Cursor.getDefaultCursor());
    }
  
 /**
  * The applications' entry point.
  */
  public static void main(String[] args)
    {
    // First register the new operators we will use.
    SimpleContrastDescriptor.register();
    // Then start the GUI and application.
    new AdjustImage();
    }

  }
