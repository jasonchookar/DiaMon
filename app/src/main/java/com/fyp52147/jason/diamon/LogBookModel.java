package com.fyp52147.jason.diamon;

public class LogBookModel {
    public String mDateTime;
    public String mGlucoseLevel;
    public String mCondition;
    public String mMeal;
    public String mBP;

    public LogBookModel(){}

    public LogBookModel(String mDateTime, String mGlucoseLevel, String mBP, String mMeal, String mCondition) {
        this.mDateTime = mDateTime;
        this.mGlucoseLevel = mGlucoseLevel;
        this.mBP = mBP;
        this.mMeal = mMeal;
        this.mCondition = mCondition;
    }

    public String getmDateTime() {
        return mDateTime;
    }

    public void setmDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getmGlucoseLevel() {
        return mGlucoseLevel;
    }

    public void setmGlucoseLevel(String mGlucoseLevel) {
        this.mGlucoseLevel = mGlucoseLevel;
    }

    public String getmBP() {
        return mBP;
    }

    public void setmBP (String mBP){
        this.mBP = mBP;
    }

    public String getmMeal() {
        return mMeal;
    }

    public void setmMeal(String mMeal) {
        this.mMeal = mMeal;
    }

    public String getmCondition() {
        return mCondition;
    }

    public void setmCondition(String mCondition) {
        this.mCondition = mCondition;
    }
}
