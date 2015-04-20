package com.cocorporation.androidman;

/**
 * Created by Corentin on 4/19/2015.
 */

import android.graphics.Rect;

public class AbstractEntity {
    // Center of the entity
    private Rectangle hitBox;
    private Rectangle shape;

    public AbstractEntity(float x, float y, float width) {
        this.hitBox = new Rectangle(x, y, (width-10.0f));
        this.shape = new Rectangle(x, y, width);
    }

    public AbstractEntity(Rectangle rect) {
        this.hitBox = new Rectangle(rect.getCenterX(), rect.getCenterY(), (rect.getWidth()-10.0f));
        this.shape = rect;
    }

    public boolean checkCollision(Rectangle otherHitBox)
    {
        return shape.intersect(otherHitBox);
    }

    public boolean checkHit(Rectangle otherHitBox)
    {
        return hitBox.intersect(otherHitBox);
    }
}
