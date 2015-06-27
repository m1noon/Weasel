package com.minoon.weasel.transformer;

import android.view.View;

/**
 * Created by a13587 on 15/06/28.
 */
public class AlphaTransformer implements Transformer {
    private static final String TAG = AlphaTransformer.class.getSimpleName();

    final float mFromAlpha;

    final float mToAlpha;

    public AlphaTransformer(float fromAlpha, float toAlpha) {
        mFromAlpha = fromAlpha;
        mToAlpha = toAlpha;
    }

    @Override
    public void transform(View view, float offset) {
        view.setAlpha(mFromAlpha + (mToAlpha - mFromAlpha) * offset);
    }
}
