package com.sessionfire.timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import com.sessionfive.core.ui.HelpLabelFactory;



public class TimerUI extends JPanel {

	private static final long serialVersionUID = -430613350613330878L;
	private JTextField time;
	private Color defaultForeground;
	
	public TimerUI() {
		FormLayout layout = new FormLayout(
				"90dlu", // columns
				"pref, pref"); // rows

		CellConstraints cc = new CellConstraints();
		JPanel subContentPane = new JPanel(layout);
		subContentPane.setOpaque(false);

		subContentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		add(subContentPane, BorderLayout.CENTER);

		setOpaque(false);
		
		time = HudWidgetFactory.createHudTextField("0:20");
		defaultForeground = time.getForeground();
		time.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				updateTimeSetting(time.getText());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateTimeSetting(time.getText());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				updateTimeSetting(time.getText());
			}
		});

		subContentPane.add(time, cc.xy(1, 1));
		subContentPane.add(HelpLabelFactory.createHelpLabel("Press T to start, ESC to stop"),  cc.xy(1, 2));
	}
	
	public void updateTimeSetting(String newValue) {
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		try {
			Date parsed = format.parse(newValue);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsed);
			int minutes = cal.get(Calendar.MINUTE);
			int seconds = cal.get(Calendar.SECOND);
			
			int totalSeconds = (minutes * 60) + seconds;
			if (totalSeconds > 0) {
				time.setForeground(defaultForeground);
				Activator.getInstance().getTimerController().setTime(totalSeconds * 1000);
			}
			else {
				time.setForeground(Color.RED);
			}
		} catch (ParseException e) {
			time.setForeground(Color.RED);
		}
	}

}
