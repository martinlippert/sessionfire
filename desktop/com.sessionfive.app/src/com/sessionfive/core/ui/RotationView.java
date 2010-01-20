package com.sessionfive.core.ui;

import javax.swing.JPanel;

import com.sessionfive.app.SelectionChangedEvent;
import com.sessionfive.app.SelectionListener;
import com.sessionfive.app.SelectionService;
import com.sessionfive.core.Shape;

public class RotationView implements View, SelectionListener {

	private Shape[] selectedShapes;

	public RotationView(final SelectionService selectionService) {
		selectedShapes = new Shape[0];
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		this.selectedShapes = event.getSelectedShapes();
	}

	@Override
	public JPanel createUI() {
		return null;
	}

	public void setRotation(float x, float y, float z) {
		for (Shape shape : selectedShapes) {
			shape.setRotation(x, y, z);
		}
	}

}
