package com.example.administrator.thinker_soft.myfirstpro.lvadapter;

import com.example.administrator.thinker_soft.myfirstpro.entity.DeviceTypeInfo;
import com.example.administrator.thinker_soft.myfirstpro.wheel.WheelAdapter;

import java.util.List;

public class WheelViewAdapter implements WheelAdapter {
	private List<DeviceTypeInfo> devInfo;
	public WheelViewAdapter(List<DeviceTypeInfo> devInfo){
		this.devInfo = devInfo;
	}
	@Override
	public int getItemsCount() {
		return devInfo.size();
	}
	@Override
	public String getItem(int index) {
		return devInfo.get(index).getC_EQUIPMENT_TYPE_NAME();
	}
	@Override
	public int getMaximumLength() {
		return -1;
	}
}
