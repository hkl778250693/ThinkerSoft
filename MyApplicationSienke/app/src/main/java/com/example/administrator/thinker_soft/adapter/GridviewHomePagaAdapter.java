package com.example.administrator.thinker_soft.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.model.HomePageItem;
import com.example.administrator.thinker_soft.model.HomePageViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/5/24.
 */
public class GridviewHomePagaAdapter extends BaseAdapter {
    private Context context;
    private List<HomePageItem> homePageItems = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private static HashMap<Integer, Boolean> isCheck = new HashMap<Integer, Boolean>();

    public GridviewHomePagaAdapter(Context context, List<HomePageItem> homePageItems) {
        this.context = context;
        this.homePageItems = homePageItems;
        if (context != null) {
            layoutInflater = LayoutInflater.from(context);
        }
    }

    @Override
    public int getCount() {
        return homePageItems== null ? 0 : homePageItems.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return homePageItems== null ? null : homePageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomePageViewHolder viewholder;
        if (convertView == null) {
            viewholder = new HomePageViewHolder();
            convertView = layoutInflater.inflate(R.layout.gridview_move_homepage_item, null);
            viewholder.image = (ImageView) convertView.findViewById(R.id.image);
            viewholder.text = (TextView) convertView.findViewById(R.id.text);
            viewholder.checkBox= (CheckBox) convertView.findViewById(R.id.is_checked);
            convertView.setTag(viewholder);
        } else {
            viewholder = (HomePageViewHolder) convertView.getTag();
        }

        if (homePageItems.size() == position) {
            viewholder.text.setVisibility(View.GONE);
            viewholder.image.setVisibility(View.GONE);
            viewholder.checkBox.setVisibility(View.GONE);
        }else {
            HomePageItem item = homePageItems.get(position);
            // 正常显示
            if(homePageItems.get(position) != null){
                viewholder.image.setImageResource(item.getImage());
                viewholder.text.setText(item.getText());
                viewholder.checkBox.setChecked(item.isChecked());
            }
        }
        return convertView;
    }

    public static HashMap<Integer, Boolean> getIsCheck() {
        return isCheck;
    }

    public static void setIsCheck(HashMap<Integer, Boolean> isCheck) {
        GridviewHomePagaAdapter.isCheck = isCheck;
    }
}
