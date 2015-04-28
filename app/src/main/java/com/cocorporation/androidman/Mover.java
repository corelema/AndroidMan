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
                                switch (direction) {
                                    case UP:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX(),
                                                entity.getShape().getCenterY() + speed,
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        if (!collisionWithBackground(newPosition))
                                            entity.moveToPoint(newPosition);
                                        break;
                                    case DOWN:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX(),
                                                entity.getShape().getCenterY() - speed,
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        if (!collisionWithBackground(newPosition))
                                            entity.moveToPoint(newPosition);
                                        break;
                                    case LEFT:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX() - speed,
                                                entity.getShape().getCenterY(),
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        if (!collisionWithBackground(newPosition))
                                            entity.moveToPoint(newPosition);
                                        break;
                                    case RIGHT:
                                        newPosition = new Rectangle(
                                                entity.getShape().getCenterX() + speed,
                                                entity.getShape().getCenterY(),
                                                entity.getShape().getWidth(),
                                                entity.getShape().getHeight());
                                        if (!collisionWithBackground(newPosition))
                                            entity.moveToPoint(newPosition);
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

    private boolean collisionWithBackground(Rectangle newPosition)
    {
        for (Rectangle rec : backgroundRectangles)
        {
            if (newPosition.intersect(rec))
            {
                Log.i(TAG, "Intersect with rectangle number " + backgroundRectangles.indexOf(rec));
                return true;
            }
        }
        return false;
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
