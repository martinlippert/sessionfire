package com.sessionfive.core.ui;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.app.SelectionChangedEvent;
import com.sessionfive.app.SelectionListener;
import com.sessionfive.app.SelectionService;
import com.sessionfive.core.Shape;

public class RotationView implements View, SelectionListener {

	private Shape[] selectedShapes;

	private JSlider xRotationSlider;
	private JSlider yRotationSlider;
	private JSlider zRotationSlider;

	public RotationView(final SelectionService selectionService) {
		selectedShapes = new Shape[0];
		selectionService.addSelectionListener(this);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		this.selectedShapes = event.getSelectedShapes();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateControls();
			}
		});
	}

	@Override
	public JPanel createUI() {
		xRotationSlider = new JSlider(0, 360, 0);
		yRotationSlider = new JSlider(0, 360, 0);
		zRotationSlider = new JSlider(0, 360, 0);

		ChangeListener rotationSliderListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				setRotation(xRotationSlider.getValue(), yRotationSlider
						.getValue(), zRotationSlider.getValue());
			}
		};

		xRotationSlider.addChangeListener(rotationSliderListener);
		yRotationSlider.addChangeListener(rotationSliderListener);
		zRotationSlider.addChangeListener(rotationSliderListener);

		FormLayout layout = new FormLayout("10dlu, fill:pref:grow",
				"pref, 0dlu, pref, 0dlu, pref");

		JPanel panel = new JPanel(layout);
		panel.setOpaque(false);
		CellConstraints cc = new CellConstraints();

		panel.add(HudWidgetFactory.createHudLabel("X"), cc.xy(1, 1));
		panel.add(xRotationSlider, cc.xy(2, 1));
		panel.add(HudWidgetFactory.createHudLabel("Y"), cc.xy(1, 3));
		panel.add(yRotationSlider, cc.xy(2, 3));
		panel.add(HudWidgetFactory.createHudLabel("Z"), cc.xy(1, 5));
		panel.add(zRotationSlider, cc.xy(2, 5));

		return panel;
	}

	public void setRotation(float x, float y, float z) {
		for (Shape shape : selectedShapes) {
			shape.setRotation(x, y, z);
		}
	}

	public void updateControls() {
		if (selectedShapes.length > 0 && xRotationSlider != null
				&& yRotationSlider != null && zRotationSlider != null) {
			xRotationSlider.setValue((int) selectedShapes[0]
					.getRotationAngleX());
			yRotationSlider.setValue((int) selectedShapes[0]
					.getRotationAngleY());
			zRotationSlider.setValue((int) selectedShapes[0]
					.getRotationAngleZ());
		}
	}

}
