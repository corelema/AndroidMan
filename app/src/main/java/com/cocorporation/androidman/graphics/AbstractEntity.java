package com.cocorporation.androidman.graphics;

/**
 * Created by Corentin on 4/19/2015.
 */

import android.util.Log;

public abstract class AbstractEntity {
    private static final String TAG = "AbstractEntity";

    // Geometric variables
    private float factor = 2.0f;
    private float offsetx = 0.0f;
    private float offsety = 0.0f;

    // Motion variables
    private Direction direction;
    private float speed;

    private short[] indices = {0,1,2,0,2,3};

    // Hitbox of the entity, smaller than the shape
    private Rectangle hitBox;

    // Entire rectangle to be generated when rendering the entity
    private Rectangle shape;

    // Own vertices of the entity
    private float vertices[];

    public AbstractEntity(float x, float y, float width, float factor, float offsetx, float offsety) {
        this.hitBox = new Rectangle(x, y, width-10.0f, width-10.0f);
        this.shape = new Rectangle(x, y, width, width);
        this.factor = factor;
        this.offsetx = offsetx;
        this.offsety = offsety;
        Log.i(TAG, "Entity created with width = " + width * factor);
    }

    public AbstractEntity(Rectangle rect, float factor, float offsetx, float offsety) {
        this.hitBox = new Rectangle(rect.getCenterX(), rect.getCenterY(), (rect.getWidth()-10.0f)*factor, (rect.getWidth()-10.0f)*factor);
        this.shape = new Rectangle(rect.getCenterX(), rect.getCenterY(), (rect.getWidth())*factor, (rect.getWidth())*factor);
    }

    public boolean checkCollision(Rectangle otherHitBox)
    {
        return shape.intersect(otherHitBox);
    }

    public boolean checkHit(Rectangle otherHitBox)
    {
        return hitBox.intersect(otherHitBox);
    }

    public void moveToPoint(float x, float y)
    {
        hitBox.setCenterX(x);
        hitBox.setCenterY(y);

        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    public void moveToPoint(Rectangle newPosition)
    {
        hitBox.setCenterX(newPosition.getCenterX());
        hitBox.setCenterY(newPosition.getCenterY());

        shape.setCenterX(newPosition.getCenterX());
        shape.setCenterY(newPosition.getCenterY());
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public Rectangle getShape() {
        return shape;
    }

    public float[] generateVertices()
    {
        vertices = new float[4*3];

        float x1 = shape.getCenterX() - shape.getWidth();
        float x2 = shape.getCenterX() + shape.getWidth();
        float y1 = shape.getCenterY() - shape.getHeight();
        float y2 = shape.getCenterY() + shape.getHeight();

        Log.i(TAG, "x1 = " + x1);
        Log.i(TAG, "x2 = " + x2);
        Log.i(TAG, "y1 = " + y1);
        Log.i(TAG, "y2 = " + y2);

        vertices[0] = x1;
        vertices[1] = y2;
        vertices[2] = 0.0f;
        vertices[3] = x1;
        vertices[4] = y1;
        vertices[5] = 0.0f;
        vertices[6] = x2;
        vertices[7] = y1;
        vertices[8] = 0.0f;
        vertices[9] = x2;
        vertices[10] = y2;
        vertices[11] = 0.0f;

        return vertices;
    }

    public short[] getIndices() {
        return indices;
    }

    public void setMoveParameters(Direction direction, float speed)
    {
        this.direction = direction;
        this.speed = speed;
    }

    public void stopMoving()
    {
        this.direction = Direction.LEFT;
        this.speed = 0.0f;
    }

    public Direction getDirection() {
        return direction;
    }

    public float getSpeed() {
        return speed;
    }
}
