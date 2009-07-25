package com.sessionfive.shapes;

import java.awt.Font;

import javax.media.opengl.GL2;

import com.sessionfive.core.AbstractShape;
import com.sun.opengl.util.awt.TextRenderer;

public class TextShape extends AbstractShape {

	private Font font;
	private TextRenderer renderer;
	private String text;

	public TextShape(String text, String font, int fontSize, float x, float y,
			float z, float rotation) {
		super(x, y, z, rotation);
		this.text = text;
		this.renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 60), true,
				false);
	}

	@Override
	public float getWidth() {
		return (float) renderer.getBounds(text).getWidth() * 0.05f;
	}

	@Override
	public float getHeight() {
		return (float) renderer.getBounds(text).getHeight() * 0.05f;
	}

	@Override
	public void display(GL2 gl) {
		gl.getContext().getGLDrawable().getWidth();
		gl.getContext().getGLDrawable().getHeight();
		
		float x = getX();
		float y = getY();
		float z = getZ();
		float w = getWidth();
		float h = getHeight();
		
        gl.glPushMatrix();
		gl.glTranslatef(x + (w/2),
                y + (h/2),
                0);
        gl.glRotatef(getRotation(), 0f, 0f, 1f);
		gl.glTranslatef(-(x + (w/2)),
                -(y + (h/2)),
                0);
		
		renderer.begin3DRendering();
		renderer.setColor(1, 0, 0, 1);
		renderer.draw3D(text, x, y, z, 0.05f);
		renderer.end3DRendering();

		gl.glPopMatrix();
	}

	public Font getFont() {
		return font;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
