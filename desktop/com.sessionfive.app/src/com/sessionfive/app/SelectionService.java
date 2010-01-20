package com.sessionfive.app;

import com.sessionfive.core.Shape;

public interface SelectionService {
	
	public Shape[] getSelection();
	public void addSelectionListener(SelectionListener listener);
	public void removeSelectionListener(SelectionListener listener);
	public void setSelection(Shape[] selectedShapes);

}
