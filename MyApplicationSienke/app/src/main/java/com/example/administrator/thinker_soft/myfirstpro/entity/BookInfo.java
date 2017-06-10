package com.example.administrator.thinker_soft.myfirstpro.entity;

import java.io.Serializable;

public class BookInfo implements Serializable{
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
	
}
