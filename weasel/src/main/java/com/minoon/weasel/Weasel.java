package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.minoon.weasel.util.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a13587 on 15/06/28.
 */
public class Weasel<E extends Enum> {
    private static final String TAG = Logger.createTag(Weasel.class.getSimpleName());

    public enum WeaselEvent {
        START_SCROLL_BACK,
        START_SCROLL_FORWARD,
    }

    public static RecyclerWeaselBuilder chase(RecyclerView recyclerView) {
        return new RecyclerWeaselBuilder(recyclerView);
    }

    final List<View> mView;

    private Map<E, Animator> mAnimatorMap;

    private SmoothChaseHelper mSmoothHelper;

    private RecyclerWeaselConnector mRecyclerConnector;

    /* package */ Weasel(@NonNull View view) {
        mView = new ArrayList<>();
        mView.add(view);
        mRecyclerConnector = new RecyclerWeaselConnector(this);
    }

    /* package */ void setSmoothHelper(SmoothChaseHelper helper) {
        mSmoothHelper = helper;
    }

    /* package */ void addEventAnimator(E event, Animator animator) {
        if (mAnimatorMap == null) {
            mAnimatorMap = new HashMap<>();
        }
        mAnimatorMap.put(event, animator);
    }

    public void chase(int scrollPosition) {
        if (mSmoothHelper != null) {
            for (View view: mView) {
                mSmoothHelper.transform(view, scrollPosition);
            }
        }
    }

    public void event(Event ev, int scrollPosition) {
        if(mAnimatorMap == null) {
            return;
        }
        Animator animator = mAnimatorMap.get(ev);
        Logger.i(TAG, "onEvent. animator=" + animator + ", ev=" + ev);
        if(animator != null) {
            for (View view: mView) {
                animator.animate(view);
            }
        }
    }

    public void addChaseView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(mRecyclerConnector);
    }

    public void addChaseView(ScrollableView scrollableView) {
        scrollableView.addWeasel(this);
    }

    public void addChaserView(View view) {
        Logger.d(TAG, "add chaser view. chaser view size='%s'", mView.size());
        mView.add(view);
    }

    public void removeChaserView(View view) {
        Logger.d(TAG, "add chaser view. chaser view size='%s'", mView.size());
        mView.remove(view);
    }

    public int getChaserViewSize() {
        return mView.size();
    }

    public boolean containsChaserView(View view) {
        return mView.contains(view);
    }
}
