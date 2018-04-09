package com.araia.henock.volve.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.araia.henock.volve.Objects.Review_Body;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Review extends Fragment {


    RatingBar ratingBar;
    EditText Comment;
    SpotsDialog spotsDialog;


    public Review() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);

        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        Comment = (EditText) view.findViewById(R.id.Comment);
        view.findViewById(R.id.OK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Comment.getText().toString().equals(""))
                    callApi();
                else
                    Toast.makeText(getActivity(), "Please Enter Some Comments", Toast.LENGTH_LONG).show();
            }
        });

        view.findViewById(R.id.Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        return view;
    }

    private void callApi() {
        spotsDialog.show();
        String mRequestBody = "";
        Review_Body review_body = new Review_Body();
        review_body.setComments(Comment.getText().toString());
        review_body.setRating(String.valueOf(ratingBar.getRating()));
        review_body.setScheduleId(getArguments().getString("id"));
        review_body.setTrainer(getArguments().getString("triner"));

        Gson gson = new Gson();
        mRequestBody = gson.toJson(review_body);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = GeneralUtils.URL + "api/review/LeaveReview/";
        final String finalMRequestBody = mRequestBody;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();

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

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return finalMRequestBody == null ? null : finalMRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", finalMRequestBody, "utf-8");
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);
    }
}
