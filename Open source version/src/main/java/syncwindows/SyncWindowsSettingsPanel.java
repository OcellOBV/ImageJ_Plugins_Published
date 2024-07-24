package syncwindows;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class SyncWindowsSettingsPanel extends JPanel {
	
	protected JButton[] copyRootButton;
	protected JCheckBox[] dataOverlay;
	protected JTextField[] dataRoot;
	protected JComboBox<?>[] dataFilename;
	
    public static SyncWindowsSettingsPanel createSettingsPanel(String pathSystem) {
        switch (pathSystem) {
            case "moldev":
                return new SyncWindowsSettingsPanelMoldev();
            case "regular":
                return new SyncWindowsSettingsPanelRegular();
            default:
                throw new IllegalArgumentException("Invalid pathSystem: " + pathSystem);
        }
    }
    
    
	public JButton getCopyButton(int i) {
		return copyRootButton[i];
	}
	
	public JCheckBox getOverlayCheckBox(int i) {
		return dataOverlay[i];
	}
	
	public JTextField getRootTextField(int i) {
		return dataRoot[i];
	}
	
	public JComboBox<?>[] getFilenameComboBox() {
		return dataFilename;
	}
	
	public JComboBox<String> getFilenameComboBox(int i) {
		return (JComboBox<String>)dataFilename[i];
	}
}
