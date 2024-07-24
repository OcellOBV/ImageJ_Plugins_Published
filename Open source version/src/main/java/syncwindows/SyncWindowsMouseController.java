package syncwindows;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import ij.gui.ImageCanvas;

public class SyncWindowsMouseController implements MouseListener {

	private SyncWindowsModelIF model; // TODO
	private SyncWindowsViewIF view;

	public SyncWindowsMouseController(SyncWindowsModelIF model, SyncWindowsViewIF view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		ImageCanvas canvas = (ImageCanvas) e.getSource();
		view.removeCursor(canvas);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		ImageCanvas canvas = (ImageCanvas) e.getSource();
		Rectangle currentSrcRect = view.getCurrentSrcRect(canvas);
		view.setZoom();
		model.setCurrentSrcRect(currentSrcRect);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

}
