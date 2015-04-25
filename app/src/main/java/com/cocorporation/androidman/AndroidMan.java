package com.cocorporation.androidman;

/**
 * Created by Corentin on 4/19/2015.
 */
public class AndroidMan extends AbstractEntity {
    private boolean godModeOn = false;

    public AndroidMan()
    {
        super(0.0f, 0.0f, 25.0f, 1.0f, 0.0f, 0.0f);
    }

    public AndroidMan(float x, float y)
    {
        super(x, y, 25.0f, 1.0f, 0.0f, 0.0f);
    }

    public void setGodModeOn()
    {
        godModeOn = true;
    }
}
