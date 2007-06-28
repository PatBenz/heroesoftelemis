package core;
import interfaceGraphique.Home;

import java.awt.image.renderable.ParameterBlock;
import java.io.File;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomInputStream;
import com.pixelmed.display.SourceImage;

public class Test {

	static String premier_path;
	static String second_path;
	static String result_path;
	static ImageRadio premiere_image;
	static ImageRadio seconde_image;
	static ImageRadio result_image;
    /** The main method. */
    public static void main(String[] args) {
    	
    	DicomInputStream dis_1 = null;
    	SourceImage si_1 = null;
    	AttributeList list_1=new AttributeList();
    	//--------------------------------------------------------
    	DicomInputStream dis_2 = null;
    	SourceImage si_2 = null;
    	AttributeList list_2=new AttributeList();
    	//--------------------------------------------------------
    	Home.main(new String[0]);
        try {

        	dis_1= new DicomInputStream(new File(premier_path));
        	//dis.setReadingDataSet();
        	//si=new SourceImage(dis);
        	System.out.println(dis_1.getTransferSyntaxInUse().getUID());
        	//System.out.println(dis.getByteOffsetOfStartOfData());
        	list_1.read(dis_1);
        	if(list_1.isImage())
        	{
        		si_1=new SourceImage(list_1);
        	}
        	
        	/*dis.setReadingMetaHeader();
        	list_1.read(dis);*/
        }
        catch (Exception e) {
        	e.printStackTrace();System.exit(-1);}
        //System.err.print(list_1);
    	
        /* Create an operator to decode the image file. */
    	ParameterBlock pb = new ParameterBlock();
        pb.add(si_1.getBufferedImage());
        //System.out.println(si.getHeight());
        RenderedOp im1 = JAI.create("awtImage", pb);
        
        //example of JAI operation - edge detection
        /*float[] prewitt_h_data        = { 1.0F,  0.0F, -1.0F,
				1.0F,  0.0F, -1.0F,
				1.0F,  0.0F, -1.0F};
        float[] prewitt_v_data        = {-1.0F, -1.0F, -1.0F,
					0.0F,  0.0F,  0.0F,
					1.0F,  1.0F,  1.0F};
        KernelJAI kern_h = new KernelJAI(3,3,prewitt_h_data );
        KernelJAI kern_v = new KernelJAI(3,3,prewitt_v_data);

        ParameterBlock params = new ParameterBlock();
        params.addSource(im1);
        params.add(kern_h);
        params.add(kern_v);

        RenderedImage im2 =
        	JAI.create("gradientmagnitude", params);*/
        
        DcmIOtools.saveJAImageToDicom(im1, list_1, dis_1.getTransferSyntaxInUse().getUID(), "output.dcm");
    	
       
 
        
    }
}
