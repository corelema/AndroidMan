package com.cocorporation.androidman;

import android.util.Log;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Rectangle {
    private static final String TAG = "Rectangle";

    private float centerX;
    private float centerY;

    private float width;
    private float height;

    public Rectangle(float centerX, float centerY, float width, float height){
        this.centerX=centerX;
        this.centerY=centerY;
        this.width=width;
        this.height = height;
    }

    public boolean intersect(Rectangle rect)
    {
        if (abs(centerX, rect.getCenterX())<width+rect.getWidth()
                && abs(centerY, rect.getCenterY())<height+rect.getHeight())
        {
            Log.i(TAG, "Intersection between those two rectangles:");
            Log.i(TAG, "Rect1: x1 + " + (centerX - width) + " x2 = " + (centerX + width) + " y1 = " + (centerY - height) + " y2 = " + (centerY + height));
            Log.i(TAG, "Rect2: x1 + " + (rect.getCenterX() - rect.getWidth()) + " x2 = " + (rect.getCenterX() + rect.getWidth()) + " y1 = " + (rect.getCenterY() - rect.getHeight()) + " y2 = " + (rect.getCenterY() + rect.getHeight()));
            return true;
        }
        return false;
    }

    private float abs(float f1, float f2)
    {
        return (f1-f2>0?f1-f2:f2-f1);
    }

    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }
}
