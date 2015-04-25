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
import android.util.Log;
import android.view.MotionEvent;

public class GLRenderer implements Renderer {
    private static final String TAG = "GLRendererDebug";

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    private static float factor = 1.0f;
    private static float offsetx = 0.0f;
    private static float offsety = 0.0f;

    // Background variables
    Background background;

    // Manage the entities
    EntityManagement entityManagement;
    int numberOfIndicesToPlot = 0;

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
        factor = mScreenHeight/570.0f;
        background = new Background(factor, offsetx, offsety);
        background.createBasicBackground();
        entityManagement = new EntityManagement(background.getInitPosXAndroidMan(), background.getInitPosYAndroidMan());
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
        bufferizeObjects();

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

        // Draw the objects
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfIndicesToPlot,
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

        // Fill the buffers
        bufferizeObjects();

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

    private void bufferizeObjects()
    {
        AndroidMan androidMan = entityManagement.getAndroidMan();

        // The vertex buffer.
        float[] backgroundVertices = background.getVertices();
        float[] androidManVertices = androidMan.generateVertices();
        //to do:ghosts
        ByteBuffer bb = ByteBuffer.allocateDirect((backgroundVertices.length + androidManVertices.length) * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(backgroundVertices);
        vertexBuffer.put(androidManVertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        short[] backgroundIndices = background.getIndices();
        short[] androidManIndices = androidMan.getIndices();
        addOffsetToIndies(androidManIndices, (short)(backgroundIndices.length*4/6));
        numberOfIndicesToPlot = backgroundIndices.length + androidManIndices.length;

        ByteBuffer dlb = ByteBuffer.allocateDirect(numberOfIndicesToPlot* 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(backgroundIndices);
        drawListBuffer.put(androidManIndices);
        drawListBuffer.position(0);
    }

    private void addOffsetToIndies(short[] indices, short offset)
    {
        if (indices[0]==0)
        {
            for (int i=0;i<indices.length;i++)
            {
                indices[i]+=offset;
            }
        }
    }

    public void processTouchEvent(MotionEvent event)
    {
        int eventAction = event.getAction();

        Log.i(TAG, "event.getX() = " + event.getX());
        Log.i(TAG, "event.getY() = " + event.getY());
        Log.i(TAG, "mScreenWidth = " + mScreenWidth);
        Log.i(TAG, "mScreenHeight = " + mScreenHeight);
        Log.i(TAG, "--------------------");

        if (eventAction == MotionEvent.ACTION_DOWN || eventAction == MotionEvent.ACTION_MOVE)
        {
            Direction direction;
            if (event.getY()>mScreenHeight*3/4)
            {
                // Down screen touch
                direction = Direction.DOWN;
            }
            else if (event.getY()<mScreenHeight/4)
            {
                // Up screen touch
                direction = Direction.UP;
            }
            else if(event.getX()<mScreenWidth/2)
            {
                // Left screen touch
                direction = Direction.LEFT;
            }
            else
            {
                // Right screen touch
                direction = Direction.RIGHT;
            }
            entityManagement.getAndroidMan().move(direction, 2.0f);
        }
        else if (eventAction == MotionEvent.ACTION_UP)
        {
            entityManagement.getAndroidMan().move(Direction.LEFT, 0.0f);
        }
    }
}
