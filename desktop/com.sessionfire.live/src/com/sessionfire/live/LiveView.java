package com.sessionfire.live;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.core.PresentationChangedEvent;
import com.sessionfive.core.PresentationChangedListener;
import com.sessionfive.core.ui.ShowsHelp;
import com.sessionfive.core.ui.View;

public class LiveView implements View, ShowsHelp {

	private JCheckBox liveEnabledBox;
	private LiveController controller;
	private JTextField nameField;
	private boolean inChange;
	private JButton uploadButton;
	private JLabel uploadInfo;
	private ImageIcon loadingIcon;
	private ImageIcon loadingIconEmpty;
	private UploadResult uploadCallback;

	public LiveView() {
		this.loadingIcon = new ImageIcon(this.getClass().getResource("uploadicon.gif"));
		this.loadingIconEmpty = new ImageIcon(this.getClass().getResource("uploadicon-empty.gif"));
		
		this.controller = new LiveController();
		this.controller.addPresentationChangedListener(new PresentationChangedListener() {
			@Override
			public void presentationChanged(
					PresentationChangedEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateControls();
					}
				});
			}
		});
		
		this.uploadCallback = new UploadResult() {

			@Override
			public void uploadSuccessful(String url) {
				String id = controller.getID();
				if (id != null && id.length() > 0) {
					liveEnabledBox.setEnabled(true);
					uploadButton.setText("Update Upload");
					uploadInfo.setText(url);
				}
				else {
					liveEnabledBox.setEnabled(false);
					uploadButton.setText("Upload");
					uploadInfo.setText(" ");
				}
				uploadInfo.setIcon(loadingIconEmpty);
			}

			@Override
			public void uploadFailed() {
			}
		};
	}

	@Override
	public JPanel createUI() {
		FormLayout layout = new FormLayout("10dlu, fill:pref:grow", // columns
				"pref, 1dlu, pref, 1dlu, pref, 3dlu, pref"); // rows

		CellConstraints cc = new CellConstraints();
		JPanel contentPane = new JPanel(layout);
		contentPane.setOpaque(false);
		contentPane.setDoubleBuffered(false);
		contentPane.setBorder(new EmptyBorder(15, 15, 0, 15));

		uploadButton = HudWidgetFactory.createHudButton("Upload");
		contentPane.add(uploadButton, cc.xyw(1, 1, 2));
		uploadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				uploadInfo.setIcon(loadingIcon);
				uploadInfo.setText("... uploading");
				controller.upload(uploadCallback);
			}
		});
		
		uploadInfo = HudWidgetFactory.createHudLabel(" ");
		uploadInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(uploadInfo, cc.xyw(1, 3, 2));

		liveEnabledBox = HudWidgetFactory.createHudCheckBox("Live");
		liveEnabledBox.setSelected(false);
		liveEnabledBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setLiveSync(liveEnabledBox.isSelected());
			}
		});
		contentPane.add(liveEnabledBox, cc.xyw(1, 5, 2));

		nameField = HudWidgetFactory.createHudTextField("");
		contentPane.add(nameField, cc.xyw(1, 7, 2));
		nameField.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setName();
			}

			public void insertUpdate(DocumentEvent e) {
				setName();
			}

			public void changedUpdate(DocumentEvent e) {
				setName();
			}
		});


		updateControls();
		return contentPane;
	}

	@Override
	public void showhelp() {
	}

	@Override
	public void hidehelp() {
	}
	
	protected void setName() {
		if (!inChange) {
			controller.setName(nameField.getText());
		}
	}
	
	private void updateControls() {
		inChange = true;

		String name = controller.getName();
		String id = controller.getID();

		if (!nameField.getText().equals(name)) {
			nameField.setText(name);
		}
		
		if (id != null && id.length() > 0) {
			liveEnabledBox.setEnabled(true);
			uploadButton.setText("Update Upload");
		}
		else {
			liveEnabledBox.setEnabled(false);
			uploadButton.setText("Upload");
			uploadInfo.setText(" ");
		}
		uploadInfo.setIcon(loadingIconEmpty);
		uploadButton.setEnabled(name != null && name.length() > 4);
		
		inChange = false;
	}

}
