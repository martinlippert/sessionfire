package com.sessionfive.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.j2d.TextRenderer;

public class TextShape2 {
	private Color color = Color.WHITE;

	private TextRenderer textRenderer;

	private String layerText;

	//Bšse referenz
	private final Display display;

	public TextShape2(Display display) {
		Font font = new Font("SansSerif", Font.BOLD, 14);
		textRenderer = new TextRenderer(font, true, false);
		this.display = display;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		display.fireDisplayChangedEvent();
	}

	public void setText(String layerText) {
		this.layerText = layerText;
		display.fireDisplayChangedEvent();
	}

	public void display(GLAutoDrawable drawable) {
		List<String> rows = RowMaker.makeRows(layerText, 60);
		Rectangle2D bounds = textRenderer.getBounds(rows.get(0));

		drawBackgroundRectangle(drawable, bounds.getWidth());

		textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		textRenderer.setColor(color);

		int x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
		int y = drawable.getHeight() - 20;
		textRenderer.draw(rows.get(0), x, y);

		if (rows.size() > 1) {
			// bounds = textRenderer.getBounds(rows.get(1));
			x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			y = drawable.getHeight() - 40;
			textRenderer.draw(rows.get(1), x, y);
		}
		if (rows.size() > 2) {
			// bounds = textRenderer.getBounds(rows.get(1));
			x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			y = drawable.getHeight() - 60;
			textRenderer.draw(rows.get(2), x, y);
		}

		textRenderer.endRendering();
	}

	private void drawBackgroundRectangle(GLAutoDrawable drawable, double width) {
		GL gl = drawable.getGL();
		GLU glu = new GLU();
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		glu.gluOrtho2D(0, drawable.getWidth(), 0, drawable.getHeight());
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();

		gl.glEnable(GL.GL_BLEND); // activate blending mode
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); // define
		float alpha = ((float) color.getAlpha() / 255f) - 0.08f;
		// System.out.println("TextShape2.drawBackgroundRectangle()"+alpha);
		gl.glColor4f(0f, 0f, 0f, alpha);
		gl.glBegin(GL.GL_POLYGON);
		int x1 = (int) (drawable.getWidth() - (width + 20));
		int x2 = drawable.getWidth() - 5;
		int y1 = drawable.getHeight() - 5;
		int y2 = drawable.getHeight() - 60;
		gl.glVertex3f(x1, y1, 0.0f);
		gl.glVertex3f(x2, y1, 0.0f);
		gl.glVertex3f(x2, y2, 0.0f);
		gl.glVertex3f(x1, y2, 0.0f);
		gl.glEnd();
		gl.glFlush();

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
	}

	public DisplayChangedListener getDisplayChangedListener() {
		// TODO Auto-generated method stub
		return null;
	}

}
