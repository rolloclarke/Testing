import io.ctc.images.FlirData;
import io.ctc.images.ImageInfo;
import io.ctc.images.ImageUtil;
import io.ctc.utils.Base64Util;
import io.ctc.utils.FileUtil;
import io.ctc.utils.StreamHelper;
import io.ctc.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;

import alchemy.petsitters.app.Utils;
import junit.framework.TestCase;

public class Images extends TestCase {

	public void testCTCJPEG() throws Exception {

		File file = new File("/Users/Rollo/Desktop/fire.jpg");

		ImageInfo ii = ImageUtil.readImage(file);
		
		FlirData flir = ii.getFlirData();
		
		// extract png data
		byte[] png = flir.getRawThermalImage();
		
		byte[] compressed = ImageUtil.getCompressedDataFromPNG(png);
		byte[] decompressed = ImageUtil.decompressPNGData(compressed);
		byte[] raw = ImageUtil.defilterPNGData(decompressed, 120, 160, 2);
		FileUtil.writeFile(new File("/Users/Rollo/Desktop/raw-data.raw"), raw);
		
		//print(Base64Util.encode(raw, Base64Util.standardBase64Table));
		//print("\n\n");
		// find lowest and largest values
		int lowest = 999999;
		int highest = -1;
		for (int i = 0; i < raw.length; i+=2) {
			int v = ImageUtil.int16u(raw, i, false);
			if (v < lowest) lowest = v;
			if (v > highest) highest = v;
		}
		//print(lowest);
		//print("-" + highest);
		
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE HTML>\n");
		sb.append("<html><head><meta charset=\"utf-8\"><script src='thermal.js'></script><link rel=\"stylesheet\" type=\"text/css\" href=\"thermal.css\"></head><body>");
		sb.append("<div class='tic'><canvas id='canvas'></canvas></div>");
		
		sb.append("</body></html>");
		sb.append("<script>var photo = new Image();photo.src='data:image/jpeg;base64,");
		sb.append(Base64Util.encode(flir.getEmbeddedImage(), Base64Util.standardBase64Table));
		sb.append("';");
		sb.append("var raw = base64ToArrayBuffer('" +  Base64Util.encode(raw, Base64Util.standardBase64Table) + "');");
		sb.append("window.ctc = new CTCThermal(document.getElementById('canvas'), raw, photo, {real2IR:" + flir.getPipReal2IR() + ", offsetX:" + flir.getPipOffsetX() + ", offsetY:" + flir.getPipOffsetY() + "}, ");
		sb.append("{planckR1:" + flir.getPlanckR1() + 
				", planckR2:" + flir.getPlanckR2() + 
				", planckB:" + flir.getPlanckB() + 
				", planckF:" + flir.getPlanckF() + 
				", planckO:" + flir.getPlanckO() + 
				", emissivity:" + flir.getEmissivity() + 
				", reflectedTemp:" + flir.getRefelctedApparentTemperature() + "});</script>");
		FileUtil.writeFile(new File("/Users/Rollo/Desktop/raw.html"), sb.toString().getBytes());
		
		// write new ctc data
//		byte[] ctc = "Boomtown".getBytes(StringUtil.UTF8);
//		ImageUtil.writeFile(new File("/Users/Rollo/Desktop/test2.jpg"), ii, ctc);
		
		print(flir.getPlanckR1());

	}

	public void testZ() {
		byte[] b = new byte[] {(byte) 0xFF, (byte) 0xFF};
		
		print(ImageUtil.readAsShort(b, 0, 2, true));
	}


	private void print(Object str) {
		System.out.print(str);

	}

	
}