package com.sessionfive.core.ui;

import javax.swing.SwingUtilities;

import com.sessionfive.app.SelectionChangedEvent;
import com.sessionfive.app.SelectionListener;
import com.sessionfive.app.SelectionService;
import com.sessionfive.core.Shape;
import com.sessionfive.core.ShapeChangedEvent;
import com.sessionfive.core.ShapeChangedListener;


public abstract class AbstractView implements View, SelectionListener, ShapeChangedListener {
	
	private boolean selfChanging;
	private Shape[] selectedShapes;
	
	public AbstractView(final SelectionService selectionService) {
		this.selfChanging = false;
		this.selectedShapes = new Shape[0];
		selectionService.addSelectionListener(this);
	}
	
	protected void updateControls() {
		if (!selfChanging) {
			doUpdateControls();
		}
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		removeShapeListener(this.selectedShapes);
		this.selectedShapes = event.getSelectedShapes();
		addShapeListener(this.selectedShapes);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateControls();
			}
		});
	}
	
	public Shape[] getSelectedShapes() {
		return this.selectedShapes;
	}
	
	public void setSelfChanging(boolean selfChanging) {
		this.selfChanging = selfChanging;
	}
	
	public boolean isSelfChanging() {
		return this.selfChanging;
	}

	@Override
	public void shapeChanged(ShapeChangedEvent event) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				updateControls();
			}
		});
	}

	protected void addShapeListener(Shape[] shapes) {
		for (Shape shape : shapes) {
			shape.addShapeChangedListener(this);
		}
	}

	protected void removeShapeListener(Shape[] shapes) {
		for (Shape shape : shapes) {
			shape.removeShapeChangedListener(this);
		}
	}

	protected abstract void doUpdateControls();
	
}
