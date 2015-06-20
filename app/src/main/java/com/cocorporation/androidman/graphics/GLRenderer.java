package com.cocorporation.androidman.graphics;

/**
 * Created by Corentin on 4/18/2015.
 */

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.cocorporation.androidman.messages.MessagesManager;
import com.cocorporation.androidman.messages.MessagesType;

public class GLRenderer implements Renderer {
    private static final String TAG = "GLRendererDebug";

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];

    // Geometric variables
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public static float uvs[];
    public FloatBuffer uvBuffer;
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
        MessagesManager.getInstance().registerForMessage(MessagesType.DELETEAPPLE, background);
        entityManagement = new EntityManagement(factor, offsetx, offsety, background);
    }

    public void onPause()
    {
        /* Do stuff to pause the renderer */
        MessagesManager.getInstance().unsubscribe(MessagesType.DELETEAPPLE, background);
    }

    public void onResume()
    {
        /* Do stuff to resume the renderer */
        MessagesManager.getInstance().registerForMessage(MessagesType.DELETEAPPLE, background);
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

        // clear Screen and Depth Buffer, we have set the clear color as black.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Bufferize Background
        bufferizeBackground();

        // Set our shader programm
        GLES20.glUseProgram(riGraphicTools.sp_SolidColor);

        // Render Background
        renderBackground(mtrxProjectionAndView);

        // Set our shader programm
        GLES20.glUseProgram(riGraphicTools.sp_Image);

        // Bufferize Apples
        bufferizeApples();

        // Render Apples
        renderApples(mtrxProjectionAndView);

        // Bufferize AndroidMan
        bufferizeAndroidMan();

        // Render AndroidMan
        renderAndroidMan(mtrxProjectionAndView);

        // Bufferize Ghosts
        bufferizeGhosts();

        // Render AndroidMan
        renderGhosts(mtrxProjectionAndView);

        // Save the current time to see how long it took <img class="wp-smiley" alt=":)" src="http://androidblog.reindustries.com/wp-includes/images/smilies/icon_smile.gif"> .
        mLastTime = now;

    }

    private void renderBackground(float[] m)
    {
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

    private void renderAndroidMan(float[] m)
    {
        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image,
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image,
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 0); //TODO CAN BE DONE ONLY ONCE NO?

        // Draw AndroidMan
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfIndicesToPlot,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private void renderGhosts(float[] m)
    {
        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image,
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image,
                "s_texture" );

        // Set the sampler texture unit to 0, where we have saved the texture.
        GLES20.glUniform1i(mSamplerLoc, 1); //TODO CAN BE DONE ONLY ONCE NO?

        // Draw Ghosts
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfIndicesToPlot,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private void renderApples(float[] m)
    {
        // get handle to vertex shader's vPosition member
        int mPositionHandle =
                GLES20.glGetAttribLocation(riGraphicTools.sp_Image, "vPosition");

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, 3,
                GLES20.GL_FLOAT, false,
                0, vertexBuffer);

        // Get handle to texture coordinates location
        int mTexCoordLoc = GLES20.glGetAttribLocation(riGraphicTools.sp_Image,
                "a_texCoord" );

        // Enable generic vertex attribute array
        GLES20.glEnableVertexAttribArray ( mTexCoordLoc );

        // Prepare the texturecoordinates
        GLES20.glVertexAttribPointer ( mTexCoordLoc, 2, GLES20.GL_FLOAT,
                false,
                0, uvBuffer);

        // Get handle to shape's transformation matrix
        int mtrxhandle = GLES20.glGetUniformLocation(riGraphicTools.sp_Image,
                "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mtrxhandle, 1, false, m, 0);

        // Get handle to textures locations
        int mSamplerLoc = GLES20.glGetUniformLocation (riGraphicTools.sp_Image,
                "s_texture" );

        // Set the sampler texture unit to 1, where we have saved the texture.
        GLES20.glUniform1i ( mSamplerLoc, 1); //TODO need to see if I can just do it once at the setup

        GLES20.glActiveTexture(GLES20.GL_TEXTURE1); //TODO need to see why I don't need to call it with 0

        // Draw Apples
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfIndicesToPlot,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
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
        //Create the AndroidMan information
        setupImage(background.getNumberOfApples());
        //setupApple();

        // Set the clear color to black
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1);

        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);

        // Create the solid shaders
        int vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER, riGraphicTools.vs_SolidColor);
        int fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER, riGraphicTools.fs_SolidColor);

        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);                  // creates OpenGL ES program executables

        // Create the image shaders
        vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.vs_SolidColor);
        fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                riGraphicTools.fs_SolidColor);

        riGraphicTools.sp_SolidColor = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, vertexShader);
        GLES20.glAttachShader(riGraphicTools.sp_SolidColor, fragmentShader);
        GLES20.glLinkProgram(riGraphicTools.sp_SolidColor);

        // Create the shaders, images
        vertexShader = riGraphicTools.loadShader(GLES20.GL_VERTEX_SHADER,
                riGraphicTools.vs_Image);
        fragmentShader = riGraphicTools.loadShader(GLES20.GL_FRAGMENT_SHADER,
                riGraphicTools.fs_Image);

        riGraphicTools.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(riGraphicTools.sp_Image, vertexShader);
        GLES20.glAttachShader(riGraphicTools.sp_Image, fragmentShader);
        GLES20.glLinkProgram(riGraphicTools.sp_Image);

    }

    //TODO have to make a separate function for AndroidMan and for Ghosts
    public void setupImage(int numberOfApples)
    {
        // Create our UV coordinates.
        uvs = new float[numberOfApples * 8];
        for (int i=0;i<numberOfApples;i++)
        {
            uvs[i*8] = 0.0f;
            uvs[i*8 + 1] = 0.0f;
            uvs[i*8 + 2] = 0.0f;
            uvs[i*8 + 3] = 1.0f;
            uvs[i*8 + 4] = 1.0f;
            uvs[i*8 + 5] = 1.0f;
            uvs[i*8 + 6] = 1.0f;
            uvs[i*8 + 7] = 0.0f;
            //TODO Can we do it more efficiently?
        };

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);

        // Generate Textures, if more needed, alter these numbers.
        int[] texturenames = new int[2];
        GLES20.glGenTextures(2, texturenames, 0);

        // Retrieve our image from resources.
        int id1 = mContext.getResources().getIdentifier("mipmap/ic_launcher", null,
                mContext.getPackageName());
        int id2 = mContext.getResources().getIdentifier("mipmap/apple", null,
                mContext.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp1 = BitmapFactory.decodeResource(mContext.getResources(), id1);
        Bitmap bmp2 = BitmapFactory.decodeResource(mContext.getResources(), id2);

        // Bind texture to texturename
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[0]);


        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp1, 0);

