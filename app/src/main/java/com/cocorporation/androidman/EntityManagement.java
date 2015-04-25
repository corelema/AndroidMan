package com.cocorporation.androidman;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corentin on 4/19/2015.
 */
public class EntityManagement {
    private float verticesEntities[];
    private short indices[];
    private float factor;
    private float offsetx;
    private float offsety;

    private List<Ghost> listOfGhost;

    private AndroidMan androidMan;

    public EntityManagement(){
        listOfGhost = new ArrayList<Ghost>();
        androidMan = new AndroidMan(200.0f, 200.0f);
    }

    public void addGhost()
    {
        listOfGhost.add(new Ghost());
    }

    public int getNumberOfGhost()
    {
        return listOfGhost.size();
    }

    public short[] getIndices() {
        return indices;
    }

    public AndroidMan getAndroidMan() {
        return androidMan;
    }

    public void moveAndroidMan(Direction direction)
    {

    }
}
