package syncwindows;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import imagepath.MoldevEmptyPath;
import imagepath.MoldevPathModel;
import imagepath.PathModel;

public class SyncWindowsSettingsViewMoldev extends SyncWindowsSettingsView {

	private SyncWindowsSettingsPanelMoldev settingsPanel;
	
	public SyncWindowsSettingsViewMoldev(String name) {
		super();
		createMenu(name);
		settingsPanel = new SyncWindowsSettingsPanelMoldev();
		add(settingsPanel, BorderLayout.NORTH);
		updateView();
	}
	
	
	public void registerActionListener(int i, ActionListener controller) {
		settingsPanel.getCopyButton(i).addActionListener(controller);
		settingsPanel.getRootTextField(i).addActionListener(controller);
		settingsPanel.getPlateComboBox(i).addActionListener(controller);
		settingsPanel.getWellRowComboBox(i).addActionListener(controller);
		settingsPanel.getWellColComboBox(i).addActionListener(controller);
		settingsPanel.getSiteComboBox(i).addActionListener(controller);
		settingsPanel.getChannelComboBox(i).addActionListener(controller);
		settingsPanel.getTimeComboBox(i).addActionListener(controller);
		settingsPanel.getFilenameComboBox(i).addActionListener(controller);
		settingsPanel.getOverlayCheckBox(i).addActionListener(controller);


		if (i == 0) {
			settingsPanel.getLockPlateCheckBox().addActionListener(controller);
			settingsPanel.getLockWellRowCheckBox().addActionListener(controller);
			settingsPanel.getLockWellColCheckBox().addActionListener(controller);
			settingsPanel.getLockChannelCheckBox().addActionListener(controller);
			settingsPanel.getLockSiteCheckBox().addActionListener(controller);
			settingsPanel.getLockTimeCheckBox().addActionListener(controller);
			settingsPanel.getLockFilenameCheckBox().addActionListener(controller);
		}
	}

	public void registerOverlayListener(int i, ActionListener controller) {
		settingsPanel.getOverlayCheckBox(i).addActionListener(controller);
	}
	
	@Override
	public SyncWindowsSettingsPanelMoldev getSettingsPanelMoldev() {
		return settingsPanel;
	}
	
	public void setPath(int i, PathModel path) {	
		if (path instanceof MoldevEmptyPath) {
			settingsPanel.getWellRowComboBox(i).removeAllItems();;
			settingsPanel.getWellColComboBox(i).removeAllItems();
			settingsPanel.getSiteComboBox(i).removeAllItems();
			settingsPanel.getChannelComboBox(i).removeAllItems();
			settingsPanel.getTimeComboBox(i).removeAllItems();
		}
		MoldevPathModel moldevPath = (MoldevPathModel) path;
		String root = moldevPath.getRoot();
		String plate = moldevPath.getPlate();
		String well_row = moldevPath.getWellRow();
		String well_col = moldevPath.getWellCol();
		String site = moldevPath.getSite();
		String channel = moldevPath.getChannel();
		String time = moldevPath.getTime();
		String filename = moldevPath.getFilename();

		setPathControllerEnabled(false);
		settingsPanel.getRootTextField(i).setText(root);
		setComboBoxModelWithMemory(settingsPanel.getPlateComboBox(i), moldevPath.getPlates(), moldevPath.getPlate());
		settingsPanel.getPlateComboBox(i).setSelectedItem(plate);
		settingsPanel.getWellRowComboBox(i).setSelectedItem(well_row);
		settingsPanel.getWellColComboBox(i).setSelectedItem(well_col);
		settingsPanel.getSiteComboBox(i).setSelectedItem(site);
		settingsPanel.getChannelComboBox(i).setSelectedItem(channel);
		settingsPanel.getTimeComboBox(i).setSelectedItem(time);
		setPathControllerEnabled(true);
		settingsPanel.getFilenameComboBox(i).setEditable(true);
		settingsPanel.getFilenameComboBox(i).setSelectedItem(filename);
		settingsPanel.getFilenameComboBox(i).setEditable(false);
	}
	
	public void lockAll() {
		settingsPanel.getLockPlateCheckBox().doClick();
		settingsPanel.getLockSiteCheckBox().doClick();
		settingsPanel.getLockWellColCheckBox().doClick();
		settingsPanel.getLockWellRowCheckBox().doClick();
		settingsPanel.getLockTimeCheckBox().doClick();
	}

}
