package com.example.administrator.thinker_soft.mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.mobile_business.BusinessEmailListviewItem;

import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
public class EmailInfoAdapter extends BaseAdapter {
    private Context context;
    private List<BusinessEmailListviewItem> businessEmailListviewItems;
    private LayoutInflater layoutInflater;


    public EmailInfoAdapter(Context context, List<BusinessEmailListviewItem> businessEmailListviewItemList) {
        this.context = context;
        this.businessEmailListviewItems = businessEmailListviewItemList;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        if (businessEmailListviewItems == null) {
            return 0;
        } else {
            return businessEmailListviewItems.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (businessEmailListviewItems == null) {
            return null;
        } else {
            return businessEmailListviewItems.get(position);
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
            convertView = layoutInflater.inflate(R.layout.email_info_item, null);
            viewHolder.check = (CheckBox) convertView.findViewById(R.id.check);
            viewHolder.email_adress = (TextView) convertView.findViewById(R.id.email_adress);
            viewHolder.check1 = (CheckBox) convertView.findViewById(R.id.check1);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BusinessEmailListviewItem item = businessEmailListviewItems.get(position);
        viewHolder.email_adress.setText(item.getEmailAdress());
        viewHolder.title.setText(item.getTitle());
        viewHolder.content.setText(item.getContent());
        viewHolder.time.setText(item.getTime());
        return convertView;
    }


    public class ViewHolder {
        public CheckBox check;
        public TextView email_adress;
        public CheckBox check1;
        public TextView title;
        public TextView time;
        public TextView content;
    }
}
