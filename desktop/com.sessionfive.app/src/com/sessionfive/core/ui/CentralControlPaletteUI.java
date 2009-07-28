package com.sessionfive.core.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

public class CentralControlPaletteUI {
	
	private final CentralControlPalette centralControlPalette;

	private TranslucentPalette window;
	private JButton choosePresentationButton;
	private JComboBox layoutChoice;
	private JComboBox animationChoice;

	public CentralControlPaletteUI(CentralControlPalette centralControlPalette) {
		this.centralControlPalette = centralControlPalette;
		window = new TranslucentPalette("Session Five - Central Control", false);
		initComponents();
		window.pack();
		window.setLocation(100, 100);
		window.setAlwaysOnTop(true);
	}

	public void show() {
		window.showPalette();
	}
	
	private void initComponents() {
		choosePresentationButton = new JButton("Choose Presentation...");
		choosePresentationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.choosePresentation();
			}
		});
		
		JComponent contentPane = (JComponent) window.getEmbeddedContentPane();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(new GridLayout(4, 1, 15, 15));
		contentPane.add(choosePresentationButton);
		
		DefaultComboBoxModel layoutModel = new DefaultComboBoxModel();
		layoutModel.addElement("Line");
		layoutModel.addElement("Line with rotation");
		layoutModel.addElement("Tiling");
		layoutChoice = new JComboBox(layoutModel);
		contentPane.add(layoutChoice);

		DefaultComboBoxModel animationModel = new DefaultComboBoxModel();
		animationModel.addElement("Zoom Out - Zoom In");
		animationModel.addElement("Move To");
		animationChoice = new JComboBox(animationModel);
		contentPane.add(animationChoice);
		
		JButton backgroundChooser = new JButton("Choose Background Color...");
		contentPane.add(backgroundChooser);
	}

}
