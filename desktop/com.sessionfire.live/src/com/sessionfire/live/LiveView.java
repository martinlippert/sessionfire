package com.sessionfire.live;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.core.ui.ShowsHelp;
import com.sessionfive.core.ui.View;

public class LiveView implements View, ShowsHelp {

	private JCheckBox liveEnabledBox;
	private LiveController controller;

	public LiveView() {
		this.controller = new LiveController();
	}

	@Override
	public JPanel createUI() {
		FormLayout layout = new FormLayout(
				"10dlu, fill:pref:grow", // columns
				"pref, 3dlu, pref"); // rows

		CellConstraints cc = new CellConstraints();
		JPanel contentPane = new JPanel(layout);
		contentPane.setOpaque(false);
		contentPane.setDoubleBuffered(false);
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		
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
		
		return contentPane;
	}

	@Override
	public void showhelp() {
	}

	@Override
	public void hidehelp() {
	}

}
