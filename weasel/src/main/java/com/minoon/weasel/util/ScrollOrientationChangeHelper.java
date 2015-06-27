package com.minoon.weasel.util;

import android.util.Log;

/**
 * Helper class for check scroll orientation changing.
 *
 * Created by hiroki-mino on 2015/07/01.
 */
public class ScrollOrientationChangeHelper {
    private static final String TAG = ScrollOrientationChangeHelper.class.getSimpleName();

    public interface ScrollOrientationChangeListener {
        void onOrientationChage(boolean up);
    }

    private int mTabHideSignal = 0;
    private int mTabHideSensitivity = 500;
    private final ScrollOrientationChangeListener mListener;
    private boolean pastOrientation = true;

    public ScrollOrientationChangeHelper(ScrollOrientationChangeListener listener) {
        mListener = listener;
    }

    public ScrollOrientationChangeHelper(ScrollOrientationChangeListener listener, int sensitivity) {
        mListener = listener;
        mTabHideSensitivity = sensitivity;
    }

    public void onScroll(int dy) {
        Log.v(TAG, "onScrolled. dy: " + dy);

        if (Math.signum(dy) * Math.signum(mTabHideSignal) < 0) {
            // 今までと反対方向なのでシグナルの値をリセット
            mTabHideSignal = dy;
        } else {
            // シグナルの値を移動分だけ蓄積
            mTabHideSignal += dy;
        }

        if (-mTabHideSensitivity < mTabHideSignal && mTabHideSignal < mTabHideSensitivity) {
            // シグナルが[-規定値 ~ +規定値]以内であればなにもしない
            return;
        }

        // シグナルが規定値以下になったら（十分上方向にスクロールされたら）タブを表示（逆も同様）
        boolean up = (mTabHideSignal <= -mTabHideSensitivity);
        Log.v(TAG, "up: " + up + ", signal: " + mTabHideSignal);
        if(pastOrientation == up) {
            return;
        }
        pastOrientation = up;
        mListener.onOrientationChage(up);
    }
}
