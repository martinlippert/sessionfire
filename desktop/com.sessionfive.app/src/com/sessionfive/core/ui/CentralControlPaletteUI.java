package com.sessionfive.core.ui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.dnd.DropTarget;
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
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
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
import com.sessionfive.app.DropListener;
import com.sessionfive.app.SelectionService;
import com.sessionfive.core.AnimationStyle;
import com.sessionfive.core.LayerType;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.PresentationChangedEvent;
import com.sessionfive.core.PresentationChangedListener;
import com.sessionfive.core.Shape;

public class CentralControlPaletteUI {

	private final CentralControlPalette centralControlPalette;
	private final Presentation presentation;
	private final SelectionService selectionService;

	private TranslucentPalette window;
	private JButton choosePresentationButton;
	private JButton savePresentationButton;
	private JButton startPresentationButton;
	private JButton helpButton;
	private JComboBox layoutChoice;
	private JComboBox animationChoice;
	private JComboBox animationPathChoice;

	private JTextField layerText;
	private final GLCanvas canvas;

	private JSlider spaceRotationSlider;
	private JCheckBox reflectionEnabledBox;
	private JSlider focusScaleSlider;

	private List<TranslucentPalette> extensionPalettes;

	private JPanel subContentPane;

	private boolean helpshown = false;
	private boolean inChange;

	private Collection<HelpWindow> helpWindows;
	private DefaultComboBoxModel animationStyleModel;
	private DefaultComboBoxModel animationPathLayouterModel;
	private DefaultComboBoxModel layoutModel;
	private JCheckBox expertSettingsBox;
	private boolean expertSettingsVisible;
	private JPanel rotationViewPanel;
	private JLabel spaceRotationLabel;
	private JLabel focusScaleLabel;

