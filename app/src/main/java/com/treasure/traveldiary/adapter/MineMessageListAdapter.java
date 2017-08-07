package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.bean.PushBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/7.
 */

public class MineMessageListAdapter extends BaseAdapter{
    private Context context;
    private List<PushBean>list;
    private LayoutInflater inflater;

    public MineMessageListAdapter(Context context, List<PushBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null){
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View ret = null;
        ViewHolder viewHolder = null;
        if (ret != null){
            ret = view;
            viewHolder = (ViewHolder) ret.getTag();
        }else {
            ret = inflater.inflate(R.layout.message_list_item,viewGroup,false);
            viewHolder = new ViewHolder(ret);
            ret.setTag(viewHolder);
        }
        PushBean pushBean = list.get(i);
        if (pushBean != null){
            if (!Tools.isNull(pushBean.getTitle())){
                viewHolder.msg_title.setText(pushBean.getTitle());
            }
            if (!Tools.isNull(pushBean.getTime())){
                viewHolder.msg_time.setText(pushBean.getTime());
            }
            if (!Tools.isNull(pushBean.getMessage())){
                viewHolder.msg_content.setText(pushBean.getMessage());
            }
        }
        return ret;
    }
    public static class ViewHolder{
        private TextView msg_title,msg_time,msg_content;
        public ViewHolder(View view) {
            msg_title = (TextView)view.findViewById(R.id.message_item_title);
            msg_time = (TextView)view.findViewById(R.id.message_item_time);
            msg_content = (TextView)view.findViewById(R.id.message_item_content);
        }
    }
}
