package com.araia.henock.volve.FragmentsTrainee;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.araia.henock.volve.R;


public class TraineeBookingsFragment extends Fragment {
    View view;


    public TraineeBookingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_blank, container, false);

        return view;
    }

}
