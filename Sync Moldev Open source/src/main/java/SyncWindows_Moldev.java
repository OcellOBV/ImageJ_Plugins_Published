import ij.plugin.frame.PlugInFrame;
import syncwindows.SyncWindowsPlugIn;
public class SyncWindows_Moldev extends PlugInFrame  {
	public SyncWindows_Moldev() {
		super("SyncWindows_Moldev");
	}
	@Override
	public void run(String arg) {
		new SyncWindowsPlugIn("ImageJPlugIn","regular");
	}
}