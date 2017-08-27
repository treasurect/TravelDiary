package com.treasure.traveldiary.adapter;

import android.content.Context;
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
import com.treasure.traveldiary.bean.ForumBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/1.
 */

public class TravellerForumListAdapter extends BaseAdapter {
    private Context context;
    private List<ForumBean> list;
    private LayoutInflater inflater;

    public TravellerForumListAdapter(Context context, List<ForumBean> list) {
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
        final ForumBean listBean = list.get(position);
        if (view != null) {
            ret = view;
            holder = (ViewHolder) ret.getTag();
        } else {
                ret = inflater.inflate(R.layout.forum_list_item, viewGroup, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }

        if (!Tools.isNull(listBean.getPublish_content())) {
            holder.content.setText(listBean.getPublish_content());
        }

        if (!Tools.isNull(listBean.getPublish_from())) {
            holder.from.setText(listBean.getPublish_from());
        }
        if (!Tools.isNull(listBean.getPublish_time())) {
            holder.time.setText(listBean.getPublish_time());
        }
        if (listBean.getLeaveMes_list() != null) {
            holder.leaveMes_num.setText((listBean.getLeaveMes_list().size() - 1)+"");
        }
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forumClick.layoutClick(listBean);
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private TextView content, from, time,leaveMes_num;
        private LinearLayout layout;

        public ViewHolder(View view) {
            content = ((TextView) view.findViewById(R.id.forum_list_content));
            from = ((TextView) view.findViewById(R.id.forum_list_from));
            time = ((TextView) view.findViewById(R.id.forum_list_time));
            leaveMes_num = ((TextView) view.findViewById(R.id.forum_list_leaveMes_num));
            layout = (LinearLayout) view.findViewById(R.id.forum_list_layout);
        }
    }

    public interface ForumClick {
        void layoutClick(ForumBean forumBean);
    }

    public ForumClick forumClick = null;

    public void setForumClick(ForumClick forumClick) {
        this.forumClick = forumClick;
    }
}
