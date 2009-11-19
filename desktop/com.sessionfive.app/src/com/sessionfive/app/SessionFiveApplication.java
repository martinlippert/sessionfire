package com.sessionfive.app;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javax.imageio.ImageIO;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLException;
import javax.media.opengl.GLPbuffer;
import javax.swing.ImageIcon;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.ui.CentralControlPalette;
import com.sessionfive.core.ui.CentralControlPaletteUI;
import com.sun.opengl.util.Screenshot;

public class SessionFiveApplication implements IApplication {

	private Frame fullScreenFrame;

	private GLCapabilities caps;
	private GLCanvas canvas;

	private KeyListener keyListener;
	private Display display;
	private Frame frame;

	private AnimationController animationController;
	private Presentation presentation;

	private static SessionFiveApplication application;
	private static String generalStatus = "";

	private CentralControlPalette centralControlPalette;
	private CentralControlPaletteUI centralControlPaletteUI;
	private StringBuffer goToKeyframeDirectlyBuffer;

	private DisplayRepaintManager displayRepaintManager;

	private DisplayRepaintManager fullScreenDisplayRepaintManager;

	public SessionFiveApplication() {
		application = this;
		goToKeyframeDirectlyBuffer = new StringBuffer();
	}

	public static SessionFiveApplication getInstance() {
		return application;
	}

	public AnimationController getAnimationController() {
		return animationController;
	}

	public Presentation getPresentation() {
		return presentation;
	}

