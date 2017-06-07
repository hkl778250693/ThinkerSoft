package cc.thksoft.myfirstpro.myactivitymanager;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

public class MyActivityManager {
	private static MyActivityManager instance;
	private Stack<Activity> activityStack;// activityջ

	private MyActivityManager() {
	}

	public static MyActivityManager getInstance() {
		if (instance == null) {
			instance = new MyActivityManager();
		}
		return instance;
	}

	// ��һ��activityѹ��ջ��
	public void pushOneActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
		Log.d("MyActivityManager ", "size = " + activityStack.size());
	}

	// ��ȡջ����activity���Ƚ����ԭ��
	public Activity getLastActivity() {
		return activityStack.lastElement();
	}

	// �Ƴ�һ��activity
	public void popOneActivity(Activity activity) {
		if (activityStack != null && activityStack.size() > 0) {
			if (activity != null) {
				activity.finish();
				activityStack.remove(activity);
				activity = null;
			}
		}
	}

	// �˳�����activity
	public void finishAllActivity() {
		if (activityStack != null) {
			try {
				while (activityStack.size() > 0) {
					Activity activity = getLastActivity();
					if (activity == null)
						break;
					popOneActivity(activity);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				System.exit(0);
			}
		}
	}
}
