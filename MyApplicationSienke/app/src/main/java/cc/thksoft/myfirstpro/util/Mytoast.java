package cc.thksoft.myfirstpro.util;

import android.content.Context;
import android.widget.Toast;

public class Mytoast extends Toast {

	public Mytoast(Context context) {
		super(context);
	}

	private static Toast mToast = null;  
	
    public static void showToast(Context context, String text, int duration) {  
        if (mToast == null) {  
            mToast = Toast.makeText(context, text, duration);  
        } else {  
            mToast.setText(text);  
            mToast.setDuration(duration);  
        }  
        mToast.show();  
    }  
    @Override
    public void setGravity(int gravity, int xOffset, int yOffset) {
    	
    	super.setGravity(gravity, xOffset, yOffset);
    }
    
}
