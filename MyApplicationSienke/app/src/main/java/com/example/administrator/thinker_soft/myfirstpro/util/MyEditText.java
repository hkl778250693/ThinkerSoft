package com.example.administrator.thinker_soft.myfirstpro.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class MyEditText extends EditText {

	public MyEditText(Context context) {
		super(context);
	}
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
	public void insertDrawable(int id) {
	        final SpannableString ss = new SpannableString("easy");
	        //�õ�drawable���󣬼���Ҫ�����ͼƬ
	        Drawable d = getResources().getDrawable(id);
	        d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
	        //�����drawable��������ַ���easy
	        ImageSpan span = new ImageSpan(d, ImageSpan.ALIGN_BASELINE);
	        //����0���ǲ�����"easy".length()����4��[0,4)��ֵ��ע����ǵ����Ǹ������ͼƬ��ʱ��ʵ���Ǹ�����"easy"����ַ�����
	        ss.setSpan(span, 0, "easy".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
	        append(ss);
	    }
	@Override
	public void setCompoundDrawables(Drawable left, Drawable top,
			Drawable right, Drawable bottom) {
		super.setCompoundDrawables(left, top, right, bottom);
	}
}
