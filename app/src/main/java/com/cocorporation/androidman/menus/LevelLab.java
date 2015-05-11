package com.cocorporation.androidman.menus;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Corentin on 5/3/2015.
 */
public class LevelLab {
    private ArrayList<Level> mLevels;

    private static LevelLab sLevelLab;
    private Context mAppContext;

    private LevelLab(Context appContext) {
        mAppContext = appContext;
        mLevels = new ArrayList<Level>();
        for (int i = 0; i < 10; i++) {
            Level l = new Level();
            l.setTitle("Level #" + i);
            l.setPassed(false);
            mLevels.add(l);
        }
    }

    public static LevelLab get(Context c) {
        if (sLevelLab == null) {
            sLevelLab= new LevelLab(c.getApplicationContext());
        }
        return sLevelLab;
    }

    public Level getLevel(UUID id) {
        for (Level l : mLevels) {
            if (l.getId().equals(id))
                return l;
        }
        return null;
    }

    public ArrayList<Level> getLevels() {
        return mLevels;
    }
}
