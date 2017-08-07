package com.treasure.traveldiary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.treasure.traveldiary.R;

public class SmallGameFragment extends BaseFragment {
    private boolean isPrepare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_small_game, container, false);
        isPrepare = true;
        initFindId();
        return view;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepare || !isVisible)
            return;
    }

    private void initFindId() {

    }
}
