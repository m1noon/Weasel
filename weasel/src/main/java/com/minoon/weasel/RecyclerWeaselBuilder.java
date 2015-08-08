package com.minoon.weasel;

import android.support.v7.widget.RecyclerView;

/**
 * Created by hiroki-mino on 2015/07/02.
 */
public class RecyclerWeaselBuilder {
    private static final String TAG = RecyclerWeaselBuilder.class.getSimpleName();

    private RecyclerView mRecyclerView;

    public RecyclerWeaselBuilder(RecyclerView verticalDraggableView) {
        this.mRecyclerView = verticalDraggableView;
    }

    /**
     * set the basic state before change.
     *
     * @param state
     * @return
     */
    public SmoothWeaselBuilder from(State state) {
        return SmoothWeaselBuilder.create(mRecyclerView).from(state);
    }

    /**
     * set the event.
     *
     * @param event
     * @param state
     * @param duration
     * @return
     */
    public EventWeaselBuilder at(Event event, State state, long duration) {
        return EventWeaselBuilder.create(mRecyclerView).at(event, state, duration);
    }
}
