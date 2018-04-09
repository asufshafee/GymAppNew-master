package com.araia.henock.volve.Utils;

/**
 * Created by Fazal Mola on 2/28/2018.
 */

public class ItemObject {
    private String trainee_name;
    private String time;
    public ItemObject(String contents, String time) {
        this.trainee_name = contents;
        this.time = time;
    }
    public String getTrainee_name() {
        return trainee_name;
    }
    public String getTime() {
        return time;
    }
}
