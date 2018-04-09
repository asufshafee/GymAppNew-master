package com.araia.henock.volve.FragmentsTrainer;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.araia.henock.volve.Activities.MainDrawerActivity;
import com.araia.henock.volve.Activities.PaymentActivity;
import com.araia.henock.volve.Objects.ModelNearByTrainers;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class TrainerDetailsFragment extends Fragment {
    View view;
    Button btnBookNow, selectTime, selectDate;
    ImageView drawer;
    ModelNearByTrainers modelNearByTrainers;
    ImageView Image;
    TextView Charge, available_hours, available_days, address, hoursBooking, distance;
    RatingBar rating;
    Boolean Day = false, Time = false;
    MyApplication myApplication;
    int hours, mints;
    SpotsDialog spotsDialog;
    Bundle bundle;

    public TrainerDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainer_details, container, false);

        distance = (TextView) view.findViewById(R.id.distance);

        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        modelNearByTrainers = (ModelNearByTrainers) getArguments().getSerializable("info");
        hoursBooking = (TextView) view.findViewById(R.id.hours);
        Image = (ImageView) view.findViewById(R.id.image);
        Charge = (TextView) view.findViewById(R.id.charges);
        available_hours = (TextView) view.findViewById(R.id.available_hours);
        available_days = (TextView) view.findViewById(R.id.available_days);
        address = (TextView) view.findViewById(R.id.address);
        rating = (RatingBar) view.findViewById(R.id.rating);
        hours = 0;


        rating.setRating(Float.valueOf(modelNearByTrainers.getRating()));
        Picasso.with(getActivity().getApplicationContext()).load(GeneralUtils.URL + modelNearByTrainers.getProfilePhoto()).placeholder(R.drawable.profilepic).error(R.drawable.profilepic).into(Image);
        Charge.setText("$" + modelNearByTrainers.getCost() + "/hour");
        distance.setText(modelNearByTrainers.getDistance() + " km");


        available_hours.setText(modelNearByTrainers.getAvailable().getAvailableFrom() + "-" + modelNearByTrainers.getAvailable().getAvailableTo());

        String Days = "";

        if (modelNearByTrainers.getAvailable().getSunday().equals(true)) {
            Days = Days + "Sunday,";
        }
        if (modelNearByTrainers.getAvailable().getMonday().equals(true)) {
            Days = Days + "Monday,";
        }
        if (modelNearByTrainers.getAvailable().getThursday().equals(true)) {
            Days = Days + "Thursday,";
        }
        if (modelNearByTrainers.getAvailable().getSaturday().equals(true)) {
            Days = Days + "Saturday,";
        }
        if (modelNearByTrainers.getAvailable().getWednesday().equals(true)) {
            Days = Days + "Wednesday,";
        }
        if (modelNearByTrainers.getAvailable().getFriday().equals(true)) {
            Days = Days + "Friday,";
        }
        if (modelNearByTrainers.getAvailable().getTuesday().equals(true)) {
            Days = Days + "Tuesday";
        }
        if (Days.equals("")) {

            Days = "Not Available";
        }
        available_days.setText(Days);
        address.setText(getCompleteAddressString(Double.parseDouble(modelNearByTrainers.getLattitude()), Double.parseDouble(modelNearByTrainers.getLongitude()), getActivity()));


        btnBookNow = view.findViewById(R.id.book_now);
        selectDate = view.findViewById(R.id.select_date);
        selectTime = view.findViewById(R.id.select_time);
        drawer = view.findViewById(R.id.drawer);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();
            }
        });
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, onDateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        selectTime.setText(selectedHour + ":" + selectedMinute);
                        hours = selectedHour;
                        mints = selectedHour;
                        Time = true;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        btnBookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Time) {
                    Toast.makeText(getActivity(), "Select Time", Toast.LENGTH_LONG).show();
                    return;
                } else if (!Day) {
                    Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_LONG).show();
                    return;
                } else if (hoursBooking.getText().equals("")) {
                    Toast.makeText(getActivity(), "Select Training Hours", Toast.LENGTH_SHORT).show();
                } else {
                    CheckAvailability();
                }


            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            selectDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
            Day = true;
        }
    };


    public static String getCompleteAddressString(double LATITUDE, double LONGITUDE, Activity activity) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("GetCart", strReturnedAddress.toString());
            } else {
                Log.d("GetCart", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("GetCart", "Canont get Address!");
        }
        return strAdd;
    }


    private void CheckAvailability() {
        spotsDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jsonObject = new JSONObject();
        if (hoursBooking.getText().toString().equals("")) {
            hours = hours;

            if (hours > 24) {
                hours = hours % 24;
            }
        } else {
            hours = hours + Integer.parseInt(hoursBooking.getText().toString());

            if (hours > 24) {
                hours = hours % 24;
            }
        }


        try {
            String USername = myApplication.getLoginSession().getToken().getEmail();

            jsonObject.put("Trainer", modelNearByTrainers.getEmail());
            jsonObject.put("DateFrom", selectDate.getText().toString());
            jsonObject.put("TimeFrom", selectTime.getText().toString());
            jsonObject.put("TimeTo", String.valueOf(hours) + ":" + String.valueOf(mints));


        } catch (JSONException Ex) {

        }

        final String mRequestBody = jsonObject.toString();
        Log.e("test_values", jsonObject.toString());
        String URL = GeneralUtils.URL + "api/booking/ValidateBooking";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {

                        Intent intent = new Intent(getActivity(), PaymentActivity.class);
                        GeneralUtils.date = selectDate.getText().toString();
                        GeneralUtils.time2 = selectTime.getText().toString();
                        GeneralUtils.hours = String.valueOf(hours) + ":" + String.valueOf(mints);
                        GeneralUtils.email = modelNearByTrainers.getEmail();
                   /*     intent.putExtra("date",selectDate.getText().toString());
                        intent.putExtra("time",selectTime.getText().toString());
                        intent.putExtra("hours",String.valueOf(hours) + ":" + String.valueOf(mints));*/
                        getActivity().startActivity(intent);
                          Toast.makeText(getActivity(), "Trainer Available", Toast.LENGTH_SHORT).show();
                        //   BookTrainer();

                    } else {
                        Toast.makeText(getActivity(), "Trainer Schedule Not Matched", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException Ex) {
                    Toast.makeText(getActivity(), "Trainer not available at this time", Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "Trainer not available" , Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + myApplication.getLoginSession().getToken().getAccess_token());
                Log.e("access_token", myApplication.getLoginSession().getToken().getAccess_token());
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }


    private void BookTrainer() {
        spotsDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jsonObject = new JSONObject();
        hours = hours + Integer.parseInt(available_hours.getText().toString());

        if (hours > 24) {
            hours = hours % 24;
        }

        try {
//modelNearByTrainers.getEmail()
            jsonObject.put("Trainer", modelNearByTrainers.getEmail());
            jsonObject.put("DateFrom", selectDate.getText().toString());
            jsonObject.put("TimeFrom", selectTime.getText().toString());
            jsonObject.put("TimeTo", String.valueOf(hours) + ":" + String.valueOf(mints));
        } catch (JSONException Ex) {

        }

        final String mRequestBody = jsonObject.toString();
        Log.e("response to be sent", jsonObject.toString());
        String URL = GeneralUtils.URL + "api/booking/PlaceBooking";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {
                        Toast.makeText(getActivity(), "Trainer Booked Success", Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException Ex) {
                    Toast.makeText(getActivity(), "Trainer not available at this time", Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + myApplication.getLoginSession().getToken().getAccess_token());
                Log.e("access_token", myApplication.getLoginSession().getToken().getAccess_token());
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }
}
