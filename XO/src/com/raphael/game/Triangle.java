package com.raphael.game;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.raphael.framework.impl.MyGLRenderer;

import android.opengl.GLES20;

public class Triangle {

    //private FloatBuffer vertexBuffer;
    
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "attribute vec4 aColor;" +
            "varying vec4 vColor;" +
            "void main() {" +
            // the matrix must be included as a modifier of gl_Position
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "  vColor = aColor;" +
            "}";
    
    // Use to access and set the view transformation
   // private int mMVPMatrixHandle;

        private final String fragmentShaderCode =
            "precision mediump float;" +
            "varying vec4 vColor;" +
            "void main() {" +
			//"vec4 color = vec4(vColor.x*(gl_FragCoord.x/1080.0),vColor.y*(gl_FragCoord.y/1920.0),vColor.z,vColor.a);"+
            "  gl_FragColor = vColor;" +
            "}";
    
    int mProgram;
        
    // number of coordinates per vertex in this array
   // static final int COORDS_PER_VERTEX = 3;
   // static float triangleCoords[] = {   // in counterclockwise order:
   //         540,  1040, 0.0f, // top
   //         580, 919, 0.0f, // bottom left
   //          500, 921, 0.0f  // bottom right
   // };

    // Set color with red, green, blue and alpha (opacity) values
   // float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };
    int vertexShader,fragmentShader;
    
    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        //ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
        //        triangleCoords.length * 4);
        // use the device hardware's native byte order
        //bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        //vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        //vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        //vertexBuffer.position(0);
        
         vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
         fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }
    
    public void recompile(){
        vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        
        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }
    
    //private int mPositionHandle;
    //private int mColorHandle;

    ///private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    //private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        //GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        //mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
       //GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
       // GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
         //                            GLES20.GL_FLOAT, false,
        //                             vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
       // mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
      //  GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        
        // get handle to shape's transformation matrix
        //mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // Pass the projection and view transformation to the shader
        //GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);

        // Draw the triangle
        //GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        //GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
    
}
