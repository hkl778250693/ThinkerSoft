package com.example.administrator.thinker_soft.android_cbjactivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.thinker_soft.R;
import com.example.administrator.thinker_soft.myfirstpro.appcation.MyApplication;
import com.example.administrator.thinker_soft.myfirstpro.fragment.newMissionFragment;
import com.example.administrator.thinker_soft.myfirstpro.fragment.oldMissionFragment;
import com.example.administrator.thinker_soft.myfirstpro.myactivitymanager.MyActivityManager;
import com.example.administrator.thinker_soft.viewbadger.BadgeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ����������
 *
 * @author Administrator
 */
public class ActualMissionNEW extends FragmentActivity implements OnClickListener {
    private MyApplication myapp;
    public List<String[]> allWork_lists = new ArrayList<String[]>();
    private Intent intent;
    private String TAG = "ActualMissionNEW  ����������";
    private TextView mission_current;
    private TextView mission_history;
    private TextView prompt_current;
    private TextView prompt_history;
    public BadgeView badgeView;
    private ViewPager mission_viewpager;
    private PagerTitleStrip pagerTitleStrip;
    private List<Fragment> fragmentlist;
    private com.example.administrator.thinker_soft.myfirstpro.fragment.oldMissionFragment oldMissionFragment;
    private com.example.administrator.thinker_soft.myfirstpro.fragment.newMissionFragment newMissionFragment;
    private LinearLayout work_back_Btn;
    private List<Map<String, String>> ALLworkList_before = null;
    private Double mLat1;
    private Double mLat2;
    private Double mLon1;
    private Double mLon2;
    private SharedPreferences sharedPreferences;
    private BroadcastReceiver receiver;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int key = msg.getData().getInt("key");
            switch (key) {
                case 1:
                    if (badgeView != null && !badgeView.isShown())
                        badgeView.show();
                    break;
                case 2:

                    break;

                default:
                    break;
            }
        }

        ;
    };

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missions_new);
        Log.v("onCreate", "onCreate");
        MyActivityManager mam = MyActivityManager.getInstance();
        mam.pushOneActivity(this);
        myapp = (MyApplication) getApplication();
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("�Ѿ����յ�������Ϣ:" + intent.getAction());
                System.out.println("�Ѿ����յ�������Ϣ:" + intent.getStringExtra("json"));
                System.out.println("�Ѿ����յ�������Ϣ");
                Message msg = new Message();
                Bundle data = new Bundle();
                data.putInt("key", 1);
                msg.setData(data);
                handler.sendMessage(msg);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("ActualMissionNEWBroadcastReceiver");
        registerReceiver(receiver, filter);
        // intent = getIntent();
        ALLworkList_before = myapp.getNewWorkList();
        Log.e(TAG, "ALLworkList_before=" + ALLworkList_before);
        if (savedInstanceState == null) {
            ALLworkList_before_fenlei();
        } else {
            allWork_lists = (List<String[]>) savedInstanceState.getSerializable("allWork_lists");
        }
        guiwadget();
        init();
    }

    /**
     * ���������ļ��Ϸֳɵ�ǰ���񼯺Ϻ�
     */
    private void ALLworkList_before_fenlei() {
        for (int i = 0; i < ALLworkList_before.size(); i++) {
            String[] result = new String[19];
            Map<String, String> localMap = ALLworkList_before.get(i);
            result[0] = localMap.get("N_TASK_ID");// �������
            result[1] = localMap.get("C_TASK_NUMBER");// ������
            result[2] = localMap.get("C_TASK_NAME");// ��������
            result[3] = localMap.get("C_TASK_CONTENT");// ��������
            result[4] = localMap.get("C_TASK_ADDRESS");// �����ַ
            result[5] = localMap.get("C_TASK_TYPE_NAME");//
            result[6] = localMap.get("C_TASK_X");// ���񾭶�
            result[7] = localMap.get("C_TASK_Y");// ����γ��

            // 2��ľ�γ�ȣ�������2��֮��ľ��루���ص������ľ��룩

            sharedPreferences = getSharedPreferences("IP_PORT_DBNAME", 0);
            String sb = sharedPreferences.getString("sb", "");
            if (sb == null || "".equals(sb)) {
                Toast.makeText(getApplicationContext(), "û�п��þ�γ��", Toast.LENGTH_SHORT).show();
                return;
            }
            int loc = sb.indexOf("$");
            String Latitude = sb.substring(0, loc - 1);
            String Longitude = sb.substring(loc + 1, sb.length());
            Log.i(TAG, "Latitude=" + Latitude + "   Longitude=" + Longitude);
            if ((result[6] == null || "".equals(result[6]))
                    && (result[7] == null || "".equals(result[7]))) {
                result[18] = "";
            } else {
                mLat1 = Double.valueOf(Double.parseDouble(Latitude));
                mLon1 = Double.valueOf(Double.parseDouble(Longitude));
                mLat2 = Double.valueOf(Double.parseDouble(localMap
                        .get("C_TASK_Y")));
                mLon2 = Double.valueOf(Double.parseDouble(localMap
                        .get("C_TASK_X")));
                /*double d = DistanceUtil.getDistance(
                        new LatLng(mLat1.doubleValue(), mLon1.doubleValue()),
                        new LatLng(mLat2.doubleValue(), mLon2.doubleValue()));
                int j = (int) (d / 1000.0D);
                int k = (int) d % 1000;
                result[18] = (String.valueOf(j) + "." + String.valueOf(k) + "km");*/
            }

            result[8] = localMap.get("D_TASK_TIME");// �����·�ʱ��
            result[9] = localMap.get("D_TASK_MAN");// �����·���
            result[10] = localMap.get("TASK_STATUS");// ����״̬ 0��δ��� 1������� 2���ѹ鵵
            // 3��ע��
            result[11] = localMap.get("C_TASK_EQUIPMENT_NAME");// �����豸����
            // �û���д�û�����������д������
            result[12] = localMap.get("D_PIGEONHOLE_TIME");// �鵵ʱ��
            result[13] = localMap.get("N_PERSONNEL_ID");// ��Ա���
            result[14] = localMap.get("C_USER_NAME");// ��Ա����
            result[15] = localMap.get("C_FEEDBACK");// ��Ա������Ϣ
            result[16] = localMap.get("RECEIVE_TASKS");// ��Ա״̬
            result[17] = localMap.get("C_REMARK");// ��ע

            allWork_lists.add(result);
        }
        // Log.v(TAG,
        // "allWork_lists=" + allWork_lists + "������="
        // + allWork_lists.get(0)[1]);
    }

    public List<String[]> getAllWork_lists() {
        return allWork_lists;
    }

    public void setAllWork_lists(List<String[]> allWork_lists) {
        this.allWork_lists = allWork_lists;
    }

    private void guiwadget() {
        work_back_Btn = (LinearLayout) findViewById(R.id.work_back_Btn);
        work_back_Btn.setOnClickListener(this);
        mission_viewpager = (ViewPager) findViewById(R.id.mission_viewpager);
        //pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        LayoutInflater inflater = getLayoutInflater();
        String temp = getResources().getString(R.string.pulldown_to_loading);
        //temp = String.format(temp, "�޸ĳɹ�û�У���");
        mission_current = (TextView) findViewById(R.id.mission_current);
        mission_history = (TextView) findViewById(R.id.mission_history);
        prompt_current = (TextView) findViewById(R.id.prompt_current);
        prompt_history = (TextView) findViewById(R.id.prompt_history);
        badgeView = new BadgeView(ActualMissionNEW.this, prompt_current);
        badgeView.setText("����");
        badgeView.setTextSize(9);
        if ("show".equals(getIntent().getAction())) {
            badgeView.show();
        }
        prompt_current.setOnClickListener(this);
        prompt_history.setOnClickListener(this);
        // pagerTitleStrip = (PagerTitleStrip)
        // findViewById(R.id.pagerTitleStrip);
    }

    @SuppressLint("NewApi")
    private void init() {
/*		pagerTabStrip.setTabIndicatorColor(getResources().getColor(
                R.color.btn_color_xz));
		pagerTabStrip.setBackgroundColor(getResources().getColor(
				R.color.dialog_bg));*/
        // pagerTitleStrip.setBackgroundColor(getResources().getColor(R.color.btn_menu_blue));
        fragmentlist = new ArrayList<Fragment>();
        oldMissionFragment = new oldMissionFragment();
        newMissionFragment = new newMissionFragment();
        fragmentlist.add(newMissionFragment);
        fragmentlist.add(oldMissionFragment);
        mission_viewpager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentlist));
        mission_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int location) {
                // newMissionFragment.newBackCancel();
                // oldMissionFragment.oldBackCancel();
                Drawable hdrawable = getResources().getDrawable(R.mipmap.mission_bottombg_hx);
                hdrawable.setBounds(0, 0, hdrawable.getMinimumWidth(), hdrawable.getMinimumHeight());
                Drawable ndrawable = getResources().getDrawable(R.mipmap.mission_bottombg_nx);
                ndrawable.setBounds(0, 0, ndrawable.getMinimumWidth(), ndrawable.getMinimumHeight());
                if (location == 0) {
                    mission_current.setTextColor(getResources().getColor(R.color.mission_tv_blue));
                    mission_history.setTextColor(getResources().getColor(R.color.mission_tv_grey));
                    prompt_current.setCompoundDrawables(null, null, null, hdrawable);
                    prompt_history.setCompoundDrawables(null, null, null, ndrawable);
                } else {
                    mission_current.setTextColor(getResources().getColor(R.color.mission_tv_grey));
                    mission_history.setTextColor(getResources().getColor(R.color.mission_tv_blue));
                    prompt_current.setCompoundDrawables(null, null, null, ndrawable);
                    prompt_history.setCompoundDrawables(null, null, null, hdrawable);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    public List<Map<String, String>> getALLworkList_before() {
        return ALLworkList_before;
    }

    public void setALLworkList_before(
            List<Map<String, String>> ALLworkList_before) {
        this.ALLworkList_before = ALLworkList_before;
    }

    @Override
    protected void onResume() {
        // ALLworkList_before = myapp.getNewWorkList();
        // Log.e(TAG, "ALLworkList_before=" + ALLworkList_before);
        // ALLworkList_before_fenlei();
        Log.v(TAG, TAG + "onResume");
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("onStart", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("onStop", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("onDestroy", "onDestroy");
        unregisterReceiver(receiver);
        if (fragmentlist != null) {
            fragmentlist.clear();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v("onSaveInstanceState", "onSaveInstanceState");
        outState.putSerializable("allWork_lists", (Serializable) allWork_lists);
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.v("onRestoreInstanceState", "onRestoreInstanceState");

    }

    /**
     * ���ؼ�����
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            newMissionFragment.NWcancelFresh();// ȡ�����������
            oldMissionFragment.OWcancelFresh();// ȡ������������

            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.work_back_Btn:
                newMissionFragment.NWcancelFresh();// ȡ�����������
                oldMissionFragment.OWcancelFresh();// ȡ������������
                finish();
                break;
            case R.id.mission_viewpager:

                break;
            case R.id.prompt_current:
                Drawable hdrawable = getResources().getDrawable(R.mipmap.mission_bottombg_hx);
                hdrawable.setBounds(0, 0, hdrawable.getMinimumWidth(), hdrawable.getMinimumHeight());
                Drawable ndrawable = getResources().getDrawable(R.mipmap.mission_bottombg_nx);
                ndrawable.setBounds(0, 0, ndrawable.getMinimumWidth(), ndrawable.getMinimumHeight());
                mission_current.setTextColor(getResources().getColor(R.color.mission_tv_blue));
                mission_history.setTextColor(getResources().getColor(R.color.mission_tv_grey));
                prompt_current.setCompoundDrawables(null, null, null, hdrawable);
                prompt_history.setCompoundDrawables(null, null, null, ndrawable);
                if (mission_viewpager != null) {
                    mission_viewpager.setCurrentItem(0);
                }
                break;
            case R.id.prompt_history:
                Drawable hdrawable1 = getResources().getDrawable(R.mipmap.mission_bottombg_hx);
                hdrawable1.setBounds(0, 0, hdrawable1.getMinimumWidth(), hdrawable1.getMinimumHeight());
                Drawable ndrawable1 = getResources().getDrawable(R.mipmap.mission_bottombg_nx);
                ndrawable1.setBounds(0, 0, ndrawable1.getMinimumWidth(), ndrawable1.getMinimumHeight());
                mission_current.setTextColor(getResources().getColor(R.color.mission_tv_grey));
                mission_history.setTextColor(getResources().getColor(R.color.mission_tv_blue));
                prompt_current.setCompoundDrawables(null, null, null, ndrawable1);
                prompt_history.setCompoundDrawables(null, null, null, hdrawable1);
                if (mission_viewpager != null) {
                    mission_viewpager.setCurrentItem(1);
                }
                break;
            default:
                break;
        }

    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        private String[] titlearray;
        private FragmentManager fm;

        public MyFragmentPagerAdapter(FragmentManager fm,
                                      List<Fragment> fragments) {
            super(fm);
            titlearray = new String[]{"��ǰ����", "��ʷ����"};
            this.fragments = fragments;
            this.fm = fm;
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }

/*		@Override
        public CharSequence getPageTitle(int position) {
			return this.titlearray[position];
		}*/

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container,
                    position);
            return fragment;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (resultCode == 1) {
                boolean isFresh = data.getBooleanExtra("isFresh", false);
                Log.e(TAG, "isFresh=" + isFresh);
                if (isFresh == true) {
                    newMissionFragment.Fresh();
                }
            }
        }
    }

    public BadgeView getBadgeView() {
        return badgeView;
    }

    public void setBadgeView(BadgeView badgeView) {
        this.badgeView = badgeView;
    }
}
