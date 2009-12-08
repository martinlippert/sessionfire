package com.sessionfive.core.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.media.opengl.GLCanvas;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.explodingpixels.macwidgets.HudWidgetFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.PresentationChangedEvent;
import com.sessionfive.core.PresentationChangedListener;

public class CentralControlPaletteUI {

	private final CentralControlPalette centralControlPalette;
	private final Presentation presentation;

	private TranslucentPalette window;
	private JButton choosePresentationButton;
	private JButton savePresentationButton;
	private JButton startPresentationButton;
	private JButton helpButton;
	private JComboBox layoutChoice;
	private JComboBox animationChoice;
	private JTextField layerText;
	private final GLCanvas canvas;

	private JSlider xRotationSlider;
	private JSlider yRotationSlider;
	private JSlider zRotationSlider;

	private List<TranslucentPalette> extensionPalettes;

	private JPanel subContentPane;

	private boolean helpshown = false;
	private boolean inChange;

	private Collection<HelpWindow> helpWindows;
	private DefaultComboBoxModel animationModel;
	private DefaultComboBoxModel layoutModel;

	public CentralControlPaletteUI(CentralControlPalette centralControlPalette,
			Presentation presentation, GLCanvas canvas) {
		this.centralControlPalette = centralControlPalette;
		this.presentation = presentation;
		this.canvas = canvas;

		Window windowAncestor = SwingUtilities.getWindowAncestor(canvas);
		window = new TranslucentPalette("Sessionfire - Central Control", false, windowAncestor);
		initComponents();
		window.pack();
		window.setLocation(100, 100);
		initExtensions();

		helpWindows = new HashSet<HelpWindow>();
		presentation.addPresentationChangedListener(new PresentationChangedListener() {
			@Override
			public void presentationChanged(PresentationChangedEvent event) {
				if (!inChange) {
					updateControls();
				}
			}
		});

		updateControls();
	}

	public void show() {
		window.showPalette();

		TranslucentPalette previousWindow = window;
		for (TranslucentPalette palette : extensionPalettes) {
			palette.setLocation(previousWindow.getLocationOnScreen().x, previousWindow
					.getLocationOnScreen().y
					+ previousWindow.getSize().height + 5);
			palette.setSize(previousWindow.getSize().width, palette.getSize().height);
			palette.showPalette();
			previousWindow = palette;
		}
	}

	public void setStatus(String status) {
		window.setStatus(status);
	}

