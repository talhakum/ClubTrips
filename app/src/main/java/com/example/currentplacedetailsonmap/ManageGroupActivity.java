package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<String> userNames = new ArrayList<>();
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_group);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarManageGroup);

        //setting the title
        toolbar.setTitle("Manage Group");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyView);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(context, MapsActivityCurrentPlace.class);
                        intent.putExtra("methodName", "focusClickedUser");
                        intent.putExtra("clickedUser", userNames.get(position));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MyAdapter(userNames);
        recyclerView.setAdapter(adapter);

        // Fill out user names
        prepareUserNames();
    }

    private void prepareUserNames() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("locations").child(SaveSharedPreference.getGroupName(ManageGroupActivity.this).toString());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                userNames.clear();

                for (DataSnapshot dataSnapshot1 : list) {
                    Log.e("friendtest: ", dataSnapshot1.getKey() + " lat: " + dataSnapshot1.child("lat").getValue() + " lng: " + dataSnapshot1.child("lng").getValue());
                    userNames.add(dataSnapshot1.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void go(View v) {
        Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
        startActivity(intent);
    }
}
