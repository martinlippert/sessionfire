package com.sessionfive.core.ui;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class HelpLabelFactory {
	
	public static JComponent createHelpLabel(String helpText) {
		JLabel helpLabel = new JLabel(helpText);
		helpLabel.setForeground(new Color(100, 100, 100));
		helpLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		helpLabel.setVerticalAlignment(SwingConstants.TOP);
		helpLabel.setFont(helpLabel.getFont().deriveFont(helpLabel.getFont().getSize2D() - 2));
		return helpLabel;
	}

}
