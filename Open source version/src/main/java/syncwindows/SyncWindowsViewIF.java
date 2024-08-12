package syncwindows;

import java.awt.Rectangle;
import java.awt.event.KeyListener;

import ij.ImageListener;
import ij.ImagePlus;
import ij.gui.ImageCanvas;

public interface SyncWindowsViewIF {
	
	public void registerImageListener(ImageListener controller);
	public void registerKeyListener(KeyListener controller);
	
	public void removeImageListener(ImageListener controller);

	public SyncStackWindow getSyncStackWindow(int i);
	public Rectangle getCurrentSrcRect(ImageCanvas canvas);
	
	public void setSyncStackWindow(int i, SyncStackWindow syncStackWindow);
	public void setSrcRect();
	public void setCursorOnImps();
	public void setSlice(ImagePlus imp);
	public void setOverlayWindow(int i);
	
	public void setZoom();
	public void allWindowsToFront();
	public void removeCursor(ImageCanvas canvas);
	public void syncProperties();
	public void performComputations();
	
	public int getLineToDelete(ImagePlus imp);
	public boolean isImageListenerEnabled(int i);
	public void setImageListenerEnabled(int i, boolean value);
    public void setAllImageListenerEnabled(boolean value);
	
}
