package com.araia.henock.volve.Objects;

import java.io.Serializable;

/**
 * Created by Fazal Mola on 3/7/2018.
 */


public class Available implements Serializable{

    private String AvailableFrom;

    private String AvailableTo;

    private Boolean Saturday;

    private Boolean Thursday;

    private Boolean Monday;

    private Boolean Tuesday;

    private Boolean Wednesday;

    private Boolean Friday;

    private Boolean Sunday;

    public String getAvailableFrom() {
        return AvailableFrom;
    }

    public void setAvailableFrom(String AvailableFrom) {
        this.AvailableFrom = AvailableFrom;
    }

    public String getAvailableTo() {
        return AvailableTo;
    }

    public void setAvailableTo(String AvailableTo) {
        this.AvailableTo = AvailableTo;
    }

    public Boolean getSaturday() {
        return Saturday;
    }

    public void setSaturday(Boolean Saturday) {
        this.Saturday = Saturday;
    }

    public Boolean getThursday() {
        return Thursday;
    }

    public void setThursday(Boolean Thursday) {
        this.Thursday = Thursday;
    }

    public Boolean getMonday() {
        return Monday;
    }

    public void setMonday(Boolean Monday) {
        this.Monday = Monday;
    }

    public Boolean getTuesday() {
        return Tuesday;
    }

    public void setTuesday(Boolean Tuesday) {
        this.Tuesday = Tuesday;
    }

    public Boolean getWednesday() {
        return Wednesday;
    }

    public void setWednesday(Boolean Wednesday) {
        this.Wednesday = Wednesday;
    }

    public Boolean getFriday() {
        return Friday;
    }

    public void setFriday(Boolean Friday) {
        this.Friday = Friday;
    }

    public Boolean getSunday() {
        return Sunday;
    }

    public void setSunday(Boolean Sunday) {
        this.Sunday = Sunday;
    }
}
