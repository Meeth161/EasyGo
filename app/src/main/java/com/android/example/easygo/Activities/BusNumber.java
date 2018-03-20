package com.android.example.easygo.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.example.easygo.Adapters.BusNumberAdapter;
import com.android.example.easygo.BusStop;
import com.android.example.easygo.R;
import com.android.example.easygo.Route;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BusNumber extends AppCompatActivity {

    RecyclerView rvBusNumber;
    BusNumberAdapter adapter;

    DatabaseReference mRef;

    ProgressDialog progressDialog;

    ArrayList<Route> busRoutes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_number);

        progressDialog = new ProgressDialog(BusNumber.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        mRef = FirebaseDatabase.getInstance().getReference();

        rvBusNumber = (RecyclerView) findViewById(R.id.rv_busNumber);
        rvBusNumber.setLayoutManager(new LinearLayoutManager(BusNumber.this));
        adapter = new BusNumberAdapter(BusNumber.this, busRoutes);
        rvBusNumber.setAdapter(adapter);

        DatabaseReference bnRef = mRef.child("routes");
        bnRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String n = ds.getKey();
                    String s = ds.child("origin").child("name").getValue(String.class);
                    String d = ds.child("dest").child("name").getValue(String.class);
                    busRoutes.add(new Route(n, s, d));
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}

