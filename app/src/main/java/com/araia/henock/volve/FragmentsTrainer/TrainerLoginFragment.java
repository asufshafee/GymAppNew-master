package com.araia.henock.volve.FragmentsTrainer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.araia.henock.volve.Fragments.ForgotPasswordFragment;
import com.araia.henock.volve.FragmentsTrainee.ScheduleFragment;
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.Objects.Token;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class TrainerLoginFragment extends Fragment {
    View view;
    TextView tvForgot, tvSignUp;
    Button btnLogin;
    EditText etEmail, etPassword;
    Fragment fragment;
    SpotsDialog spotsDialog;


    MyApplication myApplication;

    public TrainerLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainer_login, container, false);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        tvForgot = view.findViewById(R.id.ForgotPass);
        tvSignUp = view.findViewById(R.id.TrainerSignUp);
        btnLogin = view.findViewById(R.id.login);
        etEmail = view.findViewById(R.id.email);
        etPassword = view.findViewById(R.id.password);

        clickHandling();

        return view;
    }

    private void clickHandling() {
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ForgotPasswordFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null).commit();

            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new TrainerSignupFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Email", Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    callApi();

                }
            }
        });

    }

    private void callApi() {
        spotsDialog.show();
        //   new SpotsDialog(getActivity(), R.style.Custom).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = GeneralUtils.URL + "token";
        Log.e("url", URL);
        final String mRequestBody = "username=" + etEmail.getText().toString().trim() + "&password=" + etPassword.getText().toString().trim() + "&grant_type=password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //    new SpotsDialog(getActivity(), R.style.Custom).hide();
                spotsDialog.dismiss();
                if (response.contains("access_token")) {
                    Log.e("response", "Login");
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ModelLogin login = new ModelLogin();
                    Token token = new Token();
                    token = gson.fromJson(response, Token.class);
                    login.setToken(token);
                    if (!login.getToken().getUserType().equals("Trainee")) {
                      //  Toast.makeText(getActivity(), "Login Successfully", Toast.LENGTH_SHORT).show();
                        myApplication.createLoginSession(login);
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScheduleFragment()).commit();

                    } else {
                        Toast.makeText(getActivity(), "You cannot Login as a Trainer, Please Login as a Trainee", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.contains("The user name or password is incorrect.")) {
                    Log.e("response", "Not Login");
                    Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
                else if (response.contains("User is not allowed to access the application!")) {
                    Log.e("response", "User is not allowed to access this application");
                    Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
             //   Log.e("token_trainer_1",myApplication.getLoginSession().getToken()+"");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                //    new SpotsDialog(getActivity(), R.style.Custom).hide();
                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
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
