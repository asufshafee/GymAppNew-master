package com.araia.henock.volve.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class ConfirmationActivity extends AppCompatActivity {

    MyApplication myApplication;
    TextView textViewId;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_confirmation);

        myApplication = (MyApplication) getApplicationContext();
        //Getting Intent
        Intent intent = getIntent();
        spotsDialog = new SpotsDialog(ConfirmationActivity.this, R.style.Custom);


        try {
            JSONObject jsonDetails = new JSONObject(intent.getStringExtra("PaymentDetails"));

            //Displaying payment details
            showDetails(jsonDetails.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        findViewById(R.id.Done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookTrainer(textViewId.getText().toString());

            }
        });
    }

    private void showDetails(JSONObject jsonDetails, String paymentAmount) throws JSONException {
        //Views
        textViewId = (TextView) findViewById(R.id.paymentId);
        TextView textViewStatus = (TextView) findViewById(R.id.paymentStatus);
        TextView textViewAmount = (TextView) findViewById(R.id.paymentAmount);

        //Showing the details from json object
        textViewId.setText(jsonDetails.getString("id"));
        textViewStatus.setText(jsonDetails.getString("state"));
        textViewAmount.setText(paymentAmount + " USD");
    }


    private void BookTrainer(String Token) {
        spotsDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("Trainer", GeneralUtils.email);
            jsonObject.put("DateFrom", GeneralUtils.date);
            jsonObject.put("TimeFrom", GeneralUtils.time2);
            jsonObject.put("TimeTo", GeneralUtils.hours);
            jsonObject.put("Method", "paypal");
            jsonObject.put("PaymentIdOrToken", Token);
            Log.e("values", GeneralUtils.email + GeneralUtils.date);

        } catch (JSONException Ex) {

        }

        final String mRequestBody = jsonObject.toString();
        String URL = GeneralUtils.URL + "api/booking/PlaceBooking";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                spotsDialog.dismiss();
                Log.e("message_response", response);
                if (response.contains("successfully been placed!")) {

                    Toast.makeText(getApplicationContext(), "Trainer Booked Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Credit Card info is incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                Log.e("LOG_VOLLEY", error.toString());

                Toast.makeText(getApplicationContext(), "Payment Error", Toast.LENGTH_SHORT).show();
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

        //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
