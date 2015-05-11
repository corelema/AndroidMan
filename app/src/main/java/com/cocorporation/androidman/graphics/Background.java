package com.cocorporation.androidman.graphics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Background {
    // Geometric variables
    private float vertices[];
    private short indices[];
    private float factor;
    private float offsetx;
    private float offsety;

    // Initial position for AndroidMan, depends on which background is implemented
    private float initPosXAndroidMan;

    private float initPosYAndroidMan;

    private final int numberOfBorders = 49;

    // List of rectangles making the board
    private List<Rectangle> rectangleList;

    public Background(float factor, float offsetx, float offsety)
    {
        rectangleList = new ArrayList<Rectangle>();
        this.factor = factor;
        this.offsetx = offsetx;
        this.offsety = offsety;
    }

    public void createBasicBackground()
    {
        initPosXAndroidMan = offsetx + factor * 35.0f;
        initPosYAndroidMan = offsety + factor * 215.0f;

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
        createRectangle(280,290,420,480,29);
        createRectangle(390,400,360,490,30);
        createRectangle(180,230,420,430,31);
        createRectangle(340,390,420,430,32);
        createRectangle(170,180,240,310,33);
        createRectangle(390,400,240,310,34);
        createRectangle(230,340,240,250,35);
        createRectangle(280,290,180,240,36);
        createRectangle(60,120,180,190,37);
        createRectangle(170,230,180,190,38);
        createRectangle(340,400,180,190,39);
        createRectangle(450,510,180,190,40);
        createRectangle(110,120,120,180,41);
        createRectangle(450,460,120,180,42);
        createRectangle(10,60,120,130,43);
        createRectangle(510,560,120,130,44);
        createRectangle(170,180,70,130,45);
        createRectangle(390,400,70,130,46);
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
    }

    private void createRectangle(float x1,
                                 float x2,
                                 float y1,
                                 float y2,
                                 int index)
    {
        float scaledX1 = offsetx + factor * x1;
        float scaledX2 = offsetx + factor * x2;
        float scaledY1 = offsety + factor * y1;
        float scaledY2 = offsety + factor * y2;
        rectangleList.add(new Rectangle((scaledX1+scaledX2)/2, (scaledY1+scaledY2)/2, (scaledX2-scaledX1)/2, (scaledY2-scaledY1)/2));

        vertices[index*12 + 0] = scaledX1;
        vertices[index*12 + 1] = scaledY2;
        vertices[index*12 + 2] = 0.0f;
        vertices[index*12 + 3] = scaledX1;
        vertices[index*12 + 4] = scaledY1;
        vertices[index*12 + 5] = 0.0f;
        vertices[index*12 + 6] = scaledX2;
        vertices[index*12 + 7] = scaledY1;
        vertices[index*12 + 8] = 0.0f;
        vertices[index*12 + 9] = scaledX2;
        vertices[index*12 + 10] = scaledY2;
        vertices[index*12 + 11] = 0.0f;
    }

    public float[] getVertices() {
        return vertices;
    }

    public short[] getIndices() {
        return indices;
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
}
