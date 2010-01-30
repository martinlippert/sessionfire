package com.sessionfive.shapes;

import static javax.media.opengl.GL.GL_BLEND;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_MODULATE;
import static javax.media.opengl.GL.GL_TEXTURE_ENV;
import static javax.media.opengl.GL.GL_TEXTURE_ENV_MODE;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
import javax.media.opengl.Threading;

import com.sessionfive.core.AbstractShape;
import com.sun.opengl.util.texture.Texture;
import com.sun.opengl.util.texture.TextureCoords;
import com.sun.opengl.util.texture.TextureData;
import com.sun.opengl.util.texture.TextureIO;

public class ImageShape extends AbstractShape {

	private Texture t;
	private final File file;
	
	public ImageShape(File file) {
		this.file = file;
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		if (t == null) return;
		
		Texture tex = t;
		TextureCoords tc = tex.getImageTexCoords();
		float tx1 = tc.left();
		float ty1 = tc.top();
		float tx2 = tc.right();
		float ty2 = tc.bottom();
		
		GL gl = drawable.getGL();

		// Enable blending, using the SrcOver rule
		gl.glEnable(GL_BLEND);
		gl.glEnable(GL.GL_ALPHA_TEST);
		gl.glAlphaFunc(GL.GL_GREATER, 0.1f);
		gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

		// Use the GL_MODULATE texture function to effectively multiply
		// each pixel in the texture by the current alpha value
		gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

		float x = getX();
		float y = getY();
		float z = getZ();
		float w = getWidth();
		float h = getHeight();

        gl.glPushMatrix();
		gl.glTranslatef(x + (w/2),
                y + (h/2),
                0);
        gl.glRotatef(getRotationAngleX(), 1f, 0f, 0f);
        gl.glRotatef(getRotationAngleY(), 0f, 1f, 0f);
        gl.glRotatef(getRotationAngleZ(), 0f, 0f, 1f);
		gl.glTranslatef(-(x + (w/2)),
                -(y + (h/2)),
                0);

		tex.bind();
		tex.enable();
		gl.glBegin(GL.GL_QUADS);

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

		if (isReflectionEnabled()) {
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
		}
		gl.glEnd();
		tex.disable();

        gl.glPopMatrix();
		gl.glDisable(GL_BLEND);
		gl.glDisable(GL.GL_ALPHA_TEST);
	}
	
	@Override
	public void initialize(final GLContext context) throws Exception {
		if (t == null) {
			super.initialize(context);
			final TextureData textureData = initTextureData();
			Threading.invokeOnOpenGLThread(new Runnable() {
				public void run() {
					try {
						context.makeCurrent();
						initTexture(textureData);
						context.release();
					} catch (GLException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
	
	private TextureData initTextureData() throws IOException {
		return TextureIO.newTextureData(this.file, true, null);
	}
	
	private void initTexture(final TextureData textureData) {
		if (t == null && textureData != null) {
			try {
				t = TextureIO.newTexture(textureData);
				t.setTexParameteri(GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				t.setTexParameteri(GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				
				float imageRatio = (float)t.getImageHeight() / (float)t.getImageWidth();
				float width = getWidth();
				float newHeight = width * imageRatio;
				float depth = getDepth();
				setSize(width, newHeight, depth);
			} catch (GLException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void release(final GLContext context) throws Exception {
		super.release(context);
		
		if (t != null) {
			Threading.invokeOnOpenGLThread(new Runnable() {
				public void run() {
					context.makeCurrent();
					t.dispose();
					t = null;
					context.release();
				}
			});
		}
	}

	public File getFile() {
		return this.file;
	}

}
