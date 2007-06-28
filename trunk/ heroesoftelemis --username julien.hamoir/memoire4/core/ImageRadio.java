/*
 * Created on 04-avr.-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package core;

/**
 * @author Mbenza B. Patrick
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.image.BufferedImage;
public class ImageRadio {
	private String path;
	private BufferedImage image;
public ImageRadio(String s,BufferedImage im){
	this.path=s;
	this.image=im;
}
public String getPath(){return this.path;}
public BufferedImage getImage(){return this.image;}
//public Point[] getCarre(){return this.carre;}
//public Point getCroix(){return this.croix;}
}
