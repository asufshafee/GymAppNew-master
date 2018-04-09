package com.araia.henock.volve.FragmentsTrainer;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.araia.henock.volve.Objects.ModelCreateProfile;
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GPSTracker;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class TrainerUpdateProfileFragment extends Fragment {
    View view;
    Button btnName, btnPassword, btnTrainingDetails, btnUpdatePassword, btnUpdateName, btnUpdateProfile;
    EditText etName, etPassword, etConfirmPassword;
    TextView tvName;
    Dialog dialog;
    ImageView profileImage;
    Fragment fragment;
    String filePath;
    SpotsDialog spotsDialog;

    MyApplication myApplication;
    Available available;

    public TrainerUpdateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainer_update_profile, container, false);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        myApplication = (MyApplication) getActivity().getApplicationContext();

        myApplication = (MyApplication) getActivity().getApplicationContext();

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

        btnName = view.findViewById(R.id.change_name);
        //btnPassword = view.findViewById(R.id.change_password);
        btnTrainingDetails = view.findViewById(R.id.training_details);
        tvName = view.findViewById(R.id.trainer_name);
        tvName.setText(myApplication.getLoginSession().getToken().getName());

        profileImage = view.findViewById(R.id.imageView);
        btnUpdateProfile = view.findViewById(R.id.update_profile);


        if (myApplication.getLoginSession().getToken().getBase64() != null && !myApplication.getLoginSession().getToken().getBase64().equals("")) {
            byte[] decodedString = Base64.decode(myApplication.getLoginSession().getToken().getBase64(), Base64.URL_SAFE);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImage.setImageBitmap(decodedByte);
        } else {

            Picasso.with(getActivity()).load(GeneralUtils.URL + myApplication.getLoginSession().getToken().getProfilePhoto()).into(profileImage);
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileImageUploading();
            }
        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    callApi(filePath);
                } else {
                    Toast.makeText(getActivity(), "Please choose an image to upload", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();
            }
        });

        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog_name);
                dialog.setCancelable(true);
                etName = (EditText) dialog.findViewById(R.id.name);
                btnUpdateName = (Button) dialog.findViewById(R.id.btn_update_name);
                btnUpdateName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        apiCallName(etName.getText().toString());
                    }
                });
                dialog.show();
            }
        });
       /* btnPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog_password);
                dialog.setCancelable(true);
                //etPassword = (EditText) dialog.findViewById(R.id.password);
              //  etConfirmPassword = dialog.findViewById(R.id.confirm_password);

                btnUpdatePassword = (Button) dialog.findViewById(R.id.btn_update_password);
                btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                            Toast.makeText(getActivity(), "Both Password is not matching", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                apiCallPassword(etPassword.getText().toString());

                            } catch (JSONException Ex) {

                                Toast.makeText(getActivity(), "Something Went Wrong!!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });*/
        btnTrainingDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new TrainerProfileFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("tag").commit();
            }
        });

        return view;
    }
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
    private void callApi(final String filePath) {
        spotsDialog.show();
        if (available.getAvailableTo().toLowerCase().contains("true")||available.getAvailableTo().toLowerCase().contains("false") )
        {
            available.setAvailableFrom("0");
        }
        if (available.getAvailableFrom().toLowerCase().contains("true")||available.getAvailableFrom().toLowerCase().contains("false") )
        {
            available.setAvailableFrom("0");
        }
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        GPSTracker gpsTracker = new GPSTracker(getActivity());
        ModelCreateProfile modelCreateProfile = new ModelCreateProfile();
        modelCreateProfile.setCost(myApplication.getLoginSession().getToken().getCost());
        modelCreateProfile.setLattitude(String.valueOf(gpsTracker.getLatitude()));
        modelCreateProfile.setLattitude(String.valueOf(gpsTracker.getLongitude()));
        modelCreateProfile.setProfilePhoto(getFileToByte(filePath));
        modelCreateProfile.setAvailable(available);

        Gson gson = new Gson();
        final String mRequestBody = gson.toJson(modelCreateProfile);

        String[] Split = filePath.split("\\.");

        String URL = GeneralUtils.URL + "api/Trainer/" + Split[Split.length - 1] + "/CreateProfile";
        //  myApplication.onShowLoading();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
                //  myApplication.onHideLoading();
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
                        modelLogin.getToken().setBase64(getFileToByte(filePath));
                        myApplication.createLoginSession(modelLogin);

                    }
                } catch (JSONException Ex) {
                    Toast.makeText(getActivity(), "Not updated", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                //    myApplication.onHideLoading();
                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "Not updated", Toast.LENGTH_LONG).show();
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

    private void apiCallName(final String Name) {
        spotsDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = GeneralUtils.URL + "api/ManageAccount/"+ etName.getText().toString() + "/ChangeName";
        //final String mRequestBody = "username="+etEmail.getText().toString().trim()+"&password="+etPassword.getText().toString().trim()+"&grant_type=password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int Code = jsonObject.getInt("status");
                    if (Code == 200) {

                        Toast.makeText(getActivity(), "Name has been Changed", Toast.LENGTH_LONG).show();
                        tvName.setText(Name);
                        dialog.dismiss();
                        ModelLogin modelLogin = new ModelLogin();
                        modelLogin = myApplication.getLoginSession();
                        modelLogin.getToken().setName(Name);
                        myApplication.createLoginSession(modelLogin);
                    } else {
                        Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
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
        };

        requestQueue.add(stringRequest);
    }

    private void apiCallPassword(String Password) throws JSONException {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Email", myApplication.getLoginSession().getToken().getEmail());
        jsonObject.put("OldPassword", Password);
        jsonObject.put("NewPassword", Password);

        final String mRequestBody = jsonObject.toString();
        String URL = GeneralUtils.URL + "api/account/ChangePassword";
        //final String mRequestBody = "username="+etEmail.getText().toString().trim()+"&password="+etPassword.getText().toString().trim()+"&grant_type=password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    int Code = jsonObject.getInt("status");
                    if (Code == 200) {
                        Toast.makeText(getActivity(), "Password Changed Changed.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Error!!.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "Server error" , Toast.LENGTH_SHORT).show();
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
