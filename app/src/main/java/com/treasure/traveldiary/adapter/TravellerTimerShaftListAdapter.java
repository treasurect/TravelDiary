package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.treasure.traveldiary.R;

import java.util.List;

/**
 * Created by treasure on 2017/8/16.
 */

public class TravellerTimerShaftListAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public TravellerTimerShaftListAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.diary_timer_shaft_list_item, viewGroup, false);
        
        String s = list.get(position);
        ViewHolder holder = new ViewHolder(view);

        holder.text.setText(s);
        if (position == list.size() - 1){
            holder.img.setVisibility(View.VISIBLE);
            holder.text2.setText("账户注册");
        }else if (position == list.size() - 2){
            holder.img.setVisibility(View.VISIBLE);
            holder.text2.setText("首次发布");
        }else if (position == 0){
            holder.img2.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static class ViewHolder {
        private TextView text;
        private TextView text2;
        private ImageView img;
        private ImageView img2;

        public ViewHolder(View view) {
            text = ((TextView) view.findViewById(R.id.timer_shaft_list_text));
            text2 = ((TextView) view.findViewById(R.id.timer_shaft_list_text2));
            img = ((ImageView) view.findViewById(R.id.timer_shaft_list_img));
            img2 = ((ImageView) view.findViewById(R.id.timer_shaft_list_arrows));
        }
    }
}
