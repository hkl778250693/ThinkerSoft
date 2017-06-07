package cc.thksoft.myfirstpro.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener {

	  private Context context;  
	    private SensorManager sensorManager;  
	    private Sensor sensor;  
	      
	    private float lastX ;   
	      
	    private OnOrientationListener onOrientationListener ;   
	  
	    public MyOrientationListener(Context context)  
	    {  
	        this.context = context;  
	    }  
	  

	    public void start()  
	    {  
	        // ��ô�����������  
	        sensorManager = (SensorManager) context  
	                .getSystemService(Context.SENSOR_SERVICE);  
	        if (sensorManager != null)  
	        {  

	            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);  
	        }  

	        if (sensor != null)  
	        {//SensorManager.SENSOR_DELAY_UI  
	            sensorManager.registerListener(this, sensor,  
	                    SensorManager.SENSOR_DELAY_UI);  
	        }  
	  
	    }  
	  

	    public void stop()  
	    {  
	        sensorManager.unregisterListener(this);  
	    }  
	  
	    @Override  
	    public void onAccuracyChanged(Sensor sensor, int accuracy)  
	    {  
	          
	    }  
	  
	    @Override  
	    public void onSensorChanged(SensorEvent event)  
	    {  
	        // ���ܷ����Ӧ��������    
	        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)    
	        {    
	            // �������ǿ��Եõ����ݣ�Ȼ�������Ҫ������    
	            float x = event.values[SensorManager.DATA_X];    
	              
	            if( Math.abs(x- lastX) > 1.0 )  
	            {  
	                onOrientationListener.onOrientationChanged(x);  
	            }  
//	            Log.e("DATA_X", x+"");  
	            lastX = x ;   
	              
	        }    
	    }  
	      
	    public void setOnOrientationListener(OnOrientationListener onOrientationListener)  
	    {  
	        this.onOrientationListener = onOrientationListener ;  
	    }  
	      
	      
	    public interface OnOrientationListener   
	    {  
	        void onOrientationChanged(float x);  
	    }

}
