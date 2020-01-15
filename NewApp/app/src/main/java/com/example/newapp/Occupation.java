package com.example.newapp;

public class Occupation {
    int students;
    int working;
    int self;
    int others;

    public Occupation(int students, int working, int self, int others) {
        this.students = students;
        this.working = working;
        this.self = self;
        this.others = others;
    }

    public int getStudents() {
        return students;
    }

    public void setStudents(int students) {
        this.students = students;
    }

    public int getWorking() {
        return working;
    }

    public void setWorking(int working) {
        this.working = working;
    }

    public int getSelf() {
        return self;
    }

    public void setSelf(int self) {
        this.self = self;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }
}
