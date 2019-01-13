package com.raphael.framework.gl;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.raphael.framework.FileIO;
import com.raphael.framework.impl.GLGame;
import com.raphael.framework.impl.GLGraphics;

public class Texture {
	GLGraphics glGraphics;
	FileIO fileIO;
	String fileName;
	int textureId;
	int width;
	int height;
	public Bitmap bitmap;
	TextureRegion region;
	boolean file;

	public Texture(GLGame glGame, String fileName) {
		this.glGraphics = glGame.getGLGraphics();
		this.fileIO = glGame.getFileIO();
		this.fileName = fileName;
		file = true;
		load();
	}

	public Texture(GLGame glGame, Bitmap bitmap) {
		this.glGraphics = glGame.getGLGraphics();
		this.bitmap = bitmap;
		file = false;
		load();
	}

	private void load() {
		int[] textureIds = new int[1];
		GLES20.glGenTextures(1, textureIds, 0);
		textureId = textureIds[0];
		if (file) {
			InputStream in = null;
			try {
				in = fileIO.readAsset(fileName);
				bitmap = BitmapFactory.decodeStream(in);
				width = bitmap.getWidth();
				height = bitmap.getHeight();
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				setFilters();
				//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
				//bitmap.recycle();
			} catch (IOException e) {
				throw new RuntimeException("Couldn't load texture '" + fileName
						+ "'", e);
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (IOException e) {
					}
			}
		} else {
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			setFilters();
			//GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			//bitmap.recycle();
		}

		region = new TextureRegion(this, 0, 0, bitmap.getWidth(),
				bitmap.getHeight());
	}

	public void reload() {
		load();
		bind();
		setFilters();
		//GLES20.glBindTexture(GL10.GL_TEXTURE_2D, 0);
	}

	public void setFilters() {
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	}

	public void bind() {
	    // Set the active texture unit to texture unit 0.
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    // Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
	}

	public void dispose() {
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		int[] textureIds = { textureId };
		GLES20.glDeleteTextures(1, textureIds, 0);
	}

	public TextureRegion region() {
		return region;
	}

}