package com.sessionfive.core.ui;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.app.SelectionService;
import com.sessionfive.core.Shape;

public class PositionSizeView extends AbstractView {

	private JSpinner xField;
	private JSpinner yField;
	private JSpinner zField;
	private JTextField widthField;
	private JTextField heightField;
	private JTextField depthField;

	public PositionSizeView(SelectionService selectionService) {
		super(selectionService);
	}

	@Override
	protected void doUpdateControls() {
		Shape[] shapes = getSelectedShapes();

		if (shapes.length > 0) {
			xField.setValue((double)shapes[0].getX());
			yField.setValue((double)shapes[0].getY());
			zField.setValue((double)shapes[0].getZ());
		}
	}

	@Override
	public JPanel createUI() {
		final SpinnerModel positionModelX = new SpinnerNumberModel(0d, -100d,
				100d, 1d);
		final SpinnerModel positionModelY = new SpinnerNumberModel(0d, -100d,
				100d, 1d);
		final SpinnerModel positionModelZ = new SpinnerNumberModel(0d, -100d,
				100d, 1d);
		
		xField = new JSpinner(positionModelX);
		yField = new JSpinner(positionModelY);
		zField = new JSpinner(positionModelZ);

		ChangeListener changeListenerX = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setPositionX(((Double) positionModelX.getValue()).floatValue());
			}
		};
		ChangeListener changeListenerY = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setPositionY(((Double) positionModelY.getValue()).floatValue());
			}
		};
		ChangeListener changeListenerZ = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setPositionZ(((Double) positionModelZ.getValue()).floatValue());
			}
		};
		xField.addChangeListener(changeListenerX);
		yField.addChangeListener(changeListenerY);
		zField.addChangeListener(changeListenerZ);

		widthField = HudWidgetFactory.createHudTextField("");
		heightField = HudWidgetFactory.createHudTextField("");
		depthField = HudWidgetFactory.createHudTextField("");

		FormLayout layout = new FormLayout(
				"fill:pref:grow, fill:pref:grow, fill:pref:grow",
				"pref, 0dlu, pref, 0dlu, pref");

		JPanel panel = new JPanel(layout);
		panel.setOpaque(false);
		CellConstraints cc = new CellConstraints();

		panel.add(HudWidgetFactory.createHudLabel("X"), cc.xy(1, 1));
		panel.add(xField, cc.xy(1, 3));
		panel.add(HudWidgetFactory.createHudLabel("Y"), cc.xy(2, 1));
		panel.add(yField, cc.xy(2, 3));
		panel.add(HudWidgetFactory.createHudLabel("Z"), cc.xy(3, 1));
		panel.add(zField, cc.xy(3, 3));

		return panel;
	}

	protected void setPositionX(float x) {
		setSelfChanging(true);
		
		Shape[] shapes = getSelectedShapes();
		if (shapes.length > 0 && shapes[0].getX() != x) {
			float y = shapes[0].getY();
			float z = shapes[0].getZ();
			shapes[0].setPosition(x, y, z);
		}

		setSelfChanging(false);
	}

	protected void setPositionY(float y) {
		setSelfChanging(true);

		Shape[] shapes = getSelectedShapes();
		if (shapes.length > 0 && shapes[0].getY() != y) {
			float x = shapes[0].getX();
			float z = shapes[0].getZ();
			shapes[0].setPosition(x, y, z);
		}

		setSelfChanging(false);
	}

	protected void setPositionZ(float z) {
		setSelfChanging(true);

		Shape[] shapes = getSelectedShapes();
		if (shapes.length > 0 && shapes[0].getZ() != z) {
			float x = shapes[0].getX();
			float y = shapes[0].getY();
			shapes[0].setPosition(x, y, z);
		}

		setSelfChanging(false);
	}

}
