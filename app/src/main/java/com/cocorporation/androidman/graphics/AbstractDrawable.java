package com.cocorporation.androidman.graphics;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Corentin on 7/18/2015.
 */
public abstract class AbstractDrawable implements InterfaceDrawable {
    protected float[] vertices;
    protected short[] indices;
    protected int numberOfIndicesToPlot;
    protected float uvs[];
    protected FloatBuffer uvBuffer;
    protected Context mContext;

    FloatBuffer vertexBuffer;
    ShortBuffer drawListBuffer;

    @Override
    public void bufferize() {
        refreshVertices();//TODO remove
        refreshIndices();//TODO remove

        // The vertex buffer.
        ByteBuffer bb = ByteBuffer.allocateDirect((vertices.length * 4));
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        numberOfIndicesToPlot = indices.length;

        ByteBuffer dlb = ByteBuffer.allocateDirect(numberOfIndicesToPlot* 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(indices);
        drawListBuffer.position(0);
    }

    @Override
    public void render(float[] m, int texture) {
        updateUvBuffer();

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
        GLES20.glUniform1i( mSamplerLoc, 2);

        //GLES20.glActiveTexture(texture); //TODO REPLACE BY THE GOOD ONE

        // Draw Apples
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, numberOfIndicesToPlot,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    public abstract void setupImage(int textureNumber);

    protected abstract void refreshVertices();

    protected abstract void refreshIndices();

    protected abstract void updateUvs();

    protected abstract void setupVariables();

    protected abstract void updateUvBuffer();
}
