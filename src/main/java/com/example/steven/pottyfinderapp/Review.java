package com.example.steven.pottyfinderapp;

/**
 * Created by Miguel on 3/16/2016.
 */
public class Review {
    private String name;
    private String desc;
    private String rate;

    public Review(String name, String desc, String rate){
        this.name = name;
        this.desc = desc;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
