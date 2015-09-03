package com.minoon.weasel;

/**
 * Created by a13587 on 15/06/29.
 */
public interface ScrollableView<E extends Enum> {

    /**
     * Add scroll listener.
     *
     * @param listener
     */
    void addWeasel(Weasel<E> listener);

    /**
     * Create WeaselBuilder to setup scroll animation.
     *
     * @return
     */
    WeaselBuilder<E> startWeasel();
}
