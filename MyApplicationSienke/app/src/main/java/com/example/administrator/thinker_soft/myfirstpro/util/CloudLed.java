package com.example.administrator.thinker_soft.myfirstpro.util;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;

public class CloudLed {
	boolean m_isOn;  
    Camera m_Camera;  
      
    public boolean getIsOn() { return m_isOn; }  
      
    public CloudLed()  
    {  
        m_isOn = false;  
    }  
      
    public void turnOn()  
    {  
        if(!m_isOn)  
        {  
            m_isOn = true;  
            try  
            {  
                m_Camera = Camera.open();  
                Parameters mParameters;
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
                m_Camera.setParameters(mParameters);
                m_Camera.startPreview(); // ��ʼ����
            }catch(Exception ex){}
        }
    }

    public void turnOff()
    {
        if(m_isOn)
        {
            m_isOn = false;
            try
            {
                Parameters mParameters;
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Parameters.FLASH_MODE_OFF);
                m_Camera.setParameters(mParameters);  
                m_Camera.stopPreview();
                m_Camera.release();  
            }catch(Exception ex){}  
        }  
    }  
}
