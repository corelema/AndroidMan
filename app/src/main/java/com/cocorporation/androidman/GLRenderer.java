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
    public static float verticesEntities[];
    public static short indices[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    private static float factor = 1.0f;
    private static float offsetx = 0.0f;
    private static float offsety = 0.0f;

    // Background variables
    private final int numberOfBorders = 49;
    private int numberOfElements = 0;

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

        createRectangle(0,120,360,370,0);
        createRectangle(110,120,370,420,1);
        createRectangle(0,120,420,430,2);
        createRectangle(0,10,430,620,3);
        createRectangle(10,570,610,620,4);
        createRectangle(560,570,430,610,5);
        createRectangle(450,570,420,430,6);
        createRectangle(450,460,370,420,7);
        createRectangle(450,570,360,370,8);
        createRectangle(450,570,300,310,9);
        createRectangle(450,460,250,300,10);
        createRectangle(450,570,240,250,11);
        createRectangle(560,570,10,240,12);
        createRectangle(0,570,0,10,13);
        createRectangle(0,10,10,240,14);
        createRectangle(0,120,240,250,15);
        createRectangle(110,120,250,300,16);
        createRectangle(0,120,300,310,17);
        createRectangle(280,290,540,620,18);
        createRectangle(280,290,10,120,19);
        createRectangle(230,340,120,130,20);
        createRectangle(60,120,540,560,21);
        createRectangle(170,230,540,560,22);
        createRectangle(340,400,540,560,23);
        createRectangle(450,510,540,560,24);
        createRectangle(60,120,480,490,25);
        createRectangle(230,340,480,490,26);
        createRectangle(450,510,480,490,27);
        createRectangle(170,180,360,490,28);
        createRectangle(280,290,430,480,29);
        createRectangle(390,400,360,490,30);
        createRectangle(180,230,420,430,31);
        createRectangle(340,390,420,430,32);
        createRectangle(170,180,240,310,33);
        createRectangle(390,400,240,310,34);
        createRectangle(230,340,240,250,35);
        createRectangle(280,290,170,240,36);
        createRectangle(60,120,180,190,37);
        createRectangle(170,230,180,190,38);
        createRectangle(340,400,180,190,39);
        createRectangle(450,510,180,190,40);
        createRectangle(110,120,120,180,41);
        createRectangle(450,460,120,180,42);
        createRectangle(10,60,120,130,43);
        createRectangle(510,560,120,130,44);
        createRectangle(170,180,70,120,45);
        createRectangle(390,400,70,120,46);
        createRectangle(60,230,60,70,47);
        createRectangle(340,510,60,70,48);


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

    public void createAndroidMan(float x, float y)
    {
        createRectangle(x-25, x+25, y-25, y+25, numberOfBorders + (numberOfElements++));
    }

    private void createRectangle(float x1,
                                 float x2,
                                 float y1,
                                 float y2,
                                 int index)
    {
        vertices[index*12 + 0] = offsetx + factor * x1;
        vertices[index*12 + 1] = offsety + factor * y2;
        vertices[index*12 + 2] = 0.0f;
        vertices[index*12 + 3] = offsetx + factor * x1;
        vertices[index*12 + 4] = offsety + factor * y1;
        vertices[index*12 + 5] = 0.0f;
        vertices[index*12 + 6] = offsetx + factor * x2;
        vertices[index*12 + 7] = offsety + factor * y1;
        vertices[index*12 + 8] = 0.0f;
        vertices[index*12 + 9] = offsetx + factor * x2;
        vertices[index*12 + 10] = offsety + factor * y2;
        vertices[index*12 + 11] = 0.0f;
    }

    private void createEntity(float x1,
                                 float x2,
                                 float y1,
                                 float y2,
                                 int index)
    {
        vertices[index*12 + 0] = offsetx + factor * x1;
        vertices[index*12 + 1] = offsety + factor * y2;
        vertices[index*12 + 2] = 0.0f;
        vertices[index*12 + 3] = offsetx + factor * x1;
        vertices[index*12 + 4] = offsety + factor * y1;
        vertices[index*12 + 5] = 0.0f;
        vertices[index*12 + 6] = offsetx + factor * x2;
        vertices[index*12 + 7] = offsety + factor * y1;
        vertices[index*12 + 8] = 0.0f;
        vertices[index*12 + 9] = offsetx + factor * x2;
        vertices[index*12 + 10] = offsety + factor * y2;
        vertices[index*12 + 11] = 0.0f;
    }
}
