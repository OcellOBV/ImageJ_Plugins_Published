package syncwindows;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class SyncWindowsMouseMotionController implements MouseMotionListener {

	private SyncWindowsModelIF model;
	private SyncWindowsViewIF view;

	public SyncWindowsMouseMotionController(SyncWindowsModelIF model, SyncWindowsViewIF view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// panning
		view.setSrcRect();
		view.setCursorOnImps();
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		view.setCursorOnImps();
	}

}