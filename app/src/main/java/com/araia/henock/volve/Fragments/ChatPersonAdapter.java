package com.araia.henock.volve.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.araia.henock.volve.Objects.BookingList;
import com.araia.henock.volve.Objects.Chat;
import com.araia.henock.volve.R;

import android.widget.TextView;

import com.araia.henock.volve.Objects.Person;
import com.araia.henock.volve.Utils.MyApplication;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ChatPersonAdapter extends RecyclerView.Adapter<ChatPersonAdapter.ViewHolder> {

    private List<BookingList> mDataSet;

    Context context;
    MyApplication myApplication;
    private DatabaseReference mFirebaseRef;

    FirebaseDatabase database;

    public ChatPersonAdapter(List<BookingList> dataSet, Context context, FirebaseDatabase firebaseDatabas) {
        mDataSet = dataSet;
        this.context = context;
        this.database = firebaseDatabas;
        myApplication = (MyApplication) context.getApplicationContext();
    }

    @Override
    public ChatPersonAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        final Person person = new Person();



        if (myApplication.getLoginSession().getToken().getUserType().contains("ee")) {

            person.setEmail(mDataSet.get(position).getBooking().getTrainerEmail());
            person.setUsername(mDataSet.get(position).getBooking().getTrainerName());
            holder.Username.setText(person.getUsername());
            person.setStatus("online");
            mFirebaseRef = database.getReference(myApplication.getLoginSession().getToken().getEmail().replaceAll("[^A-Za-z0-9]", "") + person.getEmail().replaceAll("[^A-Za-z0-9]", ""));

        } else {
            person.setEmail(mDataSet.get(position).getBooking().getTraineeEmail());
            person.setUsername(mDataSet.get(position).getBooking().getTraineeName());
            holder.Username.setText(person.getUsername());
            person.setStatus("online");
            mFirebaseRef = database.getReference(person.getEmail().replaceAll("[^A-Za-z0-9]", "") + myApplication.getLoginSession().getToken().getEmail().replaceAll("[^A-Za-z0-9]", ""));
        }

        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {

                        Chat model = dataSnapshot.getValue(Chat.class);
                        holder.Message.setText(model.getMessage());
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

        holder.Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Fragment fragment = null;
                Class fragmentClass = null;

                fragmentClass = Chat_F.class;
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("person", person);
                    fragment.setArguments(bundle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, fragment, "Chat_Users").addToBackStack(null).commit();


            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    /**
     * Inner Class for a recycler view
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView Username, Message;
        ImageView Profile, Status;
        LinearLayout Click;

        ViewHolder(View v) {
            super(v);
            Message = (TextView) itemView.findViewById(R.id.Message);
            Username = (TextView) itemView.findViewById(R.id.Username);
            Profile = (ImageView) itemView.findViewById(R.id.Image);
            Click = (LinearLayout) itemView.findViewById(R.id.Click);

        }
    }
}
