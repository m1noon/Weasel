package com.minoon.weasel.sample.ui;

import android.support.v7.widget.RecyclerView;

/**
 * Created by a13587 on 15/08/24.
 */
public interface ScrollChaser {
    void chaseStart(RecyclerView recyclerView);

    void chaseEnd(RecyclerView recyclerView);
}
