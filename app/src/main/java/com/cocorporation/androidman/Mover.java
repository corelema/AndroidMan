package com.cocorporation.androidman;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/26/2015.
 */
public class Mover {
    private static final String TAG = "Mover";

    private int numberOfNeighboorsToTest = 40;

    private List<Rectangle> backgroundRectangles;
    private List<AbstractEntity> movingEntities;

    public Mover(Background background)
    {
        this.backgroundRectangles = background.getRectangleList();
        movingEntities = new ArrayList<AbstractEntity>();
    }

    public void addMovingEntity(AbstractEntity entity)
    {
        movingEntities.add(entity);
    }

    public boolean removeMovingEntity(AbstractEntity entity)
    {
        return movingEntities.remove(entity);
    }

    public void launchMovement()
    {
        Thread motionThread = new Thread(){
            public void run(){
                try
                {
                    while(true)
                    {
                        for (AbstractEntity entity : movingEntities) {
                            //Log.i(TAG, "Speed = " + speed);
                            float speed = entity.getSpeed();
                            Direction direction = entity.getDirection();
                            Rectangle shape = entity.getShape();
                            if (speed != 0.0f) {
                                Rectangle newPosition;
                                int collidedRectangleIndice;
                                switch (direction) {
                                    case UP:
                                        newPosition = new Rectangle(
                                                shape.getCenterX(),
                                                shape.getCenterY() + speed,
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted;
                                            if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getY1()-newPosition.getHeight()))-1.0f == shape.getCenterY())
                                            {
                                                newPositionAdjusted = findNearestPath(entity);
                                            }
                                            else {
                                                newPositionAdjusted = new Rectangle(
                                                        newPosition.getCenterX(),
                                                        (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight())-1.0f,
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted!=null)
                                                entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                    case DOWN:
                                        newPosition = new Rectangle(
                                                shape.getCenterX(),
                                                shape.getCenterY() - speed,
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted;
                                            if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getY2()+newPosition.getHeight()))+1.0f == shape.getCenterY())
                                            {
                                                newPositionAdjusted = findNearestPath(entity);
                                            }
                                            else {
                                                newPositionAdjusted = new Rectangle(
                                                        newPosition.getCenterX(),
                                                        (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY2()+newPosition.getHeight()+1.0f),
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted!=null)
                                                entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                    case LEFT:
                                        newPosition = new Rectangle(
                                                shape.getCenterX() - speed,
                                                shape.getCenterY(),
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted;
                                            if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX2()+newPosition.getWidth()))+1.0f == shape.getCenterX())
                                            {
                                                newPositionAdjusted = findNearestPath(entity);
                                            }
                                            else {
                                                newPositionAdjusted = new Rectangle(
                                                        ((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX2()+newPosition.getWidth()))+1.0f,
                                                        shape.getCenterY(),
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted!=null)
                                                entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                    case RIGHT:
                                        newPosition = new Rectangle(
                                                shape.getCenterX() + speed,
                                                shape.getCenterY(),
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted;
                                            if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX1()-newPosition.getWidth()))-1.0f == shape.getCenterX())
                                            {
                                                newPositionAdjusted = findNearestPath(entity);
                                            }
                                            else {
                                                newPositionAdjusted = new Rectangle(
                                                        ((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX1()-newPosition.getWidth()))-1.0f,
                                                        shape.getCenterY(),
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted!=null)
                                                entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                }
                            }
                            Thread.sleep(10);
                        }
                    }
                }
                catch(Throwable t)
                {

                }
                finally
                {

                }
            }
        };
        motionThread.start();
    }

    private int collisionWithBackground(Rectangle newPosition)
    {
        for (Rectangle rec : backgroundRectangles)
        {
            if (newPosition.intersect(rec))
            {
                Log.i(TAG, "Intersect with rectangle number " + backgroundRectangles.indexOf(rec));
                return backgroundRectangles.indexOf(rec);
            }
        }
        return -1;
    }

    private Rectangle findNearestPath(AbstractEntity entity)
    {
        Direction direction = entity.getDirection();
        Rectangle shape = entity.getShape();
        float speed = entity.getSpeed();
        Rectangle newPosition = null;
        Rectangle testPosition;
        for (int i=0;i<numberOfNeighboorsToTest;i++)
        {
            switch (direction)
            {
                case UP:
                    testPosition = new Rectangle(
                            shape.getCenterX() + (float)i,
                            shape.getCenterY() + 5.0f,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterY(shape.getCenterY());
                        if (abs(newPosition.getCenterX(), shape.getCenterX()) > speed)
                        {
                            if (newPosition.getCenterX() > shape.getCenterX())
                                newPosition.setCenterX(shape.getCenterX()+speed);
                            else
                                newPosition.setCenterX(shape.getCenterX()-speed);
                        }
                        break;
                    }

                    testPosition = new Rectangle(
                            shape.getCenterX() - (float)i,
                            shape.getCenterY() + 5.0f,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterY(shape.getCenterY());
                        if (abs(newPosition.getCenterX(), shape.getCenterX()) > speed)
                        {
                            if (newPosition.getCenterX() > shape.getCenterX())
                                newPosition.setCenterX(shape.getCenterX()+speed);
                            else
                                newPosition.setCenterX(shape.getCenterX()-speed);
                        }
                        break;
                    }
                    break;

                case DOWN:
                    testPosition = new Rectangle(
                            shape.getCenterX() + (float)i,
                            shape.getCenterY() - 5.0f,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterY(shape.getCenterY());
                        if (abs(newPosition.getCenterX(), shape.getCenterX()) > speed)
                        {
                            if (newPosition.getCenterX() > shape.getCenterX())
                                newPosition.setCenterX(shape.getCenterX()+speed);
                            else
                                newPosition.setCenterX(shape.getCenterX()-speed);
                        }
                        break;
                    }

                    testPosition = new Rectangle(
                            shape.getCenterX() - (float)i,
                            shape.getCenterY() - 5.0f,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterY(shape.getCenterY());
                        if (abs(newPosition.getCenterX(), shape.getCenterX()) > speed)
                        {
                            if (newPosition.getCenterX() > shape.getCenterX())
                                newPosition.setCenterX(shape.getCenterX()+speed);
                            else
                                newPosition.setCenterX(shape.getCenterX()-speed);
                        }
                        break;
                    }
                    break;

                case LEFT:
                    testPosition = new Rectangle(
                            shape.getCenterX() - 5.0f,
                            shape.getCenterY() + (float)i,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterX(shape.getCenterX());
                        if (abs(newPosition.getCenterY(), shape.getCenterY()) > speed)
                        {
                            if (newPosition.getCenterY() > shape.getCenterY())
                                newPosition.setCenterY(shape.getCenterY() + speed);
                            else
                                newPosition.setCenterY(shape.getCenterY() - speed);
                        }
                        break;
                    }

                    testPosition = new Rectangle(
                            shape.getCenterX() - 5.0f,
                            shape.getCenterY() - (float)i,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterX(shape.getCenterX());
                        if (abs(newPosition.getCenterY(), shape.getCenterY()) > speed)
                        {
                            if (newPosition.getCenterY() > shape.getCenterY())
                                newPosition.setCenterY(shape.getCenterY() + speed);
                            else
                                newPosition.setCenterY(shape.getCenterY() - speed);
                        }
                        break;
                    }
                    break;

                case RIGHT:
                    testPosition = new Rectangle(
                            shape.getCenterX() + 5.0f,
                            shape.getCenterY() + (float)i,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterX(shape.getCenterX());
                        if (abs(newPosition.getCenterY(), shape.getCenterY()) > speed)
                        {
                            if (newPosition.getCenterY() > shape.getCenterY())
                                newPosition.setCenterY(shape.getCenterY() + speed);
                            else
                                newPosition.setCenterY(shape.getCenterY() - speed);
                        }
                        break;
                    }

                    testPosition = new Rectangle(
                            shape.getCenterX() + 5.0f,
                            shape.getCenterY() - (float)i,
                            shape.getWidth(),
                            shape.getHeight());
                    if (collisionWithBackground(testPosition) == -1)
                    {
                        newPosition = testPosition;
                        newPosition.setCenterX(shape.getCenterX());
                        if (abs(newPosition.getCenterY(), shape.getCenterY()) > speed)
                        {
                            if (newPosition.getCenterY() > shape.getCenterY())
                                newPosition.setCenterY(shape.getCenterY()+speed);
                            else
                                newPosition.setCenterY(shape.getCenterY()-speed);
                        }
                        break;
                    }
                    break;
            }
            if (newPosition != null)
            {
                break;
            }
        }
        return newPosition;
    }

    private float abs(float f1, float f2)
    {
        return (f1-f2>0?f1-f2:f2-f1);
    }

    public void slowMotionForGhosts()
    {
        //TBD
    }

    public void stopMovement()
    {
        //TBD
    }
}
