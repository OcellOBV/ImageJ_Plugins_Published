package syncwindows;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import org.apache.commons.io.FilenameUtils;

import ij.IJ;
import ij.ImagePlus;
import imagepath.MoldevEmptyPath;
import imagepath.RegularEmptyPath;
import syncwindows.SyncStackWindow.BackgroundStates;

public class SyncWindowsPathController implements Runnable {

	protected SyncWindowsModelIF model;
	protected SyncWindowsViewIF view;

	private KeyListener keyController;
	private MouseListener mouseController;
	private MouseMotionListener motionController;

	private int i;

	public SyncWindowsPathController(SyncWindowsModelIF model, SyncWindowsViewIF view, int i) {
		this.model = model;
		this.view = view;
		this.i = i;
	}

	public int get_i() {
		return i;
	}

	public void setKeyController(KeyListener controller) {
		keyController = controller;
	}

	public void setMouseController(MouseListener controller) {
		mouseController = controller;
	}

	public void setMotionController(MouseMotionListener controller) {
		motionController = controller;
	}

	public void run() {
		if(model.getPath(i).getFilename()==null || model.getPath(i) instanceof MoldevEmptyPath || model.getPath(i) instanceof RegularEmptyPath)
			return;
		String filename = model.getPath(i).getFullFile();
		System.out.println(filename);
		File file = new File(filename);
		if (!file.exists()) {
			System.out.println("File doesn't exist");
			IJ.log("The file does not exist!");
			return;
		}
		SyncStackWindow syncStackWindow = view.getSyncStackWindow(i);
		if(syncStackWindow!=null && syncStackWindow.isShowing()) {
			if (isSameFilename(filename, syncStackWindow)) {
				System.out.println("IS SAME FILE !!!");
				return;
			}
		}
		

		if (Thread.currentThread().isInterrupted()) {
			System.out.println("Interrupted before opening image");
			return;
		}
		System.out.println("createSyncStackWindow for i="+i);

		// somehow it doesnt work to remove the image listener, therefore the function below has been created as a work-around
		//view.removeImageListener(imageController);
		view.setImageListenerEnabled(false);
		
		//I think we can do something about memory right there. Or in the SyncWindowsView.setSyncStackWindow
		System.out.println("memory left = " + IJ.freeMemory());
		IJ.log(IJ.freeMemory());
		
		syncStackWindow = createSyncStackWindow(syncStackWindow, filename);
		//view.registerImageListener(imageController);

		if (Thread.currentThread().isInterrupted()) {
			view.setImageListenerEnabled(true);
			System.out.println("Interrupted after opening image");
			return;
		}
		if (syncStackWindow == null) {
			view.setImageListenerEnabled(true);
			System.out.println("syncStackWindow == null");
			return;
		}

		view.setSyncStackWindow(i, syncStackWindow);
		IJ.freeMemory();
		view.syncProperties();
		view.setImageListenerEnabled(true);
	}

	private boolean isSameFilename(String filename, SyncStackWindow syncStackWindow) {
		if (syncStackWindow == null) return false;

		String targetFilename = FilenameUtils.getBaseName(filename);
		String currentFilename = syncStackWindow.getCanvas().getImage().getShortTitle();
		System.out.println(targetFilename);
		System.out.println(currentFilename);
		return targetFilename.equals(currentFilename);

	}

	protected  synchronized SyncStackWindow createSyncStackWindow(SyncStackWindow syncStackWindow, String filename) {
		closeSyncStackWindow(syncStackWindow, filename);
		IJ.log("openImage("+filename+")");
		ImagePlus imp = IJ.openImage(filename); // this one slows down the opening images from server!
		imp.setTitle(get_i() + " : " + imp.getTitle());
		syncStackWindow = new SyncStackWindow(imp);
		System.out.println("new SyncStackWindow "+imp.getShortTitle()+" created (order 1)");
		syncStackWindow.registerKeyListener(keyController);
		syncStackWindow.registerMouseListener(mouseController);
		syncStackWindow.registerMouseMotionListener(motionController);
		syncStackWindow.setBackgroundState(BackgroundStates.NORMAL);
		return syncStackWindow;
	}

	protected void closeSyncStackWindow(SyncStackWindow syncStackWindow, String filename) {
		try {
			String[] imageTitles=ij.WindowManager.getImageTitles();
			for(String title :imageTitles){
				System.out.print(title);
				if(title.startsWith(Integer.toString(get_i()))) {
					IJ.selectWindow(title);
					IJ.run("Close");
					System.gc();
					System.out.println(title);
				}
			}
			if (syncStackWindow != null) {
				System.out.println("Removing listeners.");
				syncStackWindow.setBackgroundState(BackgroundStates.LOADING);
				syncStackWindow.removeKeyListener(keyController);
				syncStackWindow.removeMouseListener(mouseController);
				syncStackWindow.removeMouseMotionListener(motionController);
				if (syncStackWindow.isShowing() && syncStackWindow.getTitle()!=filename) {
					syncStackWindow.close();
				}
		
			}
		}
		catch (Exception e) {
			IJ.log("Problem closing the image");
}
	}
}
