package com.example.administrator.thinker_soft.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.activity.ContinueCheckUserActivity;
import com.example.administrator.thinker_soft.activity.NewTaskActivity;
import com.example.administrator.thinker_soft.activity.NoCheckUserListActivity;
import com.example.administrator.thinker_soft.activity.SecurityStatisticsActivity;
import com.example.administrator.thinker_soft.activity.TaskChooseActivity;
import com.example.administrator.thinker_soft.activity.UserListActivity;
import com.example.administrator.thinker_soft.mode.Tools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
public class SecurityChooseFragment extends Fragment {
    private View view;
    //继续安检  //用户列表 //未检用户 //新建任务   //安检统计     //任务选择
    private LinearLayout continueSecurity, userList, noCheckUser, newTask, securityStatistics, taskChoose;
    private ArrayList<String> stringList = new ArrayList<>();//得到的字符串集合
    private ArrayList<String> transferStringList = new ArrayList<>();//传递的字符串集合
    private Set<String> stringSet = new HashSet<>();//保存字符串参数
    private int task_total_numb;
    private Bundle params;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_security, null);

        defaultSetting();//初始化设置
        getTaskParams();//获取任务编号参数
        bindView();//绑定控件ID
        setViewClickListener();//点击事件
        return view;
    }

    //获取任务编号参数
    public void getTaskParams() {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            Log.i("SecurityChooseFragment=", "intent不为空");
        }
    }

    //绑定控件
    public void bindView() {
        continueSecurity = (LinearLayout) view.findViewById(R.id.continue_security);
        userList = (LinearLayout) view.findViewById(R.id.user_list);
        noCheckUser = (LinearLayout) view.findViewById(R.id.no_check_user);
        newTask = (LinearLayout) view.findViewById(R.id.new_task);
        securityStatistics = (LinearLayout) view.findViewById(R.id.security_statistics);
        taskChoose = (LinearLayout) view.findViewById(R.id.task_choose);
    }

    //初始化设置
    private void defaultSetting() {
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //点击事件
    private void setViewClickListener() {
        continueSecurity.setOnClickListener(clickListener);
        userList.setOnClickListener(clickListener);
        noCheckUser.setOnClickListener(clickListener);
        newTask.setOnClickListener(clickListener);
        securityStatistics.setOnClickListener(clickListener);
        taskChoose.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.continue_security:    //继续安检
                    Intent intent = new Intent(getActivity(), ContinueCheckUserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_list:
                    Intent intent1 = new Intent(getActivity(), UserListActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.no_check_user:
                    Intent intent2 = new Intent(getActivity(), NoCheckUserListActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.new_task:
                    if (Tools.NetIsAvilable(getActivity())) {
                        Intent intent3 = new Intent(getActivity(), NewTaskActivity.class);
                        startActivity(intent3);
                    } else {
                        Toast.makeText(getActivity(), "网络未连接，请打开网络！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.security_statistics:
                    Intent intent4 = new Intent(getActivity(), SecurityStatisticsActivity.class);
                    startActivity(intent4);
                    /*Uri uri = Uri.parse("http://88.88.88.231:8080/yewu");
                    Intent intent6 = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent6);*/
                    break;
                case R.id.task_choose:
                    Intent intent5 = new Intent(getActivity(), TaskChooseActivity.class);
                    startActivityForResult(intent5, 100);
                    break;
            }
        }
    };

    public void transferParams(Bundle bundle) {
        Iterator iterator = sharedPreferences.getStringSet("stringSet", null).iterator();
        while (iterator.hasNext()) {
            transferStringList.add(iterator.next().toString());
        }
        bundle.putStringArrayList("taskId", transferStringList);
        bundle.putInt("task_total_numb", task_total_numb);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


}
