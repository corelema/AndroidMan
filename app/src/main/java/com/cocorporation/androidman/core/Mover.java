package com.cocorporation.androidman.core;

import android.graphics.Rect;
import android.util.Log;

import com.cocorporation.androidman.graphics.Background;
import com.cocorporation.androidman.graphics.Direction;
import com.cocorporation.androidman.messages.MessagesManager;
import com.cocorporation.androidman.messages.MessagesType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Corentin on 4/26/2015.
 */
public class Mover {
    private static final String TAG = "Mover";

    public static final int NO_COLLISION = -1;

    private static final int numberOfNeighboorsToTest = 40;
    private static final float colleteralStepsCheck = 5.0f;
    private static final float SPACE_BETWEEN_ENTITY_AND_WALL = 1.0f;
    private static final float SMALLEST_STEP_FURTHER = 1.0f;
    private static final float STEP_AHEAD_TO_CHECK = 5.0f;
    private static final float SPEED_NULL = 0.0f;

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

    public void methodTest() {
        System.out.println("test");


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
                            updateOneStepPosition(speed, shape, currentDirection);
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

    private void updateOneStepPosition(float speed, Rectangle currentShape, Direction direction) {
        boolean positionUpdated = false;
        Rectangle newPosition = newPositionFromDirectionAndSpeed(speed, currentShape, direction);
        int collidedRectangleIndice = collisionWithBackground(newPosition);
        if (!isCollided(collidedRectangleIndice))
        {
            positionUpdated = true;
        }
        else {
            Rectangle newPositionAdjusted;
            if (isAlreadyAgainstWall(collidedRectangleIndice, newPosition, currentShape, direction)) {
                newPositionAdjusted = stepCloserToNearestPath(androidMan, direction);
            } else {
                newPositionAdjusted = newAdjustedPositionFromNewPositionAndCollidedRectangle(newPosition, collidedRectangleIndice, direction);
            }

            if (newPositionAdjusted != null)
            {
                newPosition = newPositionAdjusted;
                positionUpdated = true;
            }
        }

        if (positionUpdated) {
            checkBoundaries(newPosition);
            androidMan.moveToPoint(newPosition);
            eatApplesTouched(androidMan);
        }
    }

    private void checkBoundaries(Rectangle position)
    {
        if (position.getCenterX() > maxX)
        {
            position.setCenterX(minX + position.getCenterX() - maxX);
        } else if (position.getCenterX() < minX)
        {
            position.setCenterX(maxX - position.getCenterX() + minX);
        }
    }

    private boolean isAlreadyAgainstWall(int collidedRectangleIndice, Rectangle newPosition, Rectangle currentShape, Direction direction) {
        boolean isAlreadyAgainstWall = true; //defaulted to true to avoid allowing to go through walls.

        float closestPosition = 0.0f;
        switch (direction) {
            case UP:
                closestPosition = (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight());
                isAlreadyAgainstWall = (closestPosition - SPACE_BETWEEN_ENTITY_AND_WALL == currentShape.getCenterY());
                break;

            case DOWN:
                closestPosition = (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY2() + newPosition.getHeight());
                isAlreadyAgainstWall = (closestPosition + SPACE_BETWEEN_ENTITY_AND_WALL == currentShape.getCenterY());
                break;

            case LEFT:
                closestPosition = (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX2() + newPosition.getWidth());
                isAlreadyAgainstWall = (closestPosition + SPACE_BETWEEN_ENTITY_AND_WALL == currentShape.getCenterX());
                break;

            case RIGHT:
                closestPosition = (float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX1() - newPosition.getWidth());
                isAlreadyAgainstWall = (closestPosition - SPACE_BETWEEN_ENTITY_AND_WALL == currentShape.getCenterX());
                break;
        }

        return isAlreadyAgainstWall;
    }

    private Rectangle newPositionFromDirectionAndSpeed(float speed, Rectangle shape, Direction direction) {
        Rectangle newPosition = null;
        switch (direction) {
            case UP:
                newPosition = shape.addToY(speed);
                break;

            case DOWN:
                newPosition = shape.addToY(-speed);
                break;

            case LEFT:
                newPosition = shape.addToX(-speed);
                break;

            case RIGHT:
                newPosition = shape.addToX(speed);
                break;
        }

        return newPosition;
    }

    private Rectangle newAdjustedPositionFromNewPositionAndCollidedRectangle(Rectangle newPosition, int collidedRectangleIndice, Direction direction) {
        Rectangle newPositionAdjusted = null;
        switch (direction) {
            case UP:
                newPositionAdjusted = newPosition.replaceY((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY1() - newPosition.getHeight()) - SPACE_BETWEEN_ENTITY_AND_WALL);
                break;

            case DOWN:
                newPositionAdjusted = newPosition.replaceY((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getY2() + newPosition.getHeight() + SPACE_BETWEEN_ENTITY_AND_WALL));
                break;

            case LEFT:
                newPositionAdjusted = newPosition.replaceX((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX2() + newPosition.getWidth()) + SPACE_BETWEEN_ENTITY_AND_WALL);
                break;

            case RIGHT:
                newPositionAdjusted = newPosition.replaceX((float) (int) (backgroundRectangles.get(collidedRectangleIndice).getX1() - newPosition.getWidth()) - SPACE_BETWEEN_ENTITY_AND_WALL);
                break;
        }
        return newPositionAdjusted;
    }

    private boolean isCollided(int collidedRectangleIndice) {
        return !(collidedRectangleIndice == NO_COLLISION);
    }

    /**
     * Use whenever we want to check if it collides with the background
     * but we don't want to keep the indice of the collided rectangle.*/
    private boolean isCollided(Rectangle rectangle) {
        int collidedRectangleIndice = collisionWithBackground(rectangle);
        return isCollided(collidedRectangleIndice);
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
                            updateGhostDirection(ghost);
                            Rectangle shape = ghost.getShape();
                            if (ghost.getSpeed() != SPEED_NULL) {
                                updateOneStepPositionGhost(ghost, shape);
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

    private void updateOneStepPositionGhost(Ghost ghost, Rectangle shape) {
        Rectangle newPosition;
        Direction direction = ghost.getCurrentDirection();
        newPosition = newPositionFromDirectionAndSpeed(ghost.getSpeed(),shape, direction);
        int collidedRectangleIndice = collisionWithBackground(newPosition);
        if (isCollided(collidedRectangleIndice)) {
            Rectangle newPositionAdjusted;
            if (isAlreadyAgainstWall(collidedRectangleIndice, newPosition, shape, direction)) {
                newPositionAdjusted = stepCloserToNearestPath(ghost, direction);
            } else {
                newPositionAdjusted = newAdjustedPositionFromNewPositionAndCollidedRectangle(newPosition, collidedRectangleIndice, direction);
            }
            if (newPositionAdjusted != null) {
                ghost.moveToPoint(newPositionAdjusted);
            }
            else
            {
                ghost.resetDirectionCountDown();
            }
        } else {
            checkBoundaries(newPosition);
            ghost.moveToPoint(newPosition);
        }
    }

    private Direction updateGhostDirection(Ghost ghost) {
        Direction currentDirection = ghost.getCurrentDirection();

        if (ghost.canChangeDirection()) {
            List<Direction> possibleDirections = findPossibleDirections(ghost);

            if (possibleDirections.size() == 1) {
                if (possibleDirections.get(0) != currentDirection)//direction change and only one choice
                {
                    currentDirection = possibleDirections.get(0);
                    ghost.setDirection(currentDirection, true);
                }
                //no else necessary, as it means that we are staying in the same direction
            } else if (possibleDirections.size() != 0)//more than one direction is possible, we have to pick one and stick to it for a while
            {
                int newDirection = (int) Math.floor(Math.random() * possibleDirections.size());
                currentDirection = possibleDirections.get(newDirection);
                ghost.setDirection(currentDirection, true);
            } else {
                //throw error, ghost is stuck
            }
        }

        return currentDirection;
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
        return NO_COLLISION;
    }

    private List<Direction> findPossibleDirections(AbstractEntity entity) {
        Direction currentDirection = entity.currentDirection;
        Rectangle currentPosition = entity.getShape();
        Rectangle testPosition;
        float currentSpeed = entity.getSpeed();

        Set<Direction> directionsPossible = new HashSet<>();

        switch(currentDirection)
        {
            case UP:
                testPosition = currentPosition.addToY(currentSpeed + SMALLEST_STEP_FURTHER);

                if (!isCollided(testPosition))
                    directionsPossible.add(Direction.UP);

                collateralLeftRightCheck(currentPosition, directionsPossible);
                break;
            case DOWN:
                testPosition = currentPosition.addToY(- currentSpeed - SMALLEST_STEP_FURTHER);

                if (!isCollided(testPosition))
                    directionsPossible.add(Direction.DOWN);

                collateralLeftRightCheck(currentPosition, directionsPossible);
                break;
            case LEFT:
                testPosition = currentPosition.addToX(- currentSpeed - SMALLEST_STEP_FURTHER);

                if (!isCollided(testPosition))
                    directionsPossible.add(Direction.LEFT);

                collateralUpDownCheck(currentPosition, directionsPossible);
                break;
            case RIGHT:
                testPosition = currentPosition.addToX(currentSpeed + SMALLEST_STEP_FURTHER);

                if (!isCollided(testPosition))
                    directionsPossible.add(Direction.RIGHT);

                collateralUpDownCheck(currentPosition, directionsPossible);
                break;
        }

        return  new ArrayList<>(directionsPossible);
    }

    private void collateralUpDownCheck(Rectangle currentPosition, Set<Direction> directionsPossible) {
        Rectangle testPosition = currentPosition.addToY(-colleteralStepsCheck);

        if (!isCollided(testPosition)) //down is ok
            directionsPossible.add(Direction.DOWN);

        testPosition = currentPosition.addToY(colleteralStepsCheck);

        if (!isCollided(testPosition)) //up is ok
            directionsPossible.add(Direction.UP);
    }

    private void collateralLeftRightCheck(Rectangle currentPosition, Set<Direction> directionsPossible) {
        Rectangle testPosition = currentPosition.addToX(-colleteralStepsCheck);

        if (!isCollided(testPosition)) //left is ok
            directionsPossible.add(Direction.LEFT);

        testPosition = currentPosition.addToX(colleteralStepsCheck);

        if (!isCollided(testPosition)) //right is ok
            directionsPossible.add(Direction.RIGHT);
    }

    private Rectangle stepCloserToNearestPath(AbstractEntity entity, Direction direction)
    {
        Rectangle shape = entity.getShape();
        float speed = entity.getSpeed();
        Rectangle newPosition = null;
        Rectangle testPosition;
        for (int i = 0 ; i < numberOfNeighboorsToTest ; i++)
        {
            float incrementMax = (float) i;
            testPosition = sideStepToTest(shape, incrementMax, direction);
            if (!isCollided(testPosition))
            {
                newPosition = getFurtherPositionPossible(direction, shape, speed, incrementMax);
            } else {
                testPosition = sideStepToTest(shape, - incrementMax, direction);
                if (!isCollided(testPosition))
                {
                    newPosition = getFurtherPositionPossible(direction, shape, - speed, - incrementMax);
                }
            }
            if (newPosition != null)
            {
                break;
            }
        }
        return newPosition;
    }

    private Rectangle getFurtherPositionPossible(Direction direction, Rectangle shape, float speed, float incrementMax) {
        Rectangle newPosition = sideStepToGetTo(shape, incrementMax, direction);
        if (isOutOfReachInOneStep(shape, newPosition, speed))
            newPosition = sideStepToGetTo(shape, speed, direction);
        return newPosition;
    }

    private Rectangle sideStepToGetTo(Rectangle shape, float increment, Direction direction) {
        Rectangle newPosition = null;

        switch (direction) {
            case UP:
            case DOWN:
                newPosition = shape.addToX(increment);
                break;

            case LEFT:
            case RIGHT:
                newPosition = shape.addToY(increment);
                break;
        }
        return newPosition;
    }

    private Rectangle sideStepToTest(Rectangle shape, float increment, Direction direction) {
        Rectangle testPosition = null;
        switch (direction) {
            case UP:
                testPosition = shape.addToX(increment).addToY(STEP_AHEAD_TO_CHECK);
                break;

            case DOWN:
                testPosition = shape.addToX(increment).addToY(- STEP_AHEAD_TO_CHECK);
                break;

            case LEFT:
                testPosition = shape.addToX(- STEP_AHEAD_TO_CHECK).addToY(increment);
                break;

            case RIGHT:
                testPosition = shape.addToX(STEP_AHEAD_TO_CHECK).addToY(increment);
                break;
        }
        return testPosition;
    }

    private boolean isOutOfReachInOneStep(Rectangle currentPosition, Rectangle newPosition, float speed) {
        return abs(newPosition.getCenterX(), currentPosition.getCenterX()) > speed;
    }

    private void eatApplesTouched(AbstractEntity entity)
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
