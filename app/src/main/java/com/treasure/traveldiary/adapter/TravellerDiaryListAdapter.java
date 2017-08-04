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
import com.treasure.traveldiary.bean.DiaryBean;
import com.treasure.traveldiary.utils.LogUtil;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/1.
 */

public class TravellerDiaryListAdapter extends BaseAdapter {
    private Context context;
    private List<DiaryBean> list;
    private LayoutInflater inflater;

    public TravellerDiaryListAdapter(Context context, List<DiaryBean> list) {
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
    public int getItemViewType(int position) {
        if (list.get(position).getDiary_type() == 0){
            return 0;
        }else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View ret = null;
        ViewHolder holder = null;
          final DiaryBean listBean = list.get(position);
        if (view != null) {
            ret = view;
             holder = (ViewHolder) ret.getTag();
        } else {
            if (getItemViewType(position) == 0) {
                ret = inflater.inflate(R.layout.diary_text_list_item, viewGroup, false);
            }else{
                ret = inflater.inflate(R.layout.diary_image_list_item, viewGroup, false);
            }
             holder = new ViewHolder(ret);
            ret.setTag(holder);
        }

        if (!Tools.isNull(listBean.getUser_icon())) {
            holder.user_icon.setImageURI(Uri.parse(listBean.getUser_icon()));
        }

        if (!Tools.isNull(listBean.getUser_nick())) {
            holder.user_name.setText(listBean.getUser_nick());
        }
        if (!Tools.isNull(listBean.getPublish_time())) {
            holder.user_time.setText(listBean.getPublish_time());
        }
        if (!Tools.isNull(listBean.getUser_desc())) {
            holder.user_desc.setText(listBean.getUser_desc());
        }
        if (!Tools.isNull(listBean.getUser_title())) {
            holder.user_title.setText(listBean.getUser_title());
        }
        if (listBean.getDiary_type() == 1){
            if (listBean.getDiary_image() !=null){
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.image3.setVisibility(View.VISIBLE);
                if (listBean.getDiary_image().size() == 1){
                    holder.image1.setImageURI(Uri.parse(listBean.getDiary_image().get(0).toString()));
                    holder.image2.setVisibility(View.GONE);
                    holder.image3.setVisibility(View.GONE);
                }else if (listBean.getDiary_image().size() == 2){
                    holder.image1.setImageURI(Uri.parse(listBean.getDiary_image().get(0).toString()));
                    holder.image2.setImageURI(Uri.parse(listBean.getDiary_image().get(1).toString()));
                    holder.image3.setVisibility(View.GONE);
                }else if (listBean.getDiary_image().size() == 3){
                    holder.image1.setImageURI(Uri.parse(listBean.getDiary_image().get(0).toString()));
                    holder.image2.setImageURI(Uri.parse(listBean.getDiary_image().get(1).toString()));
                    holder.image3.setImageURI(Uri.parse(listBean.getDiary_image().get(2).toString()));
                }
            }
        }
        holder.text_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaryTextClick.textClick(listBean);
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private SimpleDraweeView image1,image2,image3;
        private SimpleDraweeView user_icon;
        private TextView user_name, user_time, user_desc, user_title;
        private LinearLayout text_layout;

        public ViewHolder(View view) {
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = ((TextView) view.findViewById(R.id.diary_list_user_name));
            user_time = ((TextView) view.findViewById(R.id.diary_list_user_time));
            user_desc = ((TextView) view.findViewById(R.id.diary_list_user_desc));
            user_title = ((TextView) view.findViewById(R.id.diary_list_user_title));
            text_layout = (LinearLayout)view.findViewById(R.id.diary_list_layout);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            image2 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image2);
            image3 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image3);
        }
    }
    public interface DiaryTextClick{
        void textClick(DiaryBean diaryBean);
    }
    public DiaryTextClick diaryTextClick = null;

    public void setDiaryTextClick(DiaryTextClick diaryTextClick) {
        this.diaryTextClick = diaryTextClick;
    }
}
