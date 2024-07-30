package syncwindows;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import ij.IJ;
import imagepath.MoldevEmptyPath;
import imagepath.MoldevPathModel;
import imagepath.MoldevPathModelFactory;

public class SyncWindowsSettingsControllerMoldev extends SyncWindowsSettingsController {
	
	protected SyncWindowsSettingsViewMoldev view;
	
	public SyncWindowsSettingsControllerMoldev(SyncWindowsModelIF model, SyncWindowsSettingsViewMoldev view,
			SyncWindowsPathController pathController) {
		this.model = model;
		this.view = view;
		// TODO niet heel mooi hoe ik pathcontroller heb toegevoegd. dat moet beter kunnen. maar hoe? 
		this.pathController = pathController;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof JTextField) {
			JTextField textField = (JTextField) event.getSource();
			String name = textField.getName();
			if (name.startsWith(SyncWindowsConstants.ROOT_NAME)) rootAction(textField);
		}
		if (event.getSource() instanceof JComboBox<?> && !(model.getMoldevPath(get_i()) instanceof MoldevEmptyPath) ) {
			JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
			String name = comboBox.getName();
			firstImageEmpty=view.getSettingsPanelMoldev().getFilenameComboBox(0).getItemCount()==0;
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.PLATE_NAME)) plateAction(comboBox);
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.WELL_ROW_NAME)) wellRowAction(comboBox);
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.WELL_COL_NAME)) wellColAction(comboBox);
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.SITE_NAME)) siteAction(comboBox);
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.CHANNEL_NAME)) channelAction(comboBox);
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.TIME_NAME)) timeAction(comboBox);
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.FILENAME_NAME)) filenameAction(comboBox);
			view.pack();
		}
		if (event.getSource() instanceof JCheckBox) {
			JCheckBox checkBox = (JCheckBox) event.getSource();
			String name = checkBox.getName();
			if (name.startsWith(SyncWindowsConstants.OVERLAY_NAME)) overlayAction(checkBox);
			if (name.startsWith(SyncWindowsConstants.LOCK_NAME)) lockAction(checkBox);
		}
		if (event.getSource() instanceof JButton) {
			JButton button = (JButton) event.getSource();
			String name = button.getName();
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.COPY_NAME)) copyAction(button);
		}
	}
	
	protected void rootAction(JTextField textField) {
		String path = textField.getText();
		System.out.println( isValid(path));
		view.setValid(textField, isValid(path));
		if (!isValid(path)) {
			System.out.println("Path is invalid");
			IJ.log("This path is invalid. Please fill in the input or outputfolder of the experiment.");
			return;
		}
		MoldevPathModel moldevPath = MoldevPathModelFactory.createMoldevPath(path,model.getSortingMethod());
		view.setValid(textField, moldevPath != null);
		if (moldevPath == null) return;
		model.setPath(get_i(), moldevPath);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getPlateComboBox(get_i()), model.getMoldevPath(get_i()).getPlates(), model.getMoldevPath(get_i()).getPlate());
	}
	
	protected void plateAction(JComboBox<String> comboBox) {
		boolean isLocked = view.getSettingsPanelMoldev().getLockPlateCheckBox().isSelected();
		String plate;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			plate = (String) view.getSettingsPanelMoldev().getPlateComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getPlateComboBox(get_i()).setSelectedItem(plate);
			ignoreUpdate = false;
		}else {
			plate = (String) comboBox.getSelectedItem();
		}
		model.getMoldevPath(get_i()).setPlate(plate);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getWellRowComboBox(get_i()), model.getMoldevPath(get_i()).getWellRows(),  model.getMoldevPath(get_i()).getWellRow());
	}

	protected void wellRowAction(JComboBox<String> comboBox) {
		boolean isLocked = view.getSettingsPanelMoldev().getLockWellRowCheckBox().isSelected();
		String well_row;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			well_row = (String)view.getSettingsPanelMoldev().getWellRowComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getWellRowComboBox(get_i()).setSelectedItem(well_row);
			ignoreUpdate = false;
		}else {
			well_row = (String) comboBox.getSelectedItem();
		}
		model.getMoldevPath(get_i()).setRow(well_row);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getWellColComboBox(get_i()), model.getMoldevPath(get_i()).getWellCols(), model.getMoldevPath(get_i()).getWellCol());
	}

	protected void wellColAction(JComboBox<String> comboBox) {
		boolean isLocked= view.getSettingsPanelMoldev().getLockWellColCheckBox().isSelected();
		String well_col;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			well_col = (String) view.getSettingsPanelMoldev().getWellColComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getWellColComboBox(get_i()).setSelectedItem(well_col);
			ignoreUpdate = false;
		}else {
			well_col = (String) comboBox.getSelectedItem();
		}
		model.getMoldevPath(get_i()).setCol(well_col);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getSiteComboBox(get_i()), model.getMoldevPath(get_i()).getSites(), model.getMoldevPath(get_i()).getSite());
	}


	protected void siteAction(JComboBox<String> comboBox) {
		boolean isLocked = view.getSettingsPanelMoldev().getLockSiteCheckBox().isSelected();
		String site;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			site = (String)view.getSettingsPanelMoldev().getSiteComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getSiteComboBox(get_i()).setSelectedItem(site);
			ignoreUpdate = false;
		}else {
			site = (String) comboBox.getSelectedItem();
		}
		model.getMoldevPath(get_i()).setSite(site);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getChannelComboBox(get_i()), model.getMoldevPath(get_i()).getChannels(), model.getMoldevPath(get_i()).getChannel());
	}

	protected void channelAction(JComboBox<String> comboBox) {
		boolean isLocked = view.getSettingsPanelMoldev().getLockChannelCheckBox().isSelected();
		String channel;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			channel = (String)view.getSettingsPanelMoldev().getChannelComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getChannelComboBox(get_i()).setSelectedItem(channel);
			ignoreUpdate = false;
		}else {
			channel = (String) comboBox.getSelectedItem();
		}

		model.getMoldevPath(get_i()).setChannel(channel);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getTimeComboBox(get_i()), model.getMoldevPath(get_i()).getTimes(), model.getMoldevPath(get_i()).getTime());
	}
	
	
	protected void timeAction(JComboBox<String> comboBox) {
		boolean isLocked = view.getSettingsPanelMoldev().getLockTimeCheckBox().isSelected();
		String time;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			time = (String)view.getSettingsPanelMoldev().getTimeComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getTimeComboBox(get_i()).setSelectedItem(time);
			ignoreUpdate = false;
		}else {
			time = (String) comboBox.getSelectedItem();
		}

		model.getMoldevPath(get_i()).setTime(time);
		view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getFilenameComboBox(get_i()), model.getMoldevPath(get_i()).getFilenames(), model.getMoldevPath(get_i()).getFilename());
	}

	protected void filenameAction(JComboBox<String> comboBox) {
		boolean isLocked = view.getSettingsPanelMoldev().getLockFilenameCheckBox().isSelected();
		String filename;
		if(isLocked && get_i()!=0 && !firstImageEmpty) {
			ignoreUpdate = true;
			filename = (String)view.getSettingsPanelMoldev().getFilenameComboBox(0).getSelectedItem();
			view.getSettingsPanelMoldev().getFilenameComboBox(get_i()).setSelectedItem(filename);
			ignoreUpdate = false;
		}else {
			filename = (String) comboBox.getSelectedItem();
		}
		
		if  (view.isSettingsControllerEnabled()) {
			model.getMoldevPath(get_i()).setFilename(filename);
			if (get_i() == 0) {
				for (int i = 1; i<SyncWindowsConstants.N; i++) {
					if (!(model.getMoldevPath(i) instanceof MoldevEmptyPath)){ 
						view.setComboBoxModelWithMemory(view.getSettingsPanelMoldev().getPlateComboBox(i), model.getMoldevPath(i).getPlates(), model.getMoldevPath(i).getPlate());
					}
				}
			}
		}



		if (view.isPathControllerEnabled())
		{
			if (updatePathThread != null && updatePathThread.isAlive()) {
				System.out.println("KILL i="+get_i());
				updatePathThread.interrupt();
				updatePathThread=null;
			}
			updatePathThread = new Thread(pathController);
			updatePathThread.start();
		}
	}
	

	protected void lockAction(JCheckBox checkBox) {
		boolean isLocked = checkBox.isSelected();
		String name = checkBox.getName().replace(SyncWindowsConstants.LOCK_NAME, "");
		switch (name) {
		case SyncWindowsConstants.PLATE_NAME:
			setLocks(view.getSettingsPanelMoldev().getPlateComboBox(), isLocked);
			break;
		case SyncWindowsConstants.WELL_ROW_NAME:
			setLocks(view.getSettingsPanelMoldev().getWellRowComboBox(), isLocked);
			break;
		case SyncWindowsConstants.WELL_COL_NAME:
			setLocks(view.getSettingsPanelMoldev().getWellColComboBox(), isLocked);
			break;
		case SyncWindowsConstants.SITE_NAME:
			setLocks(view.getSettingsPanelMoldev().getSiteComboBox(), isLocked);
			break;
		case SyncWindowsConstants.CHANNEL_NAME:
			setLocks(view.getSettingsPanelMoldev().getChannelComboBox(), isLocked);
			break;
		case SyncWindowsConstants.TIME_NAME:
			setLocks(view.getSettingsPanelMoldev().getTimeComboBox(), isLocked);
			break;
		case SyncWindowsConstants.FILENAME_NAME:
			setLocks(view.getSettingsPanelMoldev().getFilenameComboBox(), isLocked);
			break;
		}
	}

	@Override
	protected SyncWindowsSettingsPanel getSettingsPanel() {
		return view.getSettingsPanelMoldev();
	}
	
}
