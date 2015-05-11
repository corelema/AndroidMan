package com.cocorporation.androidman.menus;

import android.support.v4.app.Fragment;

/**
 * Created by Corentin on 5/2/2015.
 */
public class LevelListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new LevelListFragment();
    }
}
