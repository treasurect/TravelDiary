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
import com.treasure.traveldiary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by treasure on 2017/8/1.
 */

public class TravellerDiaryListAdapter extends BaseAdapter {
    private Context context;
    private List<DiaryBean> list;
    private LayoutInflater inflater;
    private SharedPreferences mPreferences;
    private boolean isClickLike;

    public TravellerDiaryListAdapter(Context context, List<DiaryBean> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        mPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
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
        if (list.get(position).getDiary_type().equals("0")) {
            return 0;
        } else if (list.get(position).getDiary_type().equals("1")) {
            if (list.get(position).getDiary_image().size() == 1) {
                return 1;
            } else if (list.get(position).getDiary_image().size() == 2) {
                return 2;
            } else {
                return 3;
            }
        } else {
            return 4;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 5;
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
            } else if (getItemViewType(position) == 1) {
                ret = inflater.inflate(R.layout.diary_image1_list_item, viewGroup, false);
            } else if (getItemViewType(position) == 2) {
                ret = inflater.inflate(R.layout.diary_image2_list_item, viewGroup, false);
            } else if (getItemViewType(position) == 3) {
                ret = inflater.inflate(R.layout.diary_image3_list_item, viewGroup, false);
            } else {
                ret = inflater.inflate(R.layout.diary_video_list_item, viewGroup, false);
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
        if (!Tools.isNull(listBean.getState())){
            if (listBean.getState().equals("公开")){
                holder.state_img.setImageResource(R.mipmap.ic_lock_open);
                holder.state_text.setText("公开");
            }else {
                holder.state_img.setImageResource(R.mipmap.ic_lock_close);
                holder.state_text.setText("私密");
            }
        }
        if (listBean.getLikeBean() != null){
            holder.like_num.setText((listBean.getLikeBean().size() - 1)+"");
            for (int i = 0; i < listBean.getLikeBean().size(); i++) {
                if (listBean.getLikeBean().get(i).equals(mPreferences.getString("user_name",""))){
                    isClickLike = true;
                }
            }
            if (isClickLike){
                holder.like_img.setImageResource(R.mipmap.ic_like_click);
                isClickLike = false;
            }else {
                holder.like_img.setImageResource(R.mipmap.ic_like_unclick);
            }
        }
        if (listBean.getDiary_type().equals("1")) {
            if (listBean.getDiary_image() != null) {
                if (listBean.getDiary_image().size() == 1) {
                    holder.image1.setImageURI(Uri.parse(listBean.getDiary_image().get(0).toString()));
                } else if (listBean.getDiary_image().size() == 2) {
                    holder.image1.setImageURI(Uri.parse(listBean.getDiary_image().get(0).toString()));
                    holder.image2.setImageURI(Uri.parse(listBean.getDiary_image().get(1).toString()));
                } else if (listBean.getDiary_image().size() == 3) {
                    holder.image1.setImageURI(Uri.parse(listBean.getDiary_image().get(0).toString()));
                    holder.image2.setImageURI(Uri.parse(listBean.getDiary_image().get(1).toString()));
                    holder.image3.setImageURI(Uri.parse(listBean.getDiary_image().get(2).toString()));
                }
            }
        }
        if (listBean.getDiary_type().equals("2")) {
            if (!Tools.isNull(listBean.getDiary_video_first())) {
                holder.video_image.setImageURI(Uri.parse(listBean.getDiary_video_first()));
            }
        }
        holder.text_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaryTextClick.layoutClick(listBean);
            }
        });
        holder.like_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diaryTextClick.likeClick(listBean);
            }
        });
        return ret;
    }

    public static class ViewHolder {
        private ImageView like_img;
        private TextView like_num;
        private SimpleDraweeView video_image;
        private SimpleDraweeView image1, image2, image3;
        private SimpleDraweeView user_icon;
        private TextView user_name, user_time, user_desc, user_title;
        private LinearLayout text_layout;
        private TextView state_text;
        private ImageView state_img;

        public ViewHolder(View view) {
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = ((TextView) view.findViewById(R.id.diary_list_user_name));
            user_time = ((TextView) view.findViewById(R.id.diary_list_user_time));
            user_desc = ((TextView) view.findViewById(R.id.diary_list_user_desc));
            user_title = ((TextView) view.findViewById(R.id.diary_list_user_title));
            text_layout = (LinearLayout) view.findViewById(R.id.diary_list_layout);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            image2 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image2);
            image3 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image3);
            video_image = (SimpleDraweeView) view.findViewById(R.id.diary_list_video);
            state_text = (TextView)view.findViewById(R.id.diary_list_state_text);
            state_img = (ImageView) view.findViewById(R.id.diary_list_state_img);
            like_img = (ImageView) view.findViewById(R.id.diary_list_like_img);
            like_num = (TextView) view.findViewById(R.id.diary_list_like_num);
        }
    }

    public interface DiaryTextClick {
        void layoutClick(DiaryBean diaryBean);
        void likeClick(DiaryBean diaryBean);
    }

    public DiaryTextClick diaryTextClick = null;

    public void setDiaryTextClick(DiaryTextClick diaryTextClick) {
        this.diaryTextClick = diaryTextClick;
    }
}
