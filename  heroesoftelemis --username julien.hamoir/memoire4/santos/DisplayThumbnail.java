/*
 * Created on May 31, 2005
 * @author Rafael Santos (rafael.santos@lac.inpe.br)
 * 
 * Part of the Java Advanced Imaging Stuff site
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI)
 * 
 * STATUS: Complete but with some minor bugs and annoyances.
 * 
 * Redistribution and usage conditions must be done under the
 * Creative Commons license:
 * English: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.en
 * Portuguese: http://creativecommons.org/licenses/by-nc-sa/2.0/br/deed.pt
 * More information on design and applications are on the projects' page
 * (http://www.lac.inpe.br/~rafael.santos/Java/JAI).
 */


package santos;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.renderable.ParameterBlock;
import javax.media.jai.BorderExtenderConstant;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import com.sun.media.jai.widget.DisplayJAI;

/**
 * This class represents a small thumbnail of an image as a component, and
 * allows the user to interactively position a viewport over it. A method
 * returns the original image positioned to correspond to the viewport.
 * Some decorations are also displayed over the thumbnail (border, tile markers).
 *
 * Note: I've tried to create an application which used this component and the
 * whole image on a JScrollPane so when the user changed the viewport using the
 * JScrollBars the thumbbail would also change. I've run into all sorts of
 * problems, the worst being that I could not use the AdjustmentListener to
 * listen for changes in the viewport since changes on the thumbail fired
 * AdjustmentEvents, at the end I just gave up.
 *
 * Another note: I know that the movement of the viewport is sometimes
 * erratical, which makes selection of border regions difficult, will fix
 * it someday.
 */
