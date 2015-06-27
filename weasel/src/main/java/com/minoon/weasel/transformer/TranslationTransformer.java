package com.minoon.weasel.transformer;

import android.view.View;

import com.minoon.weasel.State;

/**
 * Created by a13587 on 15/06/28.
 */
public class TranslationTransformer implements Transformer {
    private static final String TAG = TranslationTransformer.class.getSimpleName();

    State mFromState;
    State mToState;

    public TranslationTransformer(State fromState, State toState) {
        mFromState = fromState;
        mToState = toState;
    }

    @Override
    public void transform(View view, float offset) {
        int fromX = mFromState.getTranslateX();
        int toX = mToState.getTranslateX();
        int fromY = mFromState.getTranslateY();
        int toY = mToState.getTranslateY();
        view.setTranslationX(fromX + (toX - fromX) * offset);
        view.setTranslationY(fromY + (toY - fromY) * offset);
    }
}