//TODO need to see if I can optimize
        GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texturenames[1]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR);

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp2, 0);


        // We are done using the bitmap so we should recycle it.
        bmp1.recycle();
        bmp2.recycle();

    }

    private void bufferizeBackground()
    {

        // The vertex buffer.
        float[] backgroundVertices = background.getVertices();
        //to do:ghosts
        ByteBuffer bb = ByteBuffer.allocateDirect((backgroundVertices.length * 4));
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(backgroundVertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        short[] backgroundIndices = background.getIndices();
        numberOfIndicesToPlot = backgroundIndices.length;

        ByteBuffer dlb = ByteBuffer.allocateDirect(numberOfIndicesToPlot* 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(backgroundIndices);
        drawListBuffer.position(0);
    }

    private void bufferizeAndroidMan()
    {
        AndroidMan androidMan = entityManagement.getAndroidMan();

        // The vertex buffer.
        float[] androidManVertices = androidMan.generateVertices();
        ByteBuffer bb = ByteBuffer.allocateDirect((androidManVertices.length) * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(androidManVertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        short[] androidManIndices = androidMan.getIndices();
        numberOfIndicesToPlot = androidManIndices.length;

        ByteBuffer dlb = ByteBuffer.allocateDirect(numberOfIndicesToPlot* 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(androidManIndices);
        drawListBuffer.position(0);
    }

    private void bufferizeGhosts()
    {
        // The vertex buffer.
        float[] ghostsVertices = entityManagement.getVerticesGhosts();
        ByteBuffer bb = ByteBuffer.allocateDirect((ghostsVertices.length) * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(ghostsVertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        short[] ghostsIndices = entityManagement.getIndicesGhosts();
        numberOfIndicesToPlot = ghostsIndices.length;

        ByteBuffer dlb = ByteBuffer.allocateDirect(numberOfIndicesToPlot* 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(ghostsIndices);
        drawListBuffer.position(0);

        //Log.i(TAG, "Number of ghosts = " + listOfGhosts.size());
        Log.i(TAG, "GhostsVertices.length = " + ghostsVertices.length);
        Log.i(TAG, "ghostsIndices.length = " + ghostsIndices.length);
        Log.i(TAG, "numberOfIndicesToPlot = " + numberOfIndicesToPlot);
    }

    private void bufferizeApples()
    {

        // The vertex buffer.
        float[] applesVertices = background.getVerticesApples();
        ByteBuffer bb = ByteBuffer.allocateDirect((applesVertices.length * 4));
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(applesVertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        short[] applesIndices = background.getIndicesApples();
        numberOfIndicesToPlot = applesIndices.length;

        ByteBuffer dlb = ByteBuffer.allocateDirect(numberOfIndicesToPlot* 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(applesIndices);
        drawListBuffer.position(0);
    }

    private void addOffsetToIndices(short[] indices, short offset)
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
            entityManagement.getAndroidMan().setMoveParameters(direction, 2.0f);
        }
        else if (eventAction == MotionEvent.ACTION_UP)
        {
            entityManagement.getAndroidMan().stopMoving();
        }
    }
}
