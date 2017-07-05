package com.example.administrator.thinker_soft.meter_code.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/28 0028.
 */
public class MeterUserListviewItem implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(meterID);
        parcel.writeString(userID);
        parcel.writeString(userName);
        parcel.writeString(meterNumber);
        parcel.writeString(lastMonth);
        parcel.writeString(thisMonth);
        parcel.writeString(address);
        parcel.writeString(meterState);
        parcel.writeInt(ifEdit);
    }
    public static final Parcelable.Creator<MeterUserListviewItem>CREATOR = new Parcelable.Creator<MeterUserListviewItem>(){
        @Override
        public MeterUserListviewItem createFromParcel(Parcel parcel) {
            MeterUserListviewItem item = new MeterUserListviewItem();
            item.meterID = parcel.readString();
            item.userID = parcel.readString();
            item.userName = parcel.readString();
            item.meterNumber = parcel.readString();
            item.lastMonth = parcel.readString();
            item.thisMonth = parcel.readString();
            item.address = parcel.readString();
            item.meterState = parcel.readString();
            item.ifEdit = parcel.readInt();
            return item;
        }

        @Override
        public MeterUserListviewItem[] newArray(int i) {
            return new MeterUserListviewItem[i];
        }
    };
}
