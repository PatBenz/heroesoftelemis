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
import java.awt.RenderingHints;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.LookupTableJAI;
import javax.media.jai.PlanarImage;

/**
 * This class implements the RenderedImageFactory interface and is used to
 * create a RenderedImage (or an equivalent chain of operators) as a result
 * of the simplecontrast operator.
 */
public class SimpleContrastRIF implements RenderedImageFactory
  {
 /**
  * Empty constructor -- some examples point that I must have it, I am not
  * sure why.
  */
  public SimpleContrastRIF()
    {
    }
  
 /**
  * The create method, that will be called to create a RenderedImage (or chain
  * of operators that represents one).
  * Inside this operator I will call an operator to calculate the mean vector
  * for the whole input image -- I am not sure whether this is the best approach.
  */
  public RenderedImage create(ParameterBlock paramBlock, RenderingHints hints)
    { 
    // Get data from the ParameterBlock.
    RenderedImage source = paramBlock.getRenderedSource(0);
    // Let's apply a colormap if needed.
    if (source.getColorModel() instanceof IndexColorModel)
      {
      // Retrieve the IndexColorModel
      IndexColorModel icm = (IndexColorModel)source.getColorModel();
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
      source = JAI.create("lookup", source, lut);
      }
    float contrastAdj = paramBlock.getFloatParameter(0);
    float brightnessAdj = paramBlock.getFloatParameter(1);
    // Why there isn't any ParameterBlock.getBooleanParameter()?
    Boolean independentBands = (Boolean)paramBlock.getObjectParameter(2);
    // We will copy the input image layout to the output image.
    ImageLayout layout = new ImageLayout(source);
    // Get the average vector from the input image. See comments on this method.
    // First get the mean value of all bands.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(source);
    pb.add(null); // no ROI, whole image
    pb.add(1); pb.add(1); // check all pixels
    PlanarImage meanImage = JAI.create("mean",pb);
    // Get the mean pixel values
    double[] means = (double[])meanImage.getProperty("mean");
    // Create a new image (or chain) with this information.
    return new SimpleContrastOpImage(source,contrastAdj,brightnessAdj,
                                     independentBands.booleanValue(),means,
                                     layout,hints,false);
    }
  }
