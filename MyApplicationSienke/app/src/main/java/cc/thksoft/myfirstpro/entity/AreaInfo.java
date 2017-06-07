package cc.thksoft.myfirstpro.entity;

import java.io.Serializable;

public class  AreaInfo implements Serializable{
	private String ID;
	private String Area;
	private String AreaRemark;

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getArea() {
		return Area;
	}

	public void setArea(String Area) {
		this.Area = Area;
	}

	public String getAreaRemark() {
		return AreaRemark;
	}

	public void setAreaRemark(String AreaRemark) {
		this.AreaRemark = AreaRemark;
	}

}
