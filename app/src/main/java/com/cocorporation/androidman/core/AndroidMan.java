package com.cocorporation.androidman.core;

/**
 * AndroidMan Object.
 *
 * <P>Contains the particularities of AndroidMan, like the godMode.
 *
 * @author Corentin Leman
 * @version 1.0
 */
public class AndroidMan extends AbstractEntity {
    private boolean godModeOn = false;

    /**
     * Constructor.
     *
     * <P>Will default AndroidMan at 0,0 with a width of 25px.
     *
     */
    public AndroidMan()
    {
        super(0.0f, 0.0f, 25.0f, 1.0f, 0.0f, 0.0f);
    }

    /**
     * Constructor.
     *
     * <P>Refer to parent.
     *
     * {@inheritDoc}
     *
     */
    public AndroidMan(float posX, float posY, float width, float factor, float offsetx, float offsety)
    {
        super(posX, posY, width, factor, offsetx, offsety);
    }

    /*******************GETTERS AND SETTERS*******************/

    /**
     * Set god mode on.
     *
     */
    public void setGodModeOn()
    {
        godModeOn = true;
    }

    /**
     * Reset god mode to 0.
     *
     */
    public void setGodModeOff()
    {
        godModeOn = false;
    }
}
