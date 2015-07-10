package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.view.View;

import com.minoon.weasel.transformer.Transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by a13587 on 15/06/28.
 */
public class WeaselBuilder {
    private static final String TAG = WeaselBuilder.class.getSimpleName();

    private ScrollableView mScrollableView;
    private State fromState;
    private State toState;
    private float ratio = 1.0f;
    private int offset = 0;
    private final List<Transformer> transformers;
    private final Map<Event, Animator> animators;

    public WeaselBuilder(ScrollableView scrollableView) {
        mScrollableView = scrollableView;
        transformers = new ArrayList<>();
        animators = new HashMap<>();
    }

    /**
     * set the basic state before change.
     *
     * @param state
     * @return
     */
    public WeaselBuilder from(State state) {
        fromState = state;
        return this;
    }

    /**
     * set the state after the change has been completed.
     *
     * @param state
     * @return
     */
    public WeaselBuilder to(State state) {
        toState = state;
        return this;
    }

    /**
     * the ratio of the chaser view scrolling size to the base view scrolling size.
     *
     * @param ratio
     * @return
     */
    public WeaselBuilder ratio(float ratio) {
        this.ratio = ratio;
        return this;
    }

    /**
     * the offset pixel size of the start position.
     *
     * @param offset
     * @return
     */
    public WeaselBuilder offset(int offset) {
        this.offset = Math.max(offset, 0);
        return this;
    }

    /**
     * set custom transform.
     *
     * @param transformer
     * @return
     */
    public WeaselBuilder transform(Transformer transformer) {
        transformers.add(transformer);
        return this;
    }

    public WeaselBuilder at(Event event, State state, long duration) {
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
        mScrollableView.addWeasel(weasel);
    }
}
