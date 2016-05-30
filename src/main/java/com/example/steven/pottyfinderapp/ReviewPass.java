package com.example.steven.pottyfinderapp;

import java.math.BigInteger;

/**
 * Created by Steven on 4/15/2016.
 */
public class ReviewPass {

    private String rcomment;
    private int rtable;
    private String rid;
    private String rate;

    public ReviewPass(String rcomment, int rtable, String rid, String rate) {
        this.rcomment = rcomment;
        this.rtable = rtable;
        this.rid = rid;
        this.rate = rate;
    }

    public String getRcomment() {
        return rcomment;
    }

    public void setRcomment(String rcomment) {
        this.rcomment = rcomment;
    }

    public int getRtable() {
        return rtable;
    }

    public void setRtable(int rtable) {
        this.rtable = rtable;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
