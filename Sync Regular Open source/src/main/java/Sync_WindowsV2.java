import ij.plugin.frame.PlugInFrame;
import syncwindows.SyncWindowsPlugIn;
public class Sync_WindowsV2 extends PlugInFrame  {
	public Sync_WindowsV2() {
		super("Sync_WindowsV2");
	}
	@Override
	public void run(String arg) {
		new SyncWindowsPlugIn("ImageJPlugIn","moldev");
	}
}