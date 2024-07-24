package syncwindows;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;

import ij.IJ;
import ij.ImageJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.Overlay;
import ij.gui.Roi;
import ij.gui.ShapeRoi;
import ij.plugin.frame.RoiManager;

public class SyncWindowsView implements SyncWindowsViewIF {

	protected SyncWindowsModelIF model;

	private int overlayWindow = -1;
	private boolean enableImageListener = true;

	protected SyncStackWindow[] syncStackWindows;
	protected RoiManager roiManager;

	final private int defaultSettingsViewHeight = 180;
	final private Color cursorColor = Color.red;

	public SyncWindowsView(SyncWindowsModelIF model) {
		this.model = model;
		initializeWindows();
	}

	private void initializeWindows() {
		syncStackWindows = new SyncStackWindow[SyncWindowsConstants.N];
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			syncStackWindows[i] = null;
	}

	@Override
	public void registerImageListener(ImageListener controller) {
		System.out.println("AddImageListener");
		ImagePlus.addImageListener(controller);
	}

	@Override
	public void removeImageListener(ImageListener controller) {
		System.out.println("RemoveImageListener");
		ImagePlus.removeImageListener(controller);
	}

	@Override
	public void registerKeyListener(KeyListener controller) {
		ImageJ imageJ = IJ.getInstance();
		imageJ.removeKeyListener(IJ.getInstance());
		imageJ.addKeyListener(controller);
	}

	@Override
	public SyncStackWindow getSyncStackWindow(int i) {
		return syncStackWindows[i];
	}


	@Override
	public void setSyncStackWindow(int i, SyncStackWindow syncStackWindow) {
		if (syncStackWindows[i] != null) {
			syncStackWindows[i].dispose();
		}
		syncStackWindows[i] = syncStackWindow;
		IJ.freeMemory(); // TODO
	}

	@Override
	public void syncProperties() {
		System.out.println("syncProperties. model.slice="+model.getCurrentSlice());
		System.out.println("syncProperties. model.mag="+model.getCurrentMagnification());
		for(SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) continue;
			if (model.getCurrentMagnification() > 0.0) {
				syncStackWindow.getCanvas().setMagnification(model.getCurrentMagnification());
			}
			syncStackWindow.getCanvas().getImage().setSlice(model.getCurrentSlice());
			syncStackWindow.setSourceRect(model.getCurrentSrcRect());
		}

