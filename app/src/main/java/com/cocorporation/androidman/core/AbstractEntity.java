package com.cocorporation.androidman.core;

/**
 * Hotels Management Object.
 *
 * <P>Base class for the ghosts and AndroidMan.
 *
 * <P>Provides inner informations about position, speed, drawing indices.
 *
 * <P>Provides function to move the entity.
 *
 * <P>Provides function to generates the vertices.
 *
 * @author Corentin Leman
 * @version 1.0
 */
import android.util.Log;

import com.cocorporation.androidman.graphics.Direction;

public abstract class AbstractEntity {
    private static final String TAG = "AbstractEntity";

    // Geometric variables
    private float factor = 2.0f;
    private float offsetx = 0.0f;
    private float offsety = 0.0f;

    // Motion variables
    protected Direction currentDirection;
    protected float speed;

    private short[] indices = {0,1,2,0,2,3};

    // Hitbox of the entity, smaller than the shape
    private Rectangle hitBox;

    // Entire rectangle to be generated when rendering the entity
    private Rectangle shape;

    /**
     * Constructor.
     *
     * <P>Note: the hitBox will be automatically reduced from the shape.
     *
     * @param	x	desired x for initial position
     * @param	y	desired y for initial position
     * @param	width	width of SHAPE
     * @param	factor	start date of the deal
     * @param	offsetx		end date of the deal
     * @param	offsety		end date of the deal
     *
     */
    public AbstractEntity(float x, float y, float width, float factor, float offsetx, float offsety) {
        this.hitBox = new Rectangle(offsetx+x, offsety+y, width-10.0f, width-10.0f);
        this.shape = new Rectangle(offsetx+x, offsety+y, width, width);
        this.factor = factor;
        this.offsetx = offsetx;
        this.offsety = offsety;
        Log.i(TAG, "Entity created with width = " + width * factor);
    }

    /**
     * Constructor.
     *
     * <P>Note: the hitBox will be automatically reduced from the shape.
     *
     * @param	rect	desired Rectangle for initial position
     * @param	factor	start date of the deal
     * @param	offsetx		end date of the deal
     * @param	offsety		end date of the deal
     *
     */
    public AbstractEntity(Rectangle rect, float factor, float offsetx, float offsety) {
        this.hitBox = new Rectangle(offsetx+rect.getCenterX(), offsety+rect.getCenterY(), rect.getWidth()-10.0f, rect.getWidth()-10.0f);
        this.shape = new Rectangle(offsetx+rect.getCenterX(), offsety+rect.getCenterY(), rect.getWidth(), rect.getWidth());
        this.factor = factor;
        this.offsetx = offsetx;
        this.offsety = offsety;
    }

    /**
     * Checks if the given rectangle intersected with the SHAPE of the entity.
     *
     * @param	otherHitBox	the rectangle of the entity to test
     * @return  true if there is an intersection, false otherwise
     *
     */
    public boolean checkCollision(Rectangle otherHitBox)
    {
        return shape.intersect(otherHitBox);
    }


    /**
     * Checks if the given rectangle intersected with the HIT BOX of the entity.
     *
     * @param	otherHitBox	the rectangle of the entity to test
     * @return  true if there is an intersection, false otherwise
     *
     */
    public boolean checkHit(Rectangle otherHitBox)
    {
        return hitBox.intersect(otherHitBox);
    }

    /**
     * Moves the entity to the given point.
     *
     * <P>Note: This function doesn't check if the position is within any boundaries.
     *
     * @param	x	the desired x
     * @param	y	the desired y
     *
     */
    public void moveToPoint(float x, float y)
    {
        hitBox.setCenterX(x);
        hitBox.setCenterY(y);

        shape.setCenterX(x);
        shape.setCenterY(y);
    }

    /**
     * Moves the entity to the given point.
     *
     * <P>Note: This function doesn't check if the position is within any boundaries.
     *
     * @param	newPosition	the desired new position
     *
     */
    public void moveToPoint(Rectangle newPosition)
    {
        hitBox.setCenterX(newPosition.getCenterX());
        hitBox.setCenterY(newPosition.getCenterY());

        shape.setCenterX(newPosition.getCenterX());
        shape.setCenterY(newPosition.getCenterY());
    }

    /**
     * Generates the vertices for the entity
     *
     * @return	a array of float, containing the 3 coordinates
     * of the 4 vertex composing the rectangles
     *
     */
    public float[] generateVertices()
    {
        // Own vertices of the entity
        float[] vertices = new float[4*3];

        float x1 = shape.getCenterX() - shape.getWidth();
        float x2 = shape.getCenterX() + shape.getWidth();
        float y1 = shape.getCenterY() - shape.getHeight();
        float y2 = shape.getCenterY() + shape.getHeight();

        /*Log.i(TAG, "x1 = " + x1);
        Log.i(TAG, "x2 = " + x2);
        Log.i(TAG, "y1 = " + y1);
        Log.i(TAG, "y2 = " + y2);*/

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

    /*******************GETTERS AND SETTERS*******************/

    /**
     * Getter for the Hit Box.
     *
     * @return	the rectangle corresponding to the Hit Box
     *
     */
    public Rectangle getHitBox() {
        return hitBox;
    }

    /**
     * Getter for the Shape.
     *
     * @return	the rectangle corresponding to the Shape
     *
     */
    public Rectangle getShape() {
        return shape;
    }

    /**
     * Getter for the Indices.
     *
     * @return	the indices that need to be plot
     *
     */
    public short[] getIndices() {
        return indices;
    }

    /**
     * Setter for the move parameters.
     *
     * @param direction the desired direction
     * @param speed     the desired speed
     *
     */
    public void setMoveParameters(Direction direction, float speed)
    {
        this.currentDirection = direction;
        this.speed = speed;
    }

    /**
     * Defaults the Direction to LEFT.
     * Resets the speed to 0.
     *
     */
    public void stopMoving()
    {
        this.currentDirection = Direction.LEFT;
        this.speed = 0.0f;
    }

    /**
     * Getter for the Direction.
     *
     * @return	the current direction
     *
     */
    public Direction getCurrentDirection() {
        return currentDirection;
    }

    /**
     * Getter for the Speed.
     *
     * @return	the current speed
     *
     */
    public float getSpeed() {
        return speed;
    }
}
