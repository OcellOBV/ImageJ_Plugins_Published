	package syncwindows;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.JButton;

public class SyncWindowsSettingsPanelMoldev extends SyncWindowsSettingsPanel {
	
	private JComboBox<?>[] dataPlate;
	private JComboBox<?>[] dataWellRow;
	private JComboBox<?>[] dataWellCol;
	private JComboBox<?>[] dataSite;
	private JComboBox<?>[] dataChannel;
	private JComboBox<?>[] dataTime;
	private JCheckBox lockPlateCheckBox;
	private JCheckBox lockWellRowCheckBox;
	private JCheckBox lockWellColCheckBox;
	private JCheckBox lockSiteCheckBox;
	private JCheckBox lockChannelCheckBox;
	private JCheckBox lockTimeCheckBox;
	private JCheckBox lockFilenameCheckBox;
	

	public JComboBox<?>[] getPlateComboBox() {
		return dataPlate;
	}
	
	public JComboBox<String> getPlateComboBox(int i) {
		return (JComboBox<String>)dataPlate[i];
	}
	
	public JComboBox<?>[] getWellRowComboBox() {
		return dataWellRow;
	}
	
	public JComboBox<String> getWellRowComboBox(int i) {
		return (JComboBox<String>)dataWellRow[i];
	}

	public JComboBox<?>[] getWellColComboBox() {
		return dataWellCol;
	}
	
	public JComboBox<String> getWellColComboBox(int i) {
		return (JComboBox<String>)dataWellCol[i];
	}

	public JComboBox<?>[] getSiteComboBox() {
		return dataSite;
	}
	
	public JComboBox<String> getSiteComboBox(int i) {
		return (JComboBox<String>)dataSite[i];
	}
	
	public JComboBox<?>[] getChannelComboBox() {
		return dataChannel;
	}
	

	public JComboBox<String> getChannelComboBox(int i) {
		return (JComboBox<String>)dataChannel[i];
	}
	
	public JComboBox<?>[] getTimeComboBox() {
		return dataTime;
	}
	
	public JComboBox<String> getTimeComboBox(int i) {
		return (JComboBox<String>)dataTime[i];
	}

	
	
	public JCheckBox getLockPlateCheckBox() {
		return lockPlateCheckBox;
	}
	
	public JCheckBox getLockWellRowCheckBox() {
		return lockWellRowCheckBox;
	}
	
	public JCheckBox getLockWellColCheckBox() {
		return lockWellColCheckBox;
	}
	
	public JCheckBox getLockSiteCheckBox() {
		return lockSiteCheckBox;
	}
	
	public JCheckBox getLockChannelCheckBox() {
		return lockChannelCheckBox;
	}
	
	public JCheckBox getLockTimeCheckBox() {
		return lockTimeCheckBox;
	}
	
	public JCheckBox getLockFilenameCheckBox() {
		return lockFilenameCheckBox;
	}
	
	public SyncWindowsSettingsPanelMoldev() {
		initialize();
	}
		
