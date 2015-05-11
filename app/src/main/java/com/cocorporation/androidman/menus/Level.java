package com.cocorporation.androidman.menus;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Corentin on 5/3/2015.
 */
public class Level {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mPassed;

    public Level() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Date getDate() {
        return mDate;
    }

    public boolean isPassed() {
        return mPassed;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public void setPassed(boolean mPassed) {
        this.mPassed = mPassed;
    }

}
