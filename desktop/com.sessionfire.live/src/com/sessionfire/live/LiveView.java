package com.sessionfire.live;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
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

	public LiveView() {
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
	}

	@Override
	public JPanel createUI() {
		FormLayout layout = new FormLayout("10dlu, fill:pref:grow", // columns
				"pref, 3dlu, pref, 3dlu, pref"); // rows

		CellConstraints cc = new CellConstraints();
		JPanel contentPane = new JPanel(layout);
		contentPane.setOpaque(false);
		contentPane.setDoubleBuffered(false);
		contentPane.setBorder(new EmptyBorder(15, 15, 0, 15));

		JButton uploadButton = HudWidgetFactory.createHudButton("Upload");
		contentPane.add(uploadButton, cc.xyw(1, 1, 2));
		uploadButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				controller.upload();
			}
		});

		liveEnabledBox = HudWidgetFactory.createHudCheckBox("Live");
		liveEnabledBox.setSelected(false);
		liveEnabledBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.setLiveSync(liveEnabledBox.isSelected());
			}
		});
		contentPane.add(liveEnabledBox, cc.xyw(1, 3, 2));

		nameField = HudWidgetFactory.createHudTextField("");
		contentPane.add(nameField, cc.xyw(1, 5, 2));
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
		if (!nameField.getText().equals(controller.getName())) {
			nameField.setText(controller.getName());
		}
		inChange = false;
	}

}
