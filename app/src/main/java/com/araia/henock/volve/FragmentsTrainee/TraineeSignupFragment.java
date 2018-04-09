package com.araia.henock.volve.FragmentsTrainee;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.araia.henock.volve.Objects.ModelLogin;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class TraineeSignupFragment extends Fragment {
    View view;
    EditText etName, etMobileNo, etPassword, etEmail;
    CheckBox checkBox;
    Button btnSignUp;
 //   ImageView ivFacebook, ivTwitter;
    TextView tvTerms, tvLogin;
    Fragment fragment;
    TextView tvOk, tvCancel;
    MyApplication myApplication;
    SpotsDialog spotsDialog;

    FirebaseAuth mAuth;
    Dialog dialog;
    private FirebaseAuth mfirebaseAuthRef;
    private final String TAG = "auth";
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String phone, mVerificationId;
    EditText etCode;
    Button btnVerify;


    public TraineeSignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainee_signup, container, false);
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        etName = view.findViewById(R.id.name);
        etEmail = view.findViewById(R.id.email);
        etMobileNo = view.findViewById(R.id.mobile_no);
        etPassword = view.findViewById(R.id.password);

        checkBox = view.findViewById(R.id.terms);
        tvTerms = view.findViewById(R.id.terms_and_conditions);
        tvLogin = view.findViewById(R.id.login);

        btnSignUp = view.findViewById(R.id.signup);

        mAuth = FirebaseAuth.getInstance();
        mfirebaseAuthRef = mfirebaseAuthRef.getInstance();
      //  ivFacebook = view.findViewById(R.id.facebook);
        //ivTwitter = view.findViewById(R.id.twitter);

        clickHandling();
        return view;
    }

    private void clickHandling() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Name is empty", Toast.LENGTH_SHORT).show();
                } else if (etEmail.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Email is empty", Toast.LENGTH_SHORT).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
                    Toast.makeText(getActivity(), "Incorrect Email format", Toast.LENGTH_SHORT).show();
                } else if (etMobileNo.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Mobile Number is empty", Toast.LENGTH_SHORT).show();
                } else if (etMobileNo.getText().toString().length() < 6) {
                    Toast.makeText(getActivity(), "Mobile Number is less than 6 digits", Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Password is empty", Toast.LENGTH_SHORT).show();
                } else {
                   // callApi();
                    sendVerificationCode();
                }

            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // test();
                fragment = new TraineeLoginFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
        });
        tvTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.terms_dialog);
                dialog.setCancelable(true);
                tvOk = (TextView) dialog.findViewById(R.id.ok);
                tvCancel = (TextView) dialog.findViewById(R.id.cancel);
                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void callApi() {
        //  new SpotsDialog(getActivity(), R.style.Custom).show();
        spotsDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = GeneralUtils.URL + "api/account/register";
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Email", etEmail.getText().toString());
            jsonObject.put("Password", etPassword.getText().toString());
            jsonObject.put("Name", etName.getText().toString());
            jsonObject.put("Phone", etMobileNo.getText().toString());
            jsonObject.put("UserType", "Trainee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String mRequestBody = jsonObject.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   new SpotsDialog(getActivity(), R.style.Custom).hide();
                spotsDialog.dismiss();
                Log.i("LOG_VOLLEY", response);
                //  Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                if (response.contains("User with this phone number already exists!")) {
                    Log.e("response", "Phone No already exists");
                    Toast.makeText(getActivity(), "Mobile No Already Used", Toast.LENGTH_SHORT).show();
                } else if (response.toLowerCase().contains("email") && response.toLowerCase().contains("taken")) {
                    Log.e("response", "Email already exists");
                    Toast.makeText(getActivity(), "Email Already used", Toast.LENGTH_SHORT).show();
                } else if (response.contains("User Successfully Registered!")) {

                    ModelLogin login = new ModelLogin();

                    try {
                        JSONObject json = new JSONObject(response);
                        JSONObject jsonObject1 = json.getJSONObject("token");
                        login.getToken().setAccess_token(jsonObject1.getString("access_token"));
                        login.getToken().setEmail(etEmail.getText().toString());
                        login.getToken().setName(jsonObject1.getString("Name"));
                        login.getToken().setPhone(jsonObject1.getString("Phone"));
                        login.getToken().setUserType(jsonObject1.getString("UserType"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    myApplication.createLoginSession(login);
                    Log.e("all_values", login.getToken().getAccess_token()+"\nUserType"+
                            login.getToken().getUserType());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new ScheduleFragment()).commit();

                    Toast.makeText(getActivity(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    Log.e("response", "User registered successfully");
                } else if (response.contains("The Password must be at least 6 characters long.")) {
                    Toast.makeText(getActivity(), "Password must be 6 characters long", Toast.LENGTH_SHORT).show();
                } else if (response.toLowerCase().contains("password")) {
                    Toast.makeText(getActivity(), "Password must be long and complex", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("response", "Password error");
                    Toast.makeText(getActivity(), "password long and complex password", Toast.LENGTH_SHORT).show();
                }
             /*   if (response.contains("exists"))
                {
                    Log.e("tag", "exists");
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                //   new SpotsDialog(getActivity(), R.style.Custom).hide();
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

    public void sendVerificationCode() {

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getActivity(), "Could not send verification code. "+e.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;


                GeneralUtils.verificationId=verificationId;
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Verification dialog code here
                customDialogChallanStatus();


              /*  Intent intent=new Intent(getActivity(),PhoneVerification.class);
                Bundle bundle = new Bundle();
                bundle.putString("mobile_no", etMobileNo.getText().toString());
                bundle.putString("password", etPassword.getText().toString());
                bundle.putString("name", etName.getText().toString());
                bundle.putString("email", etEmail.getText().toString());
                bundle.putString("type", "Trainer");
                startActivity(intent);*/
            }
        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                etMobileNo.getText().toString(),        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "code verified successfully", Toast.LENGTH_SHORT).show();
                            callApi();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getActivity(), "Wrong verification code", Toast.LENGTH_SHORT).show();
                            etCode.setText("");
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    public void customDialogChallanStatus() {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_input_dialog);
        dialog.setCancelable(false);
        etCode = (EditText) dialog.findViewById(R.id.et_verify_code);
        etCode.setHint("Enter Verification Code");
        btnVerify = (Button) dialog.findViewById(R.id.btn_verify);
        dialog.show();
        verifyCode();
    }
    public void verifyCode() {
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString().trim();
                if (code.equals(""))
                {
                    Toast.makeText(getActivity(), "Please enter code", Toast.LENGTH_SHORT).show();
                }
                else {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(GeneralUtils.verificationId, code);
                signInWithPhoneAuthCredential(credential);
            }}
        });
    }
}
