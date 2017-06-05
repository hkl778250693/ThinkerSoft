package com.example.administrator.thinker_soft.fragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.activity.UploadActivity;
import com.example.administrator.thinker_soft.adapter.GridviewTypeAdapter;
import com.example.administrator.thinker_soft.adapter.SelectTaskDownAdapter;
import com.example.administrator.thinker_soft.mode.MySqliteHelper;
import com.example.administrator.thinker_soft.model.GridviewTypeItem;
import com.example.administrator.thinker_soft.model.GridviewTypeViewholder;
import com.example.administrator.thinker_soft.model.SelectTaskDownViewHolder;
import com.example.administrator.thinker_soft.model.SelectTaskItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
public class DataTransferFragment extends Fragment {
    private View view, filterPopup, popupwindowView, uploadView, selectTaskView,noTaskView;
    private TextView upload, download, progressName, progressPercent, tips,oneTips,taskTips;
    private LinearLayout rootLinearlayout, linearlayoutDown;
    private Button finishBtn;
    private TextView confirm;
    private RadioButton cancelRb, saveRb, cancelSelect, saveSelect;
    private ImageView noTask;   //显示的任务首次已安检完
    private ImageView downFailed;
    private String taskResult, userResult; //网络请求结果
    private SharedPreferences sharedPreferences, sharedPreferences_login, public_sharedPreferences;
    private SharedPreferences.Editor editor;
    private String ip, port;  //接口ip地址   端口
    public int responseCode = 0;
    public int asyncResponseCode = 0;
    private LayoutInflater layoutInflater;
    private PopupWindow popupWindow;
    private JSONObject taskObject, userObject;
    private SQLiteDatabase db;  //数据库
    private ProgressBar downloadProgress;  //下载进度条
    private int currentProgress = 0;
    private int currentUserPercent = 0;
    private int currentPercent = 0;
    private int userProgress = 0;
    private JSONArray jsonArray;
    private long lastClickTime = 0;
    private TextView startDate;//开始日期选择器
    private TextView endDate;//结束日期选择器
    private RadioButton cancelFilter, downFilter;
    private GridviewTypeAdapter adapter;
    private SelectTaskDownAdapter taskDownAdapter;
    private List<GridviewTypeItem> gridviewTypeItemList = new ArrayList<>();
    private List<SelectTaskItem> selectTaskItemList = new ArrayList<>();
    private List<String> stringTaskList = new ArrayList<>();  //保存本地没有的任务编号
    private List<String> stringSelectTask = new ArrayList<>();  //保存勾选的任务编号
    private Cursor cursor, taskTotalCursor;
    private GridView gridView;
    private Calendar c; //日历
    private GridviewTypeItem item;
    private SelectTaskItem taskItem;
    private ArrayList<String> stringList = new ArrayList<>();//保存安检类型编号ID
    private String securityIds = "";  //存放下载数据的url的参数值
    private int res;
    private Cursor taskCursor;   //用于检查本地是否有相同的任务编号，有的话就不下载了
    private ListView listview;   //显示选择任务的集合
    private ImageView frameAnimation;
    private AnimationDrawable animationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data_transfer, null);

        bindView(); //绑定控件ID
        defaultSetting();//初始化设置
        setViewClickListener();//点击事件
        return view;
    }

    //绑定控件
    public void bindView() {
        upload = (TextView) view.findViewById(R.id.upload);
        download = (TextView) view.findViewById(R.id.download);
        rootLinearlayout = (LinearLayout) view.findViewById(R.id.root_linearlayout);
    }

    //点击事件
    private void setViewClickListener() {
        upload.setOnClickListener(clickListener);
        download.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.upload:
                    upload.setClickable(false);
                    getTaskData();
                    if (taskTotalCursor.getCount() != 0) {
                        createSavePopupwindow();
                    } else {
                        Intent intent = new Intent(getActivity(), UploadActivity.class);
                        startActivity(intent);
                    }
                    upload.setClickable(true);
                    break;
                case R.id.download:
                    download.setClickable(false);
                    showFilterPopup();
                    break;
            }
        }
    };

    //初始化设置
    private void defaultSetting() {
        sharedPreferences_login = getActivity().getSharedPreferences("login_info", Context.MODE_PRIVATE);
        sharedPreferences = getActivity().getSharedPreferences(sharedPreferences_login.getString("login_name", "") + "data", Context.MODE_PRIVATE);
        public_sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        MySqliteHelper helper = new MySqliteHelper(getActivity(), 1);
        db = helper.getWritableDatabase();
    }

    //读取安检状态信息
    public void getSecurityState() {
        gridviewTypeItemList.clear();
        cursor = db.query("SecurityState", null, null, null, null, null, null);//查询并获得游标
        Log.i("getSecurityState=>", " 查询到的状态个数为：" + cursor.getCount());
        //如果游标为空，则显示默认数据
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {
            GridviewTypeItem item = new GridviewTypeItem();
            item.setTypeId(cursor.getString(1));
            item.setTypeName(cursor.getString(2));
            gridviewTypeItemList.add(item);
        }
        Log.i("getSecurityState=>", " 安检状态个数为：" + gridviewTypeItemList.size());
    }

    //弹出上传前提示popupwindow
    public void createSavePopupwindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        uploadView = layoutInflater.inflate(R.layout.popupwindow_user_detail_info_save, null);
        popupWindow = new PopupWindow(uploadView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        tips = (TextView) uploadView.findViewById(R.id.tips);
        cancelRb = (RadioButton) uploadView.findViewById(R.id.cancel_rb);
        saveRb = (RadioButton) uploadView.findViewById(R.id.save_rb);
        //设置点击事件
        tips.setText("请确保你的任务完成哦！");
        saveRb.setText("确认完成");
        cancelRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        saveRb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //没有任务下载的提示
    public void createNoTaskPopup() {
        layoutInflater = LayoutInflater.from(getActivity());
        noTaskView = layoutInflater.inflate(R.layout.popupwindow_no_task, null);
        popupWindow = new PopupWindow(noTaskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        oneTips = (TextView) noTaskView.findViewById(R.id.one_tips);
        confirm = (TextView) noTaskView.findViewById(R.id.confirm);
        //设置点击事件
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //弹出下载前选择任务编号popupwindow
    public void showSelectTaskPoup() {
        layoutInflater = LayoutInflater.from(getActivity());
        selectTaskView = layoutInflater.inflate(R.layout.popupwindow_select_task_down, null);
        popupWindow = new PopupWindow(selectTaskView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //绑定控件ID
        cancelSelect = (RadioButton) selectTaskView.findViewById(R.id.cancel_select);
        saveSelect = (RadioButton) selectTaskView.findViewById(R.id.save_select);
        listview = (ListView) selectTaskView.findViewById(R.id.listview);
        taskTips = (TextView) selectTaskView.findViewById(R.id.task_tips);
        frameAnimation = (ImageView) selectTaskView.findViewById(R.id.frame_animation);
        noTask = (ImageView) selectTaskView.findViewById(R.id.no_task);
        taskTips.setText("正在查询任务数据，请稍后...");
        //设置点击事件
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SelectTaskDownViewHolder holder = (SelectTaskDownViewHolder) view.getTag();
                holder.checkBox.toggle();
                SelectTaskDownAdapter.getIsCheck().put(position, holder.checkBox.isChecked());
            }
        });
        cancelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                download.setClickable(true);
            }
        });
        saveSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelect.setClickable(false);
                if(noTask.getVisibility() == View.GONE){
                    saveSelectTaskInfo();  //保存勾选的任务编号信息
                    if (stringSelectTask.size() != 0) {   //如果任务勾选了
                        stringTaskList.clear();
                        for (int i = 0; i < stringSelectTask.size(); i++) {
                            getTaskData(stringSelectTask.get(i), sharedPreferences_login.getString("login_name", ""));//读取所有安检用户数据
                            if (taskCursor.getCount() == 0) {   //如果本地不存在本任务信息则将此任务编号添加到下载任务的集合中
                                stringTaskList.add(stringSelectTask.get(i));
                            } else {
                                Toast.makeText(getActivity(), "编号为" + stringSelectTask.get(i) + "的任务本地已存在，不能重复下载哦", Toast.LENGTH_SHORT).show();
                            }
                        }
                        Log.i("taskNumbList====>", "一共有" + stringTaskList.size() + "个任务");
                        if (stringTaskList.size() != 0) {  //当有需要下载的任务信息时，弹出下载进度条
                            popupWindow.dismiss();
                            showPopupwindow(); //弹出下载进度条
                            downloadProgress.setMax(10 * stringTaskList.size());
                            startAsyncTask();//开启异步任务获取所有任务编号的用户数据
                        }
                    } else {
                        Toast.makeText(getActivity(), "请选择任务编号哦！", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    popupWindow.dismiss();
                }
                saveSelect.setClickable(true);
                download.setClickable(true);
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //开始帧动画
    public void startFrameAnimation() {
        frameAnimation.setBackgroundResource(R.drawable.frame_animation_list);
        animationDrawable = (AnimationDrawable) frameAnimation.getDrawable();
        animationDrawable.start();
    }

    //下载前筛选popupwindow
    public void showFilterPopup() {
        layoutInflater = LayoutInflater.from(getActivity());
        filterPopup = layoutInflater.inflate(R.layout.popupwindow_download_detail, null);
        popupWindow = new PopupWindow(filterPopup, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView = (GridView) filterPopup.findViewById(R.id.gridview);
        startDate = (TextView) filterPopup.findViewById(R.id.start_date);
        endDate = (TextView) filterPopup.findViewById(R.id.end_date);
        cancelFilter = (RadioButton) filterPopup.findViewById(R.id.cancel_filter);
        downFilter = (RadioButton) filterPopup.findViewById(R.id.down_filter);
        //设置点击事件
        c = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
        startDate.setText(dateFormat.format(new Date()));
        endDate.setText(dateFormat.format(new Date()));
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate.setClickable(false);
                //开始时间选择器
                DatePickerDialog startDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        startDate.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                .append("-")
                                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                        startDate.setClickable(true);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                startDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        startDate.setClickable(true);
                    }
                });
                startDateDialog.show();
            }
        });
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDate.setClickable(false);
                //结束时间选择器
                DatePickerDialog endDateDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endDate.setText(new StringBuilder().append(year).append("-").append((monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : (monthOfYear + 1) + "")
                                .append("-")
                                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth + ""));
                        endDate.setClickable(true);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                endDateDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        endDate.setClickable(true);
                    }
                });
                endDateDialog.show();
            }
        });
        Log.i("NewTaskActivity", "开始时间:" + startDate.getText().toString());
        Log.i("NewTaskActivity", "结束时间:" + endDate.getText().toString());
        Log.i("NewTaskActivity", "比较结果:" + res);
        cancelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                download.setClickable(true);
            }
        });
        downFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str1 = startDate.getText().toString();
                String str2 = endDate.getText().toString();
                res = str1.compareTo(str2);
                Log.i("NewTaskActivity", "比较结果:" + res);
                if (res <= 0) {
                    saveSecurityTypeInfo();     //保存选中的安检类型编号信息
                    if (stringList.size() > 1) {
                        popupWindow.dismiss();
                        showSelectTaskPoup();
                        startFrameAnimation();
                        //开启支线程进行请求任务信息
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                securityIds = "";
                                for (int i = 0; i < stringList.size(); i++) {
                                    securityIds += stringList.get(i) + ",";
                                }
                                Log.i("securityIds","securityIds="+securityIds);
                                requireMyTask("SafeCheckPlan.do", "securityId=" + securityIds + "&startTime=" + startDate.getText().toString() + "&endTime=" + endDate.getText().toString());
                                super.run();
                            }
                        }.start();
                    } else if (stringList.size() == 1) {
                        popupWindow.dismiss();
                        showSelectTaskPoup();
                        startFrameAnimation();
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                securityIds = stringList.get(0);
                                requireMyTask("SafeCheckPlan.do", "securityId=" + securityIds + "&startTime=" + startDate.getText().toString() + "&endTime=" + endDate.getText().toString());
                                super.run();
                            }
                        }.start();
                    } else {
                        Toast.makeText(getActivity(), "请选择安检类型和时间哦！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "开始时间不能大于结束时间哦！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GridviewTypeViewholder viewholder = (GridviewTypeViewholder) view.getTag();
                viewholder.checkBox.toggle();
                GridviewTypeAdapter.getIsCheck().put(position, viewholder.checkBox.isChecked());
            }
        });
        popupWindow.update();
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.setAnimationStyle(R.style.camera);
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        new Thread() {
            @Override
            public void run() {
                getSecurityState();
                if (cursor.getCount() != 0) {
                    handler.sendEmptyMessage(10);
                } else {
                    handler.sendEmptyMessage(11);
                }
            }
        }.start();
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
                download.setClickable(true);
            }
        });
    }

    //保存选中的安检类型编号信息
    public void saveSecurityTypeInfo() {
        stringList.clear();
        int count = adapter.getCount();
        Log.i("count====>", "长度为：" + count);
        for (int i = 0; i < count; i++) {
            if (GridviewTypeAdapter.getIsCheck().get(i)) {
                item = gridviewTypeItemList.get((int) adapter.getItemId(i));
                Log.i("DataTransferFragment", "这次被勾选的安检类型编号是" + item.getTypeId());
                stringList.add(item.getTypeId());
            }
        }
        Log.i("DataTransferFragment", "安检类型集合长度为：" + stringList.size());
    }

    //保存选中的任务信息
    public void saveSelectTaskInfo() {
        stringSelectTask.clear();
        int count = taskDownAdapter.getCount();
        Log.i("count====>", "长度为：" + count);
        for (int i = 0; i < count; i++) {
            if (SelectTaskDownAdapter.getIsCheck().get(i)) {
                taskItem = selectTaskItemList.get((int) adapter.getItemId(i));
                Log.i("DataTransferFragment", "这次被勾选的任务是" + item.getTypeId());
                stringSelectTask.add(taskItem.getTaskId());
            }
        }
        Log.i("DataTransferFragment", "任务集合长度为：" + stringSelectTask.size());
    }

    //show下载popupwindow
    public void showPopupwindow() {
        layoutInflater = LayoutInflater.from(getActivity());
        popupwindowView = layoutInflater.inflate(R.layout.popupwindow_download_progressbar, null);
        popupWindow = new PopupWindow(popupwindowView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        linearlayoutDown = (LinearLayout) popupwindowView.findViewById(R.id.linearlayout_down);
        downFailed = (ImageView) popupwindowView.findViewById(R.id.down_failed);
        finishBtn = (Button) popupwindowView.findViewById(R.id.finish_btn);
        downloadProgress = (ProgressBar) popupwindowView.findViewById(R.id.download_progress);
        progressName = (TextView) popupwindowView.findViewById(R.id.progress_name);
        progressPercent = (TextView) popupwindowView.findViewById(R.id.progress_percent);
        progressName.setText("任务正在下载，请稍后...");
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadProgress.setProgress(0);
                download.setClickable(true);
                popupWindow.dismiss();
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white_transparent));
        popupWindow.showAtLocation(rootLinearlayout, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.6F);   //背景变暗
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0F);
            }
        });
    }

    //设置背景透明度
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        if (bgAlpha == 1) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getActivity().getWindow().setAttributes(lp);
    }

    //下载任务数据
    private void requireMyTask(final String method, final String keyAndValue) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url;
                    HttpURLConnection httpURLConnection;
                    Log.i("sharedPreferences====>", sharedPreferences.getString("IP", ""));
                    if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
                        ip = public_sharedPreferences.getString("security_ip", "");
                    } else {
                        ip = "192.168.2.201:";
                    }
                    if (!public_sharedPreferences.getString("security_port", "").equals("")) {
                        port = public_sharedPreferences.getString("security_port", "");
                    } else {
                        port = "8080";
                    }
                    String httpUrl = "http://" + ip + port + "/SMDemo/" + method;
                    //有参数传递
                    url = new URL(httpUrl + "?" + keyAndValue);
                    Log.i("url=============>", url + "");
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setRequestProperty("Accept-Encoding", "identity");
                    httpURLConnection.setRequestProperty("Content-Type", "applicaton/json;charset=UTF-8");
                    httpURLConnection.setConnectTimeout(6000);
                    httpURLConnection.setReadTimeout(6000);
                    httpURLConnection.connect();
                    //传回的数据解析成String
                    if ((responseCode = httpURLConnection.getResponseCode()) == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String str;
                        while ((str = bufferedReader.readLine()) != null) {
                            stringBuilder.append(str);
                        }
                        taskResult = stringBuilder.toString();
                        Log.i("taskResult=====>", taskResult);
                        JSONObject jsonObject = new JSONObject(taskResult);
                        if (jsonObject.optInt("total", 0) != 0) {
                            handler.sendEmptyMessage(1);
                        } else {
                            handler.sendEmptyMessage(2);
                        }
                    } else {
                        try {
                            Thread.sleep(3000);
                            handler.sendEmptyMessage(3);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.i("IOException", "下载任务数据网络请求异常!");
                    handler.sendEmptyMessage(3);
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //自定义异步任务下载用户数据
    //启动任务的参数，进度参数，结果参数
    public class MyAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            //在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记。
            Log.i("onPreExecute===>", "onPreExecute进来了！");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {    //所有的耗时操作都在此时进行，不能进行UI操作
            URL url = null;
            try {
                url = new URL(params[0]);
                Log.i("doInBackground===>", "url=" + url);
                HttpURLConnection httpURLConnection;
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(6000);
                httpURLConnection.setReadTimeout(6000);
                httpURLConnection.connect();
                //传回的数据解析成String
                Log.i("asyncResponseCode=>", httpURLConnection.getResponseCode() + "");
                if ((asyncResponseCode = httpURLConnection.getResponseCode()) == 200) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        stringBuilder.append(str);
                    }
                    userResult = stringBuilder.toString();
                    Log.i("userResult==========>", userResult);
                    JSONObject jsonObject = new JSONObject(userResult);
                    /*if (!jsonObject.optString("total", "").equals("0")) {
                        //有相应用户数据
                        editor.putBoolean("user_data", true);
                        editor.commit();
                        if (url.toString().contains(taskNumbList.get(0))) { //当第一个任务有数据的时候就将任务信息保存本地，下次将不会进来
                            for (int i = 0; i < taskNumbList.size(); i++) {
                                if (sharedPreferences.getBoolean("user_data", true)) {
                                    if (i == 0) {
                                        Log.i("jsonArray==========>", "jsonArray==" + jsonArray.length());
                                        for (int j = 0; j < jsonArray.length(); j++) {
                                            try {
                                                taskObject = jsonArray.getJSONObject(j);
                                                insertTaskDataBase();
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                    try {
                                        Thread.sleep(200);
                                        userProgress += 10 * taskNumbList.size() / taskNumbList.size();
                                        currentUserPercent = (1000 * (i + 1)) / (10 * taskNumbList.size());
                                        Message msg = new Message();
                                        msg.what = 9;
                                        msg.arg1 = userProgress;
                                        msg.arg2 = currentUserPercent;
                                        Log.i("down_user_progress=>", " 循环次数为" + taskNumbList.size());
                                        Log.i("down_user_progress=>", " 更新进度条" + userProgress);
                                        Log.i("down_user_progress=>", " 下载进度: " + currentUserPercent);
                                        handler.sendMessage(msg);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    handler.sendEmptyMessage(4);
                                    break;
                                }
                            }
                            *//*if(sharedPreferences.getBoolean("user_data",true)){    //下载完之后做相应处理
                                handler.sendEmptyMessage(10);
                            }*//*
                        }
                        return userResult;
                    } else {
                        editor.putBoolean("user_data", false);
                        editor.commit();
                        if (url.toString().contains(taskNumbList.get(taskNumbList.size() - 1))) {
                            //没有相应用户数据
                            handler.sendEmptyMessage(4);
                        }
                    }*/
                    if (jsonObject.optInt("total", 0) != 0) {
                        //有相应用户数据
                        if (url.toString().contains(stringTaskList.get(stringTaskList.size() - 1))) {
                            setProgress();//当第最后一个任务有数据的时候就将任务信息保存本地
                            for (int i = 0; i < 5; i++) {
                                try {
                                    Thread.sleep(200);
                                    userProgress += 10 * 5 / 5;
                                    currentUserPercent = (1000 * (i + 1)) / (10 * 5);
                                    Message msg = new Message();
                                    msg.what = 9;
                                    msg.arg1 = userProgress;
                                    msg.arg2 = currentUserPercent;
                                    Log.i("down_user_progress=>", " 更新进度条" + userProgress);
                                    Log.i("down_user_progress=>", " 下载进度: " + currentUserPercent);
                                    handler.sendMessage(msg);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.i("stringTaskList====>", "下载的任务个数为：" + stringTaskList.size());
                            for (int i = 0; i < stringTaskList.size(); i++) {
                                try {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        taskObject = jsonArray.getJSONObject(j);
                                        if (String.valueOf(taskObject.optInt("safetyplanId", 0)).equals(stringTaskList.get(i))) {
                                            insertTaskDataBase();
                                            Log.i("stringTaskList====>", "任务插入本地数据库！");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return userResult;
                    } else {
                        if (url.toString().contains(stringTaskList.get(stringTaskList.size() - 1))) {
                            //没有相应用户数据
                            //handler.sendEmptyMessage(4);
                        }
                    }
                } else {
                    if (url.toString().contains(stringTaskList.get(stringTaskList.size() - 1))) {
                        handler.sendEmptyMessage(6);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.i("IOException", "下载用户数据网络请求异常!");
                if (url.toString().contains(stringTaskList.get(stringTaskList.size() - 1))) {
                    handler.sendEmptyMessage(3);
                }
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {   //更新类似于进度条的控件的进度效果
            super.onProgressUpdate(values);
            if (isCancelled()) {
                return;
            }
            //downloadProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {  //执行UI操作
            if (s != null) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("rows");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        userObject = jsonArray.getJSONObject(i);
                        Log.i("onPostExecute========>", "更新UI！");
                        insertUserDataBase();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }
    }

    //开始异步任务
    public void startAsyncTask() {
        Log.i("startAsyncTask========>", "异步任务进来了！");
        if (!public_sharedPreferences.getString("security_ip", "").equals("")) {
            ip = public_sharedPreferences.getString("security_ip", "");
            Log.i("sharedPreferences=ip=>", ip);
        } else {
            ip = "192.168.2.201:";
        }
        if (!public_sharedPreferences.getString("security_port", "").equals("")) {
            port = public_sharedPreferences.getString("security_port", "");
            Log.i("sharedPreferences=ip=>", port);
        } else {
            port = "8080";
        }
        new Thread() {
            @Override
            public void run() {
                String url;
                String httpUrl = "http://" + ip + port + "/SMDemo/" + "getUserCheck.do?" + "safetyPlan=";
                Log.i("startAsyncTask========>", "任务编号个数为：" + stringTaskList.size());
                currentUserPercent = 0;
                for (int i = 0; i < stringTaskList.size(); i++) {
                    MyAsyncTask myAsyncTask = new MyAsyncTask();
                    url = httpUrl + stringTaskList.get(i) + "&safetyState=0";
                    Log.i("startAsyncTask========>", url);
                    myAsyncTask.execute(url);
                }
            }
        }.start();
    }

    public void setProgress() {
        try {
            for (int i = 0; i < stringTaskList.size(); i++) {
                Thread.sleep(200);
                currentProgress += 10 * stringTaskList.size() / stringTaskList.size();
                currentPercent = (1000 * (i + 1)) / (10 * stringTaskList.size());
                Message msg = new Message();
                msg.what = 7;
                msg.arg1 = currentProgress;
                msg.arg2 = currentPercent;
                handler.sendMessage(msg);
                Log.i("down_task_progress=>", " 更新进度条" + currentProgress);
                Log.i("down_task_progress=>", " 下载进度: " + currentPercent);
            }
            Thread.sleep(1000);
            handler.sendEmptyMessage(8);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 防止重复点击
     *
     * @return
     */
    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeDistance = time - lastClickTime;
        if (timeDistance > 0 && timeDistance < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    //读取下载到本地的任务数据
    public void getTaskData(String taskId, String loginName) {
        taskCursor = db.rawQuery("select * from Task where taskId=? and loginName=?", new String[]{taskId, loginName});//查询并获得游标
        //如果游标为空，则返回空
        if (cursor.getCount() == 0) {
            return;
        }
        while (cursor.moveToNext()) {

        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        selectTaskItemList.clear();
                        JSONObject jsonObject = new JSONObject(taskResult);
                        jsonArray = jsonObject.getJSONArray("rows");
                        for (int j = 0; j < jsonArray.length(); j++) {             //获取到任务的个数，用于后面下载相应的用户数据
                            JSONObject object = jsonArray.getJSONObject(j);
                            if(object.optInt("remainCounts",0) != 0){
                                SelectTaskItem item = new SelectTaskItem();
                                item.setTaskId(object.optInt("safetyplanId", 0) + "");
                                item.setTotalNumber(object.optInt("countRs", 0) + "");
                                item.setRestNumber("（"+object.optInt("remainCounts", 0)+"）");
                                item.setTaskName(object.optString("safetyPlanName", ""));
                                item.setStartDate(object.optString("safetyStart",""));
                                item.setEndDate(object.optString("safetyEnd",""));
                                selectTaskItemList.add(item);
                            }
                        }
                        Log.i("selectTaskItemList", " 任务的数量为：" + selectTaskItemList.size());
                        if(selectTaskItemList.size() != 0){
                            taskTips.setText("请选择您要下载的任务！（可多选）");
                            listview.setVisibility(View.VISIBLE);
                            noTask.setVisibility(View.GONE);
                            frameAnimation.setVisibility(View.GONE);
                            taskDownAdapter = new SelectTaskDownAdapter(getActivity(), selectTaskItemList);
                            taskDownAdapter.notifyDataSetChanged();
                            listview.setAdapter(taskDownAdapter);
                        }else {
                            taskTips.setText("所查询到任务首次已安检完哦！");
                            listview.setVisibility(View.GONE);
                            frameAnimation.setVisibility(View.GONE);
                            noTask.setVisibility(View.VISIBLE);
                        }
                        download.setClickable(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    popupWindow.dismiss();
                    createNoTaskPopup();
                    oneTips.setText("没有任务下载哦，赶快新增任务吧!");
                    //Toast.makeText(getActivity(), "没有任务下载哦！", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    popupWindow.dismiss();
                    createNoTaskPopup();
                    oneTips.setText("网络请求超时，请您检测IP或端口是否正确！");
                    //Toast.makeText(getActivity(), "网络请求超时！", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    progressName.setText("此任务首次安检已完成，您可以下载其他任务去安检哦！");
                    linearlayoutDown.setVisibility(View.GONE);
                    downloadProgress.setVisibility(View.GONE);
                    downFailed.setImageResource(R.mipmap.smile);
                    downFailed.setVisibility(View.VISIBLE);
                    finishBtn.setVisibility(View.VISIBLE);
                    //Toast.makeText(getActivity(), "没有相应的用户数据！", Toast.LENGTH_SHORT).show();
                    break;
                case 5:
                    //有相应的用户数据
                    break;
                case 6:
                    popupWindow.dismiss();
                    download.setClickable(true);
                    Toast.makeText(getActivity(), "用户信息请求网络超时！", Toast.LENGTH_SHORT).show();
                    break;
                case 7:
                    progressPercent.setText(String.valueOf(msg.arg2));
                    downloadProgress.setProgress(msg.arg1);
                    Log.i("down_progress=>", " 任务进度为：" + downloadProgress.getProgress());
                    break;
                case 8:
                    downloadProgress.setProgress(0);
                    downloadProgress.setMax(50);
                    progressName.setText("用户信息正在下载，请稍等...");
                    progressPercent.setText("0");
                    currentProgress = 0;
                    currentPercent = 0;
                    break;
                case 9:
                    progressPercent.setText(String.valueOf(msg.arg2));
                    downloadProgress.setProgress(msg.arg1);
                    if (downloadProgress.getProgress() == downloadProgress.getMax()) {
                        Log.i("down_progress=>", " 用户信息下载完成进来了！");
                        progressName.setText("数据下载完成！");
                        linearlayoutDown.setVisibility(View.GONE);
                        finishBtn.setVisibility(View.VISIBLE);
                        downFailed.setVisibility(View.GONE);
                        userProgress = 0;
                        currentUserPercent = 0;
                        download.setClickable(true);
                    }
                    break;
                case 10:
                    adapter = new GridviewTypeAdapter(getActivity(), gridviewTypeItemList);
                    adapter.notifyDataSetChanged();
                    gridView.setAdapter(adapter);
                    break;
                case 11:
                    gridviewTypeItemList.clear();
                    GridviewTypeItem item = new GridviewTypeItem();
                    item.setTypeName("无");
                    item.setTypeId("");
                    gridviewTypeItemList.add(item);
                    adapter = new GridviewTypeAdapter(getActivity(), gridviewTypeItemList);
                    gridView.setAdapter(adapter);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    //读取下载到本地的任务数据
    public void getTaskData() {
        taskTotalCursor = db.rawQuery("select * from Task where loginName=?", new String[]{sharedPreferences_login.getString("login_name", "")});//查询并获得游标
        //如果游标为空，则显示没有数据图片
        if (taskTotalCursor.getCount() == 0) {
            return;
        }
        while (taskTotalCursor.moveToNext()) {

        }
    }

    //任务数据存到本地数据库任务表
    private void insertTaskDataBase() {
        ContentValues values = new ContentValues();
        values.put("taskName", taskObject.optString("safetyPlanName", ""));
        values.put("taskId", taskObject.optInt("safetyplanId", 0) + "");
        values.put("securityType", taskObject.optString("securityName", ""));
        values.put("totalCount", taskObject.optInt("countRs", 0) + "");
        values.put("endTime", taskObject.optString("safetyEnd", ""));
        values.put("loginName", sharedPreferences_login.getString("login_name", ""));
        values.put("restCount", taskObject.optInt("remainCounts",0) + "");
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
        db.insert("Task", null, values);
    }

    //用户信息数据存到本地数据库用户表
    private void insertUserDataBase() {
        ContentValues values = new ContentValues();
        values.put("securityNumber", userObject.optInt("safetyInspectionId", 0) + "");
        values.put("userName", userObject.optString("userName", ""));
        values.put("meterNumber", userObject.optString("meterNumber", ""));
        values.put("userPhone", userObject.optString("userPhone", ""));
        values.put("securityType", userObject.optString("securityName", ""));
        values.put("oldUserId", userObject.optString("oldUserId", ""));
        Log.i("data_down_load", "下载的用户ID为：" + userObject.optString("oldUserId", ""));
        values.put("newUserId", userObject.optString("userId", ""));
        values.put("userAddress", userObject.optString("userAdress", ""));
        values.put("taskId", userObject.optInt("safetyPlan", 0) + "");
        values.put("ifChecked", "false");
        values.put("security_content", "");
        values.put("newMeterNumber", "");
        values.put("remarks", "");
        values.put("security_hidden", "");
        values.put("security_hidden_reason", "");
        values.put("photoNumber", "0");
        values.put("ifUpload", "false");
        values.put("currentTime", "");
        values.put("ifPass", "");
        values.put("loginName", sharedPreferences_login.getString("login_name", ""));
        values.put("security_state", "0");
        values.put("newUserPhone", "");
        values.put("newUserAddress", "");
        // 第一个参数:表名称
        // 第二个参数：SQl不允许一个空列，如果ContentValues是空的，那么这一列被明确的指明为NULL值
        // 第三个参数：ContentValues对象
        db.insert("User", null, values);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //释放和数据库的连接
        db.close();
        if (taskCursor != null) {
            taskCursor.close(); //游标关闭
        }
        if (taskTotalCursor != null) {
            taskTotalCursor.close();
        }
        if (cursor != null) {
            cursor.close();
        }
        if (popupWindow != null) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

}
