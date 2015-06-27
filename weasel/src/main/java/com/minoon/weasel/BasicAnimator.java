package com.minoon.weasel;

import android.view.View;

/**
 * Created by a13587 on 15/06/30.
 */
public class BasicAnimator implements Animator {
    private static final String TAG = BasicAnimator.class.getSimpleName();

    private State mState;
    private long mDuration = 1000;

    public BasicAnimator(State state, long duration) {
        mState = state;
        mDuration = duration;
    }

    @Override
    public void animate(View view) {
        view.animate().cancel();
        view.animate()
                .alpha(mState.getAlpha())
                .translationX(mState.getTranslateX())
                .translationY(mState.getTranslateY())
                .setDuration(mDuration)
                .start();
    }
}