	private void initialize() {
		GroupLayout layout = new GroupLayout(this);
		this.setBorder(BorderFactory.createTitledBorder("File selection"));
		this.setLayout(layout);

		JLabel headerNr = new JLabel("#");
		JLabel headerCopy = new JLabel("Copy");
		JLabel headerRoot = new JLabel("Root");
		JLabel headerPlate = new JLabel("Plate");
		JLabel headerWellRow = new JLabel("Row");
		JLabel headerWellCol = new JLabel("Col");
		JLabel headerSite = new JLabel("Site");
		JLabel headerChannel = new JLabel("Channel");
		JLabel headerTime = new JLabel("time");
		JLabel headerFilename = new JLabel("Filename");
		JLabel headerOverlay = new JLabel("Overlay");

		JLabel[] dataNr = new JLabel[SyncWindowsConstants.N];
		copyRootButton = new JButton[SyncWindowsConstants.N];
		dataRoot = new JTextField[SyncWindowsConstants.N];
		dataPlate = new JComboBox[SyncWindowsConstants.N];
		dataWellRow = new JComboBox[SyncWindowsConstants.N];
		dataWellCol = new JComboBox[SyncWindowsConstants.N];
		dataSite = new JComboBox[SyncWindowsConstants.N];
		dataChannel = new JComboBox[SyncWindowsConstants.N];
		dataTime = new JComboBox[SyncWindowsConstants.N];
		dataFilename = new JComboBox[SyncWindowsConstants.N];
		dataOverlay = new JCheckBox[SyncWindowsConstants.N];

		for(int i=0; i<SyncWindowsConstants.N; i++) {
			dataNr[i] = new JLabel(i+": ");
			copyRootButton[i] = new JButton();
			copyRootButton[i].setName(SyncWindowsConstants.COPY_NAME+i);
			Dimension buttonSize = new Dimension(15,15);
			copyRootButton[i].setMaximumSize(buttonSize);
			copyRootButton[i].setMinimumSize(buttonSize);
			dataRoot[i] = new JTextField("", 20);
			dataRoot[i].setName(SyncWindowsConstants.ROOT_NAME+i);
			dataPlate[i] = new JComboBox<String>();
			dataPlate[i].setName(SyncWindowsConstants.PLATE_NAME+i);
			dataWellRow[i] = new JComboBox<String>();
			dataWellRow[i].setName(SyncWindowsConstants.WELL_ROW_NAME+i);
			dataWellCol[i] = new JComboBox<String>();
			dataWellCol[i].setName(SyncWindowsConstants.WELL_COL_NAME+i);
			dataSite[i] = new JComboBox<String>();
			dataSite[i].setName(SyncWindowsConstants.SITE_NAME+i);
			dataChannel[i] = new JComboBox<String>();
			dataChannel[i].setName(SyncWindowsConstants.CHANNEL_NAME+i);
			dataTime[i] = new JComboBox<String>();
			dataTime[i].setName(SyncWindowsConstants.TIME_NAME+i);
			dataFilename[i] = new JComboBox<String>();
			dataFilename[i].setName(SyncWindowsConstants.FILENAME_NAME+i);
			dataOverlay[i] = new JCheckBox();
			dataOverlay[i].setName(SyncWindowsConstants.OVERLAY_NAME+i);
		}

		
		JLabel lockLabel = new JLabel("Lock:");
		JLabel dummyLabel = new JLabel("");
		lockPlateCheckBox = new JCheckBox();
		lockPlateCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.PLATE_NAME);
		lockWellRowCheckBox = new JCheckBox();
		lockWellRowCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.WELL_ROW_NAME);
		lockWellColCheckBox = new JCheckBox();
		lockWellColCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.WELL_COL_NAME);
		lockSiteCheckBox = new JCheckBox();
		lockSiteCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.SITE_NAME);
		lockChannelCheckBox = new JCheckBox();
		lockChannelCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.CHANNEL_NAME);
		lockTimeCheckBox = new JCheckBox();
		lockTimeCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.TIME_NAME);
		lockFilenameCheckBox = new JCheckBox();
		lockFilenameCheckBox.setName(SyncWindowsConstants.LOCK_NAME + SyncWindowsConstants.FILENAME_NAME);
		

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		ParallelGroup group = layout.createParallelGroup();
		group.addComponent(headerNr);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataNr[i]);
		group.addComponent(lockLabel);
		hGroup.addGroup(group);

		group = layout.createParallelGroup();
		group.addComponent(headerCopy);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(copyRootButton[i]);
		group.addComponent(dummyLabel);
		hGroup.addGroup(group);
		
		group = layout.createParallelGroup();
		group.addComponent(headerRoot);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataRoot[i]);
		group.addComponent(dummyLabel);
		hGroup.addGroup(group);

		group = layout.createParallelGroup();
		group.addComponent(headerPlate);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataPlate[i]);
		group.addComponent(lockPlateCheckBox);
		hGroup.addGroup(group);

		group = layout.createParallelGroup();
		group.addComponent(headerWellRow);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataWellRow[i]);
		group.addComponent(lockWellRowCheckBox);
		hGroup.addGroup(group);
		
		group = layout.createParallelGroup();
		group.addComponent(headerWellCol);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataWellCol[i]);
		group.addComponent(lockWellColCheckBox);
		hGroup.addGroup(group);
		
		group = layout.createParallelGroup();
		group.addComponent(headerSite);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataSite[i]);
		group.addComponent(lockSiteCheckBox);
		hGroup.addGroup(group);

		group = layout.createParallelGroup();
		group.addComponent(headerChannel);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataChannel[i]);
		group.addComponent(lockChannelCheckBox);
		hGroup.addGroup(group);

		group = layout.createParallelGroup();
		group.addComponent(headerTime);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataTime[i]);
		group.addComponent(lockTimeCheckBox);
		hGroup.addGroup(group);
		
		group = layout.createParallelGroup();
		group.addComponent(headerFilename);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataFilename[i]);
		group.addComponent(lockFilenameCheckBox);
		hGroup.addGroup(group);
		
		group = layout.createParallelGroup();
		group.addComponent(headerOverlay);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataOverlay[i]);
		group.addComponent(dummyLabel);
		hGroup.addGroup(group);

		layout.setHorizontalGroup(hGroup);

		GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();

		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(headerNr)
				.addComponent(headerCopy)
				.addComponent(headerRoot)
				.addComponent(headerPlate)
				.addComponent(headerWellRow)
				.addComponent(headerWellCol)
				.addComponent(headerSite)
				.addComponent(headerChannel)
				.addComponent(headerTime)
				.addComponent(headerFilename)
				.addComponent(headerOverlay));
	
		
		for (int i = 0; i<SyncWindowsConstants.N; i++) {
			vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(dataNr[i])
					.addComponent(copyRootButton[i])
					.addComponent(dataRoot[i])
					.addComponent(dataPlate[i])
					.addComponent(dataWellRow[i])
					.addComponent(dataWellCol[i])
					.addComponent(dataSite[i])
					.addComponent(dataChannel[i])
					.addComponent(dataTime[i])
					.addComponent(dataFilename[i])
					.addComponent(dataOverlay[i]));
		}
		
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(lockLabel)
				.addComponent(dummyLabel)
				.addComponent(dummyLabel)
				.addComponent(lockPlateCheckBox)
				.addComponent(lockWellRowCheckBox)
				.addComponent(lockWellColCheckBox)
				.addComponent(lockSiteCheckBox)
				.addComponent(lockChannelCheckBox)
				.addComponent(lockTimeCheckBox)
				.addComponent(lockFilenameCheckBox)
				.addComponent(dummyLabel));

		layout.setVerticalGroup(vGroup);
	}
}
