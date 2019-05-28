package com.fyp52147.jason.diamon;

public class MealModel {
    public String mDate;
    public String mBreakfast;
    public String mLunch;
    public String mDinner;

    public MealModel() {}

    public MealModel(String mDate, String mBreakfast, String mLunch, String mDinner) {
        this.mDate = mDate;
        this.mBreakfast = mBreakfast;
        this.mLunch = mLunch;
        this.mDinner = mDinner;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmBreakfast() {
        return mBreakfast;
    }

    public void setmBreakfast(String mBreakfast) {
        this.mBreakfast = mBreakfast;
    }

    public String getmLunch() {
        return mLunch;
    }

    public void setmLunch(String mLunch) {
        this.mLunch = mLunch;
    }

    public String getmDinner() {
        return mDinner;
    }

    public void setmDinner(String mDinner) {
        this.mDinner = mDinner;
    }
}
