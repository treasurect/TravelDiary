package com.treasure.traveldiary.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.treasure.traveldiary.fragments.BaseFragment;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseFragment> list;
    public HomeFragmentPagerAdapter(FragmentManager fm, List<BaseFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null){
            ret = list.size();
        }
        return ret;
    }
}
