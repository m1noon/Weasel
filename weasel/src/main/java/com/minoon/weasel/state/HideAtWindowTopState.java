package com.minoon.weasel.state;

import android.support.annotation.NonNull;
import android.view.View;

import com.minoon.weasel.State;

/**
 * Created by a13587 on 15/06/28.
 */
public class HideAtWindowTopState extends State {
    private static final String TAG = HideAtWindowTopState.class.getSimpleName();

    View mChaserView;

    public HideAtWindowTopState(@NonNull View chaserView) {
        mChaserView = chaserView;
    }

    @Override
    public int getTranslateY() {
        return (int) -(mChaserView.getY() + mChaserView.getBottom() - mChaserView.getTranslationY());
    }
}
