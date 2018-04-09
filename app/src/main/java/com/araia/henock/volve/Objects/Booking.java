package com.araia.henock.volve.Objects;

/**
 * Created by GeeksEra on 3/13/2018.
 */

public class Booking {

    private String Cost;

    private String ScheduleDetailsId;

    private String DateTo;

    private String ScheduleId;

    private String ScheduleDay;

    private String TrainerEmail;

    private String TraineeName;

    private String ScheduleFrom;

    private String DateFrom;

    private String TrainerName;

    private String TraineeEmail;

    private String ScheduleTo;

    private String PaymentStatus;

    private Review review;

    public String getCost ()
    {
        return Cost;
    }

    public void setCost (String Cost)
    {
        this.Cost = Cost;
    }

    public String getScheduleDetailsId ()
    {
        return ScheduleDetailsId;
    }

    public void setScheduleDetailsId (String ScheduleDetailsId)
    {
        this.ScheduleDetailsId = ScheduleDetailsId;
    }

    public String getDateTo ()
    {
        return DateTo;
    }

    public void setDateTo (String DateTo)
    {
        this.DateTo = DateTo;
    }

    public String getScheduleId ()
    {
        return ScheduleId;
    }

    public void setScheduleId (String ScheduleId)
    {
        this.ScheduleId = ScheduleId;
    }

    public String getScheduleDay ()
    {
        return ScheduleDay;
    }

    public void setScheduleDay (String ScheduleDay)
    {
        this.ScheduleDay = ScheduleDay;
    }

    public String getTrainerEmail ()
    {
        return TrainerEmail;
    }

    public void setTrainerEmail (String TrainerEmail)
    {
        this.TrainerEmail = TrainerEmail;
    }

    public String getTraineeName ()
    {
        return TraineeName;
    }

    public void setTraineeName (String TraineeName)
    {
        this.TraineeName = TraineeName;
    }

    public String getScheduleFrom ()
    {
        return ScheduleFrom;
    }

    public void setScheduleFrom (String ScheduleFrom)
    {
        this.ScheduleFrom = ScheduleFrom;
    }

    public String getDateFrom ()
    {
        return DateFrom;
    }

    public void setDateFrom (String DateFrom)
    {
        this.DateFrom = DateFrom;
    }

    public String getTrainerName ()
    {
        return TrainerName;
    }

    public void setTrainerName (String TrainerName)
    {
        this.TrainerName = TrainerName;
    }

    public String getTraineeEmail ()
    {
        return TraineeEmail;
    }

    public void setTraineeEmail (String TraineeEmail)
    {
        this.TraineeEmail = TraineeEmail;
    }

    public String getScheduleTo ()
    {
        return ScheduleTo;
    }

    public void setScheduleTo (String ScheduleTo)
    {
        this.ScheduleTo = ScheduleTo;
    }

    public String getPaymentStatus ()
    {
        return PaymentStatus;
    }

    public void setPaymentStatus (String PaymentStatus)
    {
        this.PaymentStatus = PaymentStatus;
    }

    public Review getReview ()
    {
        return review;
    }

    public void setReview (Review review)
    {
        this.review = review;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Cost = "+Cost+", ScheduleDetailsId = "+ScheduleDetailsId+", DateTo = "+DateTo+", ScheduleId = "+ScheduleId+", ScheduleDay = "+ScheduleDay+", TrainerEmail = "+TrainerEmail+", TraineeName = "+TraineeName+", ScheduleFrom = "+ScheduleFrom+", DateFrom = "+DateFrom+", TrainerName = "+TrainerName+", TraineeEmail = "+TraineeEmail+", ScheduleTo = "+ScheduleTo+", PaymentStatus = "+PaymentStatus+", review = "+review+"]";
    }

}

