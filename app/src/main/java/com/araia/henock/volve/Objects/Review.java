package com.araia.henock.volve.Objects;

/**
 * Created by GeeksEra on 3/13/2018.
 */

public class Review {


    private String Rating;

    private String Comment;

    public String getRating ()
    {
        return Rating;
    }

    public void setRating (String Rating)
    {
        this.Rating = Rating;
    }

    public String getComment ()
    {
        return Comment;
    }

    public void setComment (String Comment)
    {
        this.Comment = Comment;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Rating = "+Rating+", Comment = "+Comment+"]";
    }
}
