package com.cocorporation.androidman;

import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/26/2015.
 */
public class Mover {
    private static final String TAG = "Mover";

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
                            if (speed != 0.0f) {
                                Rectangle newPosition;
                                int collidedRectangleIndice;
                                switch (direction) {
                                    case UP:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX(),
                                                entity.getShape().getCenterY() + speed,
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                            else
                                        {
                                            Rectangle newPositionAdjusted = new Rectangle(
                                                    newPosition.getCenterX(),
                                                    (float)(int)(backgroundRectangles.get(collidedRectangleIndice).getY1()-newPosition.getHeight()),
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                            entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                    case DOWN:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX(),
                                                entity.getShape().getCenterY() - speed,
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted = new Rectangle(
                                                    newPosition.getCenterX(),
                                                    ((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getY2()+newPosition.getHeight()))+1.0f,
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                            entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                    case LEFT:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX() - speed,
                                                entity.getShape().getCenterY(),
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted = new Rectangle(
                                                    ((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX2()+newPosition.getWidth()))+1.0f,
                                                    entity.getShape().getCenterY(),
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                            entity.moveToPoint(newPositionAdjusted);
                                        }
                                        break;
                                    case RIGHT:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX() + speed,
                                                entity.getShape().getCenterY(),
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1)
                                        {
                                            entity.moveToPoint(newPosition);
                                        }
                                        else
                                        {
                                            Rectangle newPositionAdjusted = new Rectangle(
                                                    (float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX1()-newPosition.getWidth()),
                                                    entity.getShape().getCenterY(),
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
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

    public void slowMotionForGhosts()
    {
        //TBD
    }

    public void stopMovement()
    {
        //TBD
    }
}
