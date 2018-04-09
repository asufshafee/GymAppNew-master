package com.araia.henock.volve.FragmentsTrainer;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.araia.henock.volve.Objects.Available;
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.gson.Gson;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class NewProfile extends Fragment {
    View view;
    Button btnUpdatePersonalInfo, selectHour, selectDays, updateProfile;
    Fragment fragment;
    MyApplication myApplication;
    SpotsDialog spotsDialog;

    ImageView Profile;

    EditText cost;
    Available available;
    GoogleProgressBar mBar;

    public NewProfile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainer_profile, container, false);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        Profile = (ImageView) view.findViewById(R.id.profile);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);


        if (myApplication.getLoginSession().getToken().getBase64() != null && !myApplication.getLoginSession().getToken().getBase64().equals("")) {
            byte[] decodedString = Base64.decode(myApplication.getLoginSession().getToken().getBase64(), Base64.URL_SAFE);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Profile.setImageBitmap(decodedByte);
        } else {

            Picasso.with(getActivity()).load(GeneralUtils.URL + myApplication.getLoginSession().getToken().getProfilePhoto()).into(Profile);

        }

        available = new Available();
        ModelLogin modelNearByTrainers = new ModelLogin();
        modelNearByTrainers = myApplication.getLoginSession();
        available.setAvailableFrom(myApplication.getLoginSession().getToken().getAvailableFrom());
        available.setAvailableTo(myApplication.getLoginSession().getToken().getAvailableTo());
        available.setFriday(Boolean.valueOf(myApplication.getLoginSession().getToken().getFriday()));
        available.setMonday(Boolean.valueOf(myApplication.getLoginSession().getToken().getMonday()));
        available.setSaturday(Boolean.valueOf(myApplication.getLoginSession().getToken().getSaturday()));
        available.setSunday(Boolean.valueOf(myApplication.getLoginSession().getToken().getSunday()));
        available.setThursday(Boolean.valueOf(myApplication.getLoginSession().getToken().getThursday()));
        available.setTuesday(Boolean.valueOf(myApplication.getLoginSession().getToken().getTuesday()));
        available.setWednesday(Boolean.valueOf(myApplication.getLoginSession().getToken().getWednesday()));


        btnUpdatePersonalInfo = view.findViewById(R.id.personal_info);
        selectHour = view.findViewById(R.id.select_hours);
        selectDays = view.findViewById(R.id.select_days);
        cost = (EditText) view.findViewById(R.id.cost);
        updateProfile = view.findViewById(R.id.update_profile);


        view.findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();
            }
        });

        StartTime = myApplication.getLoginSession().getToken().getAvailableFrom();
        EndTime = myApplication.getLoginSession().getToken().getAvailableTo();
        selectHour.setText(StartTime + "-" + EndTime);

        if (myApplication.getLoginSession().getToken().getCost() == null) {
            cost.setText("0");
        } else {
            cost.setText(myApplication.getLoginSession().getToken().getCost());
        }


        btnUpdatePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new TrainerUpdateProfileFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("").commit();
            }
        });
        selectDays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog;
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialogue_days);
                dialog.setCancelable(true);

                final Button Sun = (Button) dialog.findViewById(R.id.btnSun);
                final Button Mon = (Button) dialog.findViewById(R.id.btnmon);
                final Button Tus = (Button) dialog.findViewById(R.id.btnthu);
                final Button Wed = (Button) dialog.findViewById(R.id.btnwed);
                final Button Thus = (Button) dialog.findViewById(R.id.btntue);
                final Button Fri = (Button) dialog.findViewById(R.id.btnfri);
                final Button Sat = (Button) dialog.findViewById(R.id.btnsat);


                if (available.getMonday())
                    Mon.setTextColor(Color.parseColor("#000000"));
                else {
                    Mon.setTextColor(Color.parseColor("#FF8B17"));
                }
                if (available.getTuesday())
                    Tus.setTextColor(Color.parseColor("#000000"));
                else {
                    Tus.setTextColor(Color.parseColor("#FF8B17"));
                }
                if (available.getThursday())
                    Thus.setTextColor(Color.parseColor("#000000"));
                else {
                    Thus.setTextColor(Color.parseColor("#FF8B17"));
                }

                if (available.getSaturday())
                    Sat.setTextColor(Color.parseColor("#000000"));
                else {
                    Sat.setTextColor(Color.parseColor("#FF8B17"));
                }
                if (available.getSunday())
                    Sun.setTextColor(Color.parseColor("#000000"));
                else {
                    Sun.setTextColor(Color.parseColor("#FF8B17"));
                }
                if (available.getWednesday())
                    Wed.setTextColor(Color.parseColor("#000000"));
                else {
                    Wed.setTextColor(Color.parseColor("#FF8B17"));
                }
                if (available.getFriday())
                    Fri.setTextColor(Color.parseColor("#000000"));
                else {
                    Fri.setTextColor(Color.parseColor("#FF8B17"));
                }

                Sun.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        available.setSunday(!available.getSunday());
                        if (available.getSunday())
                            Sun.setTextColor(Color.parseColor("#000000"));
                        else {
                            Sun.setTextColor(Color.parseColor("#FF8B17"));
                        }


                    }
                });
                Mon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        available.setMonday(!available.getMonday());
                        if (available.getMonday())
                            Mon.setTextColor(Color.parseColor("#000000"));
                        else {
                            Mon.setTextColor(Color.parseColor("#FF8B17"));
                        }

                    }
                });
                Tus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        available.setTuesday(!available.getTuesday());
                        if (available.getTuesday())
                            Tus.setTextColor(Color.parseColor("#000000"));
                        else {
                            Tus.setTextColor(Color.parseColor("#FF8B17"));
                        }

                    }
                });
                Wed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        available.setWednesday(!available.getWednesday());
                        if (available.getWednesday())
                            Wed.setTextColor(Color.parseColor("#000000"));
                        else {
                            Wed.setTextColor(Color.parseColor("#FF8B17"));
                        }


                    }
                });
                Thus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        available.setThursday(!available.getThursday());
                        if (available.getThursday())
                            Thus.setTextColor(Color.parseColor("#000000"));
                        else {
                            Thus.setTextColor(Color.parseColor("#FF8B17"));
                        }


                    }
                });
                Fri.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        available.setFriday(!available.getFriday());
                        if (available.getFriday())
                            Fri.setTextColor(Color.parseColor("#000000"));
                        else {
                            Fri.setTextColor(Color.parseColor("#FF8B17"));
                        }
                    }
                });
                Sat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        available.setSaturday(!available.getSaturday());
                        if (available.getSaturday())
                            Sat.setTextColor(Color.parseColor("#000000"));
                        else {
                            Sat.setTextColor(Color.parseColor("#FF8B17"));
                        }

                    }
                });


                dialog.show();
            }
        });
        selectHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog;
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_start_end_time);
                dialog.setCancelable(true);
                final Button Start = (Button) dialog.findViewById(R.id.Start);
                final Button End = (Button) dialog.findViewById(R.id.End);

                dialog.findViewById(R.id.Done).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.hide();
                        selectHour.setText(StartTime + "-" + EndTime);
                    }
                });

                dialog.findViewById(R.id.Start).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        StartTime(Start);

                    }
                });
                dialog.findViewById(R.id.End).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EndTime(End);
                    }
                });
                dialog.show();


            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new SpotsDialog(getActivity(), R.style.Custom).show();
                    }
                }, 3000);*/
                if (cost.getText().toString().equals("") || cost.getText().toString().equals("null"))
                {
                    Toast.makeText(getActivity(), "Please enter the total cost per hour", Toast.LENGTH_SHORT).show();
                }
                else {
                    callApi();
                }


            }
        });

        return view;
    }

    String StartTime = null, EndTime = null;

    public void StartTime(final Button Start) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                StartTime = selectedHour + ":" + selectedMinute;
                Start.setText(StartTime);
                available.setAvailableFrom(StartTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    public void EndTime(final Button End) {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EndTime = selectedHour + ":" + selectedMinute;
                End.setText(EndTime);
                available.setAvailableTo(EndTime);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void callApi() {
        spotsDialog.show();
        new SpotsDialog(getActivity(), R.style.Custom).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Gson gson = new Gson();
        final String mRequestBody = gson.toJson(available);

        String URL = GeneralUtils.URL + "api/ManageAccount/" + cost.getText().toString() + "/ChangeAvailability";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                spotsDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getInt("status") == 200) {
                        Toast.makeText(getActivity(), "Info updated", Toast.LENGTH_LONG).show();

                        ModelLogin modelLogin = myApplication.getLoginSession();
                        modelLogin.getToken().setAvailableFrom(available.getAvailableFrom());
                        modelLogin.getToken().setAvailableTo(available.getAvailableTo());
                        modelLogin.getToken().setThursday(String.valueOf(available.getThursday()));
                        modelLogin.getToken().setMonday(String.valueOf(available.getMonday()));
                        modelLogin.getToken().setTuesday(String.valueOf(available.getTuesday()));
                        modelLogin.getToken().setSaturday(String.valueOf(available.getSaturday()));
                        modelLogin.getToken().setWednesday(String.valueOf(available.getWednesday()));
                        modelLogin.getToken().setSunday(String.valueOf(available.getSunday()));
                        modelLogin.getToken().setFriday(String.valueOf(available.getFriday()));
                        modelLogin.getToken().setCost(cost.getText().toString());
                        myApplication.createLoginSession(modelLogin);
                    }
                    if (jsonObject.getInt("status") == 400)
                    {
                        Toast.makeText(getActivity(), "Availability to hours must be greater than the availability from hours", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException Ex) {
                    Toast.makeText(getActivity(), "Not updated", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
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
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                headers.put("Authorization", "Bearer " + myApplication.getLoginSession().getToken().getAccess_token());

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