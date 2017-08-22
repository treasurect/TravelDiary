package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;

import java.util.List;

public class ChoicenessStackAdapter extends StackAdapter<DiaryBean> {
    public static List<DiaryBean> list;

    public ChoicenessStackAdapter(Context context, List<DiaryBean> list) {
        super(context);
        this.list = list;
    }

    @Override
    public void bindView(DiaryBean data, int position, CardStackView.ViewHolder holder) {
        if (holder instanceof TextViewHolder) {
            TextViewHolder h = (TextViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof Image1ViewHolder) {
            Image1ViewHolder h = (Image1ViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof Image2ViewHolder) {
            Image2ViewHolder h = (Image2ViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof Image3ViewHolder) {
            Image3ViewHolder h = (Image3ViewHolder) holder;
            h.onBind(data, position);
        }
        if (holder instanceof VideoViewHolder) {
            VideoViewHolder h = (VideoViewHolder) holder;
            h.onBind(data, position);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case R.layout.diary_text_list_item:
                view = getLayoutInflater().inflate(R.layout.diary_text_list_item, parent, false);
                return new TextViewHolder(view);
            case R.layout.diary_image1_list_item:
                view = getLayoutInflater().inflate(R.layout.diary_image1_list_item, parent, false);
                return new Image1ViewHolder(view);
            case R.layout.diary_image2_list_item:
                view = getLayoutInflater().inflate(R.layout.diary_image2_list_item, parent, false);
                return new Image2ViewHolder(view);
            case R.layout.diary_image3_list_item:
                view = getLayoutInflater().inflate(R.layout.diary_image3_list_item, parent, false);
                return new Image3ViewHolder(view);
            default:
                view = getLayoutInflater().inflate(R.layout.diary_video_list_item, parent, false);
                return new VideoViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String diary_type = list.get(position).getDiary_type();
        if (diary_type.equals("0")) {//TODO TEST LARGER ITEM
            return R.layout.diary_text_list_item;
        } else if (diary_type.equals("1")) {
            if (list.get(position).getDiary_image().size() == 1){
                return R.layout.diary_image1_list_item;
            }else if (list.get(position).getDiary_image().size() == 2){
                return R.layout.diary_image2_list_item;
            }else {
                return R.layout.diary_image3_list_item;
            }
        }else {
            return R.layout.diary_video_list_item;
        }
    }

    static class TextViewHolder extends CardStackView.ViewHolder{
        private TextView user_title,state_text,like_num,user_desc,user_name;
        private ImageView state_img,like_img;
        private SimpleDraweeView user_icon;
        private LinearLayout layout;
        public TextViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            like_img = (ImageView)view.findViewById(R.id.diary_list_like_img);
            like_num = (TextView) view.findViewById(R.id.diary_list_like_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            layout = (LinearLayout) view.findViewById(R.id.diary_list_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            user_desc.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            DiaryBean diaryBean = list.get(position);
            user_title.setText(diaryBean.getUser_title()+"");
            state_text.setText(diaryBean.getState()+"");
            like_num.setText(diaryBean.getLikeBean().size()+"");
            user_desc.setText(diaryBean.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()+""));
            user_name.setText(diaryBean.getUser_nick()+"");
//            layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_4));
        }

    }

    static class Image1ViewHolder extends CardStackView.ViewHolder {
        private SimpleDraweeView image1;
        private TextView user_title,state_text,like_num,user_desc,user_name;
        private ImageView state_img,like_img;
        private SimpleDraweeView user_icon;
        private LinearLayout layout;

        public Image1ViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            like_img = (ImageView)view.findViewById(R.id.diary_list_like_img);
            like_num = (TextView) view.findViewById(R.id.diary_list_like_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            layout = (LinearLayout) view.findViewById(R.id.diary_list_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            user_desc.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            DiaryBean diaryBean = list.get(position);
            user_title.setText(diaryBean.getUser_title()+"");
            state_text.setText(diaryBean.getState()+"");
            like_num.setText(diaryBean.getLikeBean().size()+"");
            user_desc.setText(diaryBean.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()+""));
            user_name.setText(diaryBean.getUser_nick()+"");
            image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0)+""));
//            layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_8));
        }

    }

    static class Image2ViewHolder extends CardStackView.ViewHolder {
        private SimpleDraweeView image1,image2;
        private TextView user_title,state_text,like_num,user_desc,user_name;
        private ImageView state_img,like_img;
        private SimpleDraweeView user_icon;
        private LinearLayout layout;

        public Image2ViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            like_img = (ImageView)view.findViewById(R.id.diary_list_like_img);
            like_num = (TextView) view.findViewById(R.id.diary_list_like_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            image2 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image2);
            layout = (LinearLayout) view.findViewById(R.id.diary_list_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            user_desc.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            DiaryBean diaryBean = list.get(position);
            user_title.setText(diaryBean.getUser_title()+"");
            state_text.setText(diaryBean.getState()+"");
            like_num.setText(diaryBean.getLikeBean().size()+"");
            user_desc.setText(diaryBean.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()+""));
            user_name.setText(diaryBean.getUser_nick()+"");
            image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0)+""));
            image2.setImageURI(Uri.parse(diaryBean.getDiary_image().get(1)+""));
//            layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_12));
        }

    }

    static class Image3ViewHolder extends CardStackView.ViewHolder {
        private SimpleDraweeView image1,image2,image3;
        private TextView user_title,state_text,like_num,user_desc,user_name;
        private ImageView state_img,like_img;
        private SimpleDraweeView user_icon;
        private LinearLayout layout;

        public Image3ViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            like_img = (ImageView)view.findViewById(R.id.diary_list_like_img);
            like_num = (TextView) view.findViewById(R.id.diary_list_like_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            image2 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image2);
            image3 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image3);
            layout = (LinearLayout) view.findViewById(R.id.diary_list_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            user_desc.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            DiaryBean diaryBean = list.get(position);
            user_title.setText(diaryBean.getUser_title()+"");
            state_text.setText(diaryBean.getState()+"");
            like_num.setText(diaryBean.getLikeBean().size()+"");
            user_desc.setText(diaryBean.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()+""));
            user_name.setText(diaryBean.getUser_nick()+"");
            image1.setImageURI(Uri.parse(diaryBean.getDiary_image().get(0)+""));
            image2.setImageURI(Uri.parse(diaryBean.getDiary_image().get(1)+""));
            image3.setImageURI(Uri.parse(diaryBean.getDiary_image().get(2)+""));
//            layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_16));
        }

    }

    static class VideoViewHolder extends CardStackView.ViewHolder {
        private LinearLayout layout;
        private SimpleDraweeView image;
        private TextView user_title,state_text,like_num,user_desc,user_name;
        private ImageView state_img,like_img;
        private SimpleDraweeView user_icon;

        public VideoViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            layout = (LinearLayout) view.findViewById(R.id.diary_list_layout);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            like_img = (ImageView)view.findViewById(R.id.diary_list_like_img);
            like_num = (TextView) view.findViewById(R.id.diary_list_like_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image = (SimpleDraweeView) view.findViewById(R.id.diary_list_video);
        }

        @Override
        public void onItemExpand(boolean b) {
            user_desc.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            DiaryBean diaryBean = list.get(position);
            user_title.setText(diaryBean.getUser_title()+"");
            state_text.setText(diaryBean.getState()+"");
            like_num.setText(diaryBean.getLikeBean().size()+"");
            user_desc.setText(diaryBean.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(diaryBean.getUser_icon()+""));
            user_name.setText(diaryBean.getUser_nick()+"");
            image.setImageURI(Uri.parse(diaryBean.getDiary_video_first()+""));
//            layout.setBackgroundColor(getContext().getResources().getColor(R.color.color_20));
        }

    }

}
