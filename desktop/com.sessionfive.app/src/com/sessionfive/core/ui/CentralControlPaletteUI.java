package com.sessionfive.core.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CentralControlPaletteUI {

	private final CentralControlPalette centralControlPalette;

	private TranslucentPalette window;
	private JButton choosePresentationButton;
	private JComboBox layoutChoice;
	private JComboBox animationChoice;
	private JTextField layerText;
	private final Component canvas;

	public CentralControlPaletteUI(CentralControlPalette centralControlPalette,
			Component canvas) {
		this.centralControlPalette = centralControlPalette;
		this.canvas = canvas;
		window = new TranslucentPalette("Session Five - Central Control", false, SwingUtilities.getWindowAncestor(canvas));
		initComponents();
		window.pack();
		window.setLocation(100, 100);
	}

	public void show() {
		window.showPalette();
	}

	public void setStatus(String status) {
		window.setStatus(status);
	}

	private void initComponents() {
		choosePresentationButton = new JButton("Choose Presentation...");
		choosePresentationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.choosePresentation(canvas,
						(Layouter) layoutChoice.getSelectedItem(),
						(AnimationFactory) animationChoice.getSelectedItem());
			}
		});

		JComponent contentPane = (JComponent) window.getEmbeddedContentPane();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(new GridLayout(5, 1, 15, 15));
		contentPane.add(choosePresentationButton);

		DefaultComboBoxModel layoutModel = new DefaultComboBoxModel();
		Layouter[] allLayouter = centralControlPalette.getLayouter();
		for (Layouter layouter : allLayouter) {
			layoutModel.addElement(layouter);
		}
		layoutChoice = new JComboBox(layoutModel);
		layoutChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.changeLayout((Layouter) layoutChoice
						.getSelectedItem());
			}
		});
		contentPane.add(layoutChoice);

		DefaultComboBoxModel animationModel = new DefaultComboBoxModel();
		AnimationFactory[] animationFactories = centralControlPalette.getAnimators();
		for (AnimationFactory animationFactory : animationFactories) {
			animationModel.addElement(animationFactory);
		}
		animationChoice = new JComboBox(animationModel);
		animationChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.changeAnimation((AnimationFactory) animationChoice
						.getSelectedItem());
			}
		});
		contentPane.add(animationChoice);

		JButton backgroundChooser = new JButton("Choose Background Color...");
		contentPane.add(backgroundChooser);
		backgroundChooser.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				chooseBackground();
			}
		});
		
		layerText = new JTextField(centralControlPalette.getLayerText());
		contentPane.add(layerText);
		layerText.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				centralControlPalette.setLayerText(layerText.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				centralControlPalette.setLayerText(layerText.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				centralControlPalette.setLayerText(layerText.getText());
			}
		});
	}

	protected void chooseBackground() {
		Color newColor = JColorChooser.showDialog(window,
				"Choose Background Color", centralControlPalette
						.getBackgroundColor());

		if (newColor != null) {
			centralControlPalette.setBackgroundColor(newColor);
		}
	}

}
