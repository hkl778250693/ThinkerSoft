package com.example.administrator.thinker_soft.android_cbjactivity.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.thinker_soft.R;

/**
 * Created by Administrator on 2017/6/12 0012.
 */
public class ScanCodeMeterFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_chaobiao, null);

        return view;
    }
}
