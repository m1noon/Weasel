package com.minoon.weasel.trader;

/**
 * Created by a13587 on 15/06/27.
 */
public interface TouchEventTrader {

    /**
     * return true if content view should consume the touch event and do not want to be dragged.
     *
     * @return
     */
    boolean stealTouchEventForChild();

    /**
     * scroll the child contents.
     *
     * @param dx
     * @param dy
     */
    void scrollBy(int dx, int dy);
}
