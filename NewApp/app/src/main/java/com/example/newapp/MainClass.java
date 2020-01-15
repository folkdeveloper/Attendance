package com.example.newapp;

public class MainClass {
    String category;
    int number;

    public MainClass() {

    }

    public MainClass(String category, int number) {
        this.category = category;
        this.number = number;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
