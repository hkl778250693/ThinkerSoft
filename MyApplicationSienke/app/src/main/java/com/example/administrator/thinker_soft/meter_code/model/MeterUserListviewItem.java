package com.example.administrator.thinker_soft.meter_code.model;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class MeterUserListviewItem {
    private String meterID;
    private String userID;
    private String userName;
    private String meterNumber;
    private String lastMonth;
    private String thisMonth;
    private String address;
    private String meterState;  //抄表状态（文字）
    private int ifEdit;    //图片

    public String getMeterID() {
        return meterID;
    }

    public void setMeterID(String meterID) {
        this.meterID = meterID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(String lastMonth) {
        this.lastMonth = lastMonth;
    }

    public String getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(String thisMonth) {
        this.thisMonth = thisMonth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeterState() {
        return meterState;
    }

    public void setMeterState(String meterState) {
        this.meterState = meterState;
    }

    public int getIfEdit() {
        return ifEdit;
    }

    public void setIfEdit(int ifEdit) {
        this.ifEdit = ifEdit;
    }
}
