package syncwindows;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import ij.IJ;
import imagepath.RegularEmptyPath;
import imagepath.RegularPathModel;

public class SyncWindowsSettingsControllerRegular extends SyncWindowsSettingsController {
	
	protected SyncWindowsSettingsViewRegular view;

	public SyncWindowsSettingsControllerRegular(SyncWindowsModelIF model, SyncWindowsSettingsViewRegular view,
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
		if (event.getSource() instanceof JComboBox<?> && !(model.getRegularPath(get_i()) instanceof RegularEmptyPath) ) {
			JComboBox<String> comboBox = (JComboBox<String>) event.getSource();
			String name = comboBox.getName();
			firstImageEmpty=view.getSettingsPanelRegular().getFilenameComboBox(0).getItemCount()==0;
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.FILENAME_NAME)) filenameAction(comboBox);
			view.pack();
		}
		if (event.getSource() instanceof JCheckBox) {
			JCheckBox checkBox = (JCheckBox) event.getSource();
			String name = checkBox.getName();
			if (name.startsWith(SyncWindowsConstants.OVERLAY_NAME)) overlayAction(checkBox);
		}
		if (event.getSource() instanceof JButton) {
			JButton button = (JButton) event.getSource();
			String name = button.getName();
			if (!ignoreUpdate && name.startsWith(SyncWindowsConstants.COPY_NAME)) copyAction(button);
		}
		
	}

	@Override
	protected void rootAction(JTextField textField) {
		String path = textField.getText();
		System.out.println( isValid(path));
		view.setValid(textField, isValid(path));
		if (!isValid(path)) {
			System.out.println("Path is invalid");
			IJ.log("This path is invalid. Please fill in the input or outputfolder of the experiment.");
			return;
		}
		RegularPathModel RegularPath = new RegularPathModel(path,model.getSortingMethod());
		view.setValid(textField, RegularPath != null);
		if (RegularPath == null) return;
		model.setPath(get_i(), RegularPath);
		view.setComboBoxModelWithMemory(view.getSettingsPanelRegular().getFilenameComboBox(get_i()), model.getRegularPath(get_i()).getFilenames(), model.getRegularPath(get_i()).getFilename());

		
	}
	
	@Override
	protected void filenameAction(JComboBox<String> comboBox) {
		String filename = (String) comboBox.getSelectedItem();

		if  (view.isSettingsControllerEnabled()) {
			model.getRegularPath(get_i()).setFilename(filename);
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

	@Override
	protected SyncWindowsSettingsPanel getSettingsPanel() {
		return view.getSettingsPanelRegular();
	}

	@Override
	protected void lockAction(JCheckBox checkBox) {
		//no Lock
	}



}
