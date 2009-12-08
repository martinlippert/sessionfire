package com.sessionfive.core.ui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class HelpWindow extends JWindow {

	private static final long serialVersionUID = 1L;
	private Timer fadeInTimer;
	private Timer fadeOutTimer;
	private int currOpacity;
	private boolean toFade = true;
	private final HelpWindowPosition position;

	public HelpWindow(Component markedWidget, HelpWindowPosition pos, String... labels) {
		super(SwingUtilities.getWindowAncestor(markedWidget));
		this.position = pos;
		setFocusable(false);
		setFocusableWindowState(false);
		setBackground(new Color(0, 0, 0, 0));

		JPanel contentPanel = getContentPanel();
		setContentPane(contentPanel);

		String spaces = position.withArrow() ? "    " : "  ";
		for (String string : labels) {
			JLabel line = new JLabel(spaces + string);
			line.setForeground(Color.white);
			contentPanel.add(line);
		}

		pack();
		int width = position.withArrow() ? getWidth() + 5 : getWidth() + 8;
		setSize(width, getHeight() + 10);

		Point pt = markedWidget.getLocationOnScreen();
		Dimension widgetSize = markedWidget.getSize();
		pt.x += widgetSize.width + 2;
		if (pos == HelpWindowPosition.ABOVE) {
			pt.y += widgetSize.height - 36;
		} else {
			pt.y += widgetSize.height - 20;
		}
		// Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		// if (pt.x + getWidth() > dim.width) {
		// pt.x = dim.width - getWidth();
		// }
		setLocation(pt);

		showHoverWindow();
	}

	private JPanel getContentPanel() {

		JPanel contentPanel = new JPanel() {
			private static final long serialVersionUID = 7889351711895834584L;

			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g.create();

				int width = getSize().width;
				int height = getSize().height;
				Color backgroundColor = new Color(64, 64, 64);
				Color borderColor = new Color(200, 200, 200);

				GraphicsConfiguration gc = g2d.getDeviceConfiguration();
				BufferedImage img = gc.createCompatibleImage(width, height,
						Transparency.TRANSLUCENT);
				Graphics2D g2 = img.createGraphics();

				g2.setComposite(AlphaComposite.Clear);
				g2.fillRect(0, 0, width, height);

				g2.setComposite(AlphaComposite.Src);
				g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);

				g2.setColor(backgroundColor);
				int x;
				if (position.withArrow()) {
					x = 9;
				} else {
					x = 4;
				}
				g2.fillRoundRect(x, 0, width - 10, height - 10, 20, 20);
				g2.setColor(borderColor);
				g2.drawRoundRect(x, 0, width - 10, height - 10, 20, 20);

				// arrow
				if (position.withArrow()) {
					int xpoints[] = { 0, x, x };
					int ypointsBackground[] = null;
					int ypointsBorder[] = null;
					if (position == HelpWindowPosition.ABOVE) {
						ypointsBackground = new int[] { 24, 16, 24 };
						ypointsBorder = new int[] { 24, 16, 24 };
					} else if (position == HelpWindowPosition.BELOW) {
						ypointsBackground = new int[] { 8, 8, 17 };
						ypointsBorder = new int[] { 7, 7, 17 };
					}
					g2.setColor(borderColor);
					g2.drawPolygon(xpoints, ypointsBorder, 3);

					int xpoints2[] = { 1, 10, 10 };
					g2.setColor(backgroundColor);
					g2.fillPolygon(xpoints2, ypointsBackground, 3);
				}

				g2.dispose();

				g2d.drawImage(img, 0, 0, this);
				g2d.dispose();
			}
		};

		contentPanel.setFocusable(false);
		contentPanel.setOpaque(false);
		contentPanel.setDoubleBuffered(false);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

		return contentPanel;
	}

	public void showHoverWindow() {

		if (this.fadeInTimer != null && this.fadeInTimer.isRunning()) {
			return;
		}

		if (this.fadeOutTimer != null && this.fadeOutTimer.isRunning()) {
			this.fadeOutTimer.stop();
		}

		if (isVisible()) {
			return;
		}

		if (this.toFade) {
			// mark the popup with 0% opacity
			this.currOpacity = 0;
			com.sun.awt.AWTUtilities.setWindowOpacity(this, 0.0f);
		}

		setVisible(true);
		// pack();

		com.sun.awt.AWTUtilities.setWindowOpaque(this, false);

		if (this.toFade) {
			// start fading in
			this.fadeInTimer = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currOpacity += 18;
					if (currOpacity <= 99) {
						com.sun.awt.AWTUtilities.setWindowOpacity(HelpWindow.this,
								currOpacity / 100.0f);
						getContentPane().repaint();
					} else {
						currOpacity = 90;
						fadeInTimer.stop();
					}
				}
			});
			this.fadeInTimer.setRepeats(true);
			this.fadeInTimer.start();
		}
	}

	public void hideHoverWindow(final Runnable finishedCallback) {
		if (this.fadeOutTimer != null && this.fadeOutTimer.isRunning()) {
			return;
		}

		if (this.fadeInTimer != null && this.fadeInTimer.isRunning()) {
			this.fadeInTimer.stop();
		}

		if (this.toFade) {
			// cancel fade-in if it's running.
			if (this.fadeInTimer.isRunning())
				this.fadeInTimer.stop();

			// start fading out
			this.fadeOutTimer = new Timer(100, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					currOpacity -= 18;
					if (currOpacity >= 0) {
						com.sun.awt.AWTUtilities.setWindowOpacity(HelpWindow.this,
								currOpacity / 100.0f);
						getContentPane().repaint();
					} else {
						fadeOutTimer.stop();
						setVisible(false);

						getParent().repaint();

						dispose();
						currOpacity = 0;

						finishedCallback.run();
					}
				}
			});
			this.fadeOutTimer.setRepeats(true);
			this.fadeOutTimer.start();
		} else {
			setVisible(false);
		}
	}

}
