package com.cocorporation.androidman.graphics;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by Corentin on 7/18/2015.
 */
public interface InterfaceDrawable {
    public void bufferize();

    public void render(float[] m, int texture);
}
