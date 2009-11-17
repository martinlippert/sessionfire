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
		Font font = new Font("SansSerif", Font.BOLD, 24);
		textRenderer = new TextRenderer(font, true, false);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		//fireDisplayChangedEvent();
	}


	public void setText(String layerText) {
		this.layerText = layerText;
		
	}


	public void display(GLAutoDrawable drawable) {
		Rectangle2D bounds = textRenderer.getBounds(layerText);
		int x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
		int y = drawable.getHeight() - 23;

		textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		textRenderer.setColor(color);
		textRenderer.draw(layerText, x, y);
		textRenderer.endRendering();
	}
}
