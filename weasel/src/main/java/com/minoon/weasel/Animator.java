package com.minoon.weasel;

import android.view.View;

/**
 * Created by a13587 on 15/06/30.
 */
public interface Animator<V extends View> {
    void animate(V view);
}
