package com.minoon.weasel;

import android.view.View;

import com.minoon.weasel.transformer.AlphaTransformer;
import com.minoon.weasel.transformer.Transformer;
import com.minoon.weasel.transformer.TranslationTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 'Smooth' アニメーションのヘルパークラス
 *
 * Created by a13587 on 15/07/10.
 */
public class SmoothChaseHelper {
    private static final String TAG = SmoothChaseHelper.class.getSimpleName();

    private static final int NO_RANGE  = Integer.MIN_VALUE;

    private State mFromState;

    private State mToState;

    private List<Transformer> mTransformers;

    private int mOffset;

    private float mRatio = 1.0f;

    private int mRange = NO_RANGE;

    /* package */ SmoothChaseHelper(State fromState, State toState) {
        mFromState = fromState == null ? new State() : fromState;
        mToState = toState == null ? new State() : toState;
        mTransformers = new ArrayList<>();
        // Basic Transformer TODO ちゃんとすべて実装
        mTransformers.add(new AlphaTransformer(mFromState.getAlpha(), mToState.getAlpha()));
        mTransformers.add(new TranslationTransformer(mFromState, mToState));
    }

    /* package */ void addTransformer(Transformer transformer) {
        mTransformers.add(transformer);
    }

    /* package */ void setOffset(int offset) {
        mOffset = offset;
    }

    /* package */ void setRatio(float ratio) {
        mRatio = Math.max(0, Math.min(1, ratio));
    }

    /* package */ void setRange(int range) {
        if (range < 0) {
            throw new IllegalArgumentException("range should be over 0 value. specified range is '" + range + "'");
        }
        mRange = range;
    }


    /**
     * スクロール位置に応じてViewの変形を実行する。
     *
     * @param scrollPosition
     */
    public void transform(View view, int scrollPosition) {
        if (scrollPosition < mOffset) {
            return;
        }
        scrollPosition -= mOffset;
        // 現在のスクロールの進捗状況を計算
        int range = calculateRange();
        if (range == 0) {
            return;
        }
        float currentPosition = scrollPosition * mRatio;
        float offset = Math.max(0f, Math.min(1f, currentPosition / (float) range));
        for (Transformer t : mTransformers) {
            t.transform(view, offset);
        }
    }

    /**
     * 変形を行うスクロールの範囲を計算する。
     *
     * @return
     */
    private int calculateRange() {
        if (mRange != NO_RANGE) {
            return mRange;
        }
        // TODO ステータスから計算 ここはうまい方法考える
        int fromY = mFromState.getTranslateY();
        int toY = mToState.getTranslateY();
        return Math.abs(fromY - toY);
    }
}