	private void initComponents() {
		JComponent contentPane = (JComponent) window.getEmbeddedContentPane();
		contentPane.setLayout(new BorderLayout());

		FormLayout layout = new FormLayout(
				"fill:pref:grow", // columns
				"pref, 3dlu, pref, 6dlu, pref, 1dlu, pref, 6dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, pref, 0dlu, pref, 0dlu, pref, 0dlu, pref"); // rows

		CellConstraints cc = new CellConstraints();
		subContentPane = new JPanel(layout);
		subContentPane.setOpaque(false);

		subContentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.add(subContentPane, BorderLayout.NORTH);

		choosePresentationButton = HudWidgetFactory.createHudButton("Choose Presentation...");
		choosePresentationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.choosePresentation(canvas, (Layouter) layoutChoice
						.getSelectedItem(), (AnimationFactory) animationChoice.getSelectedItem());

				centralControlPalette.setRotation(xRotationSlider.getValue(), yRotationSlider
						.getValue(), zRotationSlider.getValue());
			}
		});
		subContentPane.add(choosePresentationButton, cc.xy(1, 1));

		savePresentationButton = HudWidgetFactory.createHudButton("Save Presentation");
		savePresentationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.savePresentation((Layouter) layoutChoice.getSelectedItem(),
						(AnimationFactory) animationChoice.getSelectedItem());
			}
		});
		savePresentationButton.setEnabled(false);
		subContentPane.add(savePresentationButton, cc.xy(1, 3));

		startPresentationButton = HudWidgetFactory.createHudButton("Start Presentation");
		startPresentationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.startPresentation();
			}
		});
		subContentPane.add(startPresentationButton, cc.xy(1, 5));
		// subContentPane.add(HelpLabelFactory.createHelpLabel("Press ESC or F11 to switch back"),
		// cc.xy(1, 5));

		layoutModel = new DefaultComboBoxModel();
		Layouter[] allLayouter = centralControlPalette.getLayouter();
		for (Layouter layouter : allLayouter) {
			layoutModel.addElement(layouter);
		}
		layoutChoice = HudWidgetFactory.createHudComboBox(layoutModel);
		layoutChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedLayouter = layoutChoice.getSelectedItem();
				if (selectedLayouter != null) {
					inChange = true;
					centralControlPalette.changeLayout((Layouter) selectedLayouter);
					inChange = false;
				}
			}
		});
		subContentPane.add(layoutChoice, cc.xy(1, 9));

		animationModel = new DefaultComboBoxModel();
		AnimationFactory[] animationFactories = centralControlPalette.getAnimators();
		for (AnimationFactory animationFactory : animationFactories) {
			animationModel.addElement(animationFactory);
		}
		animationChoice = HudWidgetFactory.createHudComboBox(animationModel);
		animationChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedAnimation = animationChoice.getSelectedItem();
				if (selectedAnimation != null) {
					inChange = true;
					centralControlPalette.changeAnimation((AnimationFactory) selectedAnimation);
					inChange = false;
				}
			}
		});
		// Workaround to prevent Classcast Exception in apple laf
		animationChoice.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				char keyChar = e.getKeyChar();
				if (keyChar == ' ') {
					e.consume();
				}
			}
		});
		subContentPane.add(animationChoice, cc.xy(1, 11));

		JButton backgroundChooser = HudWidgetFactory.createHudButton("Choose Background Color...");
		backgroundChooser.setMaximumSize(new Dimension(10, 5));
		subContentPane.add(backgroundChooser, cc.xy(1, 13));
		backgroundChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseBackground();
			}
		});

		layerText = HudWidgetFactory.createHudTextField("");
		subContentPane.add(layerText, cc.xy(1, 15));
		layerText.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				setLayerText();
			}

			public void insertUpdate(DocumentEvent e) {
				setLayerText();
			}

			public void changedUpdate(DocumentEvent e) {
				setLayerText();
			}
		});

		xRotationSlider = new JSlider(0, 360, 0);
		yRotationSlider = new JSlider(0, 360, 0);
		zRotationSlider = new JSlider(0, 360, 0);
		subContentPane.add(xRotationSlider, cc.xy(1, 17));
		subContentPane.add(yRotationSlider, cc.xy(1, 19));
		subContentPane.add(zRotationSlider, cc.xy(1, 21));

		ChangeListener rotationSliderListener = new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				inChange = true;
				centralControlPalette.setRotation(xRotationSlider.getValue(), yRotationSlider
						.getValue(), zRotationSlider.getValue());
				inChange = false;
			}
		};

		xRotationSlider.addChangeListener(rotationSliderListener);
		yRotationSlider.addChangeListener(rotationSliderListener);
		zRotationSlider.addChangeListener(rotationSliderListener);

		helpButton = HudWidgetFactory.createHudButton("?");
		subContentPane.add(helpButton, cc.xy(1, 23));

		helpButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!helpshown) {
					showhelp();
				}
			}
		});

		Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
			@Override
			public void eventDispatched(AWTEvent event) {
				if (event instanceof MouseEvent) {
					MouseEvent mevent = (MouseEvent) event;
					if (mevent.getClickCount() > 0 && helpshown) {
						hidehelp();
						mevent.consume();
					}

				}
			}
		}, AWTEvent.MOUSE_EVENT_MASK);

	}

	protected void setLayerText() {
		inChange = true;
		centralControlPalette.setLayerText(layerText.getText());
		inChange = false;
	}

	protected void initExtensions() {
		extensionPalettes = new ArrayList<TranslucentPalette>();

		PanelExtension[] extensions = centralControlPalette.getExtensionPanels();

		for (PanelExtension panelExtension : extensions) {
			JPanel panelToEmbed = panelExtension.getPanel();
			if (panelToEmbed != null) {
				TranslucentPalette palette = new TranslucentPalette(panelExtension.getName(),
						false, (Window) window.getParent());
				JComponent contentPane = (JComponent) palette.getEmbeddedContentPane();
				contentPane.setLayout(new BorderLayout());

				contentPane.add(panelToEmbed, BorderLayout.NORTH);

				palette.pack();
				extensionPalettes.add(palette);
			}
		}
	}

	private void showhelp() {
		helpWindows.add(new HelpWindow(choosePresentationButton, HelpWindowPosition.ABOVE,
				"Select your presentation as a set or a folder of images",
				"The images are sorted by date & filename"));
		helpWindows.add(new HelpWindow(startPresentationButton, HelpWindowPosition.BELOW,
				"Press to start your presentation", "and press ESC or F11 to switch back"));
		helpWindows.add(new HelpWindow(savePresentationButton, HelpWindowPosition.CENTER_NO_ARROW,
				"Saves your presentation settings"));
		helpWindows.add(new HelpWindow(animationChoice, HelpWindowPosition.BELOW,
				"Use these controls to select an animation", "and a layout of your shapes"));
		helpWindows.add(new HelpWindow(layerText, HelpWindowPosition.CENTER_NO_ARROW,
				"Change the layer text"));
		helpWindows.add(new HelpWindow(yRotationSlider, HelpWindowPosition.ABOVE,
				"Use these sliders to control", "the X,Y and Z rotation of your shapes"));
		helpWindows.add(new HelpWindow(helpButton, HelpWindowPosition.CENTER_NO_ARROW,
				"Navigation:", "Next shape: Right-Arrow, Down-Arrow, Page-Down",
				"Previous shape: Left-Arrow, Up-Arrow, Page-Up",
				"First shape: Meta-Home, Meta-Up-Arrow", 
				"Last shape: Meta-End, Meta-Down-Arrow"));
		helpshown = true;
		// helpWindow.showHoverWindow();
	}

	private void hidehelp() {
		for (HelpWindow window : helpWindows) {
			window.hideHoverWindow(new Runnable() {
				@Override
				public void run() {
					helpshown = false;
				}
			});
			window = null;
		}
		helpWindows = new HashSet<HelpWindow>();
	}

	protected void chooseBackground() {
		Color newColor = JColorChooser.showDialog(window, "Choose Background Color",
				centralControlPalette.getBackgroundColor());
		if (newColor != null) {
			centralControlPalette.setBackgroundColor(newColor);
		}
	}

	public void updateControls() {
		layoutChoice.setSelectedItem(presentation.getDefaultLayouter());
		animationChoice.setSelectedItem(presentation.getDefaultAnimation());

		layerText.setText(presentation.getLayerText());
	}

}
