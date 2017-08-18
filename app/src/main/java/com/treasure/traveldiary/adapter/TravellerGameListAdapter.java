package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.TravellerGameItemBean;

import java.util.List;

/**
 * Created by treasure on 2017.06.19.
 */

public class TravellerGameListAdapter extends BaseAdapter {
    private Context context;
    private List<TravellerGameItemBean> list;
    private LayoutInflater mInflater;

    public TravellerGameListAdapter(Context context, List<TravellerGameItemBean> list) {
        this.context = context;
        this.list = list;
        mInflater = LayoutInflater.from(context);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View ret = null;
        ViewHolder holder = null;
        if (convertView != null) {
            ret = convertView;
            holder = (ViewHolder) ret.getTag();
        } else {
            ret = mInflater.inflate(R.layout.small_game_list_item, parent, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }
        final TravellerGameItemBean homeH5GameItemBean = list.get(position);
        holder.back.setImageResource(homeH5GameItemBean.getBack());
        holder.name.setText(homeH5GameItemBean.getName());
        holder.desc.setText(homeH5GameItemBean.getDesc());
        holder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClick.itemClick(homeH5GameItemBean.getName());
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private ImageView back;
        private TextView name;
        private TextView desc;

        public ViewHolder(View view) {
            back = (ImageView) view.findViewById(R.id.small_game_item_back);
            name = (TextView) view.findViewById(R.id.small_game_item_name);
            desc = (TextView) view.findViewById(R.id.small_game_item_desc);
        }
    }

    public interface ItemClick {
        void itemClick(String name);
    }

    private ItemClick itemClick;

    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }
}
