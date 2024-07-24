package syncwindows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import ij.IJ;
import ij.plugin.frame.RoiManager;
import imagepath.MoldevEmptyPath;

public abstract class SyncWindowsSettingsController implements ActionListener {

	protected SyncWindowsModelIF model;
	protected SyncWindowsPathController pathController;

	protected Thread updatePathThread;
	protected boolean ignoreUpdate;
	protected boolean firstImageEmpty;

	public static SyncWindowsSettingsController CreateSettingsController(SyncWindowsModelIF model, SyncWindowsSettingsView view, SyncWindowsPathController pathController, String pathSystem) {
		switch (pathSystem) {
		case "moldev":
			return new SyncWindowsSettingsControllerMoldev(model, (SyncWindowsSettingsViewMoldev) view, pathController);
		case "regular":
			return new SyncWindowsSettingsControllerRegular(model, (SyncWindowsSettingsViewRegular) view, pathController);
		default:
			throw new IllegalArgumentException("Invalid pathSystem: " + pathSystem);
		}
	}

	public int get_i() {
		return pathController.get_i();
	}

	@Override
	public abstract void actionPerformed(ActionEvent event);

	protected abstract void rootAction(JTextField textField);
	
	protected abstract void filenameAction(JComboBox<String> comboBox);

	protected abstract SyncWindowsSettingsPanel getSettingsPanel();

	protected void copyAction(JButton button) {
		String path = model.getPath(0).getRoot();
		getSettingsPanel().getRootTextField(get_i()).setText(path);
		rootAction(getSettingsPanel().getRootTextField(get_i()));
	}

	protected void overlayAction(JCheckBox checkBox) {
		RoiManager roi = RoiManager.getInstance();
		if (roi != null) {
			roi.reset();
		}
		boolean value = checkBox.isSelected();
		model.setOverlayWindow(value ? get_i() : -1);
		for (int j = 0; j<SyncWindowsConstants.N; j++) {
			if (j == get_i())
				getSettingsPanel().getOverlayCheckBox(j).setSelected(value);
			else
				getSettingsPanel().getOverlayCheckBox(j).setSelected(j==get_i());
		}
	}


	protected abstract void lockAction(JCheckBox checkBox);

	protected void setLocks(JComboBox<?>[] comboBox, boolean isLocked) {
		String selectedItem = (String) comboBox[0].getSelectedItem();

		for (int i = 1; i<SyncWindowsConstants.N; i++) {
			if (isLocked && !model.getPath(0).getDefaultFilename().contains("null") && !(model.getPath(0) instanceof MoldevEmptyPath)) {
				System.out.print("I'm changing something");
				String suggestedItem = selectedItem.replace(model.getPath(0).getDefaultFilename(), model.getPath(i).getDefaultFilename());
				comboBox[i].setSelectedItem("blablab");
				try {
					comboBox[i].setSelectedItem(suggestedItem);
				}
				catch (Exception e) {
					IJ.log("setLock failed, perhaps "+suggestedItem+" doesn't exist");
				}
			}
			comboBox[i].setEnabled(!isLocked);
		}
	}

	protected boolean isValid(String path) {
		File file = new File(path);
		return file.exists();
	}

}
