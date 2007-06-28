/*
 * Created on Jun 22, 2005
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
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;

/**
 * This application demonstrates the crop and bandmerge operators. It allows the cropping
 * of image bands which are on separate files and glue the cropped parts in a single band
 * image.
 */
public class CropAndMerge
  {
  /**
   * The application entry point.
   * @param args the command-line arguments.
   */
  public static void main(String[] args)
    {
    if (args.length < 5)
      {
      System.err.println("Usage: java applications.CropAndMerge image x y width height bands");
      System.exit(0);
      }
    // Parse the parameters.
    float x = Float.parseFloat(args[0]);
    float y = Float.parseFloat(args[1]);
    float width = Float.parseFloat(args[2]);
    float height = Float.parseFloat(args[3]);
    // How many bands?
    int images = args.length-4;
    PlanarImage[] band = new PlanarImage[images];
    // Read and crop the images.
    for(int i=0;i<images;i++)
      {
      PlanarImage input = JAI.create("fileload", args[i+4]);
      ParameterBlock pb = new ParameterBlock();
      pb.addSource(input);
      pb.add(x);
      pb.add(y);
      pb.add(width);
      pb.add(height);
      // Create the output image by cropping the input image.
      band[i] = JAI.create("crop",pb,null);
      }
    // Merge the images. First create a ParameterBlockJAI and set the input images as its sources
    ParameterBlockJAI pbjai = new ParameterBlockJAI("bandmerge");
    // Set the sources to be the input images.
    for(int im=0;im<images;im++)
      pbjai.setSource(band[im], im);
    // Create the merged image.
    PlanarImage result = JAI.create("bandmerge",pbjai,null);
    // Store it as a TIFF since it supports multiband images.
    JAI.create("filestore",result,"merged.tiff","TIFF");
    }

  }