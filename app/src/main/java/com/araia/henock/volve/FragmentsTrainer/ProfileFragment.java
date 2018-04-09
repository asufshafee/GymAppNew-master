package com.araia.henock.volve.FragmentsTrainer;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.araia.henock.volve.FragmentsTrainee.ScheduleFragment;
import com.araia.henock.volve.Objects.Available;
import com.araia.henock.volve.Objects.ModelCreateProfile;
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GPSTracker;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    View view;
    Button selectHour, selectDays, updateProfile;
    Fragment fragment;
    MyApplication myApplication;
    SpotsDialog spotsDialog;
    ImageView profileImage;
    String filePath;
    TextView name;
    String first, second;
    LatLng latLngStart;
    Double lat = 0.0, lng = 0.0;

    EditText cost, skills, experience, address;
    Available available;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.update_profile, container, false);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        profileImage = (ImageView) view.findViewById(R.id.profile);
        skills = view.findViewById(R.id.skills);
        experience = view.findViewById(R.id.experience);
        address = view.findViewById(R.id.address);

        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPlace(view);
            }
        });

        name = view.findViewById(R.id.name);

        sharedPreferences = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("email", "").equals(myApplication.getLoginSession().getToken().getEmail())) {
            skills.setText(sharedPreferences.getString("skills", ""));
            address.setText(sharedPreferences.getString("address", ""));
            experience.setText(sharedPreferences.getString("experience", ""));
        } else {
            skills.setText("");
            address.setText("");
            experience.setText("");
        }

        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);


        // Picasso.with(getActivity()).load(GeneralUtils.URL + "/"+"Images/"+myApplication.getLoginSession().getToken().getEmail()+"/profilePhoto.png").placeholder(R.drawable.profilepic).error(R.drawable.profilepic).into(profileImage);
        Picasso.with(getActivity()).load("https://volveapp.com/Images/" + myApplication.getLoginSession().getToken().getEmail() + "/profilePhoto.png").placeholder(R.drawable.profilepic).error(R.drawable.profilepic).into(profileImage);
        Log.e("email", myApplication.getLoginSession().getToken().getEmail() + "\nURL: " +
                "https://volveapp.com/Images/" + myApplication.getLoginSession().getToken().getEmail() + "/profilePhoto.png");
        // https://volveapp.com/Images/abiola@gmail.com/profilePhoto.png
        //  Log.e("email",GeneralUtils.URL+myApplication.getLoginSession().getToken().getEmail()+"/profilePhoto.png");
        // Picasso.with(getActivity()).load(GeneralUtils.URL + "/Images/masaj@gmail.com/profilePhoto.png").placeholder(R.drawable.profilepic).error(R.drawable.profilepic).into(profileImage);

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


        // btnUpdatePersonalInfo = view.findViewById(R.id.personal_info);
        selectHour = view.findViewById(R.id.select_hours);
        selectDays = view.findViewById(R.id.select_days);
        cost = (EditText) view.findViewById(R.id.cost);
        updateProfile = view.findViewById(R.id.update_profile);
        GPSTracker gpsTracker = new GPSTracker(getActivity());


        view.findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();
            }
        });

        StartTime = myApplication.getLoginSession().getToken().getAvailableFrom();
        EndTime = myApplication.getLoginSession().getToken().getAvailableTo();
        //  selectHour.setText(StartTime + "-" + EndTime);
        name.setText(myApplication.getLoginSession().getToken().getName());
        //   experience.setText(myApplication.getLoginSession().getToken().getExperience());
        //   address.setText(myApplication.getLoginSession().getToken().getAddress());
        // skills.setText(myApplication.getLoginSession().getToken().getSkills());
        if (myApplication.getLoginSession().getToken().getCost().equals("null")) {
            cost.setText("0");
        } else {
            cost.setText((myApplication.getLoginSession().getToken().getCost()));
        }
        if (StartTime.equals("False") || EndTime.contains("False")) {
            selectHour.setText("Select");
        } else {
            selectHour.setText(myApplication.getLoginSession().getToken().getAvailableFrom() + "-" + myApplication.getLoginSession().getToken().getAvailableTo());
        }


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                        getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED &&
                        getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED &&
                        getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED
                        ) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
                }
                AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
                pictureDialog.setCancelable(true);
                pictureDialog.setTitle("Choose image from");
                String[] pictureDialogItems = {
                        "\tGallery",
                        "\tCamera"};
                pictureDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        galleryIntent();
                                        break;
                                    case 1:
                                        cameraIntent();
                                        break;
                                }
                            }
                        });
                pictureDialog.show();
            }
        });


       /* if (myApplication.getLoginSession().getToken().getCost() == null) {
            cost.setText("0");
        } else {
            cost.setText(myApplication.getLoginSession().getToken().getCost());
            Log.e("cost", myApplication.getLoginSession().getToken().getCost());
        }*/



     /*   btnUpdatePersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new TrainerUpdateProfileFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("").commit();
            }
        });*/
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
                if (cost.getText().toString().equals("") || cost.getText().toString().equals("null")) {
                    Toast.makeText(getActivity(), "Please enter the total cost per hour", Toast.LENGTH_SHORT).show();
                } else if (skills.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter your skills", Toast.LENGTH_SHORT).show();
                } else if (experience.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter your experience", Toast.LENGTH_SHORT).show();
                } else if (address.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter your address location", Toast.LENGTH_SHORT).show();
                } else {
                    spotsDialog.show();
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
        Log.e("available_time", available.getAvailableFrom() + " " + available.getAvailableTo());
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

        GPSTracker gpsTracker = new GPSTracker(getActivity());
        final String ImageBASE = getFileToByte(filePath);

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JSONObject jsonObject = new JSONObject();
        JSONObject main = new JSONObject();
        try {
            JSONObject availability = new JSONObject();
            String dates = selectHour.getText().toString();
            StringTokenizer tokens = new StringTokenizer(dates, "-");
            first = tokens.nextToken();// this will contain "Fruit"
            second = tokens.nextToken();// this will contain " they taste good"
            availability.put("AvailableFrom", first);
            availability.put("AvailableTo", second);
            availability.put("Monday", available.getMonday()/*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getMonday())*/);
            availability.put("Tuesday", available.getTuesday()/*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getTuesday())*/);
            availability.put("Wednesday", available.getWednesday()/*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getWednesday())*/);
            availability.put("Thursday", available.getThursday() /*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getThursday())*/);
            availability.put("Friday", available.getFriday() /*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getFriday())*/);
            availability.put("Saturday", available.getSaturday()/*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getSaturday())*/);
            availability.put("Sunday", available.getSunday()/*Boolean.parseBoolean(myApplication.getLoginSession().getToken().getSunday())*/);
            main.put("Available", availability);
            main.put("Cost", cost.getText().toString());
            main.put("Address", address.getText().toString());
            main.put("Lattitude", sharedPreferences.getString("lat", String.valueOf(gpsTracker.getLatitude())));
            main.put("Longitude", sharedPreferences.getString("lng", String.valueOf(gpsTracker.getLongitude())));
            main.put("Longitude", sharedPreferences.getString("lng", String.valueOf(gpsTracker.getLongitude())));
            //   Log.e("lat_new", lat.toString());
            main.put("Skills", skills.getText().toString());
            main.put("Experience", experience.getText().toString());
            main.put("ProfilePhoto", ImageBASE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = main.toString();
        Log.e("log_vlaues", main.toString());
//        Log.e("base_64", getFileToByte(filePath));
        /*Gson gson = new Gson();
        final String mRequestBody = gson.toJson(available);*/

        String URL = GeneralUtils.URL + "api/Trainer/png/CreateProfile";

        //   Log.e("token_trainer", myApplication.getLoginSession().getToken().getAccess_token() + "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response_success", response);
                spotsDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (response.contains("Successfully Modified the profile details!")) {
                        Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
                        ModelLogin modelLogin = myApplication.getLoginSession();
                        modelLogin.getToken().setAvailableFrom(first);
                        modelLogin.getToken().setAvailableTo(second);
                        modelLogin.getToken().setThursday(String.valueOf(available.getThursday()));
                        modelLogin.getToken().setMonday(String.valueOf(available.getMonday()));
                        modelLogin.getToken().setTuesday(String.valueOf(available.getTuesday()));
                        modelLogin.getToken().setSaturday(String.valueOf(available.getSaturday()));
                        modelLogin.getToken().setWednesday(String.valueOf(available.getWednesday()));
                        modelLogin.getToken().setSunday(String.valueOf(available.getSunday()));
                        modelLogin.getToken().setFriday(String.valueOf(available.getFriday()));
                        modelLogin.getToken().setCost(cost.getText().toString());
                        modelLogin.getToken().setName(name.getText().toString());
                        modelLogin.getToken().setBase64(ImageBASE);

                        // Log.e("profile_photo", getFileToByte(filePath));
                        editor.putString("address", address.getText().toString()).commit();
                        editor.putString("experience", experience.getText().toString()).commit();
                        editor.putString("skills", skills.getText().toString()).commit();
                        editor.putString("email", modelLogin.getToken().getEmail()).commit();

                        // modelLogin.getToken().getAddress();
                        Log.e("data", "Address" + modelLogin.getToken().getAddress() + "\ncost" + modelLogin.getToken().getCost()
                                + "saturday" + modelLogin.getToken().getSaturday());
                        myApplication.createLoginSession(modelLogin);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new ScheduleFragment()).commit();
                    } else if (response.contains("Profile Successfully Created!")) {
                        Toast.makeText(getActivity(), "Profile updated", Toast.LENGTH_SHORT).show();
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
                        modelLogin.getToken().setName(name.getText().toString());
                        modelLogin.getToken().setBase64(ImageBASE);
                        // modelLogin.getToken().setSkills(skills.getText().toString());
                        // modelLogin.getToken().setAddress(address.getText().toString());
                        // modelLogin.getToken().setExperience(experience.getText().toString());
                        // modelLogin.getToken().setProfilePhoto(getFileToByte(filePath));
                        myApplication.createLoginSession(modelLogin);

                        //Log.e("profile_photo", getFileToByte(filePath));

                        editor.putString("address", address.getText().toString()).commit();
                        editor.putString("experience", experience.getText().toString()).commit();
                        editor.putString("skills", skills.getText().toString()).commit();
                        editor.putString("email", modelLogin.getToken().getEmail()).commit();


                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, new ScheduleFragment()).commit();
                    } else if (response.contains("This user is registered as a Trainee, Cannot update the details!")) {
                        Toast.makeText(getActivity(), "Trainee profile cannot be updated", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("Because You are just creating your profile you should pass a Profile Photo.")) {
                        Toast.makeText(getActivity(), "Please must upload Profile Picture", Toast.LENGTH_SHORT).show();
                    } else if (response.contains("Object was passed as null")) {
                        Toast.makeText(getActivity(), "Image size not allowed, Please choose other image", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException Ex) {
                    Toast.makeText(getActivity(), "Profile Image Issue", Toast.LENGTH_LONG).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "" + "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
             /*   headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");*/
                headers.put("Authorization", "Bearer " + myApplication.getLoginSession().getToken().getAccess_token());
                Log.e("token_new", myApplication.getLoginSession().getToken().getAccess_token());
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
       /* requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });*/
    }

    // Image uploading
    private void profileImageUploading() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED &&
                getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1);
        }
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setCancelable(true);
        pictureDialog.setTitle("Choose image from");
        String[] pictureDialogItems = {
                "\tGallery",
                "\tCamera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                galleryIntent();
                                break;
                            case 1:
                                cameraIntent();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void cameraIntent() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, 1);
    }

    public void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                latLngStart = place.getLatLng();

                lat = latLngStart.latitude;
                lng = latLngStart.longitude;
                double precision = Math.pow(10, 4);
                lat = ((int) (precision * lat)) / precision;

                lng = ((int) (precision * lng)) / precision;
                editor.putString("lat", lat.toString()).commit();
                editor.putString("lng", lng.toString()).commit();

                //  Log.e("Tag", "Place: " + place.getAddress() + "" +place.getPhoneNumber()  + "" + end);
                address.setText(place.getName().toString());
                // use place.getName() to only picking the name

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (resultCode == -1) {

            if (requestCode == 2) {
                Uri picUri = data.getData();

                filePath = getPath(picUri);

                Log.d("picUri", picUri.toString());
                Log.d("filePath", filePath);

                profileImage.setImageURI(picUri);

            }
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                Uri tempUri = getImageUri(getActivity(), photo);
                filePath = getPath(tempUri);

                profileImage.setImageBitmap(photo);
            }
        }
    }

    private String getPath(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getActivity(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getFileToByte(String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;
    }

    /*public static String getFileToByte(String filePath) {
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;
        try {
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeString;

    }*/
    public void findPlace(View view) {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .setCountry("PK")
                    .build();

            Intent intent =
                    new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.RESULT_ERROR)
                            /*.setBoundsBias(new LatLngBounds(
                                    new LatLng(24.3539, 61.74681),
                                    new LatLng(35.91869 , 75.16683)))*/
                            // .setFilter(typeFilter)
                            .build(getActivity());
            startActivityForResult(intent, 100);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

}
