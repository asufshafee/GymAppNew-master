package com.araia.henock.volve.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.araia.henock.volve.Activities.MainDrawerActivity;
import com.araia.henock.volve.Objects.Chat;
import com.araia.henock.volve.Objects.Person;
import com.araia.henock.volve.R;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_F extends Fragment {


    private EditText metText;
    private ImageView mbtSent;
    private DatabaseReference mFirebaseRef;

    private List<Chat> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private String mId;

    MyApplication myApplication;
    Person person;
    TextView Username;

    @SuppressLint("HardwareIds")


    public Chat_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        myApplication = (MyApplication) getActivity().getApplicationContext();

        person = (Person) getArguments().getSerializable("person");

        Username = (TextView) view.findViewById(R.id.Username);
        Username.setText(person.getUsername());

        metText = (EditText) view.findViewById(R.id.etText);
        mbtSent = (ImageView) view.findViewById(R.id.btSent);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rvChat);
        mChats = new ArrayList<>();

        view.findViewById(R.id.drawer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainDrawerActivity) getActivity()).OpenOpenOrCloseDrawer();

            }
        });

        mId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ChatAdapter(mChats, mId, getActivity());
        mRecyclerView.setAdapter(mAdapter);

        /**
         * Firebase - Inicialize
         */
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        if (myApplication.getLoginSession().getToken().getUserType().contains("ee")) {
            mFirebaseRef = database.getReference(myApplication.getLoginSession().getToken().getEmail().replaceAll("[^A-Za-z0-9]", "") + person.getEmail().replaceAll("[^A-Za-z0-9]", ""));

        } else {
            mFirebaseRef = database.getReference(person.getEmail().replaceAll("[^A-Za-z0-9]", "") + myApplication.getLoginSession().getToken().getEmail().replaceAll("[^A-Za-z0-9]", ""));
        }


        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString();

                if (!message.isEmpty()) {
                    /**
                     * Firebase - Send message
                     */
                    Chat chat = new Chat();
                    chat.setId(mId);
                    chat.setMessage(message);
                    chat.setSeen("true");
                    chat.setEmail(myApplication.getLoginSession().getToken().getEmail());
                    chat.setTime(String.valueOf(Calendar.getInstance().getTimeInMillis()));
                    mFirebaseRef.push().setValue(chat);
                }

                metText.setText("");
            }
        });


        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {

                        Chat model = dataSnapshot.getValue(Chat.class);

                        mChats.add(model);
                        mRecyclerView.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e("", ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("", databaseError.getMessage());
            }
        });

        return view;
    }

}
