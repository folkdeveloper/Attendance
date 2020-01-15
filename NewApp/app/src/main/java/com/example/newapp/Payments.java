package com.example.newapp;

public class Payments {
    String buyer_name;
    String FOLKID;
    String Purpose;
    String Sub_Purpose;
    int amount;
    String Day;
    String Month;
    String Year;

    public Payments() {

    }

    public Payments(String buyer_name, String FOLKID, String Purpose, String Sub_Purpose, int amount
            , String Day, String Month, String Year) {
        this.buyer_name = buyer_name;
        this.FOLKID = FOLKID;
        this.Purpose = Purpose;
        this.Sub_Purpose = Sub_Purpose;
        this.amount = amount;
        this.Day = Day;
        this.Month = Month;
        this.Year = Year;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getFOLKID() {
        return FOLKID;
    }

    public void setFOLKID(String FOLKID) {
        this.FOLKID = FOLKID;
    }

    public String getPurpose() {
        return Purpose;
    }

    public void setPurpose(String purpose) {
        Purpose = purpose;
    }

    public String getSub_Purpose() {
        return Sub_Purpose;
    }

    public void setSub_Purpose(String sub_Purpose) {
        Sub_Purpose = sub_Purpose;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }
}
