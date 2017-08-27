package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.DiaryBean;

import java.util.List;

public class ChoiceStackAdapter extends StackAdapter<DiaryBean> {
    public static List<DiaryBean> list;

    public ChoiceStackAdapter(Context context, List<DiaryBean> list) {
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
            case R.layout.choice_text_list_item:
                view = getLayoutInflater().inflate(R.layout.choice_text_list_item, parent, false);
                return new TextViewHolder(view);
            case R.layout.choice_image1_list_item:
                view = getLayoutInflater().inflate(R.layout.choice_image1_list_item, parent, false);
                return new Image1ViewHolder(view);
            case R.layout.choice_image2_list_item:
                view = getLayoutInflater().inflate(R.layout.choice_image2_list_item, parent, false);
                return new Image2ViewHolder(view);
            case R.layout.choice_image3_list_item:
                view = getLayoutInflater().inflate(R.layout.choice_image3_list_item, parent, false);
                return new Image3ViewHolder(view);
            default:
                view = getLayoutInflater().inflate(R.layout.choice_video_list_item, parent, false);
                return new VideoViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        String diary_type = list.get(position).getDiary_type();
        if (diary_type.equals("0")) {//TODO TEST LARGER ITEM
            return R.layout.choice_text_list_item;
        } else if (diary_type.equals("1")) {
            if (list.get(position).getDiary_image().size() == 1){
                return R.layout.choice_image1_list_item;
            }else if (list.get(position).getDiary_image().size() == 2){
                return R.layout.choice_image2_list_item;
            }else {
                return R.layout.choice_image3_list_item;
            }
        }else {
            return R.layout.choice_video_list_item;
        }
    }

    static class TextViewHolder extends CardStackView.ViewHolder{
        private TextView user_title,state_text,leaveMes_num,user_desc,user_name;
        private ImageView state_img;
        private SimpleDraweeView user_icon;
        private FrameLayout layout;
        public TextViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            leaveMes_num = (TextView) view.findViewById(R.id.diary_list_leave_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            layout = (FrameLayout) view.findViewById(R.id.diary_list_hind_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            layout.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            user_title.setText(data.getUser_title()+"");
            state_text.setText(data.getState()+"");
            user_desc.setText(data.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(data.getUser_icon()+""));
            user_name.setText(data.getUser_nick()+"");
            if (data.getState().equals("公开")){
                state_img.setImageResource(R.mipmap.ic_lock_open);
            }else {
                state_img.setImageResource(R.mipmap.ic_lock_close);
            }
            leaveMes_num.setText((data.getMesBeanList().size() - 1)+"");
        }

    }

    static class Image1ViewHolder extends CardStackView.ViewHolder {
        private SimpleDraweeView image1;
        private TextView user_title,state_text,leaveMes_num,user_desc,user_name;
        private ImageView state_img;
        private SimpleDraweeView user_icon;
        private FrameLayout layout;

        public Image1ViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            leaveMes_num = (TextView) view.findViewById(R.id.diary_list_leave_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            layout = (FrameLayout) view.findViewById(R.id.diary_list_hind_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            layout.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            user_title.setText(data.getUser_title()+"");
            state_text.setText(data.getState()+"");
            user_desc.setText(data.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(data.getUser_icon()+""));
            user_name.setText(data.getUser_nick()+"");
            image1.setImageURI(Uri.parse(data.getDiary_image().get(0)+""));
            if (data.getState().equals("公开")){
                state_img.setImageResource(R.mipmap.ic_lock_open);
            }else {
                state_img.setImageResource(R.mipmap.ic_lock_close);
            }
            leaveMes_num.setText((data.getMesBeanList().size() - 1)+"");
        }

    }

    static class Image2ViewHolder extends CardStackView.ViewHolder {
        private SimpleDraweeView image1,image2;
        private TextView user_title,state_text,leaveMes_num,user_desc,user_name;
        private ImageView state_img,like_img;
        private SimpleDraweeView user_icon;
        private FrameLayout layout;

