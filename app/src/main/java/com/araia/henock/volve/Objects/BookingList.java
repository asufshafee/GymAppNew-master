package com.araia.henock.volve.Objects;

/**
 * Created by GeeksEra on 3/13/2018.
 */

public class BookingList {

    private Booking booking;

    public Booking getBooking ()
    {
        return booking;
    }

    public void setBooking (Booking booking)
    {
        this.booking = booking;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [booking = "+booking+"]";
    }
}
