package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.minoon.weasel.transformer.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hiroki-mino on 2015/07/02.
 */
public class RecyclerWeaselBuilder {
    private static final String TAG = RecyclerWeaselBuilder.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private State fromState;
    private State toState;
    private float ratio = 1.0f;
    private int offset = 0;
    private final List<Transformer> transformers;
    private final Map<Event, Animator> animators;

    public RecyclerWeaselBuilder(RecyclerView verticalDraggableView) {
        this.mRecyclerView = verticalDraggableView;
        this.transformers = new ArrayList<>();
        this.animators = new HashMap<>();
    }

    /**
     * set the basic state before change.
     *
     * @param state
     * @return
     */
    public RecyclerWeaselBuilder from(State state) {
        fromState = state;
        return this;
    }

    /**
     * set the state after the change has been completed.
     *
     * @param state
     * @return
     */
    public RecyclerWeaselBuilder to(State state) {
        toState = state;
        return this;
    }

    /**
     * the ratio of the chaser view scrolling size to the base view scrolling size.
     *
     * @param ratio
     * @return
     */
    public RecyclerWeaselBuilder ratio(float ratio) {
        this.ratio = ratio;
        return this;
    }

    /**
     * the offset pixel size of the start position.
     *
     * @param offset
     * @return
     */
    public RecyclerWeaselBuilder offset(int offset) {
        this.offset = Math.max(offset, 0);
        return this;
    }

    /**
     * set custom transform.
     *
     * @param transformer
     * @return
     */
    public RecyclerWeaselBuilder transform(Transformer transformer) {
        transformers.add(transformer);
        return this;
    }

    public RecyclerWeaselBuilder at(Event event, State state, long duration) {
        animators.put(event, new BasicAnimator(state, duration));
        return this;
    }

    /**
     * start tracking the scroll view to transform the chaser view.
     *
     * @param chaserView
     */
    public void start(@NonNull View chaserView) {
        // setup listener and add to ScrollableView.
        Weasel weasel = new Weasel(chaserView);

        // event
        for (Event ev : animators.keySet()) {
            weasel.addEventAnimator(ev, animators.get(ev));
        }

        // smooth
        SmoothChaseHelper smoothHelper = new SmoothChaseHelper(fromState, toState);
        smoothHelper.setOffset(offset);
        smoothHelper.setRatio(ratio);
        // Custom Transformer
        for (Transformer t : transformers) {
            if (t != null) {
                smoothHelper.addTransformer(t);
            }
        }
        weasel.setSmoothHelper(smoothHelper);

        // add to scrollable view and start to chase.
        RecyclerWeaselConnector connector = new RecyclerWeaselConnector(weasel);
        mRecyclerView.addOnScrollListener(connector);
    }
}
