package com.example.administrator.ydxcgld.Bean;

/**
 * Created by Administrator on 2017/9/20.
 */

public class Paihang {

    private String W_Name;
    private int num_Price;
    private int num_order;
    private int num_Star;

    public Paihang() {
    }

    @Override
    public String toString() {
        return "Paihang{" +
                "num_order=" + num_order +
                ", W_Name='" + W_Name + '\'' +
                ", num_Price=" + num_Price +
                ", num_Star=" + num_Star +
                '}';
    }

    public int getNum_order() {
        return num_order;
    }

    public void setNum_order(int num_order) {
        this.num_order = num_order;
    }

    public int getNum_Price() {
        return num_Price;
    }

    public void setNum_Price(int num_Price) {
        this.num_Price = num_Price;
    }

    public int getNum_Star() {
        return num_Star;
    }

    public void setNum_Star(int num_Star) {
        this.num_Star = num_Star;
    }

    public String getW_Name() {
        return W_Name;
    }

    public void setW_Name(String w_Name) {
        W_Name = w_Name;
    }
}