public class DisplayThumbnail extends DisplayJAI
                              implements MouseMotionListener,MouseListener
  {
  // The original image.
  private PlanarImage originalImage;
  // The scale (< 1.0) for the thumbnail creation.
  private float scale;
  // The number of tiles on the original image.
  private int imageXTiles,imageYTiles;
  // The tiles width and height on the original image.
  private int imageTileWidth,imageTileHeight;
  // The dimensions of the original image.
  private int imageWidth,imageHeight;
  // The dimensions of the visible region.
  private int visibleRegionWidth,visibleRegionHeight;
  // The dimensions of the scaled thumbnail.
  private int thumbWidth,thumbHeight;
  // The size of the border around the thumbnail.
  private final int border = 10;
  // The scaled viewport (dimensions are scaled/translated by the border).
  private Rectangle2D scaledViewport;
  // The color of the viewport. It will be set depending on whether the mouse
  // cursor is over the viewport or not.
  private Color viewportColor;
  // Colors to be used when the mouse is/isn't over the viewport.
  private static Color viewportOn = new Color(120,255,120);
  private static Color viewportOff = new Color(0,192,0);
  // Coordinates obtained when we click (press) the mouse button to start
  // dragging the viewport.
  private int lastX,lastY;  
  // Those coordinates represent the region where we can safely drag the
  // viewport without "falling outside" the image boundaries.
  private int minValidX,minValidY,maxValidX,maxValidY;

 /**
  * The constructor for the class, which creates a thumbnail version of
  * the image and sets the user interface.
  * @param image the image to be used for the thumbnail creation.
  * @param scale the scale to be used for the thumbnail creation.
  * @param width the width of the desired viewport (pixels).
  * @param height the width of the desired viewport (pixels).
  */
  public DisplayThumbnail(PlanarImage image,float scale,int width,int height)
    {
    this.scale = scale;
    originalImage = image;
    visibleRegionWidth = width;
    visibleRegionHeight = height;
    // Get some stuff about the image.
    imageXTiles = image.getNumXTiles();
    imageYTiles = image.getNumYTiles();
    imageTileWidth = image.getTileWidth();
    imageTileHeight = image.getTileHeight();
    imageWidth = image.getWidth();
    imageHeight = image.getHeight();
    // Must create a thumbnail image using that scale.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add(scale);
    pb.add(scale);
    pb.add(0.0F);
    pb.add(0.0F);
    pb.add(new InterpolationNearest());
    PlanarImage thumbnail = JAI.create("scale", pb, null);
    // Let's get the width and height of the thumbnail.
    thumbWidth = thumbnail.getWidth();
    thumbHeight = thumbnail.getHeight();
    // Now let's add a border.
    pb = new ParameterBlock();
    pb.addSource(thumbnail);
    pb.add(new Integer(border));
    pb.add(new Integer(border));
    pb.add(new Integer(border));
    pb.add(new Integer(border));
    // Fix this someday ? I'd like to ensure the image border will always be in color.
    pb.add(new BorderExtenderConstant(new double[]{0,0,128}));
    thumbnail = JAI.create("border",pb);
    // Shift the image to the original position (since the borders changed
    // the image's origin).
    pb = new ParameterBlock();
    pb.addSource(thumbnail);
    pb.add(1.0f*border);
    pb.add(1.0f*border);
    thumbnail = JAI.create("translate",pb,null);
    // Use this thumbnail as the image for the DisplayJAI component.
    set(thumbnail);
    // We'd like to listen to mouse movements.
    addMouseMotionListener(this);
    addMouseListener(this);
    // Initially the scaled viewport will be positioned at border,border.
    scaledViewport = new Rectangle2D.Float(border,border,width*scale,height*scale);
    // We assume that the mouse is off the viewport.
    viewportColor = viewportOff;
    }

 /**
  * This method will repaint the component. It will draw the thumbnail image,
  * then draw some yellow lines over the tiles' boundary (if the image is
  * tiled) then draw the viewport.
  */
  public synchronized void paintComponent(Graphics g)
    {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    // Paint the tile grid.
    g2d.setColor(Color.YELLOW);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
    float x1,x2,y1,y2;
    // Vertical tiles' boundaries.
    x1 = x2 = border;
    y1 = border; y2 = border+thumbHeight;
    for(int tx=0;tx<=imageXTiles;tx++)
      {
      g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
      x1 += imageTileWidth*scale;
      x2 += imageTileWidth*scale;
      }
    // Horizontal tiles' boundaries.
    x1 = border; x2 = border+thumbWidth;
    y1 = y2 = border;
    for(int ty=0;ty<=imageYTiles;ty++)
      {
      g2d.drawLine((int)x1,(int)y1,(int)x2,(int)y2);
      y1 += imageTileHeight*scale;
      y2 += imageTileHeight*scale;
      }
    // Paint a red border.
    g2d.setColor(Color.RED);
    g2d.drawRect(border,border,thumbWidth,thumbHeight);
    // Paint the viewport.
    g2d.setColor(viewportColor);
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
    Stroke stroke = new BasicStroke(2f);
    g2d.setStroke(stroke);
    g2d.draw(scaledViewport);
    }

 /**
  * This method will be called whenever the mouse is moved over the thumbnail.
  * Basically it will detect whether the mouse pointer is over or not over the
  * viewport and repaint to change the viewport color.
  */
  public void mouseMoved(MouseEvent e)
    {
    int x = e.getX(); int y = e.getY();
    // Ignore events outside the border.
    if ((x < border) || (y < border) ||
        (x > border+thumbWidth) || (y > border+thumbHeight))
      return;
    // Are we inside the viewport rectangle ?
    if (scaledViewport.contains(x,y)) viewportColor = viewportOn;
    else viewportColor = viewportOff;
    // Hopefully it will repaint only the needed section.
    Rectangle repaintBounds =
      new Rectangle((int)scaledViewport.getX()-5,
                    (int)scaledViewport.getY()-5,
                    (int)scaledViewport.getWidth()+10,
                    (int)scaledViewport.getHeight()+10);
    repaint(repaintBounds);
    }

 /**
  * This method will be called when we first click on the thumbnail to start
  * the dragging movement. It will calculate the new region where we can drag
  * the thumbnail so it won't "fall" outside the image boundaries.
  */
  public void mousePressed(MouseEvent e)
    {
    // Store the new dragging starting points.
    lastX = e.getX(); lastY = e.getY();
    // Calculate the new window where we can consider viewport movements.
    minValidX = lastX-(int)scaledViewport.getX()+border;
    minValidY = lastY-(int)scaledViewport.getY()+border;
    maxValidX = border+thumbWidth-(int)scaledViewport.getWidth()+
    (lastX-(int)scaledViewport.getX());
    maxValidY = border+thumbHeight-(int)scaledViewport.getHeight()+
    (lastY-(int)scaledViewport.getY());
    }

 /**
  * This method will be called when we drag the mouse around the thumbnail.
  * If the coordinates are inside the movable region we will update the
  * location of the viewport.
  */
  public void mouseDragged(MouseEvent e)
    {
    int x = e.getX(); int y = e.getY();
    if (x > maxValidX) x = maxValidX-1;
    if (x < minValidX) x = minValidX;
    if (y > maxValidY) y = maxValidY-1;
    if (y < minValidY) y = minValidY;
    if ((x >= minValidX) && (y >= minValidY) &&
        (x <= maxValidX) && (y <= maxValidY))
      {
      updateLocation(x,y);
      lastX = x; lastY = y;
      }
    }

/**
 * This method updates the viewport position based on the last position
 * where the mouse was dragged to.
 * @param x the last X position where the mouse was dragged to.
 * @param y the last Y position where the mouse was dragged to.
 */
public void updateLocation(int x,int y)
  {
  // Store the approximate region where the viewport was before the change.
  Rectangle initBounds = 
    new Rectangle((int)scaledViewport.getX()-5,
                  (int)scaledViewport.getY()-5,
                  (int)scaledViewport.getWidth()+10,
                  (int)scaledViewport.getHeight()+10);
  // Recalculate the new position for the viewport, based on mouse coordinates.
  double origX = scaledViewport.getX()+x-lastX;
  double origY = scaledViewport.getY()+y-lastY;
  // Reposition the viewport.
  scaledViewport.setFrame(origX,origY,scaledViewport.getWidth(),scaledViewport.getHeight());
  // Store the approximate region where the viewport is after the change.
  Rectangle finalBounds = 
    new Rectangle((int)scaledViewport.getX()-5,
                  (int)scaledViewport.getY()-5,
                  (int)scaledViewport.getWidth()+10,
                  (int)scaledViewport.getHeight()+10);
  // Repaint only the needed section.
  repaint(finalBounds.union(initBounds));
  }

 /**
  * This method will return the part of the original image corresponding
  * to the region under the viewport. The part of the image will be obtained
  * by cropping the original image.
  * @return a PlanarImage with the part of the original image
  */
  public PlanarImage getImage()
    {
    // Get the boundaries in the original image coordinates.
    float fromX  = (float)Math.round((scaledViewport.getX()-border)/scale);
    float fromY  = (float)Math.round((scaledViewport.getY()-border)/scale);
    float width  = (float)Math.round(scaledViewport.getWidth()/scale);
    float height = (float)Math.round(scaledViewport.getWidth()/scale);
    // Fix rounding errors to avoid exceptions on the crop.
    fromX = Math.min(fromX,(imageWidth-visibleRegionWidth));
    fromY = Math.min(fromY,(imageHeight-visibleRegionHeight));
    // Create a ParameterBlock with information for the cropping.
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(originalImage);
    pb.add(fromX);
    pb.add(fromY);
    pb.add(width);
    pb.add(height);
    // Create the output image by cropping the input image.
    PlanarImage output = JAI.create("crop",pb,null);
    // Translate the image origin.
    pb = new ParameterBlock();
    pb.addSource(output);
    pb.add(-fromX);
    pb.add(-fromY);
    // Create the output image by translating itself.
    return JAI.create("translate",pb,null);
    }
  
 /**
  * Return the real world (original image) bounds.
  * @return the original image's bounds.
  */
  public Rectangle getCroppedImageBounds()
    {
    int fromX  = (int)Math.round((scaledViewport.getX()-border)/scale);
    int fromY  = (int)Math.round((scaledViewport.getY()-border)/scale);
    int width  = (int)Math.round(scaledViewport.getWidth()/scale);
    int height = (int)Math.round(scaledViewport.getWidth()/scale);
    return new Rectangle(fromX,fromY,width,height);
    }

 /**
  * Return the scaled (thumbnail) viewport bounds.
  * @return the thumbnail's viewport bounds.
  */
  public Rectangle getViewportBounds()
    {
    Rectangle temp = scaledViewport.getBounds();
    temp.setBounds((int)temp.getX()-border,(int)temp.getY()-border,
        (int)temp.getWidth(),(int)temp.getHeight());
    return temp;
    }

  }