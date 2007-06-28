package core;
import java.awt.image.DataBuffer;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.OtherWordAttribute;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.dicom.UnsignedShortAttribute;


public class DcmIOtools {

	public static AttributeList addImageToList(RenderedImage img, AttributeList list)
    throws IOException, DicomException {
    	
    	
    	int srcWidth = img.getWidth();
    	int srcHeight = img.getHeight();
    	SampleModel srcSampleModel = img.getSampleModel();
    	Raster srcRaster = img.getData();
    	DataBuffer srcDataBuffer = srcRaster.getDataBuffer();
    	int srcNumBands = srcRaster.getNumBands();
    	
    	int srcPixels[] = null;
    	srcPixels = srcSampleModel.getPixels(0,0,srcWidth,srcHeight,srcPixels,srcDataBuffer);
    	int srcPixelsLength = srcPixels.length;
    	
    	short rows = (short)srcHeight;
		short columns = (short)srcWidth;

		Attribute pixelData = null;

		int dstPixelsLength = srcWidth*srcHeight;
		
		if (srcNumBands == 1 && srcPixelsLength == dstPixelsLength) {
			short dstPixels[] = new short[dstPixelsLength];
			int dstIndex=0;
			for (int srcIndex=0; srcIndex<srcPixelsLength;) {
				dstPixels[dstIndex++]=(short)(srcPixels[srcIndex++]);
			}
			pixelData = new OtherWordAttribute(TagFromName.PixelData);
			pixelData.setValues(dstPixels);
		}
		else {
			throw new DicomException("Unsupported pixel data form ("+srcNumBands+" bands)");
		}
		if (pixelData != null) {
			list.put(pixelData);


			{ Attribute a = new UnsignedShortAttribute(TagFromName.Rows); a.addValue(rows); list.put(a); }
			{ Attribute a = new UnsignedShortAttribute(TagFromName.Columns); a.addValue(columns); list.put(a); }

		}
    	return list;
    }
	
	public static void saveJAImageToDicom (RenderedImage img, AttributeList attributes, String tsyntax, String outfile){

		AttributeList outlist = new AttributeList();
        
        try {
        	
        	outlist = addImageToList(img, attributes);
        	File OutputFile = new File("output.dcm");
        	outlist.write(OutputFile, tsyntax, true, true);
        }catch (Exception e){e.printStackTrace();}
	}
	
}
