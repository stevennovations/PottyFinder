package com.example.steven.pottyfinderapp;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by Steven on 4/14/2016.
 */
public class Restroom {

    private int pid;
    private String rname;
    private String raccess;
    private String rtype;
    private String ruserid;
    private double rlat;
    private double rlong;
    private ArrayList<String> benefits;

    public Restroom(String rname, String raccess, String rtype, String ruserid, double rlat, double rlong) {
        this.rname = rname;
        this.raccess = raccess;
        this.rtype = rtype;
        this.ruserid = ruserid;
        this.rlat = rlat;
        this.rlong = rlong;
        this.benefits = new ArrayList();
    }

    public Restroom(int pid, String rname, double rlat, double rlong) {
        this.pid = pid;
        this.rlat = rlat;
        this.rlong = rlong;
        this.rname = rname;
    }

    public Restroom(int pid, String rname) {
        this.pid = pid;
        this.rname = rname;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRaccess() {
        return raccess;
    }

    public void setRaccess(String raccess) {
        this.raccess = raccess;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getRuserid() {
        return ruserid;
    }

    public void setRuserid(String ruserid) {
        this.ruserid = ruserid;
    }

    public double getRlat() {
        return rlat;
    }

    public void setRlat(double rlat) {
        this.rlat = rlat;
    }

    public double getRlong() {
        return rlong;
    }

    public void setRlong(double rlong) {
        this.rlong = rlong;
    }

    public void addBenefit(String i){
        benefits.add(i);
    }
}
