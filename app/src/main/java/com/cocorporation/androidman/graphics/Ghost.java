package com.cocorporation.androidman.graphics;

import android.graphics.Color;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Ghost extends AbstractEntity {
    private int color;
    private Direction lastDirection;

    private int changeDirectionCountDown;
    private static final int changeDirectionReset = 10;

    public Ghost()
    {
        super(0.0f, 0.0f, 25.0f, 1.0f, 0.0f, 0.0f);
        color=Color.RED;
        currentDirection = Direction.RIGHT;
        lastDirection = Direction.RIGHT;
        changeDirectionCountDown = 0;
        speed = 2.0f;
    }

    public Ghost(float posX, float posY, float width, float factor, float offsetx, float offsety)
    {
        super(posX, posY, width, factor, offsetx, offsety);
        color=Color.RED;
        currentDirection = Direction.RIGHT;
        lastDirection = Direction.RIGHT;
        changeDirectionCountDown = 0;
        speed = 2.0f;
    }

    @Override
    public void moveToPoint(float x, float y)
    {
        super.moveToPoint(x, y);
        if (--changeDirectionCountDown < 0)
            changeDirectionCountDown = 0;
    }

    @Override
    public void moveToPoint(Rectangle newPosition)
    {
        super.moveToPoint(newPosition);
        if (--changeDirectionCountDown < 0)
            changeDirectionCountDown = 0;
    }

    @Override
    public void setMoveParameters(Direction direction, float speed)
    {
        if (changeDirectionCountDown == 0)
        {
            super.setMoveParameters(direction, speed);
            this.currentDirection = direction;
            this.speed = speed;
        }
        changeDirectionCountDown = changeDirectionReset;
    }

    public void setDirection(Direction direction, boolean resetCountDown) {
        if (changeDirectionCountDown == 0)
        {
            this.currentDirection = direction;
        }
        if (resetCountDown)
            changeDirectionCountDown = changeDirectionReset;
    }

    public void resetDirectionCountDown()
    {
        changeDirectionCountDown = 0;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    public int getChangeDirectionCountDown() {
        return changeDirectionCountDown;
    }
}
