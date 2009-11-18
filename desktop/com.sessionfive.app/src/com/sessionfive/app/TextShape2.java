package com.sessionfive.app;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.media.opengl.GL;
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
		GL gl = drawable.getGL();
		
		gl.glColor3f(1.0f, 1.0f, 1.0f);
	    gl.glBegin(GL.GL_POLYGON);
	    gl.glVertex3f(0.25f, 0.25f, 0.0f);
	    gl.glVertex3f(0.75f, 0.25f, 0.0f);
	    gl.glVertex3f(0.75f, 0.75f, 0.0f);
	    gl.glVertex3f(0.25f, 0.75f, 0.0f);
	    gl.glEnd();
	    
		textRenderer.beginRendering(drawable.getWidth(), drawable.getHeight());
		textRenderer.setColor(color);
		List<String> rows = RowMaker.makeRows(layerText, 60);
		
		Rectangle2D bounds = textRenderer.getBounds(rows.get(0));
		int x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
		int y = drawable.getHeight() - 20;
		textRenderer.draw(rows.get(0), x, y);
		
		if(rows.size() > 1){
			bounds = textRenderer.getBounds(rows.get(1));
			x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			y = drawable.getHeight() - 40;
			textRenderer.draw(rows.get(1), x, y);		
		}
		if(rows.size() > 2){
			bounds = textRenderer.getBounds(rows.get(1));
			x = (int) (drawable.getWidth() - (bounds.getWidth() + 10));
			y = drawable.getHeight() - 60;
			textRenderer.draw(rows.get(2), x, y);		
		}

		textRenderer.endRendering();
	}
	
		
}
