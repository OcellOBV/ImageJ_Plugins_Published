package syncwindows;

import java.awt.Rectangle;
import java.util.Properties;

import ij.IJ;
import helpers.ExtendedProperties;
import imagepath.MoldevPathModel;
import imagepath.MoldevPathModelFactory;

public class SyncWindowsSettingsMenuControllerMoldev extends SyncWindowsSettingsMenuController{
	

	public SyncWindowsSettingsMenuControllerMoldev(SyncWindowsModelIF model, SyncWindowsSettingsView view) {
		this.model = model;
		this.view = view;
	}
	
	protected ExtendedProperties getPropertiesFromModel() {
		ExtendedProperties props = new ExtendedProperties();
		props.putWithException("slice", Integer.toString(model.getCurrentSlice()));
		props.putWithException("srcRect", model.getCurrentSrcRect().toString());
		props.putWithException("magnification", Double.toString(model.getCurrentMagnification()));
		props.putWithException("overlayWindow", Integer.toString(model.getOverlayWindow()));
		props.putWithException("lock_plate", String.valueOf((view.getSettingsPanelMoldev().getLockPlateCheckBox().isSelected())));
		props.putWithException("lock_row", String.valueOf((view.getSettingsPanelMoldev().getLockWellRowCheckBox().isSelected())));
		props.putWithException("lock_col", String.valueOf((view.getSettingsPanelMoldev().getLockWellColCheckBox().isSelected())));
		props.putWithException("lock_site", String.valueOf((view.getSettingsPanelMoldev().getLockSiteCheckBox().isSelected())));
		props.putWithException("lock_channel", String.valueOf((view.getSettingsPanelMoldev().getLockChannelCheckBox().isSelected())));
		props.putWithException("lock_time", String.valueOf((view.getSettingsPanelMoldev().getLockTimeCheckBox().isSelected())));
		props.putWithException("lock_filename", String.valueOf((view.getSettingsPanelMoldev().getLockFilenameCheckBox().isSelected())));
		for (int i=0; i<SyncWindowsConstants.N; i++) {
			props.putWithException("root_"+i, model.getMoldevPath(i).getRoot());
			props.putWithException("plate_"+i, model.getMoldevPath(i).getPlate());
			props.putWithException("well_row_"+i, model.getMoldevPath(i).getWellRow());
			props.putWithException("well_col_"+i, model.getMoldevPath(i).getWellCol());
			props.putWithException("site_"+i, model.getMoldevPath(i).getSite());
			props.putWithException("channel_"+i, model.getMoldevPath(i).getChannel());
			props.putWithException("filename_"+i, model.getMoldevPath(i).getFilename());
		}
		return props;
	}

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
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockPlateCheckBox().isSelected()).equals(props.getProperty("lock_plate")))) {
					view.getSettingsPanelMoldev().getLockPlateCheckBox().doClick();
				}
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockWellRowCheckBox().isSelected()).equals(props.getProperty("lock_row")))) {
					view.getSettingsPanelMoldev().getLockWellRowCheckBox().doClick();
				}
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockWellColCheckBox().isSelected()).equals(props.getProperty("lock_col")))) {
					view.getSettingsPanelMoldev().getLockWellColCheckBox().doClick();
				}
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockSiteCheckBox().isSelected()).equals(props.getProperty("lock_site")))) {
					view.getSettingsPanelMoldev().getLockSiteCheckBox().doClick();
				}
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockChannelCheckBox().isSelected()).equals(props.getProperty("lock_channel")))) {
					view.getSettingsPanelMoldev().getLockChannelCheckBox().doClick();
				}
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockTimeCheckBox().isSelected()).equals(props.getProperty("lock_time")))) {
					view.getSettingsPanelMoldev().getLockTimeCheckBox().doClick();
				}
				if (!(String.valueOf(view.getSettingsPanelMoldev().getLockFilenameCheckBox().isSelected()).equals(props.getProperty("lock_filename")))) {
					view.getSettingsPanelMoldev().getLockFilenameCheckBox().doClick();
				}

				String root = props.getProperty("root_"+i, model.getMoldevPath(i).getRoot());
				String plate = props.getProperty("plate_"+i, model.getMoldevPath(i).getPlate());
				String well_row = props.getProperty("well_row_"+i, model.getMoldevPath(i).getWellRow());
				String well_col = props.getProperty("well_col_"+i, model.getMoldevPath(i).getWellCol());
				String site = props.getProperty("site_"+i, model.getMoldevPath(i).getSite());
				String channel = props.getProperty("channel_"+i, model.getMoldevPath(i).getChannel());
				String time = props.getProperty("time_"+i, model.getMoldevPath(i).getTime());
				String filename = props.getProperty("filename_"+i, model.getMoldevPath(i).getFilename());

				MoldevPathModel moldevPath = MoldevPathModelFactory.createMoldevPath(root,model.getSortingMethod());
				System.out.println(root + " " + plate + " " + well_row + " " + well_col + " " + site + " " + channel + " " + time + " " + filename + " " );
				moldevPath.setPlate(plate);
				moldevPath.setRow(well_row);
				moldevPath.setCol(well_col);
				moldevPath.setSite(site);
				moldevPath.setChannel(channel);
				moldevPath.setTime(time);
				moldevPath.setFilename(filename);
				model.setPath(i, moldevPath);
				view.setPath(i, moldevPath);
			}
			catch (Exception e) {
				IJ.log("setModelFromProperties exception: restore moldevPath("+i+") failed");
			}
		}
	}
	protected String[] getRestoreSettingsFile() {
		String[] values = new String [2];
		values[0] = getAutoSavingPath() + "syncwin_autosave_moldev.txt";
		values[1] = getAutoSavingPath() + "rois_autosave_moldev.zip";
		return values;
	}

}
