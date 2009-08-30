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
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLDrawableFactory;
import javax.media.opengl.GLException;
import javax.media.opengl.GLPbuffer;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.swing.ImageIcon;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.sessionfive.animation.AnimationController;
import com.sessionfive.core.Animation;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.ui.CentralControlPalette;
import com.sessionfive.core.ui.CentralControlPaletteUI;
import com.sun.opengl.util.Animator;
import com.sun.opengl.util.awt.Screenshot;

public class SessionFiveApplication implements IApplication {

	private Frame fullScreenFrame;
	private Animator fullScreenAnimator;

	private GLCapabilities caps;
	private GLCanvas canvas;

	private KeyListener keyListener;
	private Animator animator;
	private Display display;
	private Frame frame;

	private AnimationController animationController;
	private Presentation presentation;

	private static SessionFiveApplication application;
	private static String generalStatus = "";

	private CentralControlPalette centralControlPalette;
	private CentralControlPaletteUI centralControlPaletteUI;
	
	public SessionFiveApplication() {
		application = this;
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

	@Override
	public Object start(IApplicationContext context) throws Exception {
		frame = new Frame("Sessionfire - A New Kind of Presentation Tool");
		frame.setIconImage(new ImageIcon(this.getClass().getResource("sf16.png")).getImage());

	    caps = new GLCapabilities(GLProfile.getDefault());
		caps.setSampleBuffers(true);
		caps.setNumSamples(2);

		canvas = new GLCanvas(caps, null, null, null);

		presentation = new Presentation();
		display = new Display(presentation);
		animationController = new AnimationController();

		canvas.addGLEventListener(display);
		animator = new Animator(canvas);

		keyListener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN
						|| e.getKeyCode() == KeyEvent.VK_DOWN
						|| e.getKeyCode() == KeyEvent.VK_RIGHT) {
					animationController.forward();

				} else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP
						|| e.getKeyCode() == KeyEvent.VK_UP
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					animationController.backward();

				} else if (e.getKeyCode() == KeyEvent.VK_F11 || e.getKeyChar() == 'f') {
					switchFullScreen();
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
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});

		frame.setVisible(true);
		animator.start();
		animationController.init(presentation, display);
		
		centralControlPalette = new CentralControlPalette(presentation, animationController);
		centralControlPaletteUI = new CentralControlPaletteUI(centralControlPalette, canvas);
		centralControlPaletteUI.show();
		centralControlPaletteUI.setStatus(generalStatus);

		return EXIT_OK;
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
			fullScreenFrame.add(fullScreenCanvas, BorderLayout.CENTER);
			fullScreenAnimator = new Animator(fullScreenCanvas);

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

			fullScreenAnimator.start();

			new Thread(new Runnable() {
				public void run() {
					animator.stop();
				}
			}).start();
		} else {
			new Thread(new Runnable() {
				public void run() {
					fullScreenAnimator.stop();
				}
			}).start();

			GraphicsDevice screenDevice = GraphicsEnvironment
					.getLocalGraphicsEnvironment().getDefaultScreenDevice();

			screenDevice.setFullScreenWindow(null);
			fullScreenFrame.setVisible(false);
			fullScreenFrame.dispose();
			fullScreenFrame = null;
			frame.toFront();
			animator.start();
		}
	}

	@Override
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
		if (!GLDrawableFactory.getFactory(caps.getGLProfile()).canCreateGLPbuffer()) {
			throw new GLException("Pbuffers not supported with this graphics card");
		}
		caps.setDoubleBuffered(false);
		GLPbuffer pbuffer = GLDrawableFactory.getFactory(caps.getGLProfile()).createGLPbuffer(caps,
				null,
				512, 512,
				canvas.getContext());
		
		Display offscreenDisplay = new Display(presentation);
		Animation animation = presentation.getAnimation(parsedNumber);
		animation.directlyGoTo(offscreenDisplay);
		
		pbuffer.addGLEventListener(offscreenDisplay);
		
		KeyFrameCreator keyFrameCreator = new KeyFrameCreator(512, 512);
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

		return result;
	}
	
	protected static class KeyFrameCreator implements Callable<byte[]> {
		
		private final int width;
		private final int height;

		public KeyFrameCreator(int width, int height) {
			this.width = width;
			this.height = height;
		}

		@Override
		public byte[] call() throws Exception {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			try {
			    BufferedImage image = Screenshot.readToBufferedImage(0, 0, width, height, true);
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

}
