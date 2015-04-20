package com.cocorporation.androidman;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Rectangle {


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
            return true;
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
