package com.example.currencyexchange;

public class RateItem {
    private String flag;
    private String code;
    private String name;
    private double rate;
    private double change;

    public RateItem(String flag, String code, String name, double rate, double change) {
        this.flag = flag;
        this.code = code;
        this.name = name;
        this.rate = rate;
        this.change = change;
    }

    public String getFlag() { return flag; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public double getRate() { return rate; }
    public double getChange() { return change; }
    public void setRate(double rate) { this.rate = rate; }
    public void setChange(double change) { this.change = change; }
}