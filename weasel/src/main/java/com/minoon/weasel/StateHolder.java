package com.minoon.weasel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a13587 on 15/06/29.
 */
/* package */ class StateHolder {
    private static final String TAG = StateHolder.class.getSimpleName();

    final private Map<Event, Animator> mAnimatorMap;
    private State mFromState;
    private State mToState;

    public StateHolder() {
        this.mAnimatorMap = new HashMap<>();
    }

    public void set(Event ev, Animator animator) {
        mAnimatorMap.put(ev, animator);
    }

    public Animator get(Event event) {
        return mAnimatorMap.get(event);
    }

    public void setFromState(State state) {
        mFromState = state;
    }

    public State getFromState() {
        return mFromState;
    }

    public void setToState(State state) {
        mToState = state;
    }

    public State getToState() {
        return mToState;
    }
}
