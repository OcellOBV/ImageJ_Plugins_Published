import ij.plugin.frame.PlugInFrame;
import syncwindows.SyncWindowsPlugIn;
public class SyncWindows_V2 extends PlugInFrame  {
	public SyncWindows_V2() {
		super("SyncWindows_V2");
	}
	@Override
	public void run(String arg) {
		new SyncWindowsPlugIn("ImageJPlugIn","regular");
	}
}