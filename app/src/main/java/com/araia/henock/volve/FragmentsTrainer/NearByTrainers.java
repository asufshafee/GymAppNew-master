package com.araia.henock.volve.FragmentsTrainer;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.araia.henock.volve.Activities.MainDrawerActivity;
import com.araia.henock.volve.Objects.ModelNearByTrainers;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.GPSTracker;
import com.araia.henock.volve.Utils.GeneralUtils;
import com.araia.henock.volve.Utils.MyApplication;
import com.araia.henock.volve.Utils.NearByTrainerAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;


public class NearByTrainers extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    double dblLat, dblLon;
    SpotsDialog spotsDialog;
    MyApplication myApplication;
    TextView no_data_found;
    private MaterialSearchBar searchBar;
    ModelNearByTrainers ListModel;
    List<ModelNearByTrainers> ListTrafficEducation;


    public NearByTrainers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.nearby_trainer, container, false);
        myApplication = (MyApplication) getActivity().getApplicationContext();
        spotsDialog = new SpotsDialog(getActivity(), R.style.Custom);
        no_data_found = view.findViewById(R.id.no_data_found);
        no_data_found.setVisibility(View.INVISIBLE);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);


        searchBar = (MaterialSearchBar) view.findViewById(R.id.searchBar);
        searchBar.setHint("Search");
        searchBar.setSpeechMode(false);

       // Log.e("Location : ", "" + dblLat + " " + dblLon);
        ((MainDrawerActivity)getActivity()).validiateUser();

        view.findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();
            }
        });

        callApi();
        searchEducationList();
        return view;
    }

    private void callApi() {
        spotsDialog.show();
        GPSTracker gpsTracker=new GPSTracker(getActivity());
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
       // GPSTracker gpsTracker = new GPSTracker(getActivity());
       String URL = GeneralUtils.URL + "api/Trainer/"+gpsTracker.getLatitude()+"/"+gpsTracker.getLongitude()+"/GetNearbyTrainers";
      //  String URL = GeneralUtils.URL + "api/Trainer/"+"0.0"+"/"+gpsTracker.getLongitude()+"/GetNearbyTrainers";
        gpsTracker.stopUsingGPS();

        Log.e("Location_sending : ", "" + gpsTracker.getLatitude() + " " + gpsTracker.getLongitude()+"\n"+URL);
        //   final String mRequestBody = "username="+etEmail.getText().toString().trim()+"&password="+etPassword.getText().toString().trim()+"&grant_type=password";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response_new", response);

                spotsDialog.dismiss();
                if (response.contains("Email")) {
                    no_data_found.setVisibility(View.INVISIBLE);
                    Log.e("response", "Login");
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    Gson gson = gsonBuilder.create();
                    ModelNearByTrainers[] nearByTrainers = gson.fromJson(response, ModelNearByTrainers[].class);

                    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    //   recyclerView.setAdapter( adapter );
                    layoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(layoutManager);
                    ListTrafficEducation = new ArrayList<>();
                    for (ModelNearByTrainers modelNearByTrainers:nearByTrainers )
                    {
                        ListTrafficEducation.add(modelNearByTrainers);
                    }
                    adapter = new NearByTrainerAdapter(getActivity(), ListTrafficEducation);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setHasFixedSize(true);
                } else if (!response.contains("Email")) {
                    Log.e("response", "Not Login");
                    no_data_found.setVisibility(View.VISIBLE);
                    //  Toast.makeText(getActivity(), "Not Login", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spotsDialog.dismiss();
                no_data_found.setVisibility(View.VISIBLE);
                Log.e("LOG_VOLLEY", error.toString());
               // Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
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

        };

        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });
    }
    /*public void searchEducationList() {

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());

                query = query.toString().toLowerCase();
                // Toast.makeText(TrafficSigns.this, "Query is: "+query, Toast.LENGTH_SHORT).show();
                List<ModelNearByTrainers> newData = new ArrayList<>();
              //  List<ContactModel> contactModelList = new ArrayList<>();
                for (int j = 0; j < newData.size(); j++) {
                    final String test = newData.get(j).getName().toLowerCase();
                    if (test.contains(query)) {
                        newData.add(newData.get(j));
                    }
                }
                // specify an adapter (see also next example)

                adapter = new NearByTrainerAdapter(getActivity(),  R.layout.nearby_trainer_item, newData);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }*/
    public void searchEducationList() {

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence query, int i, int i1, int i2) {
                Log.d("LOG_TAG", getClass().getSimpleName() + " text changed " + searchBar.getText());

                query = query.toString().toLowerCase();
                // Toast.makeText(TrafficSigns.this, "Query is: "+query, Toast.LENGTH_SHORT).show();

                List<ModelNearByTrainers> newData = new ArrayList<>();
                if (ListTrafficEducation!=null)
                for (int j = 0; j < ListTrafficEducation.size(); j++) {
                   // final String test = ListTrafficEducation.get(j).strImageTitle.toLowerCase();
                    final String test2 = ListTrafficEducation.get(j).getName().toLowerCase();
                    if (test2.contains(query)) {
                        newData.add(ListTrafficEducation.get(j));
                    }
                }
                // specify an adapter (see also next example)
                adapter = new NearByTrainerAdapter(getActivity(), newData);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
