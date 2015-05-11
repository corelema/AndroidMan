package com.cocorporation.androidman.graphics;

import android.graphics.Color;

/**
 * Created by Corentin on 4/19/2015.
 */
public class Ghost extends AbstractEntity {
    private int color;

    public Ghost()
    {
        super(0.0f, 0.0f, 25.0f, 1.0f, 0.0f, 0.0f);
        color=Color.RED;
    }

    public Ghost(float x, float y)
    {
        super(x, y, 25.0f, 1.0f, 0.0f, 0.0f);
        color=Color.RED;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
