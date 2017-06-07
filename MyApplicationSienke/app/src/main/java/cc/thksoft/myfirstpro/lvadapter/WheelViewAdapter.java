package cc.thksoft.myfirstpro.lvadapter;

import java.util.List;

import cc.thksoft.myfirstpro.entity.DeviceTypeInfo;
import cc.thksoft.myfirstpro.wheel.WheelAdapter;

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