        public Image2ViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            leaveMes_num = (TextView) view.findViewById(R.id.diary_list_leave_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            image2 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image2);
            layout = (FrameLayout) view.findViewById(R.id.diary_list_hind_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            layout.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            user_title.setText(data.getUser_title()+"");
            state_text.setText(data.getState()+"");
            user_desc.setText(data.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(data.getUser_icon()+""));
            user_name.setText(data.getUser_nick()+"");
            image1.setImageURI(Uri.parse(data.getDiary_image().get(0)+""));
            image2.setImageURI(Uri.parse(data.getDiary_image().get(1)+""));
            if (data.getState().equals("公开")){
                state_img.setImageResource(R.mipmap.ic_lock_open);
            }else {
                state_img.setImageResource(R.mipmap.ic_lock_close);
            }
            leaveMes_num.setText((data.getMesBeanList().size() - 1)+"");
        }

    }

    static class Image3ViewHolder extends CardStackView.ViewHolder {
        private SimpleDraweeView image1,image2,image3;
        private TextView user_title,state_text,leaveMes_num,user_desc,user_name;
        private ImageView state_img;
        private SimpleDraweeView user_icon;
        private FrameLayout layout;

        public Image3ViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            leaveMes_num = (TextView) view.findViewById(R.id.diary_list_leave_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image1 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image1);
            image2 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image2);
            image3 = (SimpleDraweeView) view.findViewById(R.id.diary_list_image3);
            layout = (FrameLayout) view.findViewById(R.id.diary_list_hind_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            layout.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            user_title.setText(data.getUser_title()+"");
            state_text.setText(data.getState()+"");
            user_desc.setText(data.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(data.getUser_icon()+""));
            user_name.setText(data.getUser_nick()+"");
            image1.setImageURI(Uri.parse(data.getDiary_image().get(0)+""));
            image2.setImageURI(Uri.parse(data.getDiary_image().get(1)+""));
            image3.setImageURI(Uri.parse(data.getDiary_image().get(2)+""));
            if (data.getState().equals("公开")){
                state_img.setImageResource(R.mipmap.ic_lock_open);
            }else {
                state_img.setImageResource(R.mipmap.ic_lock_close);
            }
            leaveMes_num.setText((data.getMesBeanList().size() - 1)+"");
        }

    }

    static class VideoViewHolder extends CardStackView.ViewHolder {
        private FrameLayout layout;
        private SimpleDraweeView image;
        private TextView user_title,state_text,leaveMes_num,user_desc,user_name;
        private ImageView state_img;
        private SimpleDraweeView user_icon;

        public VideoViewHolder(View view) {
            super(view);
            user_title = (TextView)view.findViewById(R.id.diary_list_user_title);
            state_img = (ImageView)view.findViewById(R.id.diary_list_state_img);
            state_text = (TextView) view.findViewById(R.id.diary_list_state_text);
            leaveMes_num = (TextView) view.findViewById(R.id.diary_list_leave_num);
            user_desc = (TextView) view.findViewById(R.id.diary_list_user_desc);
            user_icon = (SimpleDraweeView) view.findViewById(R.id.diary_list_user_icon);
            user_name = (TextView) view.findViewById(R.id.diary_list_user_name);
            image = (SimpleDraweeView) view.findViewById(R.id.diary_list_video);
            layout = (FrameLayout) view.findViewById(R.id.diary_list_hind_layout);
        }

        @Override
        public void onItemExpand(boolean b) {
            layout.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(DiaryBean data, int position) {
            user_title.setText(data.getUser_title()+"");
            state_text.setText(data.getState()+"");
            user_desc.setText(data.getUser_desc()+"");
            user_icon.setImageURI(Uri.parse(data.getUser_icon()+""));
            user_name.setText(data.getUser_nick()+"");
            image.setImageURI(Uri.parse(data.getDiary_video_first()+""));
            if (data.getState().equals("公开")){
                state_img.setImageResource(R.mipmap.ic_lock_open);
            }else {
                state_img.setImageResource(R.mipmap.ic_lock_close);
            }
            leaveMes_num.setText((data.getMesBeanList().size() - 1)+"");
        }

    }

}
