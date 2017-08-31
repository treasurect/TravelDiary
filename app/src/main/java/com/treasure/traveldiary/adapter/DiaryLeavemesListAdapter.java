package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.SUserBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/1.
 */

public class DiaryLeavemesListAdapter extends BaseAdapter {
    private Context context;
    private List<SUserBean> list;
    private LayoutInflater inflater;

    public DiaryLeavemesListAdapter(Context context, List<SUserBean> list) {
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
        final SUserBean listBean = list.get(position);
        if (view != null) {
            ret = view;
            holder = (ViewHolder) ret.getTag();
        } else {
                ret = inflater.inflate(R.layout.diary_leavemes_list_item, viewGroup, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }
        if (!Tools.isNull(listBean.getLeave_icon())){
            holder.user_icon.setImageURI(Uri.parse(listBean.getLeave_icon()));
        }
        if (!Tools.isNull(listBean.getLeave_nick())){
            holder.user_name.setText(listBean.getLeave_nick());
        }
        if (!Tools.isNull(listBean.getLeave_time())){
            holder.user_time.setText(listBean.getLeave_time());
        }
        if (!Tools.isNull(listBean.getLeave_content())){
            holder.user_desc.setText(listBean.getLeave_content());
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutClick.leave_mesClick(listBean.getLeave_name());
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private LinearLayout layout;
        private SimpleDraweeView user_icon;
        private TextView user_name, user_time, user_desc;

        public ViewHolder(View view) {
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_leaveMes_user_icon);
            user_name = ((TextView) view.findViewById(R.id.diary_leaveMes_user_name));
            user_time = ((TextView) view.findViewById(R.id.diary_leaveMes_user_time));
            user_desc = ((TextView) view.findViewById(R.id.diary_leaveMes_user_content));
            layout = (LinearLayout) view.findViewById(R.id.diary_leaveMes_item_layout);
        }
    }
    public interface LayoutClick{
        void leave_mesClick(String name);
    }
    private LayoutClick layoutClick;

    public void setLayoutClick(LayoutClick layoutClick) {
        this.layoutClick = layoutClick;
    }
}
