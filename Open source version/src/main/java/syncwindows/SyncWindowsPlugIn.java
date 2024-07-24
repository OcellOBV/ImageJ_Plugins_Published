package syncwindows;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;

import ij.IJ;
import ij.ImageJ;
import ij.ImageListener;
import ij.plugin.frame.PlugInFrame;

public class SyncWindowsPlugIn extends PlugInFrame  {
	
	public SyncWindowsPlugIn(String name, String pathSystem) {
		super(name);
		initialize(name, pathSystem);
	}
	
	// TODO when close a window, remove it from the list, now it throws an error.
	
	protected void initialize(String name, String pathSystem) {
		SyncWindowsModelIF model = new SyncWindowsModel(pathSystem);
		
		SyncWindowsViewIF windowsView = new SyncWindowsView(model);
		
		KeyListener keyController = new SyncWindowsKeyController(model, windowsView);
		MouseListener mouseController = new SyncWindowsMouseController(model, windowsView);
		MouseMotionListener motionController = new SyncWindowsMouseMotionController(model, windowsView);
		ActionListener overlayController = new SyncWindowsOverlayController(model, windowsView);


		
		windowsView.registerKeyListener(keyController);

		registerWindowListener();
		
		SyncWindowsSettingsView settingsView = SyncWindowsSettingsView.CreateSettingsView(name,pathSystem);
		
		ImageListener imageController = new SyncWindowsImageController(model, windowsView, settingsView);
		ActionListener menuController = SyncWindowsSettingsMenuController.CreateSettingsMenuController(model, settingsView, pathSystem);
		WindowListener windowController = new SyncWindowsWindowController(model, windowsView,menuController,name);

		
		settingsView.registerMenuListener(menuController,name);
		settingsView.registerKeyListener(keyController);
		settingsView.registerWindowListener(windowController);
		
		for (int i=0; i<SyncWindowsConstants.N; i++) {
			SyncWindowsPathController pathController = new SyncWindowsPathController(model, windowsView, i);
			pathController.setKeyController(keyController);
			pathController.setMouseController(mouseController);
			pathController.setMotionController(motionController);		
			
			ActionListener settingsController = SyncWindowsSettingsController.CreateSettingsController(model, settingsView, pathController, pathSystem);
			
			// TODO this registers the non loops (overlay) 3x, see impl, how to fix that?
			settingsView.registerOverlayListener(i, overlayController);
			settingsView.registerActionListener(i, settingsController);// TODO this one has to be done last!
		}
		windowsView.registerImageListener(imageController);
		settingsView.lockAll();
		
	}

	public void registerWindowListener() {
		ImageJ imageJ = IJ.getInstance();
		imageJ.addWindowListener(this);
	}

	// TODO how does this work?
	public static void main(String[] args) throws Exception {
		// set the plugins.dir property to make the plugin appear in the Plugins menu
		// see: https://stackoverflow.com/a/7060464/1207769
		Class<?> clazz = SyncWindowsPlugIn.class;
		java.net.URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
		java.io.File file = new java.io.File(url.toURI());
		System.setProperty("plugins.dir", file.getAbsolutePath());

		new ImageJ();
		SyncWindowsPlugIn sw = new SyncWindowsPlugIn("ImageJPlugIn","moldev");
	}
}
