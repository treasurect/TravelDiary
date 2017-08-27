package com.treasure.traveldiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.activity.diary_center.GameH5Activity;
import com.treasure.traveldiary.activity.diary_center.GamePuzzleActivity;
import com.treasure.traveldiary.adapter.TravellerGameListAdapter;
import com.treasure.traveldiary.bean.TravellerGameItemBean;

import java.util.ArrayList;
import java.util.List;

public class MineSmallGameFragment extends BaseFragment implements TravellerGameListAdapter.ItemClick {
    private boolean isPrepared;
    private ListView listView;
    private List<TravellerGameItemBean> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_small_game, container, false);
        initFindId(view);
        isPrepared = true;
        initListView();
        return view;
    }

    private void initFindId(View view) {
        listView = ((ListView) view.findViewById(R.id.small_game_listView));
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible)
            return;

    }

    private void initListView() {
        list = new ArrayList<>();
        TravellerGameItemBean homeH5GameItemBean = new TravellerGameItemBean();
        homeH5GameItemBean.setBack(R.drawable.gradient_green_orange_radius5);
        homeH5GameItemBean.setName("拼图游戏");
        homeH5GameItemBean.setDesc("Come On！！！");
        list.add(homeH5GameItemBean);

        homeH5GameItemBean = new TravellerGameItemBean();
        homeH5GameItemBean.setBack(R.drawable.gradient_orange_yellow_radius5);
        homeH5GameItemBean.setName("猴子接桃");
        homeH5GameItemBean.setDesc("来比比谁的分数比较高");
        list.add(homeH5GameItemBean);

        homeH5GameItemBean = new TravellerGameItemBean();
        homeH5GameItemBean.setBack(R.drawable.gradient_yellow_green_radius5);
        homeH5GameItemBean.setName("红还是绿");
        homeH5GameItemBean.setDesc("来比比观察力和反应力 ");
        list.add(homeH5GameItemBean);

        TravellerGameListAdapter adapter = new TravellerGameListAdapter(getContext(), list);
        listView.setAdapter(adapter);
        adapter.setItemClick(this);
    }

    @Override
    public void itemClick(String name) {
        switch (name){
            case "拼图游戏":
                startActivity(new Intent(getContext(), GamePuzzleActivity.class));
                break;
            default:
                Intent intent = new Intent(getContext(), GameH5Activity.class);
                intent.putExtra("game_type",name);
                startActivity(intent);
                break;
        }
    }
}
