package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by a13587 on 15/08/08.
 */
public abstract class EventWeaselBuilder {
    private static final String TAG = EventWeaselBuilder.class.getSimpleName();

    private final Map<Event, Animator> animators;

    /* package */ static EventWeaselBuilder create(RecyclerView recyclerView) {
        return new EventRecyclerWeaselBuilder(recyclerView);
    }

    /* package */ static EventWeaselBuilder create(ScrollableView scrollableView) {
        return new EventGeneralWeaselBuilder(scrollableView);
    }


    protected EventWeaselBuilder() {
        animators = new HashMap<>();
    }

    protected abstract void addWeaselToScrollView(Weasel weasel);


    public EventWeaselBuilder at(Event event, State state, long duration) {
        animators.put(event, new BasicAnimator(state, duration));
        return this;
    }

    /**
     * start tracking the scroll view to transform the chaser view.
     *
     * @param chaserView
     */
    public Weasel start(@NonNull View chaserView) {
        // setup listener and add to ScrollableView.
        Weasel weasel = new Weasel(chaserView);

        // event
        for (Event ev : animators.keySet()) {
            weasel.addEventAnimator(ev, animators.get(ev));
        }

        // add to scrollable view and start to chase.
        addWeaselToScrollView(weasel);
        return weasel;
    }
}