	public Object start(IApplicationContext context) throws Exception {
		frame = new Frame("Sessionfire - A New Kind of Presentation Tool");
		frame.setIconImage(new ImageIcon(this.getClass()
				.getResource("sf16.png")).getImage());

		caps = new GLCapabilities();
		caps.setSampleBuffers(true);
		caps.setNumSamples(2);

		canvas = new GLCanvas(caps, null, null, null);

		presentation = new Presentation();
		display = new Display(presentation);
		animationController = new AnimationController();
		
		displayRepaintManager = new DisplayRepaintManager(display, presentation, canvas);

		canvas.addGLEventListener(display);

		keyListener = new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_HOME
						|| (e.getKeyCode() == KeyEvent.VK_UP && (e
								.getModifiers() & KeyEvent.META_MASK) != 0)) {
					animationController.goTo(0);
				} else if (e.getKeyCode() == KeyEvent.VK_END
					|| (e.getKeyCode() == KeyEvent.VK_DOWN && (e
							.getModifiers() & KeyEvent.META_MASK) != 0)) {
					animationController.goTo(animationController
							.getNumberOfKeyFrames() - 1);
				} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					goToKeyframeDirectly();
				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN
						|| e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
					animationController.forward();
				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					animationController.backward();
				} else if (e.getKeyCode() == KeyEvent.VK_F11
						|| e.getKeyChar() == 'f') {
					switchFullScreen();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if(fullScreenFrame != null) {						
						switchFullScreen();
					}
				}

				if (e.getKeyCode() >= KeyEvent.VK_0
						&& e.getKeyCode() <= KeyEvent.VK_9) {
					goToKeyframeDirectlyBuffer.append(e.getKeyChar());
				} else {
					goToKeyframeDirectlyBuffer.delete(0,
							goToKeyframeDirectlyBuffer.length());
				}
			}
		};

		canvas.addKeyListener(keyListener);
		frame.addKeyListener(keyListener);

		frame.setLayout(new BorderLayout());

		frame.add(canvas, BorderLayout.CENTER);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					public void run() {
						displayRepaintManager.dispose();
						System.exit(0);
					}
				}).start();
			}
		});

		frame.setVisible(true);
		animationController.init(presentation, display);

		centralControlPalette = new CentralControlPalette(presentation,
				animationController);
		centralControlPaletteUI = new CentralControlPaletteUI(
				centralControlPalette, canvas);
		centralControlPaletteUI.show();
		centralControlPaletteUI.setStatus(generalStatus);

		return EXIT_OK;
	}

	protected void goToKeyframeDirectly() {
		try {
			int keyframeNo = Integer.parseInt(goToKeyframeDirectlyBuffer
					.toString());
			if (keyframeNo >= 0) {
				keyframeNo = Math.min(keyframeNo, animationController
						.getNumberOfKeyFrames());
				animationController.goTo(keyframeNo - 1);
			}
		} catch (NumberFormatException e) {
		}
	}

	public void switchFullScreen() {
		System.out.println("switch to full screen");

		if (fullScreenFrame == null) {
			fullScreenFrame = new Frame("Session Five");
			fullScreenFrame.setFocusable(true);
			fullScreenFrame.setResizable(false);
			fullScreenFrame.setSize(600, 500);
			fullScreenFrame.setUndecorated(true);
			fullScreenFrame.setLayout(new BorderLayout());

			final GLCanvas fullScreenCanvas = new GLCanvas(caps, null, canvas
					.getContext(), null);
			fullScreenCanvas.addGLEventListener(display);
			fullScreenCanvas.setFocusable(true);
			fullScreenDisplayRepaintManager = new DisplayRepaintManager(display, presentation, fullScreenCanvas);
			fullScreenFrame.add(fullScreenCanvas, BorderLayout.CENTER);

			fullScreenFrame.addKeyListener(keyListener);
			fullScreenCanvas.addKeyListener(keyListener);
			fullScreenFrame.addWindowListener(new WindowAdapter() {
				public void windowOpened(WindowEvent we) {
					fullScreenFrame.requestFocus();
				}
			});

			GraphicsDevice screenDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			screenDevice.setFullScreenWindow(fullScreenFrame);
		} else {
			GraphicsDevice screenDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();

			screenDevice.setFullScreenWindow(null);
			fullScreenFrame.setVisible(false);
			fullScreenFrame.dispose();
			fullScreenFrame = null;
			
			fullScreenDisplayRepaintManager.dispose();
			fullScreenDisplayRepaintManager = null;
			frame.toFront();
		}
	}

	public void stop() {
	}

	public static void setStatus(String status) {
		generalStatus = status;
		SessionFiveApplication instance = getInstance();
		if (instance != null) {
			instance.centralControlPaletteUI.setStatus(status);
		}
	}

	public byte[] getKeyFrame(int parsedNumber) {
		GLCapabilities caps = new GLCapabilities();
		caps.setSampleBuffers(true);
		caps.setNumSamples(2);
		caps.setDoubleBuffered(false);

		if (!GLDrawableFactory.getFactory().canCreateGLPbuffer()) {
			throw new GLException(
					"Pbuffers not supported with this graphics card");
		}
		GLPbuffer pbuffer = GLDrawableFactory.getFactory().createGLPbuffer(
				caps, null, 512, 512, canvas.getContext());

		Display offscreenDisplay = new Display(presentation);

		boolean alpha;
		if (parsedNumber >= 0
				&& parsedNumber < presentation.getAnimationCount()) {
			Animation animation = presentation.getAnimation(parsedNumber);
			animation.directlyGoTo(offscreenDisplay);
			alpha = true;
		} else {
			offscreenDisplay.setCamera(presentation.getStartCamera());
			alpha = false;
		}

		pbuffer.addGLEventListener(offscreenDisplay);

		KeyFrameCreator keyFrameCreator = new KeyFrameCreator(512, 512, alpha);
		FutureTask<byte[]> futureTask = new FutureTask<byte[]>(keyFrameCreator);
		offscreenDisplay.executeInDisplay(futureTask);
		pbuffer.display();

		byte[] result = null;
		try {
			result = futureTask.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		pbuffer.removeGLEventListener(offscreenDisplay);
		pbuffer.destroy();

		return result;
	}

	protected static class KeyFrameCreator implements Callable<byte[]> {

		private final int width;
		private final int height;
		private final boolean alpha;

		public KeyFrameCreator(int width, int height, boolean alpha) {
			this.width = width;
			this.height = height;
			this.alpha = alpha;
		}

		public byte[] call() throws Exception {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			try {
				BufferedImage image = Screenshot.readToBufferedImage(0, 0,
						width, height, alpha);
				if (!ImageIO.write(image, "png", bos)) {
					throw new IOException("Unsupported file format png");
				}
				return bos.toByteArray();
			} catch (GLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	public GLContext getGLContext() {
		return canvas.getContext();
	}

}
