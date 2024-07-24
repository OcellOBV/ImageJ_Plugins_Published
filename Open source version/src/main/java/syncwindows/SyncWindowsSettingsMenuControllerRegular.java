package syncwindows;

import java.awt.Rectangle;
import java.util.Properties;
import ij.IJ;
import helpers.ExtendedProperties;
import imagepath.RegularPathModel;

public class SyncWindowsSettingsMenuControllerRegular extends SyncWindowsSettingsMenuController {

	
	public SyncWindowsSettingsMenuControllerRegular(SyncWindowsModelIF model, SyncWindowsSettingsView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	protected ExtendedProperties getPropertiesFromModel() {
		ExtendedProperties props = new ExtendedProperties();
		props.putWithException("slice", Integer.toString(model.getCurrentSlice()));
		props.putWithException("srcRect", model.getCurrentSrcRect().toString());
		props.putWithException("magnification", Double.toString(model.getCurrentMagnification()));
		props.putWithException("overlayWindow", Integer.toString(model.getOverlayWindow()));
		for (int i=0; i<SyncWindowsConstants.N; i++) {
			props.putWithException("root_"+i, model.getRegularPath(i).getRoot());
			props.putWithException("filename_"+i, model.getRegularPath(i).getFilename());
		}
		return props;
	}

	@Override
	protected void setModelFromProperties(Properties props) {
		for (int i=0; i<SyncWindowsConstants.N; i++) {
			try {
				Rectangle srcRect = getRectangleFromString(props.getProperty("srcRect"));
				model.setCurrentSrcRect(srcRect);
			}
			catch (Exception e) {
				IJ.log("setModelFromProperties exception: restore srcRect failed");
			}

			model.setCurrentSlice(Integer.parseInt(props.getProperty("slice")));
			model.setCurrentMagnification(Double.parseDouble(props.getProperty("magnification")));

			try {
				String root = props.getProperty("root_"+i, model.getRegularPath(i).getRoot());
				String filename = props.getProperty("filename_"+i, model.getRegularPath(i).getFilename());

				RegularPathModel RegularPath = new RegularPathModel(root,model.getSortingMethod());
				System.out.println(root + " " + filename + " " );
				RegularPath.setFilename(filename);
				model.setPath(i, RegularPath);
				view.setPath(i, RegularPath);
			}
			catch (Exception e) {
				IJ.log("setModelFromProperties exception: restore RegularPath("+i+") failed");
			}
		}
	}
	
	protected String[] getRestoreSettingsFile() {
		String[] values = new String [2];
		values[0] = getAutoSavingPath() + "syncwin_autosave_regular.txt";
		values[1] = getAutoSavingPath() + "rois_autosave_regular.zip";
		return values;
	}


}
