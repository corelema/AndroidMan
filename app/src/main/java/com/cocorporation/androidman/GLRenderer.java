package com.cocorporation.androidman;

/**
 * Created by Corentin on 4/18/2015.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;

public class GLRenderer implements Renderer {

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    private static float factor = 1.0f;
    private static float offsetx = 0.0f;
    private static float offsety = 0.0f;

    // Background variables
    private final int numberOfBorders = 4;

    // Our screenresolution
    float   mScreenWidth = 1920;
    float   mScreenHeight = 1080;

    // Misc
    Context mContext;
    long mLastTime;
    int mProgram;

    public GLRenderer(Context c)
    {
        mContext = c;
        mLastTime = System.currentTimeMillis() + 100;
    }

    public void onPause()
    {
        /* Do stuff to pause the renderer */
    }

    public void onResume()
    {
        /* Do stuff to resume the renderer */
        mLastTime = System.currentTimeMillis();
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Get the current time
        long now = System.currentTimeMillis();

        // We should make sure we are valid and sane
        if (mLastTime > now) return;

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Update our example

        // Render our example
        Render(mtrxProjectionAndView);

        // Save the current time to see how long it took <img class="wp-smiley" alt=":)" src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif"> .
        mLastTime = now;

    }

    private void Render(float[] m) {

        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(riGraphicTools.sp_SolidColor, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_SolidColor, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        // We need to know the current width and height.
        mScreenWidth = width;
        mScreenHeight = height;

        // Redo the Viewport, making it fullscreen.
        GLES20.glViewport(0, 0, (int)mScreenWidth, (int)mScreenHeight);

        // Clear our matrices
        for(int i=0;i<16;i++)
        {
            mtrxProjection[i] = 0.0f;
            mtrxView[i] = 0.0f;
            mtrxProjectionAndView[i] = 0.0f;
        }

        // Setup our screen width and height for normal sprite translation.
        Matrix.orthoM(mtrxProjection, 0, 0f, mScreenWidth, 0.0f, mScreenHeight, 0, 50);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        // Create the triangle
        SetupBackground();

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        // Create the shaders
        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);

        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);                  // creates OpenGL ES program executables

        // Set our shader programm
        GLES20.glUseProgram(riGraphicTools.sp_SolidColor);
    }

    public void SetupBackground()
    {
        vertices = new float[numberOfBorders*4*3];

        // We have create the vertices of our view.
        //for (int i=0;i<numberOfBorders;i++)
        //{
            int i = 0;
            vertices[i*12 + 0] = 10.0f;
            vertices[i*12 + 1] = 510.0f;
            vertices[i*12 + 2] = 0.0f;
            vertices[i*12 + 3] = 10.0f;
            vertices[i*12 + 4] = 500.0f;
            vertices[i*12 + 5] = 0.0f;
            vertices[i*12 + 6] = 1000.0f;
            vertices[i*12 + 7] = 500.0f;
            vertices[i*12 + 8] = 0.0f;
            vertices[i*12 + 9] = 1000.0f;
            vertices[i*12 + 10] = 510.0f;
            vertices[i*12 + 11] = 0.0f;

            i = 1;
            vertices[i*12 + 0] = 10.0f;
            vertices[i*12 + 1] = 1010.0f;
            vertices[i*12 + 2] = 0.0f;
            vertices[i*12 + 3] = 10.0f;
            vertices[i*12 + 4] = 1000.0f;
            vertices[i*12 + 5] = 0.0f;
            vertices[i*12 + 6] = 1000.0f;
            vertices[i*12 + 7] = 1000.0f;
            vertices[i*12 + 8] = 0.0f;
            vertices[i*12 + 9] = 1000.0f;
            vertices[i*12 + 10] = 1010.0f;
            vertices[i*12 + 11] = 0.0f;

            i = 2;
            vertices[i*12 + 0] = 10.0f;
            vertices[i*12 + 1] = 1000.0f;
            vertices[i*12 + 2] = 0.0f;
            vertices[i*12 + 3] = 10.0f;
            vertices[i*12 + 4] = 510.0f;
            vertices[i*12 + 5] = 0.0f;
            vertices[i*12 + 6] = 20.0f;
            vertices[i*12 + 7] = 510.0f;
            vertices[i*12 + 8] = 0.0f;
            vertices[i*12 + 9] = 20.0f;
            vertices[i*12 + 10] = 1000.0f;
            vertices[i*12 + 11] = 0.0f;

            i = 3;
            vertices[i*12 + 0] = 990.0f;
            vertices[i*12 + 1] = 1000.0f;
            vertices[i*12 + 2] = 0.0f;
            vertices[i*12 + 3] = 990.0f;
            vertices[i*12 + 4] = 510.0f;
            vertices[i*12 + 5] = 0.0f;
            vertices[i*12 + 6] = 1000.0f;
            vertices[i*12 + 7] = 510.0f;
            vertices[i*12 + 8] = 0.0f;
            vertices[i*12 + 9] = 1000.0f;
            vertices[i*12 + 10] = 1000.0f;
            vertices[i*12 + 11] = 0.0f;
        //}

        // The indices for all textured quads
        indices = new short[numberOfBorders*6];
        int last = 0;
        for(int j=0;j<numberOfBorders;j++)
        {
            // We need to set the new indices for the new quad
            indices[(j*6) + 0] = (short) (last + 0);
            indices[(j*6) + 1] = (short) (last + 1);
            indices[(j*6) + 2] = (short) (last + 2);
            indices[(j*6) + 3] = (short) (last + 0);
            indices[(j*6) + 4] = (short) (last + 2);
            indices[(j*6) + 5] = (short) (last + 3);

            last = last + 4;
        }

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);

    }
}
