package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a13587 on 15/06/28.
 */
public class Weasel {
    private static final String TAG = Weasel.class.getSimpleName();

    public static WeaselBuilder chase(ScrollableView view) {
        return new WeaselBuilder(view);
    }

    public static RecyclerWeaselBuilder chase(RecyclerView recyclerView) {
        return new RecyclerWeaselBuilder(recyclerView);
    }

    final View mView;

    private Map<Event, Animator> mAnimatorMap;

    private SmoothChaseHelper mSmoothHelper;

    private RecyclerWeaselConnector mRecyclerConnector;

    /* package */ Weasel(@NonNull View view) {
        mView = view;
        mRecyclerConnector = new RecyclerWeaselConnector(this);
    }

    /* package */ void setSmoothHelper(SmoothChaseHelper helper) {
        mSmoothHelper = helper;
    }

    /* package */ void addEventAnimator(Event event, Animator animator) {
        if (mAnimatorMap == null) {
            mAnimatorMap = new HashMap<>();
        }
        mAnimatorMap.put(event, animator);
    }

    public void chase(int scrollPosition) {
        if (mSmoothHelper != null) {
            mSmoothHelper.transform(mView, scrollPosition);
        }
    }

    public void event(Event ev, int scrollPosition) {
        if(mAnimatorMap == null) {
            return;
        }
        Animator animator = mAnimatorMap.get(ev);
        Log.i(TAG, "onEvent. animator=" + animator + ", ev=" + ev);
        if(animator != null) {
            animator.animate(mView);
        }
    }

    public void addChaseView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(mRecyclerConnector);
    }

    public void addChaseView(ScrollableView scrollableView) {
        scrollableView.addWeasel(this);
    }
}
