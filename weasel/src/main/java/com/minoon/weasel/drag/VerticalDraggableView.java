package com.minoon.weasel.drag;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.minoon.weasel.Event;
import com.minoon.weasel.R;
import com.minoon.weasel.ScrollableView;
import com.minoon.weasel.Weasel;
import com.minoon.weasel.boundary.VerticalScrollBoundaryMaker;
import com.minoon.weasel.trader.TouchEventTrader;
import com.minoon.weasel.util.ScrollOrientationChangeHelper;
import com.minoon.weasel.util.TouchEventHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a13587 on 15/06/27.
 */
public class VerticalDraggableView extends RelativeLayout implements TouchEventHelper.Callback,
        ScrollableView,
        ScrollOrientationChangeHelper.ScrollOrientationChangeListener {
    private static final String TAG = VerticalDraggableView.class.getSimpleName();

    public enum VerticalDraggableTag {
        BOTTOM,
        TOP,
        CONTENT_END
    }

    private static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 3000;

    private int mActivePointerId = INVALID_POINTER;

    private Scroller mScroller;

    private View mDragView;

    private VerticalScrollBoundaryMaker mBoundaryMaker;

    private TouchEventTrader mTouchEventTrader;

    private TouchEventHelper mTouchEventHelper;

    private final List<Weasel> mWeasels;

    private int mContentScrollPosition = 0;

    private ScrollOrientationChangeHelper mScrollOrientationHelper;

    public VerticalDraggableView(Context context) {
        this(context, null);
    }

    public VerticalDraggableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDraggableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_draggable, this);
        mDragView = findViewById(R.id.view_draggable_fl_dragged_view);
        mBoundaryMaker = new VerticalScrollBoundaryMaker(mDragView, this);
        mWeasels = new ArrayList<>();
        // TODO enable to set top offset from assets.
        mBoundaryMaker.setTopOffset(300);
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mTouchEventHelper = new TouchEventHelper(context, this);
        mScrollOrientationHelper = new ScrollOrientationChangeHelper(this);
        setDragViewBottom();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setDragViewBottom();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return false;
        }

        switch (ev.getActionMasked() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                return false;
            case MotionEvent.ACTION_DOWN:
                int index = ev.getActionIndex();
                mActivePointerId = ev.getPointerId(index);
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                break;
            default:
                break;
        }
        boolean interceptTap = isViewUnder(mDragView, (int) ev.getX(), (int) ev.getY());
        return interceptTap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "[onTouchEvent]x=" + getScrollPositionX() + ", y=" + getScrollPositionY() + ", from=" + mDragView.getBottom());
        mTouchEventHelper.onTouchEvent(ev);
        if(ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mScroller.abortAnimation();
        }
        boolean isViewHit = isViewHit(mDragView, (int) ev.getX(), (int) ev.getY());
        return isViewHit;
    }

    /**
     * Clone given motion event and set specified action. This method is useful, when we want to
     * cancel event propagation in child views by sending event with {@link
     * MotionEvent#ACTION_CANCEL}
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

    private MotionEvent cloneMotionEventWithFixPosition(MotionEvent event) {
        return MotionEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), event.getX() - getScrollPositionX(),
                event.getY() - getScrollPositionY(), event.getMetaState());
    }

    /**
     * Calculate if one position is above any view.
     *
     * @param view to analyze.
     * @param x    position.
     * @param y    position.
     * @return true if x and y positions are below the view.
     */
    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0]
                && screenX < viewLocation[0] + view.getWidth()
                && screenY >= viewLocation[1]
                && screenY < viewLocation[1] + view.getHeight();
    }

    private int mScrollerOldX;
    private int mScrollerOldY;

    /**
     * To ensure the animation is going to work this method has been override to call
     * postInvalidateOnAnimation if the view is not settled yet.
     */
    @Override
    public void computeScroll() {
        if(!mScroller.isFinished() && mScroller.computeScrollOffset()) {
            if(mScrollerOldY == 0 && mScrollerOldX == 0) {
                mScrollerOldY = mScroller.getStartY();
                mScrollerOldX = mScroller.getStartX();
            }
            int oldX = getScrollPositionX();
            int oldY = getScrollPositionY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            final int dy = y - mScrollerOldY;
            Log.d(TAG, String.format("[computeScroll]posX='%s', posY='%s', toX='%s', toY='%s', dy='%s'", oldX, oldY, x, y, dy));
            // 縦方向
            scrollViewBy(0, dy);
            mScrollerOldY = y;
            postInvalidate();
            return;
        }
        completeScroll(true);
    }

    private void completeScroll(boolean postEvents) {
        mScroller.abortAnimation();
        mScrollerOldY = 0;
        mScrollerOldX = 0;
    }


    public void attach(Fragment fragment, FragmentManager fm) {
        fm.beginTransaction().replace(R.id.view_draggable_fl_dragged_view, fragment).commit();
    }

    public void attach(android.app.Fragment fragment, android.app.FragmentManager fm) {
        fm.beginTransaction().replace(R.id.view_draggable_fl_dragged_view, fragment).commit();
    }

    public void setTouchEventTrader(TouchEventTrader trader) {
        mTouchEventTrader = trader;
    }


    //// DragView position

    public void setDragViewBottom() {
        scrollDragViewTo(getBottomBounds());
    }

    public boolean isAtTop() {
        return mBoundaryMaker.isAtTop();
    }

    public int getTopBounds() {
        return mBoundaryMaker.getTopBounds();
    }

    public int getBottomBounds() {
        return mBoundaryMaker.getBottomBounds();
    }

    public int getYInScrollRange(int y) {
        return Math.min(Math.max(y, getTopBounds()), getBottomBounds());
    }

    public void flickToTop(float velocity) {
        int distance = calculateDistanceByVelocity(velocity);
        int duration = calculeteDurationByVelocity(velocity);
        Log.d(TAG, String.format("flickToTop. dration='%s'", duration));
        mScroller.startScroll(getScrollPositionX(), getScrollPositionY(), 0, -distance, duration);
        postInvalidate();
        notifyEvent(Event.FLICK_SCROL_DOWN);
    }

    public void flickToBottom(float velocity) {
        Log.d(TAG, "flickToBottom");
        int distance = calculateDistanceByVelocity(velocity);
        int duration = calculeteDurationByVelocity(velocity);
        mScroller.startScroll(getScrollPositionX(), getScrollPositionY(), 0, distance, duration);
        postInvalidate();
        notifyEvent(Event.FLICK_SCROLL_UP);
    }


    //// Basic Scroll Method

    private int getScrollPositionY() {
        return mDragView.getTop();
    }

    private int getScrollPositionX() {
        return mDragView.getLeft();
    }

    private int calculateDistanceByVelocity(float velocity) {
        Log.d(TAG, String.format("velocity='%s'", velocity));
        return (int)velocity / 8;
    }

    private int calculeteDurationByVelocity(float velocity) {
        int duration = 0;
        int distance = calculateDistanceByVelocity(velocity);
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        }
        return Math.min(duration, MAX_SETTLE_DURATION);
    }

    public void scrollDragViewTo(int top) {
        top = Math.max(mBoundaryMaker.getTopBounds(), Math.min(mBoundaryMaker.getBottomBounds(), top));
        mDragView.setTop(top);
    }

    //// View tool

    public boolean isViewUnder(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft() &&
                x < view.getRight() &&
                y >= view.getTop() &&
                y < view.getBottom();
    }


    @Override
    public void dispathcTouchEventForChild(MotionEvent ev) {
        ev = cloneMotionEventWithFixPosition(ev);
        mDragView.dispatchTouchEvent(ev);
    }

    private static final float X_MIN_VELOCITY = 1500;
    private static final float Y_MIN_VELOCITY = 1000;

    @Override
    public void onViewReleased(float xvel, float yvel) {
        Log.d(TAG, "onViewReleased.x=" + xvel + ", y=" + yvel);
        float xVelAbs = Math.abs(xvel);
        float yVelAbs = Math.abs(yvel);
        if(xVelAbs > X_MIN_VELOCITY && xVelAbs >= yVelAbs) {
        } else if(yVelAbs > Y_MIN_VELOCITY) {
            if (yvel > 0) {
                flickToBottom(yVelAbs);
            } else {
                flickToTop(yVelAbs);
            }
        }
    }

    @Override
    public void scrollViewBy(int dx, int dy) {
        boolean isChildScroll = false;
        if(mTouchEventTrader.stealTouchEventForChild()) {
            mTouchEventTrader.scrollBy(dx, -dy);
            mContentScrollPosition -= dy;
            isChildScroll = true;
        } else if(isAtTop()) {
            if(dy > 0) {
                scrollDragViewTo(getScrollPositionY() + dy);
            } else {
                mTouchEventTrader.scrollBy(dx, -dy);
                mContentScrollPosition -= dy;
                isChildScroll = true;
            }
        } else {
            mContentScrollPosition = 0;
            scrollDragViewTo(getScrollPositionY() + dy);
        }

        mScrollOrientationHelper.onScroll(-dy);

        notifyScroll();
    }


    //// Weasel


    @Override
    public void addWeasel(Weasel weasel) {
        mWeasels.add(weasel);
    }

    private void notifyScroll() {
        int scrollPosition = getBottomBounds() - getScrollPositionY() + mContentScrollPosition;
        for (Weasel w : mWeasels) {
            w.chase(scrollPosition);
        }
    }

    private void notifyEvent(Event ev) {
        int scrollPosition = getBottomBounds() - getScrollPositionY() + mContentScrollPosition;
        for (Weasel w : mWeasels) {
            w.event(ev, scrollPosition);
        }
    }

    ////


    @Override
    public void onOrientationChage(boolean up) {
        Event ev = up ? Event.START_SCROLL_UP : Event.START_SCROLL_DOWN;
        notifyEvent(ev);
    }
}
