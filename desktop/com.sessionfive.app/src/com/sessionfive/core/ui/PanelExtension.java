package com.sessionfive.core.ui;

public class PanelExtension {
	
	private final String name;
	private final View view;

	public PanelExtension(String name, View view) {
		this.name = name;
		this.view = view;
	}
	
	public String getName() {
		return name;
	}
	
	public View getView() {
		return view;
	}

}
