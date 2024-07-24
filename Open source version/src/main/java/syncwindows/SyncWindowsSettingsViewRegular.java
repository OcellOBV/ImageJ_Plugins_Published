package syncwindows;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import imagepath.PathModel;
import imagepath.RegularPathModel;

public class SyncWindowsSettingsViewRegular extends SyncWindowsSettingsView {

	private SyncWindowsSettingsPanelRegular settingsPanel;
	
	public SyncWindowsSettingsViewRegular(String name) {
		super();
		createMenu(name);
		settingsPanel = new SyncWindowsSettingsPanelRegular();
		add(settingsPanel, BorderLayout.NORTH);
		updateView();
	}

	@Override
	public SyncWindowsSettingsPanelRegular getSettingsPanelRegular(){
		return settingsPanel;
	}

	
	@Override
	public void registerActionListener(int i, ActionListener controller) {
		settingsPanel.getCopyButton(i).addActionListener(controller);
		settingsPanel.getRootTextField(i).addActionListener(controller);
		settingsPanel.getFilenameComboBox(i).addActionListener(controller);
		settingsPanel.getOverlayCheckBox(i).addActionListener(controller);
	}
	
	public void registerOverlayListener(int i, ActionListener controller) {
		settingsPanel.getOverlayCheckBox(i).addActionListener(controller);
	}

	@Override
	public void setPath(int i, PathModel path) {
		RegularPathModel regularPath = (RegularPathModel) path;
		String root = regularPath.getRoot();
		String filename = regularPath.getFilename();

		setPathControllerEnabled(false);
		settingsPanel.getRootTextField(i).setText(root);
		setPathControllerEnabled(true);
		settingsPanel.getFilenameComboBox(i).setEditable(true);
		setComboBoxModelWithMemory(settingsPanel.getFilenameComboBox(i), regularPath.getFilenames(), filename);
		settingsPanel.getFilenameComboBox(i).setEditable(false);
	}


	@Override
	public SyncWindowsSettingsPanelMoldev getSettingsPanelMoldev() {
		return null;
	}

	@Override
	public void lockAll() {
		//no lock
	}

}