	public CentralControlPaletteUI(CentralControlPalette centralControlPalette,
			Presentation presentation, SelectionService selectionService,
			GLCanvas canvas) {
		this.centralControlPalette = centralControlPalette;
		this.presentation = presentation;
		this.selectionService = selectionService;
		this.canvas = canvas;
		this.expertSettingsVisible = false;

		Window windowAncestor = SwingUtilities.getWindowAncestor(canvas);
		window = new TranslucentPalette("Sessionfire - Central Control", false,
				windowAncestor);
		initComponents();
		
		DropListener dropListener = new DropListener(centralControlPalette, canvas);
		new DropTarget(windowAncestor, dropListener);
		
		window.pack();
		window.setLocation(100, 100);
		initExtensions();

		helpWindows = new HashSet<HelpWindow>();
		presentation
				.addPresentationChangedListener(new PresentationChangedListener() {
					@Override
					public void presentationChanged(
							PresentationChangedEvent event) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								updateControls();
							}
						});
					}
				});

		setExpertSettingsVisible(this.expertSettingsVisible);
		updateControls();
	}

	public void show() {
		window.showPalette();
		
		positionExtensionPalettes();
		for (TranslucentPalette palette : extensionPalettes) {
			palette.showPalette();
		}
	}

	protected void positionExtensionPalettes() {
		int x = window.getLocationOnScreen().x;
		int y = window.getLocationOnScreen().y;
		int width = window.getSize().width;
		int height = window.getSize().height;
		
		for (TranslucentPalette palette : extensionPalettes) {
			y = y + height + 5;
			height = palette.getSize().height;

			palette.setLocation(x, y);
			palette.setSize(width, height);
		}
	}

	public void setStatus(String status) {
		window.setStatus(status);
	}

	private void initComponents() {
		JComponent contentPane = (JComponent) window.getEmbeddedContentPane();
		contentPane.setLayout(new BorderLayout());

		FormLayout layout = new FormLayout(
				"10dlu, fill:pref:grow", // columns
				"pref, 3dlu, pref, 6dlu, pref, 1dlu, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 6dlu, pref, 6dlu, pref, 6dlu, pref, 3dlu, pref, 3dlu, pref, 0dlu, pref, 0dlu, pref, 6dlu, pref"); // rows

		CellConstraints cc = new CellConstraints();
		subContentPane = new JPanel(layout);
		subContentPane.setOpaque(false);
		subContentPane.setDoubleBuffered(false);

		subContentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.add(subContentPane, BorderLayout.NORTH);

		choosePresentationButton = HudWidgetFactory
				.createHudButton("Choose Presentation...");
		choosePresentationButton.setMnemonic(KeyEvent.VK_O);
		choosePresentationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.choosePresentation(canvas);
			}
		});
		subContentPane.add(choosePresentationButton, cc.xyw(1, 1, 2));

		savePresentationButton = HudWidgetFactory
				.createHudButton("Save Presentation");
		savePresentationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.savePresentation(canvas);
			}
		});
		savePresentationButton.setEnabled(false);
		savePresentationButton.setMnemonic(KeyEvent.VK_S);
		subContentPane.add(savePresentationButton, cc.xyw(1, 3, 2));

		startPresentationButton = HudWidgetFactory
				.createHudButton("Start Presentation");
		startPresentationButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centralControlPalette.startPresentation();
			}
		});
		subContentPane.add(startPresentationButton, cc.xyw(1, 5, 2));

		layoutModel = new DefaultComboBoxModel();
		Layouter[] allLayouter = centralControlPalette.getLayouter();
		for (Layouter layouter : allLayouter) {
			layoutModel.addElement(layouter);
		}
		layoutChoice = HudWidgetFactory.createHudComboBox(layoutModel);
		layoutChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedLayouter = layoutChoice.getSelectedItem();
				Object selectedAnimation = animationChoice.getSelectedItem();
				if (selectedLayouter != null && selectedAnimation != null
						&& !inChange) {
					centralControlPalette.changeLayout(
							(Layouter) selectedLayouter,
							(AnimationStyle) selectedAnimation);
				}
			}
		});
		subContentPane.add(layoutChoice, cc.xyw(1, 9, 2));

		animationStyleModel = new DefaultComboBoxModel();
		AnimationStyle[] animationStyles = centralControlPalette
				.getAnimationStyles();
		for (AnimationStyle animationFactory : animationStyles) {
			animationStyleModel.addElement(animationFactory);
		}
		animationChoice = HudWidgetFactory
				.createHudComboBox(animationStyleModel);
		animationChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedAnimation = animationChoice.getSelectedItem();
				if (selectedAnimation != null && !inChange) {
					centralControlPalette
							.changeAnimationStyle((AnimationStyle) selectedAnimation);
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
		subContentPane.add(animationChoice, cc.xyw(1, 11, 2));

		animationPathLayouterModel = new DefaultComboBoxModel();
		AnimationPathLayouter[] allAnimationPathLayouter = centralControlPalette
				.getAnimationPathLayouter();
		for (AnimationPathLayouter animationPathLayouter : allAnimationPathLayouter) {
			animationPathLayouterModel.addElement(animationPathLayouter);
		}
		animationPathChoice = HudWidgetFactory
				.createHudComboBox(animationPathLayouterModel);
		animationPathChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object selectedPath = animationPathChoice.getSelectedItem();
				Object selectedStyle = animationChoice.getSelectedItem();
				if (selectedPath != null && selectedStyle != null && !inChange) {
					centralControlPalette.changeAnimationPath(
							(AnimationPathLayouter) selectedPath,
							(AnimationStyle) selectedStyle);
				}
			}
		});
		subContentPane.add(animationPathChoice, cc.xyw(1, 13, 2));

		JButton backgroundChooser = HudWidgetFactory
				.createHudButton("Choose Background Color...");
		backgroundChooser.setMaximumSize(new Dimension(10, 5));
		subContentPane.add(backgroundChooser, cc.xyw(1, 15, 2));
		backgroundChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseBackground();
			}
		});

		layerText = HudWidgetFactory.createHudTextField("");
		subContentPane.add(layerText, cc.xyw(1, 17, 2));
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
		
		reflectionEnabledBox = HudWidgetFactory.createHudCheckBox("Reflection");
		reflectionEnabledBox.setSelected(true);
		reflectionEnabledBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!inChange) {
					centralControlPalette
					.setReflectionEnabled(reflectionEnabledBox
							.isSelected());
				}
			}
		});
		subContentPane.add(reflectionEnabledBox, cc.xyw(1, 19, 2));

		expertSettingsBox = HudWidgetFactory.createHudCheckBox("");
		expertSettingsBox.setSelected(expertSettingsVisible );
		expertSettingsBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleExpertSettings();
			}
		});
		subContentPane.add(expertSettingsBox, cc.xyw(1, 21, 2));

		RotationView rotationView = new RotationView(selectionService);
		rotationViewPanel = rotationView.createUI();
		subContentPane.add(rotationViewPanel, cc.xyw(1, 23, 2));
		
		spaceRotationSlider = new JSlider(1, 50, Presentation.DEFAULT_SPACE);
		spaceRotationLabel = HudWidgetFactory.createHudLabel("||");
		subContentPane.add(spaceRotationLabel, cc.xy(1, 25));
		subContentPane.add(spaceRotationSlider, cc.xy(2, 25));

		spaceRotationSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!inChange) {
					Object selectedLayouter = layoutChoice.getSelectedItem();
					assert selectedLayouter != null;
					centralControlPalette.setSpace(spaceRotationSlider
							.getValue(), (Layouter) selectedLayouter);
				}
			}
		});
		spaceRotationSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 2 && !inChange) {
					spaceRotationSlider.setValue(Presentation.DEFAULT_SPACE);
				}
			}
		});

		focusScaleSlider = new JSlider(1, 200, 100);
		focusScaleSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (!inChange) {
					centralControlPalette
							.setFocusScale((float) (200 - focusScaleSlider
									.getValue()) / 100);
				}
			}
		});
		focusScaleSlider.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getClickCount() == 2 && !inChange) {
					focusScaleSlider
							.setValue(200 - (int) (Presentation.DEFAULT_FOCUS_SCALE * 100));
				}
			}
		});
		focusScaleLabel = HudWidgetFactory.createHudLabel("<>");
		subContentPane.add(focusScaleLabel, cc.xy(1, 27));
		subContentPane.add(focusScaleSlider, cc.xy(2, 27));

		helpButton = HudWidgetFactory.createHudButton("?");
		subContentPane.add(helpButton, cc.xyw(1, 29, 2));

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
					if (mevent.getID() == MouseEvent.MOUSE_PRESSED && helpshown) {
						hidehelp();
						mevent.consume();
					}
				}
			}
		}, AWTEvent.MOUSE_EVENT_MASK);

	}

	protected void toggleExpertSettings() {
		this.expertSettingsVisible = !this.expertSettingsVisible;
		setExpertSettingsVisible(expertSettingsVisible);
		positionExtensionPalettes();
	}

	protected void setExpertSettingsVisible(boolean visible) {
		this.rotationViewPanel.setVisible(visible);
		this.spaceRotationSlider.setVisible(visible);
		this.spaceRotationLabel.setVisible(visible);
		this.focusScaleSlider.setVisible(visible);
		this.focusScaleLabel.setVisible(visible);
		
		String expertSettingsText = visible ? "Hide advanced settings" : "Show advanced settings";
		this.expertSettingsBox.setText(expertSettingsText);
		
		this.window.pack();
	}

	protected void setLayerText() {
		if (!inChange) {
			centralControlPalette.setLayerText(layerText.getText());
		}
	}

	protected void initExtensions() {
		extensionPalettes = new ArrayList<TranslucentPalette>();

		PanelExtension[] extensions = centralControlPalette
				.getExtensionPanels();

		for (PanelExtension panelExtension : extensions) {
			JPanel panelToEmbed = panelExtension.getView().createUI();
			if (panelToEmbed != null) {
				TranslucentPalette palette = new TranslucentPalette(
						panelExtension.getName(), false, (Window) window
								.getParent());
				JComponent contentPane = (JComponent) palette
						.getEmbeddedContentPane();
				contentPane.setLayout(new BorderLayout());

				contentPane.add(panelToEmbed, BorderLayout.NORTH);

				palette.pack();
				extensionPalettes.add(palette);
			}
		}
	}

	private void showHelpInExtensions() {
		for (TranslucentPalette panel : this.extensionPalettes) {
			JComponent contentPane = panel.getEmbeddedContentPane();
			Component component = contentPane.getComponent(0);
			if (component instanceof ShowsHelp) {
				((ShowsHelp) component).showhelp();
			}
		}
	}

	private void hideHelpInExtensions() {
		for (TranslucentPalette panel : this.extensionPalettes) {
			JComponent contentPane = panel.getEmbeddedContentPane();
			Component component = contentPane.getComponent(0);
			if (component instanceof ShowsHelp) {
				((ShowsHelp) component).hidehelp();
			}
		}
	}

	private void showhelp() {
		helpWindows.add(new HelpWindow(choosePresentationButton,
				HelpWindowPosition.ABOVE,
				"Select your presentation as a set or a folder of images",
				"The images are sorted by date & filename"));
		helpWindows.add(new HelpWindow(startPresentationButton,
				HelpWindowPosition.BELOW, "Press to start your presentation",
				"and press ESC or F11 to switch back"));
		helpWindows.add(new HelpWindow(savePresentationButton,
				HelpWindowPosition.CENTER_NO_ARROW,
				" Saves your presentation settings "));
		helpWindows.add(new HelpWindow(animationChoice,
				HelpWindowPosition.ABOVE,
				"Use these controls to select an animation",
				"and a layout of your shapes"));
		helpWindows.add(new HelpWindow(animationPathChoice,
				HelpWindowPosition.BELOW,
				"Choose the path you would like to walk",
				"through your shapes"));
		helpWindows.add(new HelpWindow(layerText,
				HelpWindowPosition.CENTER_NO_ARROW, " Change the layer text "));
		helpWindows.add(new HelpWindow(rotationViewPanel,
				HelpWindowPosition.BELOW, "Use these sliders to control",
		"the X,Y and Z rotation of your shapes"));
		helpWindows.add(new HelpWindow(spaceRotationSlider,
				HelpWindowPosition.ABOVE,
				"Change the spacing between your shapes",
				"Double click returns to the default"));
		helpWindows.add(new HelpWindow(focusScaleSlider,
				HelpWindowPosition.CENTER_NO_ARROW,
				"Change the scaling of focussed shapes",
				"Double click returns to the default"));
		helpWindows.add(new HelpWindow(helpButton,
				HelpWindowPosition.BELOW,
				"Navigation while presenting:",
				"Next shape: Right-Arrow, Down-Arrow, Page-Down",
				"Previous shape: Left-Arrow, Up-Arrow, Page-Up",
				"First shape: Meta-Home, Meta-Up-Arrow",
				"Last shape: Meta-End, Meta-Down-Arrow",
				"Number & enter: Jumps to the shape"));
		showHelpInExtensions();

		helpshown = true;
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

		hideHelpInExtensions();
	}

	protected void chooseBackground() {
		Color newColor = JColorChooser.showDialog(window,
				"Choose Background Color", presentation.getBackgroundColor());
		if (newColor != null) {
			centralControlPalette.setBackgroundColor(newColor);
		}
	}

	public void updateControls() {
		inChange = true;
		layoutChoice.setSelectedItem(presentation.getDefaultLayouter());
		animationChoice.setSelectedItem(presentation.getDefaultAnimationStyle());

		if (!layerText.getText().equals(presentation.getLayerText())) {
			layerText.setText(presentation.getLayerText());
		}

		spaceRotationSlider.setValue((int) presentation.getSpace());
		reflectionEnabledBox.setSelected(presentation
				.isDefaultReflectionEnabled());
		focusScaleSlider.setValue(200 - (int) (presentation
				.getDefaultFocusScale() * 100));

		List<Shape> shapes = presentation.getShapes(LayerType.CAMERA_ANIMATED);
		savePresentationButton.setEnabled(shapes.size() > 0);
		inChange = false;
	}

}
