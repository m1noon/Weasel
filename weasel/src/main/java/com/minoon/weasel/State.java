package com.minoon.weasel;

/**
 * Created by a13587 on 15/06/28.
 */
public class State {
    private static final String TAG = State.class.getSimpleName();

    private int translateX = 0;

    private int translateY = 0;

    private float alpha = 1.0f;

    private boolean smooth = true;

    public State(int translateX, int translateY, float alpha, boolean smooth) {
        this.translateX = translateX;
        this.translateY = translateY;
        this.alpha = alpha;
        this.smooth = smooth;
    }

    public State() {
    }

    public State translageX(int value) {
        translateX = value;
        return this;
    }

    public State translateY(int value) {
        translateY = value;
        return this;
    }

    public State alpha(float value) {
        alpha = value;
        return this;
    }

    public int getTranslateX() {
        return translateX;
    }

    public void setTranslateX(int x) {
        this.translateX = x;
    }

    public int getTranslateY() {
        return translateY;
    }

    public void setTranslateY(int y) {
        this.translateY = y;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }

    @Override
    public String toString() {
        return "State{" +
                "translateX=" + translateX +
                ", translateY=" + translateY +
                ", alpha=" + alpha +
                ", smooth=" + smooth +
                '}';
    }
}
