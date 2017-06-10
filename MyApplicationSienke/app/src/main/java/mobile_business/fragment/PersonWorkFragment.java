package mobile_business.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/9.
 */
public class PersonWorkFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_person_work, null);

        bindView(); //绑定控件ID
        setViewClickListener();//点击事件

        return view;
    }

    //绑定控件ID
    public void bindView(){

    }

    //点击事件
    public void setViewClickListener(){

    }
}
