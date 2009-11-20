package com.sessionfire.twitter.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;

import com.sessionfive.app.RowMaker;
import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Shape;
import com.sun.opengl.util.j2d.TextRenderer;

public class TwitterShape extends AbstractShape implements Shape {
	private Color color = Color.WHITE;

	private TextRenderer textRenderer;

	private String layerText = "";

	private float size = 1;

	public TwitterShape() {
		Font font = new Font("SansSerif", Font.BOLD, 14);
		textRenderer = new TextRenderer(font, true, false);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		fireShapeChangedEvent();
	}

	public void setText(final String layerText) {
		new FadeAnimation().doFadeOutAnimation(this, new Runnable() {
			public void run() {
				if (layerText != null) {
					TwitterShape.this.layerText = layerText;
					if (layerText.length() > 0) {
						new FadeAnimation().doFadeInAnimation(TwitterShape.this, null);
					}
				}
			}
		});
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		List<String> rows = RowMaker.makeRows(layerText, 60);
		Rectangle2D bounds = textRenderer.getBounds(rows.get(0));
		if (rows.size() > 1) {
			Rectangle2D bounds2 = textRenderer.getBounds(rows.get(1));
			bounds = bounds.getWidth() > bounds2.getWidth() ? bounds : bounds2;
		}
		if (rows.size() > 2) {
			Rectangle2D bounds3 = textRenderer.getBounds(rows.get(2));
			bounds = bounds.getWidth() > bounds3.getWidth() ? bounds : bounds3;
		}

		drawBackgroundRectangle(drawable, bounds.getWidth());

		textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		textRenderer.setColor(color);

		int x = (int) (drawable.getWidth() - (bounds.getWidth() + 15));
		int lineheight = (int) (20 * size);
		int y = drawable.getHeight() - lineheight;
		textRenderer.draw(rows.get(0), x, y);

		if (rows.size() > 1) {
			y = y - lineheight;
			textRenderer.draw(rows.get(1), x, y);
		}
		if (rows.size() > 2) {
			y = y - lineheight;
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
		int lineheight = (int) (70 * size);
		int x1 = (int) (drawable.getWidth() - (width + 20));
		int x2 = drawable.getWidth() - 5;
		int y1 = drawable.getHeight() - 5;
		int y2 = drawable.getHeight() - lineheight;
		gl.glVertex3f(x1, y1, 0.0f);
		gl.glVertex3f(x2, y1, 0.0f);
		gl.glVertex3f(x2, y2, 0.0f);
		gl.glVertex3f(x1, y2, 0.0f);
		gl.glEnd();
		gl.glFlush();
		gl.glDisable(GL.GL_BLEND);

		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glPopMatrix();
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glPopMatrix();
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public float getWidth() {
		return 0;
	}

	public void setSize(int value) {
		this.size = (float) value / 100;
		int fontsize = (int) (14 * size);

		Font font = new Font("SansSerif", Font.BOLD, fontsize);
		textRenderer = new TextRenderer(font, true, false);

		fireShapeChangedEvent();

	}

}
