package com.sessionfire.twitter.core.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.sessionfire.twitter.core.Activator;
import com.sessionfive.app.SessionfivePanel;

public class TwitterPanel extends JPanel implements SessionfivePanel {
	// private static String TWITTER_USER_DEFAULT = "<Twitter User>";
	// private static String TWITTER_PASSWORD_DEFAULT = "<Twitter Password>";
	private static String TWITTER_KEYWORD_DEFAULT = "<Twitter Search>";

	public TwitterPanel() {
		setBorder(new EmptyBorder(15, 15, 15, 15));
		setLayout(new BorderLayout());
		setOpaque(false);
		// final JTextField twitterUser = new JTextField(TWITTER_USER_DEFAULT);
		// JTextField twitterPass = new JTextField(TWITTER_PASSWORD_DEFAULT);
		final JTextField twitterkeyword = new JTextField(TWITTER_KEYWORD_DEFAULT);
		final JCheckBox twitterEnabled = new JCheckBox("");
		twitterEnabled.setEnabled(false);

		twitterEnabled.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (twitterEnabled.isSelected()) {
					Activator.getActivator().startNewTwitterPoll(twitterkeyword.getText());
					twitterkeyword.setEditable(false);
				} else {
					Activator.getActivator().stopTwitterPoll();
					twitterkeyword.setEditable(true);
				}

			}
		});

		twitterkeyword.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (twitterkeyword.getText().isEmpty()) {
					twitterkeyword.setText(TWITTER_KEYWORD_DEFAULT);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				twitterkeyword.selectAll();
			}
		});

		twitterkeyword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					twitterEnabled.setSelected(true);
				}
			}
		});
		twitterkeyword.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) {
				boolean editable = !twitterkeyword.getText().isEmpty()
						&& !twitterkeyword.getText().equals(TWITTER_KEYWORD_DEFAULT);
				twitterEnabled.setEnabled(editable);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				boolean editable = !twitterkeyword.getText().isEmpty()
						&& !twitterkeyword.getText().equals(TWITTER_KEYWORD_DEFAULT);
				twitterEnabled.setEnabled(editable);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				boolean editable = !twitterkeyword.getText().isEmpty()
						&& !twitterkeyword.getText().equals(TWITTER_KEYWORD_DEFAULT);
				twitterEnabled.setEnabled(editable);
			}
		});

		// add(twitterUser);
		// add(twitterPass);
		add(twitterkeyword, BorderLayout.CENTER);
		add(twitterEnabled, BorderLayout.EAST);
	}

	private static final long serialVersionUID = 1L;
}
