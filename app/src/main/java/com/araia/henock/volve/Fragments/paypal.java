package com.araia.henock.volve.Fragments;

// import android.support.v4.app.Fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.araia.henock.volve.Activities.MainActivity;
import com.araia.henock.volve.R;

public class paypal extends android.support.v4.app.Fragment {

    Button btnProceed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.paypal, container, false);

        TextView tvPayPal = (TextView) rootView.findViewById(R.id.What_is_paypal);
        btnProceed = rootView.findViewById(R.id.proceed_to_paypal);
        tvPayPal.setPaintFlags(tvPayPal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });
        tvPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://www.paypal.com/your_page";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return rootView;
    }

}
