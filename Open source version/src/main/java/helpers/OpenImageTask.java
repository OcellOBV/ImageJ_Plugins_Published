package helpers;

import ij.IJ;
import ij.ImagePlus;

public class OpenImageTask implements Runnable {

	private ImagePlus imp;
	private String filename;
	
	public OpenImageTask(String filename) {
		this.filename = filename;
	}

	public ImagePlus getImagePlus() {
		return imp;
	}
	
	public void run() {
		if (filename == null) return;
		imp = IJ.openImage(filename);
	}
	
	
}
