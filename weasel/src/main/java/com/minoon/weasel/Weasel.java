package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.minoon.weasel.drag.VerticalDraggableView;
import com.minoon.weasel.transformer.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a13587 on 15/06/28.
 */
public class Weasel {
    private static final String TAG = Weasel.class.getSimpleName();

    /* package */ Weasel(@NonNull View view) {
        mTransformers = new ArrayList<>();
        mView = view;
    }

    public static WeaselBuilder chase(VerticalDraggableView view) {
        return new WeaselBuilder(view);
    }

    final View mView;

    private StateHolder mStateHolder;

    private float mRatio;

    private final List<Transformer> mTransformers;

    private int mOffset;

    /* package */ void setStateHolder(StateHolder stateHolder) {
        mStateHolder = stateHolder;
    }

    /* package */ void setRatio(float ratio) {
        mRatio = ratio;
    }

    /* package */ void setOffset(int offset) {
        mOffset = offset;
    }

    /* package */ void addTransformer(Transformer transformer) {
        mTransformers.add(transformer);
    }

    public void chase(int scrollPosition) {
        if(scrollPosition < mOffset) {
            return;
        }
        scrollPosition -= mOffset;

        State fromState = mStateHolder.getFromState();
        State toState = mStateHolder.getToState();
        if(fromState == null || toState == null) {
            return;
        }
        int fromY = fromState.getTranslateY();
        int toY = toState.getTranslateY();
        int range = Math.abs(toY - fromY);
        float currentPosition = scrollPosition * mRatio;
        float offset = Math.max(0f, Math.min(1f, currentPosition / (float)range));
        for (Transformer t : mTransformers) {
            t.transform(mView, offset);
        }
    }

    public void event(Event ev, int scrollPosition) {
        if(scrollPosition < mOffset) {
            return;
        }
        Animator animator = mStateHolder.get(ev);
        Log.i(TAG, "onEvent. animator=" + animator + ", ev=" + ev);
        if(animator != null) {
            animator.animate(mView);
        }
    }
}
