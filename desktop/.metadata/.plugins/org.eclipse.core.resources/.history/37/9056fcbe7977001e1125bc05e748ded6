package com.sessionfive.shapes;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_ONE;
import static javax.media.opengl.GL.GL_ONE_MINUS_SRC_ALPHA;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2ES1.GL_MODULATE;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV_MODE;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;

import com.sessionfive.core.AbstractShape;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureIO;

public class ImageShape extends AbstractShape {

	private Texture t;
	private final File file;

	public ImageShape(File file, float x, float y, float z, float rotation) {
		super(x, y, z, rotation);
		this.file = file;
	}

	@Override
	public float getWidth() {
		return 0;
	}

	@Override
	public float getHeight() {
		return 0;
	}

	@Override
	public void reset() {
		t = null;
	}

	@Override
	public void display(GL2 gl) {
		initializeTexture(file);
		
		Texture tex = t;
		TextureCoords tc = tex.getImageTexCoords();
		float tx1 = tc.left();
		float ty1 = tc.top();
		float tx2 = tc.right();
		float ty2 = tc.bottom();

		// Enable blending, using the SrcOver rule
		gl.glEnable(GL_BLEND);
		gl.glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

		// Use the GL_MODULATE texture function to effectively multiply
		// each pixel in the texture by the current alpha value
		gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

		float x = getX();
		float y = getY();
		float z = getZ();
		float w = 45f;
		float h = w * 0.75f;

        gl.glPushMatrix();
		gl.glTranslatef(x + (w/2),
                y + (h/2),
                0);
        gl.glRotatef(getRotation(), 0f, 0f, 1f);
		gl.glTranslatef(-(x + (w/2)),
                -(y + (h/2)),
                0);

		tex.bind();
		tex.enable();
		gl.glBegin(GL2.GL_QUADS);

        // Render image right-side up
		float a = 1f;
		gl.glColor4f(a, a, a, a);
		gl.glTexCoord2f(tx1, ty1);
		gl.glVertex3f(x, y + h, z);
		gl.glTexCoord2f(tx2, ty1);
		gl.glVertex3f(x + w, y + h, z);
		gl.glTexCoord2f(tx2, ty2);
		gl.glVertex3f(x + w, y, z);
		gl.glTexCoord2f(tx1, ty2);
		gl.glVertex3f(x, y, z);
		y -= h;

		// Render "reflected" image
		a = 1f;
		gl.glColor4f(a * 0.4f, a * 0.4f, a * 0.4f, a * 0.4f);
		gl.glTexCoord2f(tx1, ty2);
		gl.glVertex3f(x, y + h, z);
		gl.glTexCoord2f(tx2, ty2);
		gl.glVertex3f(x + w, y + h, z);
		gl.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glTexCoord2f(tx2, ty1 + ((ty2 - ty1) / 3));
		gl.glVertex3f(x + w, y + h / 3, z);
		gl.glTexCoord2f(tx1, ty1 + ((ty2 - ty1) / 3));
		gl.glVertex3f(x, y + h / 3, z);
		gl.glEnd();
		tex.disable();

        gl.glPopMatrix();
		gl.glDisable(GL_BLEND);
	}

	private void initializeTexture(File file) {
		if (t == null) {
			try {
				t = TextureIO.newTexture(file, false);
				t.setTexParameteri(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				t.setTexParameteri(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
