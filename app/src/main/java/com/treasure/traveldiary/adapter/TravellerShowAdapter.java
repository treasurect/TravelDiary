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
import com.treasure.traveldiary.bean.UserInfoBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/22.
 */

public class TravellerShowAdapter extends BaseAdapter {
    private List<UserInfoBean> list;
    private Context context;
    private LayoutInflater inflater;

    public TravellerShowAdapter(List<UserInfoBean> list, Context context) {
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
        View ret = null;
        ViewHolder holder = null;
        if (view != null) {
            ret = view;
            holder = (ViewHolder) ret.getTag();
        } else {
            ret = inflater.inflate(R.layout.traveller_detail_item, viewGroup, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }
        final UserInfoBean userInfoBean = list.get(position);
        if (!Tools.isNull(userInfoBean.getUser_icon())) {
            holder.icon.setImageURI(Uri.parse(userInfoBean.getUser_icon()));
        }
        if (!Tools.isNull(userInfoBean.getUser_name())) {
            holder.name.setText("账号：" + userInfoBean.getUser_name());
        }
        if (!Tools.isNull(userInfoBean.getNick_name())) {
            holder.nick.setText("昵称：" + userInfoBean.getNick_name());
        }
        if (!Tools.isNull(userInfoBean.getAge())) {
            holder.age.setText("年龄：" + userInfoBean.getAge() + "     性别：" + userInfoBean.getSex());
        }
        if (!Tools.isNull(userInfoBean.getUser_desc())) {
            holder.desc.setText(userInfoBean.getUser_desc());
        }
        holder.attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tShowClick.detailClick(userInfoBean,0);
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tShowClick.detailClick(userInfoBean,1);
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private SimpleDraweeView icon;
        private TextView name, nick, age, desc, attention;
        private LinearLayout layout;

        public ViewHolder(View view) {
            icon = ((SimpleDraweeView) view.findViewById(R.id.traveller_show_icon));
            name = ((TextView) view.findViewById(R.id.traveller_show_name));
            nick = ((TextView) view.findViewById(R.id.traveller_show_nick));
            age = ((TextView) view.findViewById(R.id.traveller_show_age));
            desc = ((TextView) view.findViewById(R.id.traveller_show_desc));
            attention = ((TextView) view.findViewById(R.id.traveller_show_attention));
            layout = ((LinearLayout) view.findViewById(R.id.traveller_show_layout));
        }
    }

    public interface TShowClick {
        void detailClick(UserInfoBean infoBean,int type);
    }

    private TShowClick tShowClick;

    public void settShowClick(TShowClick tShowClick) {
        this.tShowClick = tShowClick;
    }
}
