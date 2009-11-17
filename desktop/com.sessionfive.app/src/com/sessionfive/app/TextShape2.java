package com.sessionfive.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GLAutoDrawable;

import com.sun.opengl.util.j2d.TextRenderer;

public class TextShape2 {
	private Color color = Color.DARK_GRAY;

	private TextRenderer textRenderer;

	private String layerText;

	public TextShape2() {
		Font font = new Font("SansSerif", Font.BOLD, 14);
		textRenderer = new TextRenderer(font, true, false);
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		// fireDisplayChangedEvent();
	}

	public void setText(String layerText) {
		this.layerText = layerText;
	}

	public void display(GLAutoDrawable drawable) {

		textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		textRenderer.setColor(color);

		int endindex1 = layerText.length() >= 60 ? 60 : layerText.length();
		String row1 = layerText.substring(0, endindex1);
		Rectangle2D bounds = textRenderer.getBounds(row1);
		int x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
		int y = drawable.getHeight() - 20;
		textRenderer.draw(row1, x, y);

		if (layerText.length() > 60) {
			int endindex2 = layerText.length() >= 120 ? 120 : layerText.length();
			String row2 = layerText.substring(60, endindex2);
			bounds = textRenderer.getBounds(row1);
			x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			y = drawable.getHeight() - 40;
			textRenderer.draw(row2, x, y);
		}

		if (layerText.length() > 120) {
			int endindex2 = layerText.length() >= 180 ? 180 : layerText.length();
			String row3 = layerText.substring(120, endindex2);
			bounds = textRenderer.getBounds(row1);
			x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			y = drawable.getHeight() - 60;
			textRenderer.draw(row3, x, y);
		}

		textRenderer.endRendering();
	}
}
