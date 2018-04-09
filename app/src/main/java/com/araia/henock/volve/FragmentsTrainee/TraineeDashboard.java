package com.araia.henock.volve.FragmentsTrainee;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.Product;
import com.araia.henock.volve.Utils.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class TraineeDashboard extends Fragment {
    View view;
    List<Product> productList;
    RatingBar rb;
    //the recyclerview
    RecyclerView recyclerView;

    public TraineeDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.trainee_dashboard, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //initializing the productlist
        productList = new ArrayList<>();

        //adding some items to our list
        productList.add(
                new Product(
                        "Mr. Trainer full name", "4pm-5pm"
                ));
        productList.add(
                new Product(
                        "Mr. Trainer full name", "4pm-5pm"
                ));
        productList.add(
                new Product(
                        "Mr. Trainer full name", "4pm-5pm"
                ));
        productList.add(
                new Product(
                        "Mr. Trainer full name", "4pm-5pm"
                ));
        productList.add(
                new Product(
                        "Mr. Trainer full name", "4pm-5pm"
                ));

        productList.add(
                new Product(
                        "Mr. Trainer full name", "4pm-5pm"
                ));
        //creating recyclerview adapter
        ProductAdapter adapter1 = new ProductAdapter(getActivity(), productList);
        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter1);
        return view;
    }

}
