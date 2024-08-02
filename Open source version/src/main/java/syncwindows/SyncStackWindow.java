package syncwindows;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.ImageWindow;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.StackWindow;
import ij.measure.ResultsTable;
import ij.plugin.filter.ParticleAnalyzer;
import ij.process.ImageProcessor;

public class SyncStackWindow extends StackWindow {

	public enum BackgroundStates {
		NORMAL,
		LOADING,
		CHANGED,
		UPDATING,
		FINISHED,
		ERROR
	}
	
	final private Color overlayColor = Color.yellow;
	
	public SyncStackWindow(ImagePlus imp) {
		super(imp);
	}
	
	public void registerKeyListener(KeyListener controller) {
		removeKeyListener(IJ.getInstance());
		addKeyListener(controller);
		
		ic.removeKeyListener(IJ.getInstance());
		ic.addKeyListener(controller);
	}
	
	public void registerMouseListener(MouseListener controller) {
		ic.addMouseListener(controller);
	}
	
	public void registerMouseMotionListener(MouseMotionListener controller) {
		ic.addMouseMotionListener(controller);
	}
	
	public void setSourceRect(Rectangle sourceRect) {
		ic.setSourceRect(sourceRect);
		ic.getImage().repaintWindow();
	}

	public void setOverlayOnImp(Roi[] rois) {
		Overlay overlay = new Overlay();
		for (Roi roi: rois) {
			overlay.add(roi);
		}
		overlay.setStrokeColor(overlayColor);
		if (imp != null)
			imp.setOverlay(overlay);
	}
	
	public void setBackgroundState(BackgroundStates state) {
		Color color = getStateColor(state);
		setBackground(color);
		ic.getImage().repaintWindow();
		System.out.println("DRAWED");
	}
	
	private Color getStateColor(BackgroundStates state) {
		switch (state) {
		case LOADING:
			return Color.ORANGE;
		case CHANGED:
			return Color.ORANGE;
		case UPDATING:
			return Color.YELLOW;
		case ERROR:
			return Color.RED;
		case FINISHED:
			return Color.GREEN;
		case NORMAL:
		default:
			return Color.WHITE;
		}
	}
	
	public boolean equal(ImagePlus imp2) {
		return imp == imp2;
	}
	
	public boolean equal(ImageWindow w) {
		return imp == w.getCanvas().getImage();
	}
	
	public double getMagnification() {
		return ic.getMagnification();
	}
	

	public void zoomIn(Point loc) {
		ic.zoomIn(loc.x, loc.y);
	}

	public void zoomOut(Point loc) {
		ic.zoomOut(loc.x, loc.y);
	}

	public void unzoom() {
		ic.unzoom();
	}

	public void zoom100Percent() {
		ic.zoom100Percent();		
	}

	public void getCurrentOverlay() {
		// TODO zou mooier zijn als ik de roimanager niet gebruik. dus misschien toch nadenken over het overlay systeem?
		int options = ParticleAnalyzer.ADD_TO_MANAGER | ParticleAnalyzer.SHOW_PROGRESS;
		// TODO: int options = (ParticleAnalyzer.CLEAR_WORKSHEET|ParticleAnalyzer.SHOW_MASKS|ParticleAnalyzer.IN_SITU_SHOW);	       
		int measurements = 0;
		int minSize = 1;
		int maxSize = Integer.MAX_VALUE;
		ResultsTable rt = new ResultsTable();
		ParticleAnalyzer pa = new ParticleAnalyzer(options, measurements, rt, minSize, maxSize);
		
		ImageProcessor ip = imp.getProcessor();
		Rectangle sourceRect = ic.getSrcRect();

		ip.setRoi(sourceRect);
	    ip.crop();
		ip.setThreshold(ip.getAutoThreshold(), ip.maxValue(), ImageProcessor.NO_LUT_UPDATE);
		long t1 = System.currentTimeMillis(); 
		pa.analyze(imp, ip);
		long t2 = System.currentTimeMillis(); 
		System.out.println("Overlay t="+(t2-t1)+" ms"); // TODO only if debug flag enabled?
	}

	public static void main(String[] args) throws Exception {
		
	}
	
}
