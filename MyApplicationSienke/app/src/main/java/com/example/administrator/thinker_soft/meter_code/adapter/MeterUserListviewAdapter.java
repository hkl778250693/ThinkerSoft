package com.example.administrator.thinker_soft.meter_code.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.meter_code.model.MeterUserListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class MeterUserListviewAdapter extends BaseAdapter {
    private Context context;
    private List<MeterUserListviewItem> itemList;  //传递过来的数据源

    public MeterUserListviewAdapter(Context context, List<MeterUserListviewItem> itemList) {
        this.context = context;
        this.itemList = itemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    private LayoutInflater layoutInflater;
    @Override
    public int getCount() {
        if (itemList == null) {
            return 0;
        } else {
            return itemList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (itemList == null) {
            return null;
        } else {
            return itemList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.meter_user_list_item,null);
            viewHolder.meterId = (TextView) convertView.findViewById(R.id.meter_id);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.userId = (TextView) convertView.findViewById(R.id.user_id);
            viewHolder.meterNumber = (TextView) convertView.findViewById(R.id.meter_number);
            viewHolder.lastMonth = (TextView) convertView.findViewById(R.id.last_month);
            viewHolder.thisMonth = (TextView) convertView.findViewById(R.id.this_month);
            viewHolder.address = (TextView) convertView.findViewById(R.id.address);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MeterUserListviewItem item = itemList.get(position);
        viewHolder.meterId.setText(item.getMeterID());
        viewHolder.userName.setText(item.getUserName());
        viewHolder.userId.setText(item.getUserID());
        viewHolder.meterNumber.setText(item.getMeterNumber());
        viewHolder.lastMonth.setText(item.getLastMonth());
        viewHolder.thisMonth.setText(item.getThisMonth());
        viewHolder.address.setText(item.getAddress());
        return convertView;
    }

    class ViewHolder {
        TextView meterId;
        TextView userName;
        TextView userId;
        TextView meterNumber;
        TextView lastMonth;
        TextView thisMonth;
        TextView address;
    }
}
