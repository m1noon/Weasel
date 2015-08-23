package com.minoon.weasel.boundary;

import android.view.View;

/**
 * Created by a13587 on 15/06/27.
 */
@Deprecated
public class BoundaryMakerFactory {
    private static final String TAG = BoundaryMakerFactory.class.getSimpleName();

    public static BoundaryMaker create(View view, View parent) {
        return new VerticalScrollBoundaryMaker(view, parent);
    }
}
