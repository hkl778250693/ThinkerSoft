package cc.thksoft.myfirstpro.entity;

import java.io.Serializable;

public class DeviceTypeInfo implements Serializable{
	private String N_EQUIPMENT_TYPE_ID;
	private String C_EQUIPMENT_TYPE_NAME;
	private String N_EQUIPMENT_TYPE_STATUS;
	private String C_EQUIPMENT_TYPE_REMARK;
	private String D_OPERATION_TIEM;
	private String N_OPERATION_MAN;
	public String getN_EQUIPMENT_TYPE_ID() {
		return N_EQUIPMENT_TYPE_ID;
	}
	public void setN_EQUIPMENT_TYPE_ID(String n_EQUIPMENT_TYPE_ID) {
		N_EQUIPMENT_TYPE_ID = n_EQUIPMENT_TYPE_ID;
	}
	public String getC_EQUIPMENT_TYPE_NAME() {
		return C_EQUIPMENT_TYPE_NAME;
	}
	public void setC_EQUIPMENT_TYPE_NAME(String c_EQUIPMENT_TYPE_NAME) {
		C_EQUIPMENT_TYPE_NAME = c_EQUIPMENT_TYPE_NAME;
	}
	public String getN_EQUIPMENT_TYPE_STATUS() {
		return N_EQUIPMENT_TYPE_STATUS;
	}
	public void setN_EQUIPMENT_TYPE_STATUS(String n_EQUIPMENT_TYPE_STATUS) {
		N_EQUIPMENT_TYPE_STATUS = n_EQUIPMENT_TYPE_STATUS;
	}
	public String getC_EQUIPMENT_TYPE_REMARK() {
		return C_EQUIPMENT_TYPE_REMARK;
	}
	public void setC_EQUIPMENT_TYPE_REMARK(String c_EQUIPMENT_TYPE_REMARK) {
		C_EQUIPMENT_TYPE_REMARK = c_EQUIPMENT_TYPE_REMARK;
	}
	public String getD_OPERATION_TIEM() {
		return D_OPERATION_TIEM;
	}
	public void setD_OPERATION_TIEM(String d_OPERATION_TIEM) {
		D_OPERATION_TIEM = d_OPERATION_TIEM;
	}
	public String getN_OPERATION_MAN() {
		return N_OPERATION_MAN;
	}
	public void setN_OPERATION_MAN(String n_OPERATION_MAN) {
		N_OPERATION_MAN = n_OPERATION_MAN;
	}
	
}
