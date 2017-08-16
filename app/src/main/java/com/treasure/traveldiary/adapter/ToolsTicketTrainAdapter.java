package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.ToolsTicketTrainBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ToolsTicketTrainAdapter extends BaseAdapter {
    private Context context;
    private List<ToolsTicketTrainBean.ResultBean> list;
    private LayoutInflater inflater;

    public ToolsTicketTrainAdapter(Context context, List<ToolsTicketTrainBean.ResultBean> list) {
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
        ViewHolder holder = null;
        if (view != null) {
            ret = view;
            holder = (ViewHolder) ret.getTag();
        } else {
            ret = inflater.inflate(R.layout.ticket_train_list_item, viewGroup, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }
        ToolsTicketTrainBean.ResultBean resultBean = list.get(i);
        if (resultBean != null){
            if (!Tools.isNull(resultBean.getStartStationName())){
                holder.station_start.setText(resultBean.getStartStationName());
            }
            if (!Tools.isNull(resultBean.getStartTime())){
                holder.time_start.setText(resultBean.getStartTime());
            }
            if (!Tools.isNull(resultBean.getStationTrainCode())){
                holder.station_code.setText(resultBean.getStationTrainCode());
            }
            if (!Tools.isNull(resultBean.getLishi())){
                holder.station_lishi.setText(resultBean.getLishi());
            }
            if (!Tools.isNull(resultBean.getEndStationName())){
                holder.station_end.setText(resultBean.getEndStationName());
            }
            if (!Tools.isNull(resultBean.getArriveTime())){
                holder.time_end.setText(resultBean.getArriveTime());
            }
            if (!Tools.isNull(resultBean.getPricewz())){
                holder.station_pricewz.setText("无座\n"+resultBean.getPricewz());
            }
            if (!Tools.isNull(resultBean.getStationTrainCode())){
                if (resultBean.getStationTrainCode().substring(0,1).equals("D") || resultBean.getStationTrainCode().substring(0,1).equals("G")){
                    if (!Tools.isNull(resultBean.getPriceed())){
                        holder.station_priceed.setText("二等座：\n"+resultBean.getPriceed());
                    }
                    if (!Tools.isNull(resultBean.getPriceyd())){
                        holder.station_priceyd.setText("一等座：\n"+resultBean.getPriceyd());
                    }
                    if (!Tools.isNull(resultBean.getPricesw())){
                        holder.station_pricesw.setText("商务座：\n"+resultBean.getPricesw());
                    }
                }else {
                    if (!Tools.isNull(resultBean.getPriceyz())){
                        holder.station_priceed.setText("硬座：\n"+resultBean.getPriceyz());
                    }
                    if (!Tools.isNull(resultBean.getPriceyw())){
                        holder.station_priceyd.setText("硬卧：\n"+resultBean.getPriceyw());
                    }
                    if (!Tools.isNull(resultBean.getPricerw())){
                        holder.station_pricesw.setText("软卧：\n"+resultBean.getPricerw());
                    }else {
                        holder.station_pricesw.setText("");
                    }
                    if (!Tools.isNull(resultBean.getPricegrw())){
                        holder.station_price.setVisibility(View.VISIBLE);
                        holder.station_price.setText("高级软卧：\n"+resultBean.getPricegrw());
                    }else {
                        holder.station_price.setVisibility(View.GONE);
                    }
                }
            }
        }
        return ret;
    }

    private static class ViewHolder {
        private TextView station_start;
        private TextView time_start;
        private TextView station_code;
        private TextView station_lishi;
        private TextView station_end;
        private TextView time_end;
        private TextView station_pricewz;
        private TextView station_priceed;
        private TextView station_priceyd;
        private TextView station_pricesw;
        private TextView station_price;

        public ViewHolder(View view) {
            station_start = (TextView) view.findViewById(R.id.train_g_station_start);
            time_start = (TextView) view.findViewById(R.id.train_g_time_start);
            station_code = (TextView) view.findViewById(R.id.train_g_station_code);
            station_lishi = (TextView) view.findViewById(R.id.train_g_station_lishi);
            station_end = (TextView) view.findViewById(R.id.train_g_station_end);
            time_end = (TextView) view.findViewById(R.id.train_g_time_end);
            station_pricewz = (TextView) view.findViewById(R.id.train_g_station_pricewz);
            station_priceed = (TextView) view.findViewById(R.id.train_g_station_priceed);
            station_priceyd = (TextView) view.findViewById(R.id.train_g_station_priceyd);
            station_pricesw = (TextView) view.findViewById(R.id.train_g_station_pricesw);
            station_price = (TextView) view.findViewById(R.id.train_g_station_price);
        }
    }
}
