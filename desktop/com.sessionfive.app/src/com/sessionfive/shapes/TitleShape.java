package com.sessionfive.shapes;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;

import com.sessionfive.core.AbstractShape;
import com.sessionfive.core.Shape;
import com.sun.opengl.util.j2d.TextRenderer;

public class TitleShape extends AbstractShape implements Shape {
	private Color color = Color.DARK_GRAY;

	private TextRenderer textRenderer;

	private String text = "Sessionfire";

	public TitleShape() {
		Font font = new Font("SansSerif", Font.BOLD, 22);
		textRenderer = new TextRenderer(font, true, false);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void display(GLAutoDrawable drawable) {
		if (text != null && text.length() > 0) {
			Rectangle2D bounds = textRenderer.getBounds(text);
			int x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			int y =  23;
			textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
			textRenderer.setColor(color);
			textRenderer.draw(text, x, y);
			textRenderer.endRendering();
		}
	}

	@Override
	public void display(GL gl) {
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public float getWidth() {
		return 0;
	}

}
