/*
 * Created on Jun 16, 2005
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
import java.awt.image.IndexColorModel;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;

/**
 * This class is an application that will apply a look-up table to an image and
 * store it using a different name.
 */
public class Lookup
  {
  /**
   * The application's entry point.
   * @param args the command-line arguments
   */
  public static void main(String[] args)
    {
    // We need one arguments: the image file name.
    /*if (args.length != 1)
      {
      System.err.println("Usage: java operators.Lookup image");
      System.exit(0);
      }*/
    // Read the input image.
    PlanarImage image = JAI.create("fileload", "radio-frontale.jpg");
    // If the source image is colormapped, convert it to 3-band RGB.
    if(image.getColorModel() instanceof IndexColorModel)
      {
      // Retrieve the IndexColorModel
      IndexColorModel icm = (IndexColorModel)image.getColorModel();
      // Cache the number of elements in each band of the colormap.
      int mapSize = icm.getMapSize();
      // Allocate an array for the lookup table data.
      byte[][] lutData = new byte[3][mapSize];
      // Load the lookup table data from the IndexColorModel.
      icm.getReds(lutData[0]);
      icm.getGreens(lutData[1]);
      icm.getBlues(lutData[2]);
      // Create the lookup table object.
      LookupTableJAI lut = new LookupTableJAI(lutData);
      // Replace the original image with the 3-band RGB image.
      image = JAI.create("lookup", image, lut);
      }
    else // the color model wasn't an instance of IndexColorModel
      System.out.println("Color model for image "+"radio-frontale.jpg"+" is not an instance"+
                         "of IndexColorModel, no changes done.");
    // Store the image as TIFF.
    JAI.create("filestore",image,"lut_"+"radio-frontale.jpg","TIFF");
    }
  
  }