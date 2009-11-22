package com.sessionfire.timer;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TimerUI extends JPanel {

	private static final long serialVersionUID = -430613350613330878L;
	
	public TimerUI() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout());
		setOpaque(false);

		final JTextField time = new JTextField("0:20");
		time.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});

		add(time, BorderLayout.CENTER);
	}

}
