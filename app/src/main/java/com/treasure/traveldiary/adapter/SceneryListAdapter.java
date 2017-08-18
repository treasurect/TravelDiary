package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.bean.SceneryBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/1.
 */

public class SceneryListAdapter extends BaseAdapter {
    private Context context;
    private List<SceneryBean> list;
    private LayoutInflater inflater;

    public SceneryListAdapter(Context context, List<SceneryBean> list) {
        this.context = context;
        this.list = list;
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
        View ret = null;
        ViewHolder holder = null;
        final SceneryBean listBean = list.get(position);
        if (view != null) {
            ret = view;
            holder = (ViewHolder) ret.getTag();
        } else {
            ret = inflater.inflate(R.layout.scenery_list_item, viewGroup, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }

        if (listBean.getScenery_image() != null) {
            holder.list_image.setImageURI(Uri.parse(listBean.getScenery_image().get(0)));
        }

        if (!Tools.isNull(listBean.getScenery_name())) {
            holder.list_text.setText(listBean.getScenery_name());
        }
        holder.list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sceneryTextClick.layoutClick(listBean);
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private LinearLayout list_layout;
        private SimpleDraweeView list_image;
        private TextView list_text;

        public ViewHolder(View view) {
            list_image = (SimpleDraweeView) view.findViewById(R.id.scenery_list_image);
            list_text = ((TextView) view.findViewById(R.id.scenery_list_text));
            list_layout = ((LinearLayout) view.findViewById(R.id.scenery_list_layout));
        }
    }

    public interface SceneryTextClick {
        void layoutClick(SceneryBean sceneryBean);
    }

    public SceneryTextClick sceneryTextClick = null;

    public void setSceneryTextClick(SceneryTextClick sceneryTextClick) {
        this.sceneryTextClick = sceneryTextClick;
    }
}
