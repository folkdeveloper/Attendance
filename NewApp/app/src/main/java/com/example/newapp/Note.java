package com.example.newapp;

import android.util.Log;

public class Note {
    String area, fg, fid, japa, name, session, time, zfl, zmob, zread, ztl, zzdate, zzone, color,
            url, occupation, program, category, branch, organization, college, source, res_interest,
            origin;

    long edate;

    public static final String TAG = "Note";

    public Note() {

    }

    public Note(String area, String name, String fg, String ztl) {
        Log.d(TAG, "Note: Note");
        this.area = area;
        this.name = name;
        this.fg = fg;
        this.ztl = ztl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFg() {
        return fg;
    }

    public void setFg(String fg) {
        this.fg = fg;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getJapa() {
        return japa;
    }

    public void setJapa(String japa) {
        this.japa = japa;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getZfl() {
        return zfl;
    }

    public void setZfl(String zfl) {
        this.zfl = zfl;
    }

    public String getZmob() {
        return zmob;
    }

    public void setZmob(String zmob) {
        this.zmob = zmob;
    }

    public String getZread() {
        return zread;
    }

    public void setZread(String zread) {
        this.zread = zread;
    }

    public String getZtl() {
        return ztl;
    }

    public void setZtl(String ztl) {
        this.ztl = ztl;
    }

    public String getZzdate() {
        return zzdate;
    }

    public void setZzdate(String zzdate) {
        this.zzdate = zzdate;
    }

    public String getZzone() {
        return zzone;
    }

    public void setZzone(String zzone) {
        this.zzone = zzone;
    }

    public static String getTAG() {
        return TAG;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getEdate() {
        return edate;
    }

    public void setEdate(long edate) {
        this.edate = edate;
    }

    public String getRes_interest() {
        return res_interest;
    }

    public void setRes_interest(String res_interest) {
        this.res_interest = res_interest;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}
