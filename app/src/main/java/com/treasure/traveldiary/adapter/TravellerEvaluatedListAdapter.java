package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.EvaluatedBean;
import com.treasure.traveldiary.fragments.BaseFragment;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by treasure on 2017/8/7.
 */

public class TravellerEvaluatedListAdapter extends BaseAdapter{
    private Context context;
    private List<EvaluatedBean>list;
    private LayoutInflater inflater;

    public TravellerEvaluatedListAdapter(Context context, List<EvaluatedBean> list) {
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
            ret = inflater.inflate(R.layout.evaluated_list_item,viewGroup,false);
            viewHolder = new ViewHolder(ret);
            ret.setTag(viewHolder);
        }
        EvaluatedBean evaluatedBean = list.get(i);
        if (evaluatedBean != null){
            if (!Tools.isNull(evaluatedBean.getUser_icon())){
                viewHolder.user_icon.setImageURI(Uri.parse(evaluatedBean.getUser_icon()));
            }
            if (!Tools.isNull(evaluatedBean.getUser_nick())){
                viewHolder.user_name.setText(evaluatedBean.getUser_nick());
            }
            if (!Tools.isNull(evaluatedBean.getPublish_time())){
                viewHolder.user_time.setText(evaluatedBean.getPublish_time());
            }
            if (!Tools.isNull(evaluatedBean.getUser_evaluated())){
                viewHolder.user_title.setText(evaluatedBean.getUser_evaluated());
            }
            if (!Tools.isNull(String.valueOf(evaluatedBean.getStar_num()))){
                if (evaluatedBean.getStar_num() == 1){
                    viewHolder.star1.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star2.setImageResource(R.mipmap.ic_star_unclick);
                    viewHolder.star3.setImageResource(R.mipmap.ic_star_unclick);
                    viewHolder.star4.setImageResource(R.mipmap.ic_star_unclick);
                    viewHolder.star5.setImageResource(R.mipmap.ic_star_unclick);
                }else if (evaluatedBean.getStar_num() == 2){
                    viewHolder.star1.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star2.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star3.setImageResource(R.mipmap.ic_star_unclick);
                    viewHolder.star4.setImageResource(R.mipmap.ic_star_unclick);
                    viewHolder.star5.setImageResource(R.mipmap.ic_star_unclick);
                }else if (evaluatedBean.getStar_num() == 3){
                    viewHolder.star1.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star2.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star3.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star4.setImageResource(R.mipmap.ic_star_unclick);
                    viewHolder.star5.setImageResource(R.mipmap.ic_star_unclick);
                }else if (evaluatedBean.getStar_num() == 4){
                    viewHolder.star1.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star2.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star3.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star4.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star5.setImageResource(R.mipmap.ic_star_unclick);
                }else if (evaluatedBean.getStar_num() == 5){
                    viewHolder.star1.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star2.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star3.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star4.setImageResource(R.mipmap.ic_star_click);
                    viewHolder.star5.setImageResource(R.mipmap.ic_star_click);
                }
            }
        }
        return ret;
    }
    public static class ViewHolder{
        private SimpleDraweeView user_icon;
        private TextView user_name,user_time,user_title;
        private ImageView star1,star2,star3,star4,star5;
        public ViewHolder(View view) {
            user_icon = (SimpleDraweeView)view.findViewById(R.id.evaluated_list_user_icon);
            user_name = (TextView)view.findViewById(R.id.evaluated_list_user_name);
            user_time = (TextView)view.findViewById(R.id.evaluated_list_user_time);
            user_title = (TextView)view.findViewById(R.id.evaluated_list_user_title);
            star1 = (ImageView) view.findViewById(R.id.evaluated_list_user_star1);
            star2 = (ImageView) view.findViewById(R.id.evaluated_list_user_star2);
            star3 = (ImageView) view.findViewById(R.id.evaluated_list_user_star3);
            star4 = (ImageView) view.findViewById(R.id.evaluated_list_user_star4);
            star5 = (ImageView) view.findViewById(R.id.evaluated_list_user_star5);
        }
    }
}
