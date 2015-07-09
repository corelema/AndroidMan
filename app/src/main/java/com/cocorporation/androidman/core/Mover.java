package com.cocorporation.androidman.core;

import android.util.Log;

import com.cocorporation.androidman.graphics.Background;
import com.cocorporation.androidman.graphics.Direction;
import com.cocorporation.androidman.messages.MessagesManager;
import com.cocorporation.androidman.messages.MessagesType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/26/2015.
 */
public class Mover {
    private static final String TAG = "Mover";

    private static final int numberOfNeighboorsToTest = 40;
    private static final float colleteralStepsCheck = 5.0f;

    private List<Rectangle> backgroundRectangles;
    private List<Ghost> ghosts;
    private List<Rectangle> apples;
    private AndroidMan androidMan;

    private float minX;
    private float maxX;

    public Mover(Background background)
    {
        this.backgroundRectangles = background.getRectangleList();
        this.apples = background.getAppleList();
        ghosts = new ArrayList<Ghost>();
        this.minX = background.getMinX();
        this.maxX = background.getMaxX();
    }

    public void addMovingGhost(Ghost ghost)
    {
        ghosts.add(ghost);
    }

    public boolean freezeGhost(Ghost ghost)
    {
        return ghosts.remove(ghost);
    }

    public void launchMovementAndroidMan()
    {
        Thread motionThread = new Thread(){
            public void run(){
                try
                {
                    while(true)
                    {
                        //Log.i(TAG, "Speed = " + speed);
                        float speed = androidMan.getSpeed();
                        Direction currentDirection = androidMan.getCurrentDirection();
                        Rectangle shape = androidMan.getShape();
                        if (speed != 0.0f) {
                            Rectangle newPosition;
                            int collidedRectangleIndice;
                            switch (currentDirection) {
                                case UP:
                                    newPosition = new Rectangle(
                                            shape.getCenterX(),
                                            shape.getCenterY() + speed,
                                            shape.getWidth(),
                                            shape.getHeight());
                                    collidedRectangleIndice = collisionWithBackground(newPosition);
                                    if (collidedRectangleIndice == -1)
                                    {
                                        androidMan.moveToPoint(newPosition);
                                        checkCollisionWithApples(androidMan);
                                    }
                                    else {
                                        Rectangle newPositionAdjusted;
                                        if (((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight())) - 1.0f == shape.getCenterY()) {
                                            newPositionAdjusted = findNearestPath(androidMan);
                                        } else {
                                            newPositionAdjusted = new Rectangle(
                                                    newPosition.getCenterX(),
                                                    (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight()) - 1.0f,
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                        }
                                        if (newPositionAdjusted != null)
                                        {
                                            androidMan.moveToPoint(newPositionAdjusted);
                                            checkCollisionWithApples(androidMan);
                                        }
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
                                        androidMan.moveToPoint(newPosition);
                                        checkCollisionWithApples(androidMan);
                                    }
                                    else
                                    {
                                        Rectangle newPositionAdjusted;
                                        if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getY2()+newPosition.getHeight()))+1.0f == shape.getCenterY())
                                        {
                                            newPositionAdjusted = findNearestPath(androidMan);
                                        }
                                        else {
                                            newPositionAdjusted = new Rectangle(
                                                    newPosition.getCenterX(),
                                                    (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY2()+newPosition.getHeight()+1.0f),
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                        }
                                        if (newPositionAdjusted!=null)
                                        {
                                            androidMan.moveToPoint(newPositionAdjusted);
                                        }
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
                                        if (newPosition.getCenterX() < minX)
                                        {
                                            newPosition = new Rectangle(
                                                    shape.getCenterX() - speed + maxX,
                                                    shape.getCenterY(),
                                                    shape.getWidth(),
                                                    shape.getHeight());
                                        }
                                        androidMan.moveToPoint(newPosition);
                                        checkCollisionWithApples(androidMan);
                                    }
                                    else
                                    {
                                        Rectangle newPositionAdjusted;
                                        if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX2()+newPosition.getWidth()))+1.0f == shape.getCenterX())
                                        {
                                            newPositionAdjusted = findNearestPath(androidMan);
                                        }
                                        else {
                                            newPositionAdjusted = new Rectangle(
                                                    ((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX2()+newPosition.getWidth()))+1.0f,
                                                    shape.getCenterY(),
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                        }
                                        if (newPositionAdjusted!=null)
                                        {
                                            androidMan.moveToPoint(newPositionAdjusted);
                                            checkCollisionWithApples(androidMan);
                                        }
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
                                        if (newPosition.getCenterX() > maxX)
                                        {
                                            newPosition = new Rectangle(
                                                    minX + shape.getCenterX() - speed - maxX,
                                                    shape.getCenterY(),
                                                    shape.getWidth(),
                                                    shape.getHeight());
                                        }
                                        androidMan.moveToPoint(newPosition);
                                        checkCollisionWithApples(androidMan);
                                    }
                                    else
                                    {
                                        Rectangle newPositionAdjusted;
                                        if (((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX1()-newPosition.getWidth()))-1.0f == shape.getCenterX())
                                        {
                                            newPositionAdjusted = findNearestPath(androidMan);
                                        }
                                        else {
                                            newPositionAdjusted = new Rectangle(
                                                    ((float)(int)(backgroundRectangles.get(collidedRectangleIndice).getX1()-newPosition.getWidth()))-1.0f,
                                                    shape.getCenterY(),
                                                    newPosition.getWidth(),
                                                    newPosition.getHeight());
                                        }
                                        if (newPositionAdjusted!=null)
                                        {
                                            androidMan.moveToPoint(newPositionAdjusted);
                                            checkCollisionWithApples(androidMan);
                                        }
                                    }
                                    break;
                            }
                        }
                        Thread.sleep(10);
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

    public void launchMovementGhosts()
    {
        Thread motionThread = new Thread(){
            public void run(){
                try
                {
                    while(true) {
                        for (Ghost ghost : ghosts) {
                            //Log.i(TAG, "Speed = " + speed);
                            float speed = ghost.getSpeed();
                            Direction currentDirection = ghost.getCurrentDirection();
                            Direction lastDirection = ghost.getLastDirection();
                            Rectangle shape = ghost.getShape();
                            if (speed != 0.0f) {
                                Rectangle newPosition;
                                int collidedRectangleIndice;
                                if (ghost.getChangeDirectionCountDown() == 0)
                                {
                                    List<Direction> possibleDirections = findPossibleDirections(ghost);

                                    if (possibleDirections.size() == 1)
                                    {
                                        if (possibleDirections.get(0) != currentDirection)//direction change and only one choice
                                        {
                                            currentDirection = possibleDirections.get(0);
                                            ghost.setDirection(currentDirection, true);
                                        }
                                        //no else necessary, as it means that we are staying in the same direction
                                    }
                                    else if (possibleDirections.size() != 0)//more than one direction is possible, we have to pick one and stick to it for a while
                                    {
                                        int newDirection = (int) Math.floor(Math.random()*possibleDirections.size());
                                        currentDirection = possibleDirections.get(newDirection);
                                        ghost.setDirection(currentDirection, true);
                                    }
                                }

                                switch (currentDirection) {
                                    case UP:
                                        newPosition = new Rectangle(
                                                shape.getCenterX(),
                                                shape.getCenterY() + speed,
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1) {
                                            ghost.moveToPoint(newPosition);
                                        } else {
                                            Rectangle newPositionAdjusted;
                                            if (((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight())) - 1.0f == shape.getCenterY()) {
                                                newPositionAdjusted = findNearestPath(ghost);
                                            } else {
                                                newPositionAdjusted = new Rectangle(
                                                        newPosition.getCenterX(),
                                                        (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight()) - 1.0f,
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted != null) {
                                                ghost.moveToPoint(newPositionAdjusted);
                                            }
                                            else
                                            {
                                                ghost.resetDirectionCountDown();
                                            }
                                        }
                                        break;
                                    case DOWN:
                                        newPosition = new Rectangle(
                                                shape.getCenterX(),
                                                shape.getCenterY() - speed,
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1) {
                                            ghost.moveToPoint(newPosition);
                                        } else {
                                            Rectangle newPositionAdjusted;
                                            if (((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY2() + newPosition.getHeight())) + 1.0f == shape.getCenterY()) {
                                                newPositionAdjusted = findNearestPath(ghost);
                                            } else {
                                                newPositionAdjusted = new Rectangle(
                                                        newPosition.getCenterX(),
                                                        (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY2() + newPosition.getHeight() + 1.0f),
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted != null) {
                                                ghost.moveToPoint(newPositionAdjusted);
                                            }
                                            else
                                            {
                                                ghost.resetDirectionCountDown();
                                            }
                                        }
                                        break;
                                    case LEFT:
                                        newPosition = new Rectangle(
                                                shape.getCenterX() - speed,
                                                shape.getCenterY(),
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1) {
                                            if (newPosition.getCenterX() < minX)
                                            {
                                                newPosition = new Rectangle(
                                                        shape.getCenterX() - speed + maxX,
                                                        shape.getCenterY(),
                                                        shape.getWidth(),
                                                        shape.getHeight());
                                            }
                                            ghost.moveToPoint(newPosition);
                                        } else {
                                            Rectangle newPositionAdjusted;
                                            if (((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX2() + newPosition.getWidth())) + 1.0f == shape.getCenterX()) {
                                                newPositionAdjusted = findNearestPath(ghost);
                                            } else {
                                                newPositionAdjusted = new Rectangle(
                                                        ((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX2() + newPosition.getWidth())) + 1.0f,
                                                        shape.getCenterY(),
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted != null) {
                                                ghost.moveToPoint(newPositionAdjusted);
                                            }
                                            else
                                            {
                                                ghost.resetDirectionCountDown();
                                            }
                                        }
                                        break;
                                    case RIGHT:
                                        newPosition = new Rectangle(
                                                shape.getCenterX() + speed,
                                                shape.getCenterY(),
                                                shape.getWidth(),
                                                shape.getHeight());
                                        collidedRectangleIndice = collisionWithBackground(newPosition);
                                        if (collidedRectangleIndice == -1) {
                                            if (newPosition.getCenterX() > maxX)
                                            {
                                                newPosition = new Rectangle(
                                                        minX + shape.getCenterX() - speed - maxX,
                                                        shape.getCenterY(),
                                                        shape.getWidth(),
                                                        shape.getHeight());
                                            }
                                            ghost.moveToPoint(newPosition);
                                        } else {
                                            Rectangle newPositionAdjusted;
                                            if (((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX1() - newPosition.getWidth())) - 1.0f == shape.getCenterX()) {
                                                newPositionAdjusted = findNearestPath(ghost);
                                            } else {
                                                newPositionAdjusted = new Rectangle(
                                                        ((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX1() - newPosition.getWidth())) - 1.0f,
                                                        shape.getCenterY(),
                                                        newPosition.getWidth(),
                                                        newPosition.getHeight());
                                            }
                                            if (newPositionAdjusted != null) {
                                                ghost.moveToPoint(newPositionAdjusted);
                                            }
                                            else
                                            {
                                                ghost.resetDirectionCountDown();
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                        Thread.sleep(10);
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

    private List<Direction> findPossibleDirections(AbstractEntity entity) {
        Direction currentDirection = entity.currentDirection;
        Rectangle currentPosition = entity.getShape();
        Rectangle testPosition1, testPosition2;
        float currentSpeed = entity.getSpeed();

        List<Direction> directionsPossible = new ArrayList<Direction>();

        if (currentDirection == Direction.UP || currentDirection == Direction.DOWN)
        {
            testPosition1 = new Rectangle(
                    currentPosition.getCenterX() - colleteralStepsCheck,
                    currentPosition.getCenterY(),
                    currentPosition.getWidth(),
                    currentPosition.getHeight());

            if (collisionWithBackground(testPosition1) == -1) //left is ok
                directionsPossible.add(Direction.LEFT);

            testPosition2 = new Rectangle(
                    currentPosition.getCenterX() + colleteralStepsCheck,
                    currentPosition.getCenterY(),
                    currentPosition.getWidth(),
                    currentPosition.getHeight());

            if (collisionWithBackground(testPosition2) == -1) //right is ok
                directionsPossible.add(Direction.RIGHT);

        }
        else //currentDirection == LEFT or RIGHT
        {
            testPosition1 = new Rectangle(
                    currentPosition.getCenterX(),
                    currentPosition.getCenterY() - colleteralStepsCheck,
                    currentPosition.getWidth(),
                    currentPosition.getHeight());

            if (collisionWithBackground(testPosition1) == -1) //down is ok
                directionsPossible.add(Direction.DOWN);

            testPosition2 = new Rectangle(
                    currentPosition.getCenterX(),
                    currentPosition.getCenterY() + colleteralStepsCheck,
                    currentPosition.getWidth(),
                    currentPosition.getHeight());

            if (collisionWithBackground(testPosition2) == -1) //up is ok
                directionsPossible.add(Direction.UP);
        }

        switch(currentDirection) // checking if we can continue moving forward
        {
            case UP:
                testPosition1 = new Rectangle(
                        currentPosition.getCenterX(),
                        currentPosition.getCenterY() + currentSpeed+1,
                        currentPosition.getWidth(),
                        currentPosition.getHeight());

                if (collisionWithBackground(testPosition1) == -1) //continuing up is ok
                    directionsPossible.add(Direction.UP);
                break;
            case DOWN:
                testPosition1 = new Rectangle(
                        currentPosition.getCenterX(),
                        currentPosition.getCenterY() - currentSpeed-1,
                        currentPosition.getWidth(),
                        currentPosition.getHeight());

                if (collisionWithBackground(testPosition1) == -1) //continuing down is ok
                    directionsPossible.add(Direction.DOWN);
                break;
            case LEFT:
                testPosition1 = new Rectangle(
                        currentPosition.getCenterX() - currentSpeed-1,
                        currentPosition.getCenterY(),
                        currentPosition.getWidth(),
                        currentPosition.getHeight());

                if (collisionWithBackground(testPosition1) == -1) //continuing left is ok
                    directionsPossible.add(Direction.LEFT);
                break;
            case RIGHT:
                testPosition1 = new Rectangle(
                        currentPosition.getCenterX() + currentSpeed+1,
                        currentPosition.getCenterY(),
                        currentPosition.getWidth(),
                        currentPosition.getHeight());

                if (collisionWithBackground(testPosition1) == -1) //continuing right is ok
                    directionsPossible.add(Direction.RIGHT);
                break;
        }

        return directionsPossible;
    }

    private Rectangle findNearestPath(AbstractEntity entity)
    {
        Direction direction = entity.getCurrentDirection();
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

    private void checkCollisionWithApples(AbstractEntity entity)
    {
        Rectangle androidManHitBox = entity.getHitBox();
        for (Rectangle apple : apples)
        {
            if (apple.intersect(androidManHitBox))
            {
                apples.remove(apple);
                MessagesManager.getInstance().sendMessage(MessagesType.DELETEAPPLE, null);
                Log.i(TAG, "Message sent");
                break;
            }
        }
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

    public void setAndroidMan(AndroidMan androidMan) {
        this.androidMan = androidMan;
    }
}
