package com.sessionfive.core.ui;

import com.sessionfive.core.Presentation;

public interface Style {
	
	public String getName();
	public Layouter getLayout();
	public float getRotationX();
	public float getRotationY();
	public float getRotationZ();
	
	public boolean matches(Presentation presentation);

}
