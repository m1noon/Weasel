package com.minoon.weasel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.minoon.weasel.transformer.Transformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a13587 on 15/08/08.
 */
public abstract class SmoothWeaselBuilder {
    private static final String TAG = SmoothWeaselBuilder.class.getSimpleName();

    /**
     * create SmoothWeaselBuilder from RecyclerView.
     *
     * @param recyclerView
     * @return
     */
    /* package */ static SmoothWeaselBuilder create(RecyclerView recyclerView) {
        return new SmoothRecyclerWeaselBuilder(recyclerView);
    }

    /**
     * create SmoothWeaselBuilder from ScrollableView.
     *
     * @param scrollableView
     * @return
     */
    /* package */ static SmoothGeneralWeaselBuilder create(ScrollableView scrollableView) {
        return new SmoothGeneralWeaselBuilder(scrollableView);
    }

    private State fromState;
    private State toState;
    private float ratio = 1.0f;
    private int offset = 0;
    private final List<Transformer> transformers;

    protected SmoothWeaselBuilder() {
        transformers = new ArrayList<>();
    }

    protected abstract void addWeaselToScrollView(Weasel weasel);

    /**
     * set the basic state before change.
     *
     * @param state
     * @return
     */
    public SmoothWeaselBuilder from(State state) {
        fromState = state;
        return this;
    }

    /**
     * set the state after the change has been completed.
     *
     * @param state
     * @return
     */
    public SmoothWeaselBuilder to(State state) {
        toState = state;
        return this;
    }

    /**
     * the ratio of the chaser view scrolling size to the base view scrolling size.
     *
     * @param ratio
     * @return
     */
    public SmoothWeaselBuilder ratio(float ratio) {
        this.ratio = ratio;
        return this;
    }

    /**
     * the offset pixel size of the start position.
     *
     * @param offset
     * @return
     */
    public SmoothWeaselBuilder offset(int offset) {
        this.offset = Math.max(offset, 0);
        return this;
    }

    /**
     * set custom transform.
     *
     * @param transformer
     * @return
     */
    public SmoothWeaselBuilder transform(Transformer transformer) {
        transformers.add(transformer);
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
        addWeaselToScrollView(weasel);
    }
}
