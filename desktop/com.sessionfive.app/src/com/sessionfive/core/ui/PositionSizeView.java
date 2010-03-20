package com.sessionfive.core.ui;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.app.SelectionService;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapePosition;

public class PositionSizeView extends AbstractView {

	private JSpinner xField;
	private JSpinner yField;
	private JSpinner zField;
	private JSpinner widthField;
	private JSpinner heightField;
	private JSpinner depthField;

	public PositionSizeView(SelectionService selectionService) {
		super(selectionService);
	}

	@Override
	protected void doUpdateControls() {
		Shape[] shapes = getSelectedShapes();

		if (shapes.length > 0) {
			xField.setValue((double) getX());
			yField.setValue((double) getY());
			zField.setValue((double) getZ());

			widthField.setValue((double) getWidth());
			heightField.setValue((double) getHeight());
			depthField.setValue((double) getDepth());
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

		final SpinnerModel positionModelWidth = new SpinnerNumberModel(0d,
				-100d, 100d, 1d);
		final SpinnerModel positionModelHeight = new SpinnerNumberModel(0d,
				-100d, 100d, 1d);
		final SpinnerModel positionModelDepth = new SpinnerNumberModel(0d,
				-100d, 100d, 1d);

		xField = new JSpinner(positionModelX);
		yField = new JSpinner(positionModelY);
		zField = new JSpinner(positionModelZ);

		widthField = new JSpinner(positionModelWidth);
		heightField = new JSpinner(positionModelHeight);
		depthField = new JSpinner(positionModelDepth);

		ChangeListener xChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setPosition(((Double) positionModelX.getValue()).floatValue(),
						getY(), getZ());
			}
		};
		ChangeListener yChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setPosition(getX(), ((Double) positionModelY.getValue())
						.floatValue(), getZ());
			}
		};
		ChangeListener zChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setPosition(getX(), getY(),
						((Double) positionModelZ.getValue()).floatValue());
			}
		};
		xField.addChangeListener(xChangeListener);
		yField.addChangeListener(yChangeListener);
		zField.addChangeListener(zChangeListener);

		ChangeListener widthChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setSize(((Double) positionModelWidth.getValue()).floatValue(),
						getHeight(), getDepth());
			}
		};
		ChangeListener heightChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setSize(getWidth(), ((Double) positionModelHeight.getValue())
						.floatValue(), getDepth());
			}
		};
		ChangeListener depthChangeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setSize(getWidth(), getHeight(), ((Double) positionModelDepth
						.getValue()).floatValue());
			}
		};
		widthField.addChangeListener(widthChangeListener);
		heightField.addChangeListener(heightChangeListener);
		depthField.addChangeListener(depthChangeListener);

		FormLayout layout = new FormLayout(
				"fill:pref:grow, fill:pref:grow, fill:pref:grow",
				"pref, 0dlu, pref, 0dlu, pref, 0dlu, pref");

		JPanel panel = new JPanel(layout);
		panel.setOpaque(false);
		panel.setDoubleBuffered(false);
		CellConstraints cc = new CellConstraints();

		panel.add(HudWidgetFactory.createHudLabel("X"), cc.xy(1, 1));
		panel.add(xField, cc.xy(1, 3));
		panel.add(HudWidgetFactory.createHudLabel("Y"), cc.xy(2, 1));
		panel.add(yField, cc.xy(2, 3));
		panel.add(HudWidgetFactory.createHudLabel("Z"), cc.xy(3, 1));
		panel.add(zField, cc.xy(3, 3));

		panel.add(HudWidgetFactory.createHudLabel("Width"), cc.xy(1, 5));
		panel.add(widthField, cc.xy(1, 7));
		panel.add(HudWidgetFactory.createHudLabel("Height"), cc.xy(2, 5));
		panel.add(heightField, cc.xy(2, 7));
		panel.add(HudWidgetFactory.createHudLabel("Depth"), cc.xy(3, 5));
		panel.add(depthField, cc.xy(3, 7));

		return panel;
	}

	protected void setPosition(float x, float y, float z) {
		setSelfChanging(true);

		Shape[] shapes = getSelectedShapes();
		if (shapes.length > 0 && (getX() != x || getY() != y || getZ() != z)) {
			shapes[0].setPosition(new ShapePosition(x, y, z));
		}

		setSelfChanging(false);
	}

	protected void setSize(float width, float height, float depth) {
		setSelfChanging(true);

		Shape[] shapes = getSelectedShapes();
		if (shapes.length > 0
				&& (getWidth() != width || getHeight() != height || getDepth() != depth)) {
			shapes[0].setSize(width, height, depth);
		}

		setSelfChanging(false);
	}

	private float getX() {
		return getSelectedShapes()[0].getPosition().getX();
	}

	private float getY() {
		return getSelectedShapes()[0].getPosition().getY();
	}

	private float getZ() {
		return getSelectedShapes()[0].getPosition().getZ();
	}

	private float getWidth() {
		return getSelectedShapes()[0].getWidth();
	}

	private float getHeight() {
		return getSelectedShapes()[0].getHeight();
	}

	private float getDepth() {
		return getSelectedShapes()[0].getDepth();
	}

}
