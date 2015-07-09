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
    private float verticesGhosts[];
    private short indicesGhosts[];

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
        listOfGhost.add(new Ghost(background.getInitPosXGhosts(), background.getInitPosYGhosts(), ((float)((int)(25.0f * factor)))-1.0f, factor, offsetx, offsety));
        listOfGhost.add(new Ghost(background.getInitPosXGhosts(), background.getInitPosYGhosts(), ((float)((int)(25.0f * factor)))-1.0f, factor, offsetx, offsety));
        listOfGhost.add(new Ghost(background.getInitPosXGhosts(), background.getInitPosYGhosts(), ((float)((int)(25.0f * factor)))-1.0f, factor, offsetx, offsety));
        androidMan = new AndroidMan(background.getInitPosXAndroidMan(), background.getInitPosYAndroidMan(), ((float)((int)(25.0f * factor)))-1.0f, factor, offsetx, offsety);
        mover = new Mover(background);
        mover.setAndroidMan(androidMan);
        mover.addMovingGhost(listOfGhost.get(0));
        mover.addMovingGhost(listOfGhost.get(1));
        mover.addMovingGhost(listOfGhost.get(2));
        mover.launchMovementAndroidMan();
        mover.launchMovementGhosts();;
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

    public float[] getVerticesGhosts()
    {
        verticesGhosts = new float[listOfGhost.size()*4*3];
        int i = 0;
        for (Ghost ghost : listOfGhost)
        {
            float[] src = ghost.generateVertices();
            System.arraycopy(src, 0, verticesGhosts, i*4*3, src.length);
            i++;
        }

        return verticesGhosts;
    }

    public short[] getIndicesGhosts()
    {
        indicesGhosts = new short[listOfGhost.size()*6];
        int i = 0;
        short last = 0;
        for (Ghost ghost : listOfGhost)
        {
            //a faire le src tet?
            indicesGhosts[i*6+0] = (short) (ghost.getIndices()[0] + last);
            indicesGhosts[i*6+1] = (short) (ghost.getIndices()[1] + last);
            indicesGhosts[i*6+2] = (short) (ghost.getIndices()[2] + last);
            indicesGhosts[i*6+3] = (short) (ghost.getIndices()[3] + last);
            indicesGhosts[i*6+4] = (short) (ghost.getIndices()[4] + last);
            indicesGhosts[i*6+5] = (short) (ghost.getIndices()[5] + last);
            i++;
            last += 4;
        }

        return indicesGhosts;
    }

    public List<Ghost> getListOfGhost() {
        return listOfGhost;
    }
}
