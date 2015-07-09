package com.cocorporation.androidman.graphics;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class DialogHandler extends Handler {
    Context mContext;
    public static final int OPEN_ANNOTATION = 2;

    public DialogHandler(Context context) {
        super();
        this.mContext = context;
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == OPEN_ANNOTATION) {
            new Popup(mContext);
        }
    }
}
