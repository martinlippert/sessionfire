package com.sessionfive.app;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_MODELVIEW;
import static javax.media.opengl.GL.GL_PROJECTION;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.FutureTask;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sessionfive.core.Camera;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sun.opengl.util.j2d.TextRenderer;

public class Display implements GLEventListener {

	private static final GLU glu = new GLU();

	private final Presentation presentation;
	private Camera camera;
	private TextRenderer textRenderer;
	private Color color = Color.BLACK;

	private FutureTask<byte[]> futureTask;
	private List<DisplayChangedListener> changeListeners;


	public Display(Presentation presentation) {
		this.presentation = presentation;
		this.camera = presentation.getStartCamera();
		this.changeListeners = new LinkedList<DisplayChangedListener>();

		Font font = new Font("SansSerif", Font.BOLD, 24);
		textRenderer = new TextRenderer(font, true, false);
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
		fireDisplayChangedEvent();
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		fireDisplayChangedEvent();
	}

	public void display(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();

		Color backgroundColor = presentation.getBackgroundColor();
		gl.glClearColor((float) backgroundColor.getRed() / 255f,
				(float) backgroundColor.getGreen() / 255f,
				(float) backgroundColor.getBlue() / 255f, 0.0f);

		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();

		camera.setTo(gl, glu);

		List<Shape> shapes = presentation.getShapes();
		for (Shape shape : shapes) {
			shape.display(gl);
		}

		String layerText = this.presentation.getLayerText();
		if (layerText != null && layerText.length() > 0) {

			Rectangle2D bounds = textRenderer.getBounds(layerText);
			int x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			int y = drawable.getHeight() - 23;

			textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
			textRenderer.setColor(color);
			textRenderer.draw(layerText, x, y);
			textRenderer.endRendering();
		}

		// try {
		// long timestamp = System.currentTimeMillis();
		// Screenshot.writeToFile(new File("shot" + timestamp + ".jpg"), 400,
		// 300);
		// } catch (GLException e1) {
		// e1.printStackTrace();
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }

		if (futureTask != null) {
			futureTask.run();
			futureTask = null;
		}
	}

	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.setSwapInterval(0);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		int width = drawable.getWidth();
		int height = drawable.getHeight();
		reshape(drawable, 0, 0, width, height);
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL gl = drawable.getGL();

		System.err.println();
		System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
		System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
		System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));
		System.err.println();

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		double aspectRatio = (double) width / (double) height;
		glu.gluPerspective(45.0, aspectRatio, 1.0, 400.0);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void dispose(GLAutoDrawable arg0) {
	}

	public void executeInDisplay(FutureTask<byte[]> futureTask) {
		this.futureTask = futureTask;
	}

	public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
	}

	public void addDisplayChangedListener(DisplayChangedListener listener) {
		changeListeners.add(listener);
	}

	public void removeDisplayChangedListener(DisplayChangedListener listener) {
		changeListeners.remove(listener);
	}

	protected void fireDisplayChangedEvent() {
		DisplayChangedEvent event = new DisplayChangedEvent(this);

		Iterator<DisplayChangedListener> listeners = changeListeners.iterator();
		while (listeners.hasNext()) {
			listeners.next().displayChanged(event);
		}
	}
	

}
