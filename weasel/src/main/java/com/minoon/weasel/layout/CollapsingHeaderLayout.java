package com.minoon.weasel.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.minoon.weasel.R;
import com.minoon.weasel.ScrollableView;
import com.minoon.weasel.State;
import com.minoon.weasel.Weasel;
import com.minoon.weasel.WeaselBuilder;
import com.minoon.weasel.state.HideAtWindowTopState;
import com.minoon.weasel.trader.LinearLayoutRecyclerViewTrader;
import com.minoon.weasel.trader.TouchEventTrader;
import com.minoon.weasel.util.Logger;
import com.minoon.weasel.util.ScrollOrientationChangeHelper;
import com.minoon.weasel.util.TouchEventHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a13587 on 15/06/27.
 */
public class CollapsingHeaderLayout extends RelativeLayout implements TouchEventHelper.Callback,
        ScrollableView<CollapsingHeaderLayout.WeaselEvent>,
        ScrollOrientationChangeHelper.ScrollOrientationChangeListener {
    private static final String TAG = Logger.createTag(CollapsingHeaderLayout.class.getSimpleName());

    public enum WeaselEvent {
        START_SCROLL_BACK,
        START_SCROLL_FORWARD,
        DRAG_TO_TOP,
        DRAG_FROM_TOP,
        FLICK_SCROLL_BACK,
        FLICK_SCROLL_FORWARD
    }

    private static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 3000;

    private int mActivePointerId = INVALID_POINTER;

    private Scroller mScroller;

    private View mDragView;

    private View mHeaderView;

    private TouchEventTrader mTouchEventTrader;

    private TouchEventHelper mTouchEventHelper;

    private final List<Weasel<WeaselEvent>> mWeasels;

    private int mContentScrollPosition = 0;

    private ScrollOrientationChangeHelper mScrollOrientationHelper;

    private float mHeaderDragMultiplier = 1f;
    private float mHeaderAlpha = 1f;

    private boolean mInterceptHeaderTouchEventForScroll = true;

    private RecyclerView mRecyclerView;

    public Weasel mWeasel;

    public CollapsingHeaderLayout(Context context) {
        this(context, null);
    }

    public CollapsingHeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CollapsingHeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mWeasels = new ArrayList<>();
        mScroller = new Scroller(context, new DecelerateInterpolator());
        mTouchEventHelper = new TouchEventHelper(context, this);
        mScrollOrientationHelper = new ScrollOrientationChangeHelper(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CollapsingHeaderLayout);
        if (a != null) {
            mHeaderAlpha = a.getFloat(R.styleable.CollapsingHeaderLayout_chl_alpha, 1f);
            mHeaderDragMultiplier = a.getFloat(R.styleable.CollapsingHeaderLayout_chl_scrollMultiplier, 1f);
            mInterceptHeaderTouchEventForScroll = a.getBoolean(R.styleable.CollapsingHeaderLayout_chl_interceptHeaderTouchForScroll, true);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Logger.d(TAG, "onLayout. l='%s', t='%s', r='%s', b='%s'", l, t, r, b);
        // find 'HeaderView' and 'DragView'
        mHeaderView = getChildAt(0);
        mDragView = getChildAt(1);
        // find first RecyclerView in the DragView.
        mRecyclerView = getFirstRecyclerView(mDragView);
        mTouchEventTrader = new LinearLayoutRecyclerViewTrader(mRecyclerView);
        setupWeasel();
    }

    /*
     * find first RecyclerView in the target view.
     * @param targetView
     * @return RecyclerView or null.
     */
    private RecyclerView getFirstRecyclerView(View targetView) {
        if (targetView instanceof RecyclerView) {
            return (RecyclerView)targetView;
        } else if (targetView instanceof ViewGroup) {
            ViewGroup targetViewGroup = (ViewGroup) targetView;
            int childCount = targetViewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                RecyclerView r = getFirstRecyclerView(targetViewGroup.getChildAt(i));
                if (r != null) {
                    return r;
                }
            }
        }
        return null;
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
        boolean interceptTap = isViewUnder(mDragView, (int) ev.getX(), (int) ev.getY()) || mInterceptHeaderTouchEventForScroll;
        return interceptTap;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Logger.d(TAG, "[onTouchEvent]x=" + getScrollPositionX() + ", y=" + getScrollPositionY() + ", from=" + mDragView.getBottom());
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

    //// DragView position

    public boolean isAtTop() {
        return mDragView.getTop() <= 0;
    }

    public int getTopBounds() {
        return 0;
    }

    public int getBottomBounds() {
        return mHeaderView.getBottom();
    }

    public void flickToTop(float velocity) {
        int distance = calculateDistanceByVelocity(velocity);
        int duration = calculeteDurationByVelocity(velocity);
        Logger.d(TAG, String.format("flickToTop. dration='%s'", duration));
        mScroller.startScroll(getScrollPositionX(), getScrollPositionY(), 0, -distance, duration);
        postInvalidate();
        notifyEvent(WeaselEvent.FLICK_SCROLL_FORWARD);
    }

    public void flickToBottom(float velocity) {
        Logger.d(TAG, "flickToBottom");
        int distance = calculateDistanceByVelocity(velocity);
        int duration = calculeteDurationByVelocity(velocity);
        mScroller.startScroll(getScrollPositionX(), getScrollPositionY(), 0, distance, duration);
        postInvalidate();
        notifyEvent(WeaselEvent.FLICK_SCROLL_BACK);
    }


    //// Basic Scroll Method

    private int getScrollPositionY() {
        return mDragView.getTop();
    }

    private int getScrollPositionX() {
        return mDragView.getLeft();
    }

    private int calculateDistanceByVelocity(float velocity) {
        Logger.d(TAG, String.format("velocity='%s'", velocity));
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
        top = Math.max(getTopBounds(), Math.min(getBottomBounds(), top));
        int oldTop = mDragView.getTop();
        mDragView.setTop(top);
        final int dy = oldTop - top;
        if (oldTop == 0 && dy < 0) {
            notifyEvent(WeaselEvent.DRAG_FROM_TOP);
        } else if (top == 0 && dy > 0){
            notifyEvent(WeaselEvent.DRAG_TO_TOP);
        }
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


    /** {@link TouchEventHelper.Callback} */


    private static final float X_MIN_VELOCITY = 1500;
    private static final float Y_MIN_VELOCITY = 1000;

    @Override
    public void onViewReleased(float xvel, float yvel) {
        Logger.d(TAG, "onViewReleased.x=" + xvel + ", y=" + yvel);
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
    public void dispathcTouchEventForChild(MotionEvent ev) {
        ev = cloneMotionEventWithFixPosition(ev);
        mDragView.dispatchTouchEvent(ev);
    }

    @Override
    public void scrollViewBy(int dx, int dy) {
        if(mTouchEventTrader.stealTouchEventForChild()) {
            mTouchEventTrader.scrollBy(dx, -dy);
            mContentScrollPosition -= dy;
        } else if(isAtTop()) {
            if(dy > 0) {
                scrollDragViewTo(getScrollPositionY() + dy);
            } else {
                mTouchEventTrader.scrollBy(dx, -dy);
                mContentScrollPosition -= dy;
            }
        } else {
            mContentScrollPosition = 0;
            scrollDragViewTo(getScrollPositionY() + dy);
        }

        mScrollOrientationHelper.onScroll(-dy);

        notifyScroll();
    }


    /** {@link ScrollableView} */


    @Override
    public void addWeasel(Weasel<WeaselEvent> weasel) {
        mWeasels.add(weasel);
    }

    @Override
    public WeaselBuilder<WeaselEvent> startWeasel() {
        return new WeaselBuilder<>(this);
    }

    private void notifyScroll() {
        int scrollPosition = getBottomBounds() - getScrollPositionY() + mContentScrollPosition;
        for (Weasel w : mWeasels) {
            w.chase(scrollPosition);
        }
    }

    private void notifyEvent(WeaselEvent ev) {
        int scrollPosition = getBottomBounds() - getScrollPositionY() + mContentScrollPosition;
        for (Weasel<WeaselEvent> w : mWeasels) {
            w.event(ev, scrollPosition);
        }
    }


    /** {@link ScrollOrientationChangeHelper.ScrollOrientationChangeListener} */


    @Override
    public void onOrientationChage(boolean up) {
        WeaselEvent ev = up ? WeaselEvent.START_SCROLL_BACK : WeaselEvent.START_SCROLL_FORWARD;
        notifyEvent(ev);
    }

    public void enableScrollByHeaderTouchEvent(boolean enable) {
        // TODO うまく動作していないので調査
        mInterceptHeaderTouchEventForScroll = enable;
    }

    private void setupWeasel() {
        if (mWeasel == null) {
            mWeasel = startWeasel()
                    .from(new State())
                    .to(new HideAtWindowTopState(mHeaderView).alpha(mHeaderAlpha))
                    .ratio(mHeaderDragMultiplier)
                    .start(mHeaderView);
        }
    }
}
