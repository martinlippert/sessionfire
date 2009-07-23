package com.sessionfive.app;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.animation.CameraSetting;
import com.sessionfive.core.Presentation;
import com.sun.opengl.util.Animator;

public class SessionFiveApplication implements IApplication {

	public static AnimationController animationControllerInstance;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		final Frame frame = new Frame(
				"Session Five - A New Kind of Presentation Tool");

		GLProfile glp = GLProfile.getDefault();
		final GLCapabilities caps = new GLCapabilities(glp);
		caps.setSampleBuffers(true);
		caps.setNumSamples(4);
		final GLCanvas canvas = new GLCanvas(caps);

		final Presentation presentation = new Presentation();

		final CameraSetting startCameraSetting = new CameraSetting(-80f, -3.1f,
				90f, -20f, -3.1f, 0f, 0f, 1f, 0f);

		final Display display = new Display(presentation, startCameraSetting);
		final AnimationController animationController = new AnimationController();
		animationControllerInstance = animationController;
		
		canvas.addGLEventListener(display);

		final Frame fullScreenFrame = new Frame();
		fullScreenFrame.setUndecorated(true);
		fullScreenFrame.setSize(600, 500);
		fullScreenFrame.setLayout(new BorderLayout());

		KeyListener keyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				System.out
						.println("keyPressed()");

				if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN
						|| e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
					animationController.forward();

				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					animationController.backward();

				} else if (e.getKeyCode() == KeyEvent.VK_F10 || e.getKeyChar() == 'f') {
					if (!fullScreenFrame.isVisible()) {
						frame.remove(canvas);

						presentation.reset();

						fullScreenFrame.add(canvas, BorderLayout.CENTER);
						fullScreenFrame.addKeyListener(this);

						GraphicsDevice screenDevice = GraphicsEnvironment
								.getLocalGraphicsEnvironment()
								.getDefaultScreenDevice();
						GraphicsConfiguration configuration = screenDevice
								.getDefaultConfiguration();
						Rectangle bounds = configuration.getBounds();
						fullScreenFrame.setSize(bounds.width, bounds.height);
						fullScreenFrame.setLocation(0, 0);

						fullScreenFrame.setVisible(true);
						fullScreenFrame.validate();
						fullScreenFrame.toFront();

					} else {

						fullScreenFrame.remove(canvas);

						presentation.reset();

						fullScreenFrame.removeKeyListener(this);
						fullScreenFrame.setVisible(false);

						frame.add(canvas, BorderLayout.CENTER);
						frame.validate();
						frame.toFront();
					}
				} else if (e.getKeyCode() == KeyEvent.VK_F12) {
					display.doScreenshot(canvas.getWidth(), canvas.getHeight());
				}
			}
		};

		canvas.addKeyListener(keyListener);
		frame.addKeyListener(keyListener);
		
		frame.setLayout(new BorderLayout());

		frame.add(canvas, BorderLayout.CENTER);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		final Animator animator = new Animator(canvas);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});

		frame.setVisible(true);
		animator.start();
		animationController.init(presentation, display);

		new PresentationSelector(presentation).selectPresentation(canvas);
		return EXIT_OK;
	}

	@Override
	public void stop() {
	}

}
