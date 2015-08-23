package com.minoon.weasel.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * ドラッグのためのタッチイベント処理ヘルパークラス。
 * {@link android.support.v4.widget.ViewDragHelper}はViewの移動を
 * {@link View#offsetLeftAndRight(int)}などのメソッドで実行していて
 *
 * dx,dyだけを渡す。
 *
 *
 * Created by a13587 on 15/06/28.
 */
public class TouchEventHelper {
    private static final String TAG = Logger.createTag(TouchEventHelper.class.getSimpleName());


    /**
     * A Callback is used as a communication channel with the ViewDragHelper back to the
     * parent view using it. <code>on*</code>methods are invoked on siginficant events and several
     * accessor methods are expected to provide the ViewDragHelper with more information
     * about the state of the parent view upon request. The callback also makes decisions
     * governing the range and draggability of child views.
     */
    public interface Callback {

        public void scrollViewBy(int dx, int dy);

        public void dispathcTouchEventForChild(MotionEvent ev);

        public void onViewReleased(float xvel, float yvel);
    }

    /**
     * A null/invalid pointer ID.
     */
    public static final int INVALID_POINTER = -1;

    /**
     * Indicates that the pager is in an idle, settled state. The current page
     * is fully in view and no animation is in progress.
     */
    public static final int SCROLL_STATE_IDLE = 0;

    /**
     * Indicates that the pager is currently being dragged by the user.
     */
    public static final int SCROLL_STATE_DRAGGING = 1;

    /**
     * Indicates that the pager is in the process of settling to a final position.
     */
    public static final int SCROLL_STATE_SETTLING = 2;

    float mTouchSlop;
    int mMaxVelocity;
    int mMinVelocity;

    float mInitialMotionX;
    float mInitialMotionY;
    float mLastMotionX;
    float mLastMotionY;

    int mActivePointerId;

    boolean mIsBeingDragged;

    int mDragState;

    VelocityTracker mVelocityTracker;

    Callback mCallback;

    public TouchEventHelper(Context context, @NonNull Callback callback) {
        ViewConfiguration config = ViewConfiguration.get(context);
        mTouchSlop = config.getScaledTouchSlop() ^ 2;
        mMaxVelocity = config.getScaledMaximumFlingVelocity();
        mMinVelocity = config.getScaledMinimumFlingVelocity();
        mCallback = callback;
    }

    public void onTouchEvent(MotionEvent ev) {
        final int action = ev.getActionMasked();
        if(action == MotionEvent.ACTION_DOWN) {
            cancel();
        }


        if(mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                mActivePointerId = ev.getPointerId(0);
                mCallback.dispathcTouchEventForChild(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX();
                final float y = ev.getY();
                final float dx = Math.abs(x - mLastMotionX);
                final float dy = Math.abs(y - mLastMotionY);
                if(!mIsBeingDragged) {
                    // ドラッグ開始を判定
                    if(dx > mTouchSlop || dy > mTouchSlop) {
                        // スクロールが開始されたと判断
                        mIsBeingDragged = true;
                        mLastMotionX = x - mInitialMotionX > 0 ?
                                mInitialMotionX + mTouchSlop : mInitialMotionX - mTouchSlop;
                        mLastMotionY = y - mInitialMotionY > 0 ?
                                mInitialMotionY + mTouchSlop : mInitialMotionY - mTouchSlop;
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }
                if(mIsBeingDragged) {
                    // ドラッグ処理
                    mCallback.scrollViewBy((int)(x - mLastMotionX), (int)(y - mLastMotionY));
                    mCallback.dispathcTouchEventForChild(cloneMotionEventWithAction(ev, MotionEvent.ACTION_CANCEL));
                    mLastMotionX = x;
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
                    int initialVelocityX = (int) velocityTracker.getXVelocity(mActivePointerId);
                    int initialVelocityY = (int) velocityTracker.getYVelocity(mActivePointerId);
                    mCallback.onViewReleased(initialVelocityX, initialVelocityY);
                    endDrag();
                } else {
                    mCallback.dispathcTouchEventForChild(ev);
                }
                cancel();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // pointerの更新
                final int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
                cancel();
                break;
        }
    }

    public void setScrollState(int state) {

    }

    private void endDrag() {
        mIsBeingDragged = false;
        if(mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * The result of a call to this method is equivalent to
     */
    public void cancel() {
        mActivePointerId = INVALID_POINTER;

        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * Clone given motion event and set specified action. This method is useful, when we want to
     * cancel event propagation in child views by sending event with {@link
     * android.view.MotionEvent#ACTION_CANCEL}
     * action.
     *
     * @param event  event to clone
     * @param action new action
     * @return cloned motion event
     */
    private MotionEvent cloneMotionEventWithAction(MotionEvent event, int action) {
        return MotionEvent.obtain(event.getDownTime(), event.getEventTime(), action, event.getX(),
                event.getY(), event.getMetaState());
    }
}
