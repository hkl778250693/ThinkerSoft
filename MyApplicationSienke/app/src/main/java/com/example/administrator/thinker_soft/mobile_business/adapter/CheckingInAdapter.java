package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.util.List;

/**
 * Created by Administrator on 2017/6/22.
 */
public class CheckingInAdapter extends BaseAdapter {

    private Context context;
    private List<String> stringList;
    private LayoutInflater layoutInflater;

    public CheckingInAdapter(Context context, List<String> stringList1) {
        this.context = context;
        this.stringList = stringList1;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (stringList == null) {
            return 0;
        } else {
            return stringList.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (stringList == null) {
            return null;
        } else {
            return stringList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.business_checking_listview_item, null);
            viewHolder.xianshi = (TextView) convertView.findViewById(R.id.xianshi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.xianshi.setText(stringList.get(position));
        return convertView;
    }

    public class ViewHolder {
        public TextView xianshi;
    }
}
