package com.sessionfive.core.ui;

import javax.swing.JPanel;

public class PanelExtension {
	
	private final String name;
	private final JPanel panel;

	public PanelExtension(String name, JPanel panel) {
		this.name = name;
		this.panel = panel;
	}
	
	public String getName() {
		return name;
	}
	
	public JPanel getPanel() {
		return panel;
	}

}