		setWindowPositions();
	}

	protected void setWindowPositions() {
		int totalWindowWidth = 0;
		int windowWidth = 0;
		int activeWindows = 0;
		for(SyncStackWindow syncWindow: syncStackWindows) {
			if (syncWindow == null) continue;
			totalWindowWidth += syncWindow.getWidth();
			windowWidth = syncWindow.getWidth();
			activeWindows++;
		}

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		int x = 0;
		int settingsViewHeight = defaultSettingsViewHeight;
		int max_x = totalWindowWidth / 2;
		if (activeWindows>2) max_x = (screenWidth - windowWidth) / (activeWindows-1);
		for(SyncStackWindow syncWindow: syncStackWindows) {
			if (syncWindow == null) continue;
			final int taskBarHeight = 100;
			int max_y = screenHeight - syncWindow.getHeight() - taskBarHeight; 
			settingsViewHeight = Math.min(settingsViewHeight, max_y);
			syncWindow.setLocation(x, settingsViewHeight);
			x += Math.min(syncWindow.getWidth(), max_x);
		}

		if (roiManager != null) { // TODO don't do this as last, because it will get focus and then zoom doesnt work anymore
			Rectangle rmSize = roiManager.getBounds();
			int rm_x = Math.min(x, screenSize.width-rmSize.width);
			roiManager.setLocation(rm_x, 0);
		}

		SyncStackWindow currentSyncWindow = getCurrentSyncWindow();
		if (currentSyncWindow != null) {
			currentSyncWindow.toFront();
		}
	}

	@Override
	public void setZoom() {
		Point loc = null;
		try {
			loc = getZoomPoint();
		}
		catch (Exception e) {
			return;
		}
		SyncStackWindow currentSyncStackWindow = getCurrentSyncWindow();
		if (currentSyncStackWindow != null) {
			model.setCurrentSrcRect((Rectangle) currentSyncStackWindow.getCanvas().getSrcRect().clone());
			model.setCurrentMagnification(currentSyncStackWindow.getCanvas().getMagnification());
			setSrcRect(); 

			for (SyncStackWindow syncStackWindow: syncStackWindows) {
				currentSyncStackWindow.toFront();
				if (syncStackWindow == null) {
					continue;
				}
				if (syncStackWindow == currentSyncStackWindow) {
					continue;
				}
				syncStackWindow.getCanvas().setMagnification(model.getCurrentMagnification());
				if (syncStackWindow.getCanvas().getMagnification() > currentSyncStackWindow.getCanvas().getMagnification()) {
					syncStackWindow.zoomOut(loc);
				}
				else if ( (syncStackWindow.getCanvas().getMagnification() < currentSyncStackWindow.getCanvas().getMagnification())) {
					syncStackWindow.zoomIn(loc);
				}
			}
		}

		setSrcRect();
		performComputations();
		setWindowPositions();

	}

	@Override
	public void allWindowsToFront() {
		if (roiManager != null) {
			roiManager.toFront();
		}
		for (SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) continue;
			syncStackWindow.toFront();
		}

		IJ.getInstance().toFront();
	}

	@Override
	public void removeCursor(ImageCanvas canvas) {
		Overlay overlay = canvas.getImage().getOverlay();
		removeCursorRoiFromOverlay(overlay); 
		canvas.getImage().setOverlay(overlay);
	}

	@Override
	public Rectangle getCurrentSrcRect(ImageCanvas canvas) {
		if (canvas.getSrcRect() != model.getCurrentSrcRect()) {
			performComputations();
			return (Rectangle) canvas.getSrcRect().clone();
		}
		return null;
	}

	@Override
	public void setSrcRect() {
		ImagePlus currentImp = WindowManager.getCurrentImage();
		Rectangle currentSourceRect = currentImp.getCanvas().getSrcRect();

		for (SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) {
				continue;
			}
			if (syncStackWindow.equal(currentImp)) {
				continue;
			}
			syncStackWindow.setSourceRect(currentSourceRect);
		}
	}

	@Override
	public void setCursorOnImps() {
		ShapeRoi roi = getCursorRoi();
		for (SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) continue;
			setCursorOnImp(syncStackWindow, roi);
		}
	}

	private ShapeRoi getCursorRoi() {
		Point currentCursorLoc = getCurrentCursorLoc();
		return getCursorRoi(currentCursorLoc);		
	}

	private Point getCurrentCursorLoc() {
		for (SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) continue;
			ImageCanvas canvas = syncStackWindow.getCanvas();
			if (canvas.cursorOverImage()) {
				return canvas.getCursorLoc();
			}
		}
		return new Point();
	}

	public  ShapeRoi getCursorRoi(Point currentCursorLoc) {
		int x = currentCursorLoc.x;
		int y = currentCursorLoc.y;

		GeneralPath path = new GeneralPath();
		path.moveTo(x-5, y-5); path.lineTo(x-1, y-1);
		path.moveTo(x+5, y-5); path.lineTo(x+1, y-1);
		path.moveTo(x-5, y+5); path.lineTo(x-1, y+1);
		path.moveTo(x+5, y+5); path.lineTo(x+1, y+1);

		ShapeRoi roi = new ShapeRoi(path);
		roi.setStrokeColor(cursorColor);
		roi.setStrokeWidth(2);
		roi.setNonScalable(true);

		return roi;
	}

	public void setCursorOnImp(SyncStackWindow syncStackWindow, ShapeRoi roi) {
		ImageCanvas canvas = syncStackWindow.getCanvas();
		if (!canvas.cursorOverImage()) {
			ImagePlus imp = canvas.getImage();
			Overlay overlay = imp.getOverlay();
			overlay = removeCursorRoiFromOverlay(overlay);
			overlay.add(roi);
			imp.setOverlay(overlay);
		}		
	}

	public Overlay removeCursorRoiFromOverlay(Overlay overlay) {
		if (overlay == null) {
			overlay = new Overlay();
		} else {
			for (int i = overlay.size()-1; i>=0; i--) {
				Roi roi = overlay.get(i);
				if (roi.getStrokeColor() == cursorColor )
					overlay.remove(i);
			}
		}
		return overlay;
	}

	@Override
	public void setSlice(ImagePlus imp) {
		System.out.println("setSlice (syncwindows)");

		if (imp.getCanvas() == null) return;

		int currentSlice = imp.getCurrentSlice();
		model.setCurrentSrcRect((Rectangle) imp.getCanvas().getSrcRect().clone());
		model.setCurrentSlice(currentSlice);

		for(SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) continue;
			ImagePlus imp2 = syncStackWindow.getCanvas().getImage();
			imp2.setSlice(currentSlice);
			syncStackWindow.setSourceRect(model.getCurrentSrcRect());
		}

		performComputations();
	}

	@Override
	/**
	 * return -1 if we don't find the image
	 */
	public int getLineToDelete(ImagePlus imp) {
		for (int i=0; i<SyncWindowsConstants.N; i++) {
			if (syncStackWindows[i] == null) continue;
			if (syncStackWindows[i].getName() == "") continue;
			if (syncStackWindows[i].getCanvas().getImage().equals(imp)) {
				return i;
			}
		}
		return -1;
	}

	private Point getZoomPoint() {
		Point p = new Point();
		ImagePlus currentImp = WindowManager.getCurrentImage();
		ImageCanvas canvas = currentImp.getCanvas();
		p.x = canvas.getWidth()/2;
		p.y = canvas.getHeight()/2;
		return p;
	}

	protected SyncStackWindow getCurrentSyncWindow() {
		ImagePlus currentImage = WindowManager.getCurrentImage();
		for (SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) {
				continue;
			}
			if (syncStackWindow.equal(currentImage)) {
				return syncStackWindow;
			}
		}
		return null;
	}

	@Override
	public void setOverlayWindow(int i) {
		overlayWindow = i;
		performComputations();
		setWindowPositions();
	}

	@Override
	public void performComputations() {
		System.out.println("performComp (syncwindows)");
		if (roiManager == null) {
			roiManager = new RoiManager();
		}
		else if (overlayWindow > -1) {
			roiManager.reset();
		}
		roiManager = RoiManager.getInstance();
		// TODO add message to GUI: any ROI's will be removed
		if (overlayWindow > -1 && syncStackWindows[overlayWindow] != null) {
			syncStackWindows[overlayWindow].getCurrentOverlay();
			roiManager.runCommand("Show None");
		}
		for (SyncStackWindow syncStackWindow: syncStackWindows) {
			if (syncStackWindow == null) {
				continue;
			}
			Roi[] rois = roiManager.getRoisAsArray();
			syncStackWindow.setOverlayOnImp(rois);
		}
	}

	@Override
	public void setImageListenerEnabled(boolean value) {
		enableImageListener = value;
	}

	@Override
	public boolean isImageListenerEnabled() {
		return enableImageListener;
	}

}
