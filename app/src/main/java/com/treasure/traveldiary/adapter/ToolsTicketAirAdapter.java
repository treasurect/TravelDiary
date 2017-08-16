package com.treasure.traveldiary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.treasure.traveldiary.R;
import com.treasure.traveldiary.bean.ToolsTicketAirBean;
import com.treasure.traveldiary.utils.Tools;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ToolsTicketAirAdapter extends BaseAdapter {
    private Context context;
    private List<ToolsTicketAirBean.ResultBean> list;
    private LayoutInflater inflater;

    public ToolsTicketAirAdapter(Context context, List<ToolsTicketAirBean.ResultBean> list) {
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
            ret = inflater.inflate(R.layout.ticket_air_list_item, viewGroup, false);
            holder = new ViewHolder(ret);
            ret.setTag(holder);
        }
        ToolsTicketAirBean.ResultBean resultBean = list.get(i);
        if (resultBean != null){
            if (!Tools.isNull(resultBean.getFrom())){
                holder.station_start.setText(resultBean.getFrom());
            }
            if (!Tools.isNull(resultBean.getPlanTime())){
                holder.time_start.setText(resultBean.getPlanTime());
            }
            if (!Tools.isNull(resultBean.getFlightNo())){
                holder.station_code.setText(resultBean.getFlightNo());
                holder.station_flightNo.setText(resultBean.getFlightNo());
            }
            if (!Tools.isNull(resultBean.getFlightTime())){
                holder.station_lishi.setText(resultBean.getFlightTime());
            }
            if (!Tools.isNull(resultBean.getTo())){
                holder.station_end.setText(resultBean.getTo());
            }
            if (!Tools.isNull(resultBean.getPlanArriveTime())){
                holder.time_end.setText(resultBean.getPlanArriveTime());
            }
            if (!Tools.isNull(resultBean.getFlightRate())){
                holder.station_rate.setText("准点率："+resultBean.getFlightRate());
            }
            if (!Tools.isNull(resultBean.getAirLines())){
                holder.station_airLines.setText(resultBean.getAirLines());
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
        private TextView station_rate;
        private TextView station_airLines;
        private TextView station_flightNo;

        public ViewHolder(View view) {
            station_start = (TextView) view.findViewById(R.id.train_g_station_start);
            time_start = (TextView) view.findViewById(R.id.train_g_time_start);
            station_code = (TextView) view.findViewById(R.id.train_g_station_code);
            station_lishi = (TextView) view.findViewById(R.id.train_g_station_lishi);
            station_end = (TextView) view.findViewById(R.id.train_g_station_end);
            time_end = (TextView) view.findViewById(R.id.train_g_time_end);
            station_rate = (TextView) view.findViewById(R.id.train_g_station_flightRate);
            station_airLines = (TextView) view.findViewById(R.id.train_g_station_airlines);
            station_flightNo = (TextView) view.findViewById(R.id.train_g_station_flightNo);
        }
    }
}
