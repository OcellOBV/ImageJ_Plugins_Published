package syncwindows;

import java.awt.Rectangle;
import imagepath.MoldevPathModel;
import imagepath.PathModel.sortingMethodNames;
import imagepath.PathModel;
import imagepath.RegularPathModel;

public interface SyncWindowsModelIF {
	public int getCurrentSlice();
	public double getCurrentMagnification();
	public Rectangle getCurrentSrcRect();
	public MoldevPathModel getMoldevPath(int i);
	RegularPathModel getRegularPath(int i);
	public PathModel getPath(int i);
	public int getOverlayWindow();
	
	public void setCurrentSlice(int slice);
	public void setCurrentSrcRect(Rectangle rect);
	public void setCurrentMagnification(double magnification);
	void setPath(int i, PathModel path);
	public void setOverlayWindow(int i);
	sortingMethodNames getSortingMethod();
	void setSortingMethod(sortingMethodNames value);
}
