package com.cocorporation.androidman.graphics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.cocorporation.androidman.core.Rectangle;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Corentin on 7/18/2015.
 */
public class Controls extends AbstractDrawable {
    private short numberOfButtons;
    float minX;
    float maxX;
    float minY;
    float maxY;
    boolean[] buttonsState;
    Rectangle[] buttonsPositions;

    public Controls(Context c, float minX, float maxX, float minY, float maxY)
    {
        numberOfButtons = 4;
        this.mContext = c;
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        setupVariables();
        refreshIndices();
    }

    @Override
    public void setupImage(int textureNumber) {
        // Generate Textures, if more needed, alter these numbers.
        int[] textureNames = new int[1];
        GLES20.glGenTextures(1, textureNames, 0);

        // Retrieve our image from resources.
        int id = mContext.getResources().getIdentifier("mipmap/buttons", null,
                mContext.getPackageName());

        // Temporary create a bitmap
        Bitmap bmp = BitmapFactory.decodeResource(mContext.getResources(), id);

        // Bind texture to texturename
        GLES20.glActiveTexture(textureNumber);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureNames[0]);


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
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);


        // We are done using the bitmap so we should recycle it.
        bmp.recycle();
    }

    @Override
    protected void refreshVertices() {
        int index = 0;
        for (int i = 0 ; i < numberOfButtons ; i++)
        {
            vertices[index*12 + 0] = buttonsPositions[i].getX1();
            vertices[index*12 + 1] = buttonsPositions[i].getY2();
            vertices[index*12 + 2] = 0.0f;
            vertices[index*12 + 3] = buttonsPositions[i].getX1();
            vertices[index*12 + 4] = buttonsPositions[i].getY1();
            vertices[index*12 + 5] = 0.0f;
            vertices[index*12 + 6] = buttonsPositions[i].getX2();
            vertices[index*12 + 7] = buttonsPositions[i].getY1();
            vertices[index*12 + 8] = 0.0f;
            vertices[index*12 + 9] = buttonsPositions[i].getX2();
            vertices[index*12 + 10] = buttonsPositions[i].getY2();
            vertices[index*12 + 11] = 0.0f;
            index++;
        }
    }

    @Override
    protected void refreshIndices() {

        int last = 0;
        for(int j=0;j<numberOfButtons;j++)
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
    }

    @Override
    protected void setupVariables() {
        vertices = new float[numberOfButtons*4*3];
        buttonsState = new boolean[4];
        indices = new short[numberOfButtons*6];
        uvs = new float[numberOfButtons * 8];
        buttonsPositions = new Rectangle[numberOfButtons];
        setupButtonsPositions();
    }

    @Override
    protected void updateUvBuffer() {
        updateUvs();

        // The texture buffer
        ByteBuffer bb = ByteBuffer.allocateDirect(uvs.length * 4);
        bb.order(ByteOrder.nativeOrder());
        uvBuffer = bb.asFloatBuffer();
        uvBuffer.put(uvs);
        uvBuffer.position(0);
    }

    @Override
    protected void updateUvs() {
        // Create our UV coordinates.
        for (int i=0;i<numberOfButtons;i++)
        {
            float offset = 0;
            if (buttonsState[i])
                offset = 1.0f/2.0f;
            uvs[i*8 + 0] = offset+i*1.0f/8f;
            uvs[i*8 + 1] = 0.0f;

            uvs[i*8 + 2] = offset+i*1.0f/8f;
            uvs[i*8 + 3] = 1.0f;

            uvs[i*8 + 4] = offset+(i+1)*1.0f/8f;
            uvs[i*8 + 5] = 1.0f;

            uvs[i*8 + 6] = offset+(i+1)*1.0f/8f;
            uvs[i*8 + 7] = 0.0f;
        }
    }

    private void setupButtonsPositions()
    {
        float height = maxY - minY;
        float width = maxX - minX;
        float buttonSize = (height*0.8f)/3;
        float halfButtonSize = buttonSize/2;

        buttonsPositions[0] = new Rectangle(width/2 - buttonSize, height/2, halfButtonSize, halfButtonSize);
        buttonsPositions[1] = new Rectangle(width/2, height/2 - buttonSize, halfButtonSize, halfButtonSize);
        buttonsPositions[2] = new Rectangle(width/2, height/2 + buttonSize, halfButtonSize, halfButtonSize);
        buttonsPositions[3] = new Rectangle(width/2 + buttonSize, height/2, halfButtonSize, halfButtonSize);
    }

    public void touchAt(float x, float y)
    {
        for (int i=0;i<numberOfButtons;i++)
        {
            if (buttonsPositions[i].intersect(x, y))
            {
                buttonsState[i] = true;
                break;
            }
        }
    }

    public void resetButtons()
    {
        for (int i=0;i<numberOfButtons;i++)
        {
            buttonsState[i] = false;
        }
    }

    public Direction getDirection()
    {
        if (buttonsState[0])
            return Direction.LEFT;
        else if (buttonsState[1])
            return Direction.DOWN;
        else if (buttonsState[2])
            return Direction.UP;
        else return Direction.RIGHT;
    }
}
