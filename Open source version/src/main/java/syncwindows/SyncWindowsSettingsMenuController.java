package syncwindows;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileOutputStream;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ij.IJ;
import ij.plugin.frame.RoiManager;
import helpers.ExtendedProperties;
import imagepath.MoldevEmptyPath;
import imagepath.MoldevOutputPathModel;
import imagepath.PathModel.sortingMethodNames;
import imagepath.RegularEmptyPath;
import utility.FileUtil;
public abstract class SyncWindowsSettingsMenuController extends KeyAdapter implements ActionListener {

	public enum Status {
		DONE,
		SAVING,
		LOADING
	}

	protected SyncWindowsModelIF model;
	protected SyncWindowsSettingsView view;
	protected Status status = Status.DONE;

	public static SyncWindowsSettingsMenuController CreateSettingsMenuController(SyncWindowsModelIF model, SyncWindowsSettingsView view, String pathSystem) {
		switch (pathSystem) {
		case "moldev":
			return new SyncWindowsSettingsMenuControllerMoldev(model, view);
		case "regular":
			return new SyncWindowsSettingsMenuControllerRegular(model, view);
		default:
			throw new IllegalArgumentException("Invalid pathSystem: " + pathSystem);
		}
	}

	protected abstract ExtendedProperties getPropertiesFromModel();

	protected abstract void setModelFromProperties(Properties props);

	@Override
	public void actionPerformed(ActionEvent event) {
		String command = event.getActionCommand();
		System.out.println(command);
		if (command.equals("Exit")) {
			//			view.dispose();
			//			System.exit(0);
		} else if (command.equals("Import")) {
			importModel();
		} else if (command.equals("Export")) {
			exportModel();
		} else if (command.equals("Restore")) {
			restoreModel();
		} else if (command.equals("autoSave")) {
			autosaveModel();
		} else if (command.equals("QuickSave")) {
			autosaveModel();
		} else if (command.equals("Help")) {
				showHelp();
		} else if (command.equals("AZ")) {
			changeSortingMethod(sortingMethodNames.AZ);
		} else if (command.equals("ZA")) {
			changeSortingMethod(sortingMethodNames.ZA);
		} else if (command.equals("Last Modified")) {
			changeSortingMethod(sortingMethodNames.LastModified);
		}
	}

	@Override
	public void keyTyped(KeyEvent event) {
		char c = event.getKeyChar();
		if (c == 'e' || c == 'E') {
			exportModel();
		} else if (c == 'i' || c == 'I') {
			importModel();
		} else if (c == 'r' || c == 'R') {
			restoreModel();
		}
		else if (c == 'q' || c == 'Q') {
			autosaveModel();
		}
	}

	private void changeSortingMethod(sortingMethodNames value) {
		model.setSortingMethod(value);
		view.setSettingsControllerEnabled(false);
		for (int i = 0; i < SyncWindowsConstants.N; i ++) {
			view.setPath(i, model.getPath(i));
		}
		view.setSettingsControllerEnabled(true);
	}


