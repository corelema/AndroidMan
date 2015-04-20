package com.cocorporation.androidman;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Rectangle {
    public float getCenterX() {
        return centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getWidth() {
        return width;
    }

    private float centerX;
    private float centerY;

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    private float width;

    public Rectangle(float centerX, float centerY, float width){
        this.centerX=centerX;
        this.centerY=centerY;
        this.width=width;
    }

    public boolean intersect(Rectangle rect)
    {
        if (abs(centerX, rect.getCenterX())<width+rect.getWidth()
                && abs(centerY, rect.getCenterY())<width+rect.getWidth())
            return true;
        return false;
    }

    private float abs(float f1, float f2)
    {
        return (f1-f2>0?f1-f2:f2-f1);
    }
}
