package syncwindows;

import ij.ImageListener;
import ij.ImagePlus;
import imagepath.MoldevEmptyPath;
import imagepath.MoldevPathModel;
import imagepath.PathModel;
import imagepath.RegularEmptyPath;
import imagepath.RegularPathModel;

public class SyncWindowsImageController implements ImageListener {

	private SyncWindowsModelIF model;
	private SyncWindowsViewIF view;
	private SyncWindowsSettingsView settingsView;

	public SyncWindowsImageController(SyncWindowsModelIF model, SyncWindowsViewIF view, SyncWindowsSettingsView settingsView) {
		this.model = model;
		this.view = view;
		this.settingsView = settingsView;
	}

	protected boolean isUpdated(ImagePlus imp) {
		boolean isProjection = imp.getNSlices() == 1;
		if (isProjection) return false;

		int currentSlice = imp.getCurrentSlice();
		if (currentSlice == model.getCurrentSlice()) return false;
		return true;
	}

	@Override
	public void imageUpdated(ImagePlus imp) {
	    int i = 0;
	    String title = "";
	    try {
		    title = imp.getCanvas().getImage().getTitle();
	    }
	    catch (Exception e){
	    	System.out.println("No title");
	    }

	    
	    if (title != null && !title.isEmpty()) {
	        try {
	            i = Integer.parseInt(title.substring(0, 1));
	        } catch (NumberFormatException | IndexOutOfBoundsException e) {
	            System.err.println("Error converting the first character of the title to an integer: " + e.getMessage());
	            i = -1;
	        }
	    }
	    
	    if (i == -1 || !view.isImageListenerEnabled(i)) return;
	    if (!isUpdated(imp)) return;
	    
	    System.out.println("imageUpdated");
	    view.setSlice(imp);
	    view.setCursorOnImps();
	}

	/**
	 * We empty the line of the image we closed
	 */
	@Override
	public void imageClosed(ImagePlus imp) {
		int i = view.getLineToDelete(imp);
		if (!view.isImageListenerEnabled(i)) return;
		if (i == -1) return;
		
		PathModel emptyPath = null;
		if (model.getPath(i) instanceof MoldevPathModel) {
			emptyPath = new MoldevEmptyPath();
		}
		else if (model.getPath(i) instanceof RegularPathModel) {
			emptyPath = new RegularEmptyPath();
		}
		view.setSyncStackWindow(i, null);
		model.setPath(i,emptyPath);
		settingsView.setPath(i,emptyPath);
	}

	// TODO move listeners to here?
	@Override
	public void imageOpened(ImagePlus imp) {
		System.out.println("Open");
	}

}
