package com.minoon.weasel.sample.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.minoon.weasel.sample.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by a13587 on 15/08/22.
 */
public class HeaderFragment extends Fragment {
    private static final String TAG = HeaderFragment.class.getSimpleName();

    @Bind(R.id.fragment_header_iv_image)
    ImageView mImageView;

    public static HeaderFragment newInstance() {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        Glide.with(this)
                .load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVpUuW4jLKhi1d3iulS83BP1UPj5e_n3cc6_NLUi2_G-v_46fJ")
                .into(mImageView);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hello Weasel!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
