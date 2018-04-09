package com.araia.henock.volve.Objects;

/**
 * Created by GeeksEra on 3/13/2018.
 */

public class Bookings {

    private String status;

    private BookingList[] bookingList;

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public BookingList[] getBookingList ()
    {
        return bookingList;
    }

    public void setBookingList (BookingList[] bookingList)
    {
        this.bookingList = bookingList;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [status = "+status+", bookingList = "+bookingList+"]";
    }
}
