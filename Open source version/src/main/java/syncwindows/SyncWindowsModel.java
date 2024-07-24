package syncwindows;

import java.awt.Rectangle;
import imagepath.MoldevEmptyPath;
import imagepath.MoldevPathModel;
import imagepath.PathModel.sortingMethodNames;
import imagepath.RegularEmptyPath;
import imagepath.PathModel;
import imagepath.RegularPathModel;

public class SyncWindowsModel implements SyncWindowsModelIF {

	private int overlayWindow = -1;
	private int currentSlice = 1;
	private double currentMagnification = 0.0;
	private Rectangle currentSrcRect = new Rectangle(0, 0, 1024, 1024);
	private PathModel[] pathList;
	private sortingMethodNames sortingMethod = sortingMethodNames.LastModified;

	public SyncWindowsModel(String pathSystem) {
		switch (pathSystem) {
		case "moldev":
			pathList = new MoldevPathModel[SyncWindowsConstants.N];
			for (int i = 0; i<SyncWindowsConstants.N; i++)
				pathList[i] = new MoldevEmptyPath();
			break;
		case "regular":
			pathList = new RegularPathModel[SyncWindowsConstants.N];
			for (int i = 0; i<SyncWindowsConstants.N; i++)
				pathList[i] = new RegularEmptyPath();
			break;
		default:
			throw new IllegalArgumentException("Invalid pathSystem: " + pathSystem);
		}
	}

	@Override
	public int getCurrentSlice() {
		return currentSlice;
	}

	@Override
	public Rectangle getCurrentSrcRect() {
		return currentSrcRect; 
	}

	@Override
	public double getCurrentMagnification() {
		return currentMagnification; 
	}

	@Override
	public PathModel getPath(int i) {
		return pathList[i];
	}

	@Override
	public MoldevPathModel getMoldevPath(int i) {
		return (MoldevPathModel) pathList[i];
	}

	@Override
	public RegularPathModel getRegularPath(int i) {
		return (RegularPathModel) pathList[i];
	}

	@Override
	public int getOverlayWindow() {
		return overlayWindow;
	}

	@Override
	public void setCurrentSlice(int slice) {
		System.out.println("set current slice to "+slice);
		currentSlice = slice;
	}

	@Override
	public void setCurrentSrcRect(Rectangle rect) {
		currentSrcRect = rect;
	}

	@Override
	public void setCurrentMagnification(double magnification) {
		currentMagnification = magnification;
	}

	@Override
	public void setPath(int i, PathModel path) {
		pathList[i] = path;
	}


	@Override
	public void setOverlayWindow(int i) {
		overlayWindow = i; // TODO trigger observer?
	}

	@Override
	public void setSortingMethod(sortingMethodNames value) {
		sortingMethod = value;
		for (int i = 0; i < SyncWindowsConstants.N; i ++) {
			getPath(i).setSortingMethod(sortingMethod);
		}
	}


	@Override
	public sortingMethodNames getSortingMethod() {
		return sortingMethod;
	}
}
