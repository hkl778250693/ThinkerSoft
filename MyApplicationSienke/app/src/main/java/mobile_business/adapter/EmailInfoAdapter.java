package mobile_business.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.administrator.thinker_soft.R;

import java.util.List;

import mobile_business.BusinessEmailListviewItem;

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
        }    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.userlist_listview_item, null);
            viewHolder.check = (RadioButton) convertView.findViewById(R.id.check);
            viewHolder.email_adress = (TextView) convertView.findViewById(R.id.email_adress);
            viewHolder.start_check = (ImageView) convertView.findViewById(R.id.start_check);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder{
        RadioButton check;
        TextView email_adress;
        ImageView start_check;
    }
}
