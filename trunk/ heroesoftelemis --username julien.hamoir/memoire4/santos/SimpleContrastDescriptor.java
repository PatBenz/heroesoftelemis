/*
 * Created on 19/Jul/06
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
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.registry.RIFRegistry;
import javax.media.jai.util.Range;

/**
 * This class describes the operator and parameters for the 
 * "simplecontrast" operator (a PointOpImage operator). 
 * This very simple operator uses two parameters to do a very simple contrast
 * and brightness adjust on an image. The formula for the adjustment is 
 * 
 * a = average of all pixels on the image, calculated
 * dc = contrast factor (0 to 10, given)
 * db = brighness factor (-127 to 127, given)
 * then
 * 
 * r = clamp(( a + (r - a) * dc) + db);
 * g = clamp(( a + (g - a) * dc) + db);
 * b = clamp(( a + (b - a) * dc) + db);
 * 
 * where clamp ensures that the value will be between 0 and 255.
 * 
 * (based on posts on the JAI interest list by davince and Todd Krebs, see 
 * http://forums.java.net/jive/thread.jspa?messageID=90017 and 
 * http://forums.java.net/jive/thread.jspa?messageID=90144 )
 */
public class SimpleContrastDescriptor extends OperationDescriptorImpl
  {
  // A map-like array of strings with resources information.
  private static final String[][] resources =
    {
      {"GlobalName",   "SimpleContrast"},
      {"LocalName",    "SimpleContrast"},
      {"Vendor",       "br.inpe.lac"},
      {"Description",  "A simple brightness and contrast adjustment operator"},
      {"DocURL",       "http://www.lac.inpe.br/~rafael.santos"},
      {"Version",      "1.0"},
      {"arg0Desc",     "Contrast Adjustment Value"},
      {"arg1Desc",     "Brightness Adjustment Value"},
      {"arg2Desc",     "Band Independence"}
    };
  // An array of strings with the supported modes for this operator.
  private static final String[] supportedModes = {"rendered"};
  // An array of strings with the parameter names for this operator. 
  private static final String[] paramNames = {"Contrast Adjustment Value",
                                              "Brightness Adjustment Value",
                                              "Band Independence"};
  // An array of Classes with the parameters' classes for this operator.
  private static final Class[] paramClasses = {Float.class, Float.class, Boolean.class};
  // An array of Objects with the parameters' default values.
  private static final Object[] paramDefaults = 
    { new Float(0), new Float(0), new Boolean(false) };
  // An array of Ranges with ranges of valid parameter values. 
  private static final Range[] validParamValues =
    {
    new Range(Float.class, // 1st parameter
        new Float(0), new Float(10)),
    new Range(Float.class, // 2nd parameter
        new Float(-127), new Float(127)),
    null // 3rd parameter may be true or false
    };
  // The number of sources required for this operator.
  private static final int numSources = 1;
  // A flag that indicates whether the operator is already registered.
  private static boolean registered = false;
  
 /**
  * The constructor for this descriptor, which just calls the constructor
  * for its ancestral class (OperationDescriptorImpl).
  */ 
  public SimpleContrastDescriptor()
    {
    super(resources,supportedModes,numSources,paramNames,
          paramClasses,paramDefaults,validParamValues);
    }
  
 /**
  * A method to register this operator with the OperationRegistry and
  * RIFRegistry. 
  */
  public static void register()
    {
    if (!registered)
      {
      // Get the OperationRegistry.
      OperationRegistry op = JAI.getDefaultInstance().getOperationRegistry();
      // Register the operator's descriptor. 
      SimpleContrastDescriptor desc = new SimpleContrastDescriptor();
      op.registerDescriptor(desc);
      // Register the operators's RIF.
      SimpleContrastRIF rif = new SimpleContrastRIF();
      RIFRegistry.register(op,"simplecontrast","br.inpe.lac",rif);
      registered = true;
      }
    }
  
  }