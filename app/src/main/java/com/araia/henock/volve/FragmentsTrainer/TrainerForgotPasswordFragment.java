package com.araia.henock.volve.FragmentsTrainer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.araia.henock.volve.R;


public class TrainerForgotPasswordFragment extends Fragment {
    View view;
    Button btnSend;
    EditText email;
    Fragment fragment;


    public TrainerForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.forgot_password_trainer, container, false);

        btnSend = view.findViewById(R.id.send);
        email = view.findViewById(R.id.email);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "The password is send to your email", Toast.LENGTH_SHORT).show();
                fragment = new TrainerLoginFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
        });
        return view;
    }
}
