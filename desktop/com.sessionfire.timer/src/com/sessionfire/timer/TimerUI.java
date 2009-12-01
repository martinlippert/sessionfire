package com.sessionfire.timer;

import java.awt.GridLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sessionfive.core.ui.HelpLabelFactory;

public class TimerUI extends JPanel {

	private static final long serialVersionUID = -430613350613330878L;
	
	public TimerUI() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new GridLayout(2, 1));
		setOpaque(false);

		final JTextField time = new JTextField("0:20");
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

		add(time);
		add(HelpLabelFactory.createHelpLabel("Press <p> to start"));
	}
	
	public void updateTimeSetting(String newValue) {
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		try {
			Date parsed = format.parse(newValue);
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(parsed);
			int minutes = cal.get(Calendar.MINUTE);
			int seconds = cal.get(Calendar.SECOND);
			
			System.out.println(minutes + "/" + seconds);
			
			int totalSeconds = (minutes * 60) + seconds;
			Activator.getInstance().getTimerController().setTime(totalSeconds * 1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}
