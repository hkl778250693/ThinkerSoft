package com.example.administrator.thinker_soft.model;

/**
 * Created by Administrator on 2017/5/24.
 */
public class HomePageItem {
    private int image;
    private String text;
    private boolean isChecked;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
