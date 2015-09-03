package com.minoon.weasel;

/**
 * Created by a13587 on 15/06/28.
 */
public class WeaselBuilder<E extends Enum> {
    private static final String TAG = WeaselBuilder.class.getSimpleName();

    private ScrollableView mScrollableView;

    public WeaselBuilder(ScrollableView<E> scrollableView) {
        mScrollableView = scrollableView;
    }

    /**
     * set the basic state before change.
     *
     * @param state
     * @return
     */
    public SmoothWeaselBuilder from(State state) {
        SmoothWeaselBuilder builder = SmoothWeaselBuilder.create(mScrollableView);
        return builder.from(state);
    }

    /**
     * set the state when the specified event is called.
     *
     * @param event
     * @param state
     * @param duration
     * @return
     */
    public EventWeaselBuilder at(E event, State state, long duration) {
        return EventWeaselBuilder.create(mScrollableView).at(event, state, duration);
    }
}
