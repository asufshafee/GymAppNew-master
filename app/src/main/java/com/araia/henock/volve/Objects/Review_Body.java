package com.araia.henock.volve.Objects;

import com.google.gson.Gson;

/**
 * Created by GeeksEra on 4/7/2018.
 */

public class Review_Body {

    /**
     * Trainer : email@email.com
     * ScheduleId : 1
     * Rating : 4.5
     * Comments : Excellent to work with this trainer
     */

    private String Trainer;
    private String ScheduleId;
    private String Rating;
    private String Comments;

    public static Review_Body objectFromData(String str) {

        return new Gson().fromJson(str, Review_Body.class);
    }

    public String getTrainer() {
        return Trainer;
    }

    public void setTrainer(String Trainer) {
        this.Trainer = Trainer;
    }

    public String getScheduleId() {
        return ScheduleId;
    }

    public void setScheduleId(String ScheduleId) {
        this.ScheduleId = ScheduleId;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String Comments) {
        this.Comments = Comments;
    }
}
