package com.cocorporation.androidman;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/26/2015.
 */
public class Mover {
    private Background background;
    private List<AbstractEntity> movingEntities;

    public Mover(Background background)
    {
        this.background = background;
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
                                switch (direction) {
                                    case UP:
                                        entity.moveToPoint(entity.getShape().getCenterX(), entity.getShape().getCenterY() + speed);
                                        break;
                                    case DOWN:
                                        entity.moveToPoint(entity.getShape().getCenterX(), entity.getShape().getCenterY() - speed);
                                        break;
                                    case LEFT:
                                        entity.moveToPoint(entity.getShape().getCenterX() - speed, entity.getShape().getCenterY());
                                        break;
                                    case RIGHT:
                                        entity.moveToPoint(entity.getShape().getCenterX() + speed, entity.getShape().getCenterY());
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

    public void slowMotionForGhosts()
    {
        //TBD
    }

    public void stopMovement()
    {
        //TBD
    }
}
