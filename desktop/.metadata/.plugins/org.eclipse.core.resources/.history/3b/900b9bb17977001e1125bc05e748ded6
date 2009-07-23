package com.sessionfive.app;

import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import com.sessionfive.animation.Camera;
import com.sessionfive.animation.CameraSetting;
import com.sessionfive.core.Presentation;
import com.sessionfive.core.Shape;
import com.sun.opengl.util.awt.Screenshot;
import com.sun.opengl.util.awt.TextRenderer;

public class Display implements GLEventListener {

	private static final GLU glu = new GLU();

	private final Presentation presentation;
	
	private Camera camera;
	private CameraSetting cameraSetting;
	private TextRenderer textRenderer;

	private volatile boolean doScreenshot;

	private int width;

	private int height;

	public Display(Presentation presentation, CameraSetting cameraSetting) {
		this.presentation = presentation;
		this.cameraSetting = cameraSetting;
	}
	
	public CameraSetting getCameraSetting() {
		return cameraSetting;
	}
	
	public void setCameraSetting(CameraSetting cameraSetting) {
		this.cameraSetting = cameraSetting;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();

		camera.setLocation(cameraSetting.getLocation());
		camera.setTarget(cameraSetting.getTarget());
		camera.setUp(cameraSetting.getUp());
		camera.setup(gl, glu);
		
		List<Shape> shapes = presentation.getShapes();
		for (Shape shape : shapes) {
			shape.display(gl);
		}
		
        int x = drawable.getWidth() - 120;
        int y = drawable.getHeight() - 23;
        float c = 0.55f;
        textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
        textRenderer.setColor(c, c, c, c);
        textRenderer.draw("it-agile.de", x, y);
        textRenderer.endRendering();
        
        if (doScreenshot) {
        	doScreenshot = false;
			try {
				Screenshot.writeToFile(new File("shot1.jpg"), width, height);
			} catch (GLException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
	}
	
	@Override
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.setSwapInterval(0);
		gl.glEnable(GL_DEPTH_TEST);
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		camera = new Camera();
		
        Font font = new Font("SansSerif", Font.BOLD, 24);
        textRenderer = new TextRenderer(font, true, false);
	}
	
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		GL2 gl = drawable.getGL().getGL2();

		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		double aspectRatio = (double) width / (double) height;
		glu.gluPerspective(45.0, aspectRatio, 1.0, 400.0);

		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
	}

	public void doScreenshot(int width, int height) {
		this.width = width;
		this.height = height;
		this.doScreenshot = true;
	}

	@Override
	public void dispose(GLAutoDrawable arg0) {
	}

}
