package com.cocorporation.androidman.graphics;

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
    private Mover mover;

    private List<Ghost> listOfGhost;

    private AndroidMan androidMan;

    public EntityManagement(){
        listOfGhost = new ArrayList<Ghost>();
        androidMan = new AndroidMan(200.0f, 200.0f, 25.0f, factor, offsetx, offsety);
    }

    public EntityManagement(float factor, float offsetx, float offsety, Background background){
        this.factor = factor;
        this.offsetx = offsetx;
        this.offsety = offsety;
        listOfGhost = new ArrayList<Ghost>();
        androidMan = new AndroidMan(background.getInitPosXAndroidMan(), background.getInitPosYAndroidMan(), ((float)((int)(25.0f * factor)))-1.0f, factor, offsetx, offsety);
        mover = new Mover(background);
        mover.addMovingEntity(androidMan);
        mover.launchMovement();
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