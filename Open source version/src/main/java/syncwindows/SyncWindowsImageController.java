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
		if (!view.isImageListenerEnabled()) return;
		if (!isUpdated(imp)) return;
		System.out.println("imageUpdated"); // TODO this happens too much I think?
		view.setSlice(imp);
		view.setCursorOnImps();
	}

	/**
	 * We empty the line of the image we closed
	 */
	@Override
	public void imageClosed(ImagePlus imp) {
	if (!view.isImageListenerEnabled()) return;
		int i = view.getLineToDelete(imp);
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
