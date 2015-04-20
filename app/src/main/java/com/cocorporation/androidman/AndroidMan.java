package com.cocorporation.androidman;

/**
 * Created by Corentin on 4/19/2015.
 */
public class AndroidMan extends AbstractEntity {
    private boolean godModeOn = false;

    public AndroidMan()
    {
        super(0, 0, 25);
    }

    public AndroidMan(float x, float y)
    {
        super(x, y, 25);
    }

    public void setGodModeOn()
    {
        godModeOn = true;
    }
}
