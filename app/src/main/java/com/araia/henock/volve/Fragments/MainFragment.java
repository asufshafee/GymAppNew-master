package com.araia.henock.volve.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.araia.henock.volve.FragmentsTrainee.TraineeLoginFragment;
import com.araia.henock.volve.FragmentsTrainer.TrainerLoginFragment;
import com.araia.henock.volve.R;

public class MainFragment extends Fragment {
    View view;
    Button btnTrainer, btnTrainee;
    Fragment fragment;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.main_fragment, container, false);

        btnTrainer = view.findViewById(R.id.trainer_btn);
        btnTrainee = view.findViewById(R.id.trainee_btn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        btnTrainee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new TraineeLoginFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null).commit();
            }
        });
        btnTrainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new TrainerLoginFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment).addToBackStack(null).commit();

            }
        });

        return view;
    }

}
