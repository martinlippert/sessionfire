package com.sessionfire.timer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TimerUI extends JPanel {

	private static final long serialVersionUID = -430613350613330878L;
	
	public TimerUI() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout());
		setOpaque(false);

		final JTextField time = new JTextField("0:20");
		final JCheckBox enabled = new JCheckBox("");
		enabled.setEnabled(false);

		enabled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (enabled.isSelected()) {
					time.setEditable(false);
				} else {
					time.setEditable(true);
				}

			}
		});

		add(time, BorderLayout.CENTER);
		add(enabled, BorderLayout.EAST);
	}

}
