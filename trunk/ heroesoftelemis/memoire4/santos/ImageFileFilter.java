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
import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * This class is a filter that will be used by a file chooser to limit the type
 * of images that will appear on it.
 */
public class ImageFileFilter extends FileFilter
  {
 /**
  * This method returns true for those types of images we want to accept. Just to
  * illustrate, we will accept only images that *may* be 24-bit color images,
  * therefore some file types were omitted.
  */
  public boolean accept(File f)
    {
    // Accept based on the file extensions -- not really safe, but should work for
    // "normal" images.
    String fName = f.getName().toLowerCase();
    if (fName.endsWith(".tif") || fName.endsWith(".tiff")) return true;
    if (fName.endsWith(".jpg") || fName.endsWith(".jpeg")) return true;
    if (fName.endsWith(".png")) return true;
    if (fName.endsWith(".ppm")) return true;
    if (fName.endsWith(".bmp")) return true;
    // Can also show directories!
    if (f.isDirectory()) return true;
    // otherwise...
    return false;
    }
 /**
  * This method returns the description that will appear on the file chooser dialog.
  */
  public String getDescription()
    {
    return "Possibly 24-bit color images";
    }
  }