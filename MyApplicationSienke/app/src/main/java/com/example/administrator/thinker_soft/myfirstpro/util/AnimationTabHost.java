package com.example.administrator.thinker_soft.myfirstpro.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

import com.example.administrator.thinker_soft.R;


public class AnimationTabHost extends TabHost {
    private Animation slideLeftIn;
    private Animation slideLeftOut;
    private Animation slideRightIn;
    private Animation slideRightOut;

    /**
     * ��¼�Ƿ�򿪶���Ч��
     */

    private boolean isOpenAnimation;

    /**
     * ��¼��ǰ��ǩҳ������
     */

    private int mTabCount;

    public AnimationTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        slideLeftIn = AnimationUtils.loadAnimation(context,
                R.anim.slide_left_in);
        slideLeftOut = AnimationUtils.loadAnimation(context,
                R.anim.slide_left_out);
        slideRightIn = AnimationUtils.loadAnimation(context,
                R.anim.slide_right_in);
        slideRightOut = AnimationUtils.loadAnimation(context,
                R.anim.slide_right_out);
        isOpenAnimation = false;
    }


    /**
     * �����Ƿ�򿪶���Ч��
     *
     * @param isOpenAnimation true����
     */

    public void setOpenAnimation(boolean isOpenAnimation) {
        this.isOpenAnimation = isOpenAnimation;

    }
/*	    ˵���������Animation�����Զ���Ķ���Ч����������res/anim���ҵ���Ӧ��XML�ļ���������slide_left_in.xml��˵������Ĵ���÷�

	    slide_left_in.xml

	    <?xml version="1.0" encoding="utf-8"?>

	    <set xmlns:android="http://schemas.android.com/apk/res/android">

	    <translate android:fromXDelta="100%p"

	     android:toXDelta="0"                   

	                     android:duration="800"/>

	    <alpha android:fromAlpha="0.0"

	    android:toAlpha="1.0"

	    android:duration="300" />

	    </set>

	    ˵����

	    ����Ϊ����������ɼ�������������ɵģ�������Χ����һ��set��ǩ�����������һ��AnimationSet��

	    �� Translate��ǩ����Ҫ����λ�õı仯�����fromXDelta="100%p"��Ϊ������ʼʱ��X�����ϵ������ߴ硣��ָ���·��պ�һ��View�ĸ߶ȵľ���ĵط���ʼ���֣�100%p��һ�����ֵ������0Ϊ�·���С��0Ϊ�Ϸ���toXDelta="0"  ��Ϊ��������ʱ��X�����ϵ������ߴ硣��0.0��ʾ������û�У����պôﵽ�����ļ���ԭʼλ��ֹͣ����1.0��ʾ������������ֵС��1.0��ʾ������ֵ����1.0��ʾ�Ŵ� ��

	    �۲���fromXDelta��toXDelta����ָ�ؼ������parent��ƫ�ƾ��룬100%p����������parent���档������from��to�������ģ������ڸ�������ң���

	    ��duration="800"����ָ����������ʱ����ʱΪ800���룬ϵͳ��������ʱ���Զ������ٶȡ�

	    ��alpha��ǩ�ڶ������͸���ȣ�0Ϊȫ͸����1.0Ϊ��͸��������Ϊ300���룬��ViewΪ�𽥳��ֵĹ��̡�*/


    /**
     * ���ñ�ǩ����������<br>
     * <p>
     * ����˳��Ϊ���������>�������>�ҽ�����>�ҳ���
     *
     * @param animationResIDs ��������Դ�ļ�ID
     * @return true���ĸ������ļ�;<br>
     * <p>
     * false�����ĸ������ļ����޷�ƥ�䣬����Ĭ�϶�����
     */

    public boolean setTabAnimation(int[] animationResIDs) {
        if (3 == animationResIDs.length) {
            slideLeftIn = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[0]);
            slideLeftOut = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[1]);
            slideRightIn = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[2]);
            slideRightOut = AnimationUtils.loadAnimation(getContext(),
                    animationResIDs[3]);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return ���ص�ǰ��ǩҳ������
     */

    public int getTabCount() {
        return mTabCount;
    }

    @Override
    public void addTab(TabSpec tabSpec) {
        mTabCount++;
        super.addTab(tabSpec);
    }

    @Override
    public void setCurrentTab(int index) {
        int mCurrentTabID = getCurrentTab();
        if (null != getCurrentView()) {
            if (isOpenAnimation) {
                if (mCurrentTabID == (mTabCount - 1) && index == 0) {
                    getCurrentView().startAnimation(slideLeftOut);
                } else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
                    getCurrentView().startAnimation(slideRightOut);
                } else if (index > mCurrentTabID) {
                    getCurrentView().startAnimation(slideLeftOut);
                } else if (index < mCurrentTabID) {
                    getCurrentView().startAnimation(slideRightOut);
                }
            }
        }
        super.setCurrentTab(index);
        if (isOpenAnimation) {
            if (mCurrentTabID == (mTabCount - 1) && index == 0) {
                getCurrentView().startAnimation(slideLeftIn);
            } else if (mCurrentTabID == 0 && index == (mTabCount - 1)) {
                getCurrentView().startAnimation(slideRightIn);
            } else if (index > mCurrentTabID) {
                getCurrentView().startAnimation(slideLeftIn);
            } else if (index < mCurrentTabID) {
                getCurrentView().startAnimation(slideRightIn);
            }

        }

    }
}
