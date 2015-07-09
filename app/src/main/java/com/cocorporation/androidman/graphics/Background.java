package com.cocorporation.androidman.graphics;

import android.util.Log;

import com.cocorporation.androidman.core.Rectangle;
import com.cocorporation.androidman.messages.MessageReceiver;
import com.cocorporation.androidman.messages.MessagesManager;
import com.cocorporation.androidman.messages.MessagesType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Background implements MessageReceiver {
    private static final String TAG = "Background";

    // Geometric variables
    private float vertices[];
    private short indices[];
    private float verticesApples[];
    private short indicesApples[];
    private float factor;
    private float offsetx;
    private float offsety;

    // Initial position for AndroidMan, depends on which background is implemented
    private float initPosXAndroidMan;
    private float initPosYAndroidMan;

    private float initPosXGhosts;
    private float initPosYGhosts;

    private float maxX;
    private float minX;

    private int numberOfBorders = 0;

    private int numberOfApples = 0;

    // List of rectangles making the board
    private List<Rectangle> rectangleList;
    private List<Rectangle> appleList;

    public Background(float factor, float offsetx, float offsety)
    {
        rectangleList = new ArrayList<Rectangle>();
        appleList = new ArrayList<Rectangle>();
        this.factor = factor;
        this.offsetx = offsetx;
        this.offsety = offsety;
    }

    public void createBasicBackground()
    {
        initPosXAndroidMan = offsetx + factor * 35.0f;
        initPosYAndroidMan = offsety + factor * 215.0f;

        initPosXGhosts = offsetx + factor * 260.0f;
        initPosYGhosts = offsety + factor * 330.0f;

        maxX = offsetx + factor * 570;
        minX = offsetx + factor * 0;

        createRectangle(0,120,360,370);
        createRectangle(110,120,370,420);
        createRectangle(0,120,420,430);
        createRectangle(0,10,430,620);
        createRectangle(10,570,610,620);
        createRectangle(560,570,430,610);
        createRectangle(450,570,420,430);
        createRectangle(450,460,370,420);
        createRectangle(450,570,360,370);
        createRectangle(450,570,300,310);
        createRectangle(450,460,250,300);
        createRectangle(450,570,240,250);
        createRectangle(560,570,10,240);
        createRectangle(0,570,0,10);
        createRectangle(0,10,10,240);
        createRectangle(0,120,240,250);
        createRectangle(110,120,250,300);
        createRectangle(0,120,300,310);
        createRectangle(280,290,540,620);
        createRectangle(280,290,10,120);
        createRectangle(230,340,120,130);
        createRectangle(60,120,540,560);
        createRectangle(170,230,540,560);
        createRectangle(340,400,540,560);
        createRectangle(450,510,540,560);
        createRectangle(60,120,480,490);
        createRectangle(230,340,480,490);
        createRectangle(450,510,480,490);
        createRectangle(170,180,360,490);
        createRectangle(280,290,420,480);
        createRectangle(390,400,360,490);
        createRectangle(180,230,420,430);
        createRectangle(340,390,420,430);
        createRectangle(170,180,240,310);
        createRectangle(390,400,240,310);
        createRectangle(230,340,240,250);
        createRectangle(280,290,180,240);
        createRectangle(60,120,180,190);
        createRectangle(170,230,180,190);
        createRectangle(340,400,180,190);
        createRectangle(450,510,180,190);
        createRectangle(110,120,120,180);
        createRectangle(450,460,120,180);
        createRectangle(10,60,120,130);
        createRectangle(510,560,120,130);
        createRectangle(170,180,70,130);
        createRectangle(390,400,70,130);
        createRectangle(60, 230, 60, 70);
        createRectangle(340,510,60,70);

        createRectangle(230,235,300,370);
        createRectangle(235,335,300,305);
        createRectangle(335,340,300,370);

        createRectangle(235,260,368,370);
        createRectangle(310,335,368,370);

        vertices = new float[numberOfBorders*4*3];
        createVerticesRectangle();

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

        for (int k=0;k<5;k++)
        {
            createApple(27f + k * 54, 47f + k * 54, 25, 45);
            createApple(307f + k * 54, 327f + k * 54, 25, 45);
            createApple(27f + k * 54, 47f + k * 54, 575, 595);
            createApple(307f + k * 54, 327f + k * 54, 575, 595);
            createApple(27f + k * 54, 47f + k * 54, 505, 525);
            createApple(307f + k * 54, 327f + k * 54, 505, 525);
        }

        verticesApples = new float[numberOfApples*4*3];
        createVerticesApples();

        indicesApples = new short[numberOfApples*6];
        last = 0;
        for(int j=0;j<numberOfApples;j++)
        {
            // We need to set the new indices for the new quad
            indicesApples[(j*6) + 0] = (short) (last + 0);
            indicesApples[(j*6) + 1] = (short) (last + 1);
            indicesApples[(j*6) + 2] = (short) (last + 2);
            indicesApples[(j*6) + 3] = (short) (last + 0);
            indicesApples[(j*6) + 4] = (short) (last + 2);
            indicesApples[(j*6) + 5] = (short) (last + 3);

            last = last + 4;
        }
    }

    private void createRectangle(float x1,
                                 float x2,
                                 float y1,
                                 float y2)
    {
        float scaledX1 = offsetx + factor * x1;
        float scaledX2 = offsetx + factor * x2;
        float scaledY1 = offsety + factor * y1;
        float scaledY2 = offsety + factor * y2;
        rectangleList.add(new Rectangle((scaledX1+scaledX2)/2, (scaledY1+scaledY2)/2, (scaledX2-scaledX1)/2, (scaledY2-scaledY1)/2));

        numberOfBorders++;
    }

    public void createVerticesRectangle()
    {
        int index = 0;
        for (Rectangle rect : rectangleList)
        {
            vertices[index*12 + 0] = rect.getX1();
            vertices[index*12 + 1] = rect.getY2();
            vertices[index*12 + 2] = 0.0f;
            vertices[index*12 + 3] = rect.getX1();
            vertices[index*12 + 4] = rect.getY1();
            vertices[index*12 + 5] = 0.0f;
            vertices[index*12 + 6] = rect.getX2();
            vertices[index*12 + 7] = rect.getY1();
            vertices[index*12 + 8] = 0.0f;
            vertices[index*12 + 9] = rect.getX2();
            vertices[index*12 + 10] = rect.getY2();
            vertices[index*12 + 11] = 0.0f;
            index++;
        }
    }

    private void createApple(float x1,
                                 float x2,
                                 float y1,
                                 float y2)
    {
        float scaledX1 = offsetx + factor * x1;
        float scaledX2 = offsetx + factor * x2;
        float scaledY1 = offsety + factor * y1;
        float scaledY2 = offsety + factor * y2;
        appleList.add(new Rectangle((scaledX1+scaledX2)/2, (scaledY1+scaledY2)/2, (scaledX2-scaledX1)/2, (scaledY2-scaledY1)/2));

        numberOfApples++;
    }

    public void createVerticesApples()
    {
        int index = 0;
        for (Rectangle rect : appleList)
        {
            verticesApples[index*12 + 0] = rect.getX1();
            verticesApples[index*12 + 1] = rect.getY2();
            verticesApples[index*12 + 2] = 0.0f;
            verticesApples[index*12 + 3] = rect.getX1();
            verticesApples[index*12 + 4] = rect.getY1();
            verticesApples[index*12 + 5] = 0.0f;
            verticesApples[index*12 + 6] = rect.getX2();
            verticesApples[index*12 + 7] = rect.getY1();
            verticesApples[index*12 + 8] = 0.0f;
            verticesApples[index*12 + 9] = rect.getX2();
            verticesApples[index*12 + 10] = rect.getY2();
            verticesApples[index*12 + 11] = 0.0f;
            index++;
        }
    }

    @Override
    public void receiveMessage(Object message) {
        Log.i(TAG, "Received Message");
        numberOfApples--;
        if (numberOfApples == 0)
            MessagesManager.getInstance().sendMessage(MessagesType.WIN, null);

        verticesApples = new float[numberOfApples*4*3];
        createVerticesApples();
        indicesApples = new short[numberOfApples*6];
        int last = 0;
        for(int j=0;j<numberOfApples;j++)
        {
            // We need to set the new indices for the new quad
            indicesApples[(j*6) + 0] = (short) (last + 0);
            indicesApples[(j*6) + 1] = (short) (last + 1);
            indicesApples[(j*6) + 2] = (short) (last + 2);
            indicesApples[(j*6) + 3] = (short) (last + 0);
            indicesApples[(j*6) + 4] = (short) (last + 2);
            indicesApples[(j*6) + 5] = (short) (last + 3);

            last = last + 4;
        }
    }

    public float[] getVertices() {
        return vertices;
    }

    public float[] getVerticesApples() {
        return verticesApples;
    }

    public short[] getIndices() {
        return indices;
    }

    public short[] getIndicesApples() {
        return indicesApples;
    }

    public float getInitPosXAndroidMan() {
        return initPosXAndroidMan;
    }

    public float getInitPosYAndroidMan() {
        return initPosYAndroidMan;
    }

    public List<Rectangle> getRectangleList() {
        return rectangleList;
    }

    public List<Rectangle> getAppleList() {
        return appleList;
    }

    public int getNumberOfApples() {
        return numberOfApples;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinX() {
        return minX;
    }

    public float getInitPosXGhosts() {
        return initPosXGhosts;
    }

    public float getInitPosYGhosts() {
        return initPosYGhosts;
    }
}
