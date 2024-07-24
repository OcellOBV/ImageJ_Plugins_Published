package syncwindows;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import imagepath.PathModel;


public abstract class SyncWindowsSettingsView extends JFrame {


	protected JMenu fileMenu;
	protected JMenu sortingMenu;
	protected JMenu helpMenu;

	protected boolean pathControllerEnabled = true;
	protected boolean settingsControllerEnabled = true;

	protected final Border errorBorder = new LineBorder(Color.RED, 2);
	protected ActionListener menuListener;

	private ActionEvent autoSave = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"autoSave");

	public SyncWindowsSettingsView() {
		super("Settings");
	}

	public static SyncWindowsSettingsView CreateSettingsView(String name, String pathSystem) {
		switch (pathSystem) {
		case "moldev":
			return new SyncWindowsSettingsViewMoldev(name);
		case "regular":
			return new SyncWindowsSettingsViewRegular(name);
		default:
			throw new IllegalArgumentException("Invalid pathSystem: " + pathSystem);
		}

	}

	public abstract void setPath(int i, PathModel moldevPath);

	public abstract void lockAll();

	public abstract void registerActionListener(int i, ActionListener controller);

	public abstract void registerOverlayListener(int i, ActionListener controller);

	public SyncWindowsSettingsPanelMoldev getSettingsPanelMoldev() {
		return null;
	}

	public SyncWindowsSettingsPanelRegular getSettingsPanelRegular() {
		return null;
	}

	public void triggerWindowControllerManually() {
		getMenuListener().actionPerformed(autoSave);
		WindowEvent e = new WindowEvent(this,1);
		for (int k = 0; k < this.getWindowListeners().length; k++) {
			this.getWindowListeners()[k].windowClosing(e);
		}
	}

	public SyncWindowsSettingsPanel getSettingsPanel() {
		SyncWindowsSettingsPanel settingsPanel = getSettingsPanelMoldev();
		if (settingsPanel != null) {
			return settingsPanel;
		}
		else {
			return getSettingsPanelRegular();
		}
	}


	protected void createMenu(String name) {

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);

		JMenuItem restoreItem = new JMenuItem("Restore");
		restoreItem.setMnemonic('R');
		fileMenu.add(restoreItem);

		JMenuItem importItem = new JMenuItem("Import");
		importItem.setMnemonic('I');
		fileMenu.add(importItem);

		JMenuItem exportItem = new JMenuItem("Export");
		exportItem.setMnemonic('E');
		fileMenu.add(exportItem);

		JMenuItem quickSave = new JMenuItem("QuickSave");
		quickSave.setMnemonic('Q');
		fileMenu.add(quickSave);

		sortingMenu = new JMenu("Sorting");

		JRadioButtonMenuItem  alphabetical = new JRadioButtonMenuItem ("AZ");

		JRadioButtonMenuItem unalphabetical = new JRadioButtonMenuItem ("ZA");

		JRadioButtonMenuItem lastModified = new JRadioButtonMenuItem ("Last Modified");

		lastModified.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(alphabetical);
		group.add(unalphabetical);
		group.add(lastModified);

		sortingMenu.add(alphabetical);
		sortingMenu.add(unalphabetical);
		sortingMenu.add(lastModified);

		menuBar.add(sortingMenu);


		helpMenu = new JMenu("Help");
		JMenuItem  help = new JMenuItem("Help");
		helpMenu.add(help);
		menuBar.add(helpMenu);

	}

	public void updateView() {
		setVisible(true);
		setLocation(0, 0);
		pack();
	}

	public boolean isPathControllerEnabled() {
		return pathControllerEnabled;
	}

	public boolean isSettingsControllerEnabled() {
		return settingsControllerEnabled;
	}


	public void setPathControllerEnabled(boolean enabled) {
		pathControllerEnabled = enabled;
	}

	public void setSettingsControllerEnabled(boolean enabled) {
		settingsControllerEnabled = enabled;
	}

	public void registerMenuListener(ActionListener controller, String name) {
		Component[] fileMenuComponents = fileMenu.getMenuComponents();
		Component[] sortingMenuComponents = sortingMenu.getMenuComponents();
		Component helpMenuComponent = helpMenu.getMenuComponent(0);

		for (Component component : fileMenuComponents) {
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller);
			}
		}
		for (Component component : sortingMenuComponents) {
			if (component instanceof AbstractButton) {
				AbstractButton button = (AbstractButton) component;
				button.addActionListener(controller);
			}
		}

		AbstractButton button = (AbstractButton) helpMenuComponent;
		button.addActionListener(controller);

		this.menuListener = controller;
	}

	public ActionListener getMenuListener() {
		return this.menuListener;
	}

	public void registerKeyListener(KeyListener controller) {
		addKeyListener(controller);
	}

	public void registerWindowListener(WindowListener controller) {
		addWindowListener(controller);
	}

	public void setComboBoxModelWithMemory(JComboBox<String> comboBox, String[] values, String value) {
		comboBox.setModel(new DefaultComboBoxModel<>(values));
		comboBox.setSelectedItem(value);
	}

	public void setValid(JTextField textField, boolean valid) {
		if (valid) {
			System.out.println("yes valid");
			textField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
		}
		else {
			textField.setBorder(errorBorder);
		}
	}



	public static void main(String[] args) throws Exception {
		SyncWindowsModelIF model = new SyncWindowsModel("moldev");
		SyncWindowsSettingsView view = CreateSettingsView("ImageJPlugIn","moldev");
	}


}
