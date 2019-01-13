package com.raphael.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.opengl.GLES20;

import com.raphael.framework.impl.GLGraphics;

public class Vertices {
	final GLGraphics glGraphics;
	final boolean hasColor;
	final boolean hasTexCoords;
	final int vertexSize;
	final FloatBuffer vertices;
	final float[] tmpBuffer;
	final ShortBuffer indices;
	int shaderProgramHandle;
	int positionHandle, colorHandle, textureCoordinateHandle;
	float[] projection;

	public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndices,
			boolean hasColor, boolean hasTexCoords) {
		this.glGraphics = glGraphics;
		this.hasColor = hasColor;
		this.hasTexCoords = hasTexCoords;
		this.vertexSize = (2 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0)) * 4;
		this.tmpBuffer = new float[maxVertices * vertexSize / 4];

		ByteBuffer buffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
		buffer.order(ByteOrder.nativeOrder());
		vertices = buffer.asFloatBuffer();

		if (maxIndices > 0) {
			buffer = ByteBuffer.allocateDirect(maxIndices * Short.SIZE / 8);
			buffer.order(ByteOrder.nativeOrder());
			indices = buffer.asShortBuffer();
		} else {
			indices = null;
		}
		
		projection = new float[16];
	}

	public void setShaderProgram(int shaderProgramHandle) {
		this.shaderProgramHandle = shaderProgramHandle;
	}
	
	public void setProjection(float[] projection) {
		this.projection = projection.clone();
	}

	public void setVertices(float[] vertices, int offset, int length) {
		this.vertices.clear();
		this.vertices.put(vertices, offset, length);
	}

	public void setIndices(short[] indices, int offset, int length) {
		this.indices.clear();
		this.indices.put(indices, offset, length);
	}

	public void bind() {
		GLES20.glUseProgram(shaderProgramHandle);
		positionHandle = GLES20.glGetAttribLocation(shaderProgramHandle,
				"vPosition");
		// Enable a handle to the triangle vertices
		GLES20.glEnableVertexAttribArray(positionHandle);
		// Prepare the coordinates data
		vertices.position(0);
		GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false,
				vertexSize, vertices);
		if (hasColor) {
			// get handle to fragment shader's vColor member
			colorHandle = GLES20.glGetAttribLocation(shaderProgramHandle,
					"aColor");
			// Set color for drawing the triangle
			// Enable a handle to the vertex colors
			GLES20.glEnableVertexAttribArray(colorHandle);
			vertices.position(2);
			GLES20.glVertexAttribPointer(colorHandle, 4, GLES20.GL_FLOAT,
					false, vertexSize, vertices);
		}
		if (hasTexCoords) {
		    int textureUniformHandle = GLES20.glGetUniformLocation(shaderProgramHandle, "u_Texture");
		    textureCoordinateHandle = GLES20.glGetAttribLocation(shaderProgramHandle, "a_TexCoordinate"); 
		    // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
		    GLES20.glUniform1i(textureUniformHandle, 0);
			GLES20.glEnableVertexAttribArray(textureCoordinateHandle);
			vertices.position(hasColor ? 6 : 2);
			GLES20.glVertexAttribPointer(textureCoordinateHandle, 2, GLES20.GL_FLOAT,
					false, vertexSize, vertices);
		}
		// get handle to projection matrix
        int projectionHandle = GLES20.glGetUniformLocation(shaderProgramHandle, "uMVPMatrix");
        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(projectionHandle, 1, false, projection, 0);
	}

	public void draw(int primitiveType, int offset, int numVertices) {

		if (indices != null) {
			indices.position(offset);
			GLES20.glDrawElements(primitiveType, numVertices,
					GLES20.GL_UNSIGNED_SHORT, indices);
		} else {
			GLES20.glDrawArrays(primitiveType, offset, numVertices);
		}
	}

	public void unbind() {
		if (hasTexCoords)
			GLES20.glDisableVertexAttribArray(textureCoordinateHandle);
		if (hasColor)
			GLES20.glDisableVertexAttribArray(colorHandle);
		GLES20.glDisableVertexAttribArray(positionHandle);
	}

	public void setGreen(float green) {
		int handle = GLES20.glGetUniformLocation(shaderProgramHandle,"u_green");
		GLES20.glUniform1f(handle, green);	
	}
}
