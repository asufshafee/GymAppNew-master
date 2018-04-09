package com.araia.henock.volve.FragmentsTrainee;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.araia.henock.volve.Activities.MainDrawerActivity;
import com.araia.henock.volve.Objects.Bookings;
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.HeaderRecyclerViewSection;
import com.araia.henock.volve.Utils.ItemObject;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;


public class ScheduleFragment extends Fragment {
    View view;
    private RecyclerView sectionHeader;
    private SectionedRecyclerViewAdapter sectionAdapter;
    TextView tvNoDataFound;

    MyApplication myApplication;
    TextView CheckForUser;
    ImageView drawer;
    SpotsDialog spotsDialog;

    List<String> Dates, Days;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.history_activity_trainee, container, false);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        CheckForUser = (TextView) view.findViewById(R.id.CheckForUser);
        tvNoDataFound = view.findViewById(R.id.no_data_found);
        tvNoDataFound.setVisibility(View.INVISIBLE);
        Dates = new ArrayList<>();
        Days = new ArrayList<>();
        Log.e("check_values", myApplication.getLoginSession().getToken().getUserType() + "\naccess_token"
                + myApplication.getLoginSession().getToken().getAccess_token());

        if (myApplication.getLoginSession().getToken().getUserType().contains("ee")) {
            CheckForUser.setText("Schedule");
        } else {
            CheckForUser.setText("Home");
        }
        ((MainDrawerActivity) getActivity()).validiateUser();
        drawer = view.findViewById(R.id.drawer);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();
            }
        });
        sectionHeader = (RecyclerView) view.findViewById(R.id.add_header);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        sectionHeader.setLayoutManager(linearLayoutManager);
        sectionHeader.setHasFixedSize(true);
        sectionAdapter = new SectionedRecyclerViewAdapter();
        callApi();

        return view;
    }

    private List<ItemObject> getDataSource(Bookings booking, String Day, String Date) {


        List<ItemObject> data = new ArrayList<ItemObject>();

        for (int i = 0; i < booking.getBookingList().length; i++) {

            if (booking.getBookingList()[i].getBooking().getScheduleDay().contains(Day) || booking.getBookingList()[i].getBooking().getDateFrom().split(" ")[0].contains(Date))
                if (myApplication.getLoginSession().getToken().getUserType().contains("ee")) {
                    data.add(new ItemObject(booking.getBookingList()[i].getBooking().getTrainerName(), getTimeFormate(booking.getBookingList()[i].getBooking().getScheduleFrom()) + " - " + getTimeFormate(booking.getBookingList()[i].getBooking().getScheduleTo())));
                } else {
                    data.add(new ItemObject(booking.getBookingList()[i].getBooking().getTraineeName(), getTimeFormate(booking.getBookingList()[i].getBooking().getScheduleFrom()) + " - " + getTimeFormate(booking.getBookingList()[i].getBooking().getScheduleTo())));
                }
        }

        return data;
    }

    public String getTimeFormate(String ToBeSet) {

        String ReturnDate = ToBeSet;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        try {

            Date d = df.parse(ToBeSet);
            return getTime(String.valueOf(d.getTime()));

        } catch (Exception a) {

        }
        return ReturnDate;

    }

    String getTime(String milliseconds) {

        String time = "";
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(milliseconds));

        time = String.valueOf(cal.get(Calendar.HOUR));

        if (cal.get(Calendar.AM_PM) == 0)
            time = time + " AM";
        else
            time = time + " PM";

        return time;

    }


    private void callApi() {
        spotsDialog.show();
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        ModelLogin modelLogin = myApplication.getLoginSession();
        String URL = "";
        if (myApplication.getLoginSession().getToken().getUserType().contains("ee")) {

            URL = GeneralUtils.URL + "api/booking/GetTraineeBookings";
        } else {
            URL = GeneralUtils.URL + "api/booking/GetTrainerBookings";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
                Log.e("api_response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {
                        Gson gson = new Gson();
                        Bookings bookings = new Bookings();
                        bookings = gson.fromJson(response, Bookings.class);

                        for (int i = 0; i < bookings.getBookingList().length; i++) {

                            String dtStart = bookings.getBookingList()[i].getBooking().getDateFrom().split(" ")[0];
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = null;
                            try {
                                date = format.parse(dtStart);
                                System.out.println(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                            if (!Days.contains(bookings.getBookingList()[i].getBooking().getScheduleDay()) || !Dates.contains(dtStart)) {
                                HeaderRecyclerViewSection firstSection = new HeaderRecyclerViewSection(bookings.getBookingList()[i].getBooking().getScheduleDay(), getDataSource(bookings, bookings.getBookingList()[i].getBooking().getScheduleDay(), bookings.getBookingList()[i].getBooking().getScheduleDay()), dtStart.replace("/", "-"));
                                sectionAdapter.addSection(firstSection);
                                Days.add(bookings.getBookingList()[i].getBooking().getScheduleDay());
                                Dates.add(dtStart);
                            }

                        }
                        sectionHeader.setAdapter(sectionAdapter);


                    } else if (jsonObject.getInt("status") == 404) {
                        tvNoDataFound.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException Ex) {
                    Log.e("LOG_VOLLEY", Ex.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();

                Log.e("LOG_VOLLEY", error.toString());
                //  Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + myApplication.getLoginSession().getToken().getAccess_token());
                //  Log.e("token", myApplication.getLoginSession().getToken().getAccess_token());

                return headers;
            }

        };

        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }

}
