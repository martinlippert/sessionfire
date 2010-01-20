package com.sessionfive.app;

import java.util.HashSet;
import java.util.Iterator;

import com.sessionfive.core.Shape;

public class SelectionServiceImpl implements SelectionService {
	
	private HashSet<SelectionListener> listener;
	private Shape[] selection;

	public SelectionServiceImpl() {
		this.listener = new HashSet<SelectionListener>();
	}

	@Override
	public Shape[] getSelection() {
		return this.selection;
	}
	
	@Override
	public void setSelection(Shape[] selectedShapes) {
		this.selection = selectedShapes;
		fireSelectionChangedEvent(selectedShapes);
	}

	@Override
	public void addSelectionListener(SelectionListener listener) {
		this.listener.add(listener);
	}

	@Override
	public void removeSelectionListener(SelectionListener listener) {
		this.listener.remove(listener);
	}
	
	protected void fireSelectionChangedEvent(Shape[] selectedShapes) {
		SelectionChangedEvent event = new SelectionChangedEvent(selectedShapes);
		
		Iterator<SelectionListener> iterator = this.listener.iterator();
		while (iterator.hasNext()) {
			iterator.next().selectionChanged(event);
		}
	}

}
