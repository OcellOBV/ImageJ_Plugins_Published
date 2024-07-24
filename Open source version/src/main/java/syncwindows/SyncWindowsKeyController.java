package syncwindows;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import ij.IJ;
import ij.ImageJ;

public class SyncWindowsKeyController implements KeyListener {

	private SyncWindowsModelIF model;
	private SyncWindowsViewIF view;
	
	private final KeyListener imageJKeyListener;

	public SyncWindowsKeyController(SyncWindowsModelIF model, SyncWindowsViewIF view) {
		this.model = model;
		this.view = view;
		
		ImageJ imageJ = IJ.getInstance();
		imageJKeyListener = imageJ.getKeyListeners()[0];
	}

	@Override
	public void keyReleased(KeyEvent ev) {
		view.setZoom();
		imageJKeyListener.keyReleased(ev);

		switch(ev.getKeyCode()) {
		case KeyEvent.VK_A:
			view.allWindowsToFront();
			break;
		}

	}
	
	@Override
	public void keyPressed(KeyEvent ev) {
		imageJKeyListener.keyPressed(ev);
	}

	@Override
	public void keyTyped(KeyEvent ev) {
		view.setZoom();
		imageJKeyListener.keyTyped(ev);
	}

}
