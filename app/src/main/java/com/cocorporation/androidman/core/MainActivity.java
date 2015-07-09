package com.cocorporation.androidman.core;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.cocorporation.androidman.R;
import com.cocorporation.androidman.graphics.GLSurf;
import com.cocorporation.androidman.messages.MessageReceiver;
import com.cocorporation.androidman.messages.MessagesManager;
import com.cocorporation.androidman.messages.MessagesType;

/**
 * Main Activity.
 *
 * <P>Android Activity
 *
 * <P>Handles the creation of all the graphics.
 *
 * <P>Handles the registration of the surface to the message system.
 *
 * @author Corentin Leman
 * @version 1.0
 */
public class MainActivity extends Activity implements MessageReceiver {

    // Our OpenGL Surfaceview
    private GLSurfaceView glSurfaceView;

    /**
     * Handles the creation of the glSurfaceView.
     * Takes care of setting the Activity's view.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Super
        super.onCreate(savedInstanceState);

        // Fullscreen mode
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // We create our Surfaceview for our OpenGL here.
        glSurfaceView = new GLSurf(this);

        // Set our view.
        setContentView(R.layout.activity_main);

        // Retrieve our Relative layout from our main layout we just set to our view.
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.gamelayout);

        // Attach our surfaceview to our relative layout from our main layout.
        RelativeLayout.LayoutParams glParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.addView(glSurfaceView, glParams);

        MessagesManager.getInstance().registerForMessage(MessagesType.WIN, this);
    }

    /**
     * Un-subscribe the glSurfaceView from receiving the messages.
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
        MessagesManager.getInstance().unsubscribe(MessagesType.WIN, this);
    }

    /**
     * Subscribe the glSurfaceView from receiving the messages.
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        MessagesManager.getInstance().registerForMessage(MessagesType.WIN, this);
    }

    /**
     * Handles the behavior when a 'WIN' message is received.
     *
     */
    @Override
    public void receiveMessage(Object message) {

    }
}
