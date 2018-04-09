package com.araia.henock.volve.FragmentsTrainee;

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
import com.araia.henock.volve.FragmentsTrainer.NearByTrainers;
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class TraineeLoginFragment extends Fragment {
    View view;
    TextView tvForgot, tvSignUp;
    Button btnLogin;
    EditText etEmail, etPassword;
    Fragment fragment;
    MyApplication myApplication;
    SpotsDialog spotsDialog;


    public TraineeLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainee_login, container, false);
        tvForgot = view.findViewById(R.id.forgot_password);
        tvSignUp = view.findViewById(R.id.signup);
        btnLogin = view.findViewById(R.id.login);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);

        etEmail = view.findViewById(R.id.email);
        etPassword = view.findViewById(R.id.password);

        myApplication = (MyApplication) getActivity().getApplicationContext();

        clickHandling();

        return view;
    }

    private void clickHandling() {
        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new ForgotPasswordFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("tag").commit();
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new TraineeSignupFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack("tag").commit();
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
                    /*Intent intent = new Intent(getActivity(), MainDrawerActivity.class);
                    startActivity(intent);
                    getActivity().finish();*/
                }
            }
        });
    }

    private void callApi() {
       // new SpotsDialog(getActivity(), R.style.Custom).show();
        spotsDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = GeneralUtils.URL + "token";
        final String mRequestBody = "username=" + etEmail.getText().toString().trim() + "&password=" + etPassword.getText().toString().trim() + "&grant_type=password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
            //    new SpotsDialog(getActivity(), R.style.Custom).hide();
                Log.e("response", response);
                if (response.contains("access_token")) {
                    Log.e("response", "Login");

                    ModelLogin login = new ModelLogin();

                    try {
                        JSONObject json = new JSONObject(response);
                        login.getToken().setAccess_token(json.getString("access_token"));
                        login.getToken().setEmail(etEmail.getText().toString());
                        login.getToken().setName(json.getString("Name"));
                        login.getToken().setPhone(json.getString("Phone"));
                        login.getToken().setUserType(json.getString("UserType"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.e("access_token", login.getToken().getAccess_token() + "\n" + login.getToken().getEmail());
                    if (login.getToken().getUserType().equals("Trainee")) {
                        myApplication.createLoginSession(login);
                     //   Toast.makeText(getActivity(), "Login Successfully "+ myApplication.getLoginSession().getToken().getEmail(), Toast.LENGTH_SHORT).show();

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new NearByTrainers()).commit();

                    } else {
                        Toast.makeText(getActivity(), "You cannot Login as a Trainee, Please Login as a Trainer", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.contains("The user name or password is incorrect.")) {
                    Log.e("response", "Not Login");
                    Toast.makeText(getActivity(), "The user name or password is incorrect", Toast.LENGTH_SHORT).show();
                }
                else if (response.contains("User is not allowed to access the application!")) {
                    Log.e("response", "User is not allowed to access this application");
                    Toast.makeText(getActivity(), "Wrong email or password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
            //    new SpotsDialog(getActivity(), R.style.Custom).hide();

                Log.e("LOG_VOLLEY", error.toString() + error.networkResponse.statusCode);
                Toast.makeText(getActivity(), "" + "Invalid User name or Password", Toast.LENGTH_SHORT).show();
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
