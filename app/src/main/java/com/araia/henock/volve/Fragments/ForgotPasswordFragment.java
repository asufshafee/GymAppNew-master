package com.araia.henock.volve.Fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class ForgotPasswordFragment extends Fragment {
    View view;
    Button btnSend;
    EditText email;
    Fragment fragment;
    MyApplication myApplication;
    SpotsDialog spotsDialog;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.forgot_password, container, false);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        btnSend = view.findViewById(R.id.send);
        email = view.findViewById(R.id.email);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity(), "Email is empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    callApi();
                }
           //     Toast.makeText(getActivity(), "The password is send to your email", Toast.LENGTH_SHORT).show();
/*                fragment = new TraineeLoginFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();*/
            }
        });
        return view;
    }
    private void callApi() {
        spotsDialog.show();
        myApplication.onHideLoading();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = GeneralUtils.URL+"api/ManageAccount/"+email.getText().toString()+"/ResetPassword";
       //final String mRequestBody = "username="+etEmail.getText().toString().trim()+"&password="+etPassword.getText().toString().trim()+"&grant_type=password";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
                if (response.contains("Password has been sent to your email address."))
                {
                    Log.e("response", "Password sent to the email");
                    Toast.makeText(getActivity(), "Password sent to the email", Toast.LENGTH_SHORT).show();
                    fragment = new MainFragment();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.container, fragment).commit();
                }
                else if (response.contains("User does not exists"))
                {
                    Log.e("response", "User does not exists");
                    Toast.makeText(getActivity(), "Invalid Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Log.e("LOG_VOLLEY", error.toString());
                Toast.makeText(getActivity(), "Password not sent to your email", Toast.LENGTH_SHORT).show();
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

          /*  @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }*/
        };

        requestQueue.add(stringRequest);
    }
}