	private void exportModel() {
		if (status != Status.DONE) return;
		status = Status.SAVING;
		ExtendedProperties props = getPropertiesFromModel();
		JFileChooser fileChooser = new JFileChooser(getSuggestedDir());
		fileChooser.setDialogTitle("Export properties file");
		int state = fileChooser.showSaveDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			File selectedDirectory = fileChooser.getSelectedFile();
			if (!selectedDirectory.exists()) {
				selectedDirectory.mkdir();
			}
			props.write(selectedDirectory.getAbsolutePath() + "\\properties.txt" );
			saveRois(selectedDirectory.getAbsolutePath() + "\\rois.zip");
		}
		status = Status.DONE;
	}
	
	private void showHelp() {
		String imagePath = "/Crown-Bioscience-Logo-TEAL.png";
		URL imageUrl = getClass().getResource(imagePath);

		ImageIcon imageIcon = null;
		if (imageUrl != null) {
			imageIcon = new ImageIcon(imageUrl);
		} else {
			System.err.println("Image not found at " + imagePath);
			return;
		}

		JFrame helpFrame = new JFrame("Help");

		int imageWidth = imageIcon.getIconWidth();
		int imageHeight = imageIcon.getIconHeight();

		helpFrame.setSize(imageWidth, imageHeight + 325);

		StringBuilder helpContent = new StringBuilder();
		helpContent.append("Developed by CrownBioScience Netherlands\n\n")
		.append("AUTHORS:\n\n")
		.append("Ashgard Weterings\n")
		.append("Lois Van der Drift\n")
		.append("Loïc Capdeville\n")
		.append("\n\nDESCRIPTION:\n\n")
		.append("- Open multiple synchronized images\n")
		.append("- Switch between images\n")
		.append("- Use overlays to compare images\n")
		.append("- Share your point of view using the import/export feature\n\n")
		.append("Version : 1.0\n");


		JTextArea helpText = new JTextArea();
		helpText.setText(helpContent.toString());
		helpText.setEditable(false);

		JPanel helpPanel = new JPanel();
		helpPanel.setLayout(new BorderLayout());

		JLabel imageLabel = new JLabel(imageIcon);
		helpPanel.add(imageLabel, BorderLayout.NORTH);
		helpPanel.add(new JScrollPane(helpText), BorderLayout.CENTER);

		helpFrame.add(helpPanel);
		helpFrame.setVisible(true);
	}



	

	private void autosaveModel() {
		if (status != Status.DONE) return;
		int numberOfEmptyPath = 0;
		for (int i = 0; i < SyncWindowsConstants.N; i ++) {
			if (model.getPath(i) instanceof MoldevEmptyPath || model.getPath(i) instanceof RegularEmptyPath) {
				numberOfEmptyPath++;
			}
		}
		if (numberOfEmptyPath == SyncWindowsConstants.N) {
			return;
		}
		status = Status.SAVING;
		
	    String autoSavingPath = getAutoSavingPath();

	    File folder = new File(autoSavingPath);
	    if (!folder.exists()) {
	        boolean successCreatingFolder = folder.mkdirs();
	        if (successCreatingFolder) {
	            System.out.println("SyncWindows folder created in user directory");
	        } else {
	            System.out.println("Issue creating SyncWindows folder");
	        }
	    }

	    System.out.println(getRestoreSettingsFile()[1]);
	    saveRois(getRestoreSettingsFile()[1]);

	    ExtendedProperties props = getPropertiesFromModel();
	    props.write(getRestoreSettingsFile()[0]);

	    status = Status.DONE;
	}


	private void restoreModel() {
		if (status != Status.DONE) return;
		status = Status.LOADING;
		try {
			Properties props = getPropertiesFromFile(getRestoreSettingsFile()[0]);
			setModelFromProperties(props);
			openRois(getRestoreSettingsFile()[1]);
		}
		catch (Exception e){
			IJ.log("No backup");
		}
		status = Status.DONE;
	}

	private void importModel() {
		if (status != Status.DONE) return;
		status = Status.LOADING;
		JFileChooser fileChooser = new JFileChooser(getSuggestedDir());
		fileChooser.setDialogTitle("Import properties file");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int state = fileChooser.showOpenDialog(null);
		if (state == JFileChooser.APPROVE_OPTION) {
			try {
				File selectedDirectory = fileChooser.getSelectedFile();
				String filePath = selectedDirectory.getAbsolutePath();
				Properties props = getPropertiesFromFile(filePath + "\\properties.txt");
				if(props!=null) {
					setModelFromProperties(props);
				}
				openRois(filePath + "\\rois.zip");
			} catch (Exception e) {
				IJ.log("Please select a correct folder");
			}
		}
		status = Status.DONE;
	}

	protected void saveRois(String selectedDirectory) {
		RoiManager roi = RoiManager.getInstance();
		int nb = 0;
		try {
			if (roi != null) {
				nb = roi.getCount();
				System.out.println(nb);
				if (nb > 0){
					roi.runCommand("Show all");
					roi.runCommand("save", selectedDirectory);
				}
			}
			if (roi == null || nb == 0) {
				createEmptyZipFile(selectedDirectory);
			}
		} catch (Exception e) {
			System.out.println("No ROI");
		}
	}

	protected void openRois (String filePath) {
		RoiManager roiManager = RoiManager.getInstance();
		if (roiManager == null) {
			roiManager = new RoiManager();
		}
		else {
			roiManager.reset();
		}
		try {
			File zipFile = new File(filePath);
			if (zipFile.exists()) {
				ZipFile zf = new ZipFile(zipFile);
				if (zf.entries().hasMoreElements()) {
					roiManager.runCommand("open", filePath);
					roiManager.runCommand("Show All");
				}
			} 
		}	catch (Exception e) {
			System.out.println("Error opening Rois");
		}
	}

	protected String getSuggestedDir() {
		for (int i = 0; i<SyncWindowsConstants.N; i++) {
			if (!(model.getPath(i) instanceof RegularEmptyPath || model.getPath(i) instanceof MoldevEmptyPath || model.getPath(i) instanceof MoldevOutputPathModel)){
				return model.getPath(i).getRoot();
			}
		}
		try {
			Properties props = getPropertiesFromFile(getRestoreSettingsFile()[0]);
			for (int i = 0; i<SyncWindowsConstants.N; i++) {
				if (props.getProperty("root_" + i) != null){
					return props.getProperty("root_" + i);
				}
			}
		}
		catch(Exception e) {
			IJ.log("getSuggestedDir exception: unable get properties from file");
		}
		IJ.log("getSuggestedDir exception: unable to retrieve moldevPath().root");
		return "";
	}

	private Properties getPropertiesFromFile(String filename) {
		Properties props = new Properties();

		File file = new File(filename);
		if (!file.exists()) return props;

		try {
			props.load(new FileInputStream(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}      
		return props;
	}


	protected Rectangle getRectangleFromString(String str) throws Exception {
		int x = getRectangleProperty(str, "x");
		int y = getRectangleProperty(str, "y");
		int width = getRectangleProperty(str, "width");
		int height = getRectangleProperty(str, "height");
		return new Rectangle(x, y, width, height);

	}

	private int getRectangleProperty(String str, String property) throws Exception {
		Pattern pattern = Pattern.compile(property + "=(\\d+)");
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}
		throw new Exception("Invalid Rectangle String format: " + str);
	}

	protected File getSuggestedFile() {
		return new File("syncwin_" + FileUtil.getDateAndTimeAsString() + ".txt");
	}

	protected String[] getRestoreSettingsFile() {
		String[] values = new String [2];
		values[0] = getAutoSavingPath() + "syncwin_autosave.txt";
		values[1] = getAutoSavingPath() + "rois_autosave.zip";
		return values;
	}
	
	protected String getAutoSavingPath() {
	    String userHome = System.getProperty("user.home");
	    return userHome + File.separator + ".imagej" + File.separator + "SyncWindows" + File.separator;
	}

	private static void createEmptyZipFile(String filePath) {
		try {
			File zipFile = new File(filePath);
			if (zipFile.exists()) {
				zipFile.delete();
			}
			ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOutputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
