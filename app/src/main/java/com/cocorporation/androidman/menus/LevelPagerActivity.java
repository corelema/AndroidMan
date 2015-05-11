package com.cocorporation.androidman.menus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.cocorporation.androidman.R;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Corentin on 5/2/2015.
 */
public class LevelPagerActivity extends FragmentActivity {
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        final ArrayList<Level> levels = LevelLab.get(this).getLevels();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return levels.size();
            }
            @Override
            public Fragment getItem(int pos) {
                UUID crimeId =  levels.get(pos).getId();
                return LevelFragment.newInstance(crimeId);
            }
        });

        UUID crimeId = (UUID)getIntent().getSerializableExtra(LevelFragment.EXTRA_LEVEL_ID);
        for (int i = 0; i < levels.size(); i++) {
            if (levels.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
