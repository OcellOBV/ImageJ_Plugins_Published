package syncwindows;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import ij.IJ;

public class SyncWindowsWindowController implements WindowListener{

	private SyncWindowsModelIF model;
	protected SyncWindowsViewIF view;
	private ActionListener menuListener;
	private ActionEvent autoSave = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,"autoSave");
	private String name;

	public SyncWindowsWindowController(SyncWindowsModelIF model, SyncWindowsViewIF view,ActionListener menuListener, String name) {
		this.model = model;
		this.view = view;
		this.menuListener = menuListener;
		this.name = name;

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowClosing(WindowEvent e) {
		menuListener.actionPerformed(autoSave);
		if (!name.equals("ImageJPlugIn")){
			try {
				view.setImageListenerEnabled(false);	
				String[] imagesTitles = ij.WindowManager.getImageTitles();
				String[] nonImagesTitles = ij.WindowManager.getNonImageTitles();

				if (imagesTitles != null) {
					for(String title :imagesTitles){
						IJ.selectWindow(title);
						IJ.run("Close");
					}
				}
				if (nonImagesTitles != null) {
					for(String title :nonImagesTitles){
						IJ.selectWindow(title);
						IJ.run("Close");
					}
				}
				IJ.run("Quit");
				view.setImageListenerEnabled(true);	
			}
			catch (Exception f) {
				IJ.log("Problem closing all");
			}
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
