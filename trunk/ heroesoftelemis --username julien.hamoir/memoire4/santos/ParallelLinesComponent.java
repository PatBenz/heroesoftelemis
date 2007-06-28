/*
 * Created on Jun 22, 2005
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 * 
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 * 
 * STATUS: Complete but with potential bugs:
 *   - Some plotting parameters are hard-coded, e.g. the vertical scale.
 *   - Not ready to use with data with extrema outside of [0-255].
 * 
 * Redistribution and usage conditions must be done under the
 * Creative Commons license:
 * English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
 * Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
 * More information on design and applications are on the projects' page
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */

package santos;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * This class is a component that is used to display parallel lines extracted from an
 * image region, allowing the display of the distribution of the pixels values on 
 * multiple-banded images. 
 */
public class ParallelLinesComponent extends JComponent
  {
  // Some drawing/dimension constants.
  private static Insets margins = new Insets(30,45,15,15);
  private static int axisSpacing = 60;
  private static int plotHeight = 128;
  private static int numMarks = 10; // vertical ticks on the axis
  // Some colors and fonts for the graphic.
  private static Color plottingAreaBackground = Color.WHITE;
  private static Color plottingAxisColor = Color.BLACK;
  private static Color plottingLineColor = new Color(200,200,200,80);
  private static Font titleFont = new Font("Monospaced",Font.ITALIC,13);
  private static Font labelsFont = new Font("Monospaced",Font.PLAIN,9);
  // The pixels to be plotted and some attributes.
  private float[][] pixels;
  private int dimensions;
  private float min,max;
  private String title;
  
 /**
  * The constructor for the class, which takes an ImageRegion to extract its pixels and a title
  * for the plot.
  * @param r the ImageRegion whose pixels will be used in the plot
  * @param t the plot's title.
  */
  public ParallelLinesComponent(ImageRegion r,String t)
    {
    title = t;
    dimensions = r.getImageBands();
    pixels = r.getPixels();
    // Which are the extrema?
    float[][] extrema = r.getAllExtrema();
    max = Float.MIN_VALUE;
    min = Float.MAX_VALUE;
    for(int b=0;b<extrema.length;b++)
      {
      min = Math.min(min,extrema[b][1]);
      max = Math.max(max,extrema[b][1]);
      }
    }
  
 /**
  * This method allows the user to set a manual minimum value for the plot.
  * @param m the new minimum value for the plot.
  */
  public void setMinimum(float m)
    {
    min = m;
    }
  
 /**
  * This method allows the user to set a manual maximum value for the plot.
  * @param m the new maximum value for the plot.
  */
  public void setMaximum(float m)
    {
    max = m;
    }

 /**
  * Which is the minimum dimension for the component?
  */
  public Dimension getMinimumSize()
    {
    return getPreferredSize();
    }
 
 /**
  * Which is the maximum dimension for the component?
  */
  public Dimension getMaximumSize()
    {
    return getPreferredSize();
    }

 /**
  * Which is the preferred dimension for the component?
  */
  public Dimension getPreferredSize()
    {
    return new Dimension(margins.left+(dimensions-1)*axisSpacing+margins.right,
                         margins.top+plotHeight+margins.bottom);
    }
  
 /**
  * This method is responsible for the plot itself.
  */
  protected void paintComponent(Graphics g)
    {
    Graphics2D g2d = (Graphics2D)g;
    // We want it antialiased!
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    // Paint the background.
    g2d.setColor(plottingAreaBackground);
    g2d.fillRect(0,0,getPreferredSize().width,getPreferredSize().height);
    // Paint the pixels' values.
    g2d.setColor(plottingLineColor);
    for(int p=0;p<pixels.length;p++)
      {
      // The "from" and "to" of the lines.
      double x=0,y=0,lastX=0,lastY=0;
      // For each of its dimensions...
      for(int d=0;d<pixels[p].length;d++)
        {
        // Scale X and Y.
        x = margins.left+d*axisSpacing;
        y = margins.top+plotHeight-(pixels[p][d]/2.);
        if (d > 0)
          {
          g2d.drawLine((int)x,(int)y,(int)lastX,(int)lastY);
          }
        // Store the last used X and Y to use in the next line segment.
        lastX = x; lastY = y;
        }
      }
    // Paint the axis values.
    g2d.setColor(plottingAxisColor);
    for(int d=0;d<pixels[0].length;d++)
      {
      // Set coordinates.
      int x = margins.left+d*axisSpacing;
      g2d.drawLine((int)x,margins.top,(int)x,margins.top+plotHeight);
      }
    g2d.drawRect(margins.left,margins.top,axisSpacing*(dimensions-1),plotHeight);
    // Paint labels.
    g2d.setFont(labelsFont); 
    FontMetrics m = g2d.getFontMetrics();
    float deltaV = (max-min)/(numMarks-1);
    for(int l=0;l<numMarks;l++)
      {
      String label = String.format("%6.1f",new Object[]{new Float(deltaV*l)});
      int x = margins.left-m.stringWidth(label)-2;
      g2d.drawString(label,x,margins.top+plotHeight-l*deltaV/2);
      }
    // Plot the title.
    g2d.setFont(titleFont);
    m = g2d.getFontMetrics();
    int x = getPreferredSize().width/2-m.stringWidth(title)/2;
    g2d.drawString(title,x,margins.top-15);
    } // end paintComponent
  
  }