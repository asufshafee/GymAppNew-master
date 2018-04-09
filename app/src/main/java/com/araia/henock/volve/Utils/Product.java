package com.araia.henock.volve.Utils;

/**
 * Created by Belal on 10/18/2017.
 */


public class Product {
    private String title,Timing;


    public Product(String title, String timing) {
        this.title = title;
        Timing = timing;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTiming() {
        return Timing;
    }

    public void setTiming(String timing) {
        Timing = timing;
    }
}

