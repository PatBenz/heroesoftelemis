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
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import javax.media.jai.ImageLayout;
import javax.media.jai.PointOpImage;

/**
 * This class contains the code that will be executed for the 
 * simplecontrast operator.
 */
public class SimpleContrastOpImage extends PointOpImage
  {
  // The source image.
  private RenderedImage source;
  // The operator parameters.
  private float contrastAdj,brighnessAdj;
  // The bands' means' vector.
  private double[] means;

 /**
  * The constructor for the class, which will call the superclass constructor
  * and store the parameters' values locally.
  * @param source the source image
  * @param ca the contrast adjustment value
  * @param ba the brightness adjustment value
  * @param independent if true will do adjustments independently for each band
  * @param means the array of mean values for each band on the image
  * @param layout the image layout
  * @param hints the rendering hints
  * @param b indicates whether computeRect() expects contiguous sources. (??)
  */
  public SimpleContrastOpImage(RenderedImage source, 
                               float ca,float ba, boolean independent,double[] means,
                               ImageLayout layout,RenderingHints hints, boolean b)
    {
    super(source,layout,hints,b);
    this.source = source;
    this.contrastAdj = ca;
    this.brighnessAdj = ba;
    this.means = means;
    if (!independent) // means calculated over all bands
      {
      double newMean = 0;
      for (int i=0; i<means.length; i++){
      	newMean = newMean + (double)means[i];
      }
      for(int m=0;m<means.length;m++) means[m] = newMean/(1.*means.length);
      }
    }
  
 /**
  * This method will be called when we need to compute a tile for that image.
  */
  public Raster computeTile(int x,int y)
    {
    Raster raster = source.getTile(x,y);
    int minX = raster.getMinX();
    int minY = raster.getMinY();
    int width = raster.getWidth();
    int height = raster.getHeight();
    // Notice that we *must* create a WritableRaster with position coordinates!
    WritableRaster wr = 
      raster.createCompatibleWritableRaster(minX,minY,width,height);
    // The main algorithm is here. 
    double[] pixel = new double[raster.getNumBands()];
    for(int l=0;l<raster.getHeight();l++)
      for(int c=0;c<raster.getWidth();c++)
        {
        pixel = raster.getPixel(c+minX,l+minY,pixel);
        // Apply contrast and brightness 
        pixel[0] = clamp((means[0]+(pixel[0]-means[0])*contrastAdj)+brighnessAdj);          
        pixel[1] = clamp((means[1]+(pixel[1]-means[1])*contrastAdj)+brighnessAdj);          
        pixel[2] = clamp((means[2]+(pixel[2]-means[2])*contrastAdj)+brighnessAdj);          
        wr.setPixel(c+minX,l+minY,pixel);
        }
    return wr;
    }
  
  /**
   * This method "clamps" the pixels values, ensuring they will be on the [0,255]
   * range.
   */
  private double clamp(double f)
    {
    if (f < 0) f = 0;
    else if (f > 255) f = 255;
    return f;
    }

  }