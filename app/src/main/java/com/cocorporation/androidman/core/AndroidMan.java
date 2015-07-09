package com.cocorporation.androidman.core;

/**
 * Created by Corentin on 4/19/2015.
 */
public class AndroidMan extends AbstractEntity {
    private boolean godModeOn = false;

    public AndroidMan()
    {
        super(0.0f, 0.0f, 25.0f, 1.0f, 0.0f, 0.0f);
    }

    public AndroidMan(float posX, float posY, float width, float factor, float offsetx, float offsety)
    {
        super(posX, posY, width, factor, offsetx, offsety);
    }

    public void setGodModeOn()
    {
        godModeOn = true;
    }
}
