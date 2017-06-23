package com.example.administrator.thinker_soft.myfirstpro.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable{
	private String ID;
	private String NUMBER;
	private String BOOK;
	private String BOOKMEN;
	private String BOOKREMARK;

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNUMBER() {
		return NUMBER;
	}
	public void setNUMBER(String nUMBER) {
		NUMBER = nUMBER;
	}
	public String getBOOK() {
		return BOOK;
	}
	public void setBOOK(String BOOK) {
		this.BOOK = BOOK;
	}
	public String getBOOKMEN() {
		return BOOKMEN;
	}
	public void setBOOKMEN(String BOOKMEN) {
		this.BOOKMEN = BOOKMEN;
	}
	public String getBOOKREMARK() {
		return BOOKREMARK;
	}
	public void setBOOKREMARK(String BOOKREMARK) {
		this.BOOKREMARK = BOOKREMARK;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ID);
		dest.writeString(NUMBER);
		dest.writeString(BOOK);
		dest.writeString(BOOKMEN);
		dest.writeString(BOOKREMARK);
	}

	public static final Parcelable.Creator<BookInfo>CREATOR = new Parcelable.Creator<BookInfo>(){
		@Override
		public BookInfo createFromParcel(Parcel parcel) {
			BookInfo item = new BookInfo();
			item.ID = parcel.readString();
			item.NUMBER = parcel.readString();
			item.BOOK = parcel.readString();
			item.BOOKMEN = parcel.readString();
			item.BOOKREMARK = parcel.readString();
			return item;
		}

		@Override
		public BookInfo[] newArray(int i) {
			return new BookInfo[i];
		}
	};
}
