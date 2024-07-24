package syncwindows;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;

public class SyncWindowsSettingsPanelRegular extends SyncWindowsSettingsPanel{
	

	public SyncWindowsSettingsPanelRegular() {
		initialize();
	}

	private void initialize() {
		GroupLayout layout = new GroupLayout(this);
		this.setBorder(BorderFactory.createTitledBorder("File selection"));
		this.setLayout(layout);

		JLabel headerNr = new JLabel("#");
		JLabel headerCopy = new JLabel("Copy");
		JLabel headerRoot = new JLabel("Root");
		JLabel headerFilename = new JLabel("Filename");
		JLabel headerOverlay = new JLabel("Overlay");

		JLabel[] dataNr = new JLabel[SyncWindowsConstants.N];
		copyRootButton = new JButton[SyncWindowsConstants.N];
		dataRoot = new JTextField[SyncWindowsConstants.N];
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
			dataFilename[i] = new JComboBox<String>();
			dataFilename[i].setName(SyncWindowsConstants.FILENAME_NAME+i);
			dataOverlay[i] = new JCheckBox();
			dataOverlay[i].setName(SyncWindowsConstants.OVERLAY_NAME+i);
		}

		JLabel dummyLabel = new JLabel("");

		GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();

		ParallelGroup group = layout.createParallelGroup();
		group.addComponent(headerNr);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataNr[i]);
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
		group.addComponent(headerFilename);
		for (int i = 0; i<SyncWindowsConstants.N; i++)
			group.addComponent(dataFilename[i]);
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
				.addComponent(headerFilename)
				.addComponent(headerOverlay));
	
		
		for (int i = 0; i<SyncWindowsConstants.N; i++) {
			vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
					.addComponent(dataNr[i])
					.addComponent(copyRootButton[i])
					.addComponent(dataRoot[i])
					.addComponent(dataFilename[i])
					.addComponent(dataOverlay[i]));
		}
		
		vGroup.addGroup(layout.createParallelGroup(Alignment.BASELINE)
				.addComponent(dummyLabel)
				.addComponent(dummyLabel)
				.addComponent(dummyLabel));

		layout.setVerticalGroup(vGroup);
	}
		
}
