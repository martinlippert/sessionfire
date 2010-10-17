package com.sessionfire.timer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.core.ui.HelpWindow;
import com.sessionfive.core.ui.HelpWindowPosition;
import com.sessionfive.core.ui.ShowsHelp;
import com.sessionfive.core.ui.View;

public class TimerView extends JPanel implements View, ShowsHelp, TimerListener {

	private static final long serialVersionUID = -430613350613330878L;
	private JTextField time;
	private Color defaultForeground;
	private HelpWindow helpWindow;
	private JButton startButton;
	private JButton stopButton;
	private JLabel runningTime;

	public TimerView() {
		FormLayout layout = new FormLayout("fill:pref:grow, 3dlu, fill:pref:grow, 6dlu, fill:pref:grow", // columns
				"pref, 3dlu, pref"); // rows

		CellConstraints cc = new CellConstraints();
		JPanel subContentPane = new JPanel(layout);
		subContentPane.setOpaque(false);
		subContentPane.setDoubleBuffered(false);

		subContentPane.setBorder(new EmptyBorder(15, 15, 0, 15));
		add(subContentPane, BorderLayout.CENTER);

		setOpaque(false);
		setDoubleBuffered(false);

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
		subContentPane.add(time, cc.xyw(1, 1, 5));
		
		startButton = HudWidgetFactory.createHudButton("start");
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Activator.getInstance().getTimerController().startTimer();
			}
		});
		subContentPane.add(startButton, cc.xy(1, 3));
		
		stopButton = HudWidgetFactory.createHudButton("stop");
		stopButton.setEnabled(false);
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Activator.getInstance().getTimerController().stopTimer();
			}
		});
		subContentPane.add(stopButton, cc.xy(3, 3));

		runningTime = HudWidgetFactory.createHudLabel("0:20");
		runningTime.setEnabled(false);
		subContentPane.add(runningTime, cc.xy(5, 3));
		
		Activator.getInstance().getTimerController().addTimerListener(this);
	}

	@Override
	public JPanel createUI() {
		return this;
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
				Activator.getInstance().getTimerController().setTime(totalSeconds);
				runningTime.setText(convertToMMSS(totalSeconds));
			} else {
				time.setForeground(Color.RED);
			}
		} catch (ParseException e) {
			time.setForeground(Color.RED);
		}
	}

	public void showhelp() {
		helpWindow = new HelpWindow(time, HelpWindowPosition.BELOW,
				"This little widget lets you enter an intervall.",
				"Sessionfire than automatically progresses to the",
				"next shape. Press T to start, ESC to stop.");
	}

	public void hidehelp() {
		if (helpWindow == null) {
			return;
		}
		helpWindow.hideHoverWindow(new Runnable() {
			@Override
			public void run() {
			}
		});
		helpWindow = null;
	}

	@Override
	public void remainingTimeChanged(int remainingSeconds) {
		runningTime.setText(convertToMMSS(remainingSeconds));
	}

	protected String convertToMMSS(int remainingSeconds) {
		int fullMinutes = remainingSeconds / 60;
		int fullSeconds = remainingSeconds - (fullMinutes * 60);
		
		return fullMinutes + ":" + (fullSeconds < 10 ? "0" + fullSeconds : fullSeconds);
	}

	@Override
	public void timerStarted() {
		startButton.setEnabled(false);
		stopButton.setEnabled(true);
		time.setEnabled(false);
		runningTime.setEnabled(true);
	}

	@Override
	public void timerStopped() {
		startButton.setEnabled(true);
		stopButton.setEnabled(false);
		time.setEnabled(true);
		runningTime.setEnabled(false);
	}

}
