package syncwindows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SyncWindowsOverlayController implements ActionListener {

	private SyncWindowsModelIF model;
	private SyncWindowsViewIF view;
	
	public SyncWindowsOverlayController(SyncWindowsModelIF model, SyncWindowsViewIF view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		int i = model.getOverlayWindow();
		view.setOverlayWindow(i);
	}
}