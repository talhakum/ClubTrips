package com.example.currentplacedetailsonmap;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class CreateGroupActivity extends AppCompatActivity {

    EditText edtTxtGroupName;
    EditText edtTxtUsername;
    Context context = this;

    public static final int GRID_REQUEST = 1;
    int selectedImagePath = 0;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);


        Button b = (Button) findViewById(R.id.emoji);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateGroupActivity.this, PopUp.class);

                startActivityForResult(intent,GRID_REQUEST);
            }
        });

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCreateGroup);

        //setting the title
        toolbar.setTitle("Create A Group");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);

        edtTxtGroupName = (EditText) findViewById(R.id.edtTxtGroupName);
        edtTxtUsername = (EditText) findViewById(R.id.edtTxtUsername);

    }

    public void create(View v) {

        if (TextUtils.isEmpty(edtTxtGroupName.getText())) {
            Toast.makeText(this, "Group Name field is required!", Toast.LENGTH_SHORT);

            edtTxtGroupName.setError("Group name is required");
        } else if (TextUtils.isEmpty(edtTxtUsername.getText())) {
            Toast.makeText(this, "Username field is required!", Toast.LENGTH_SHORT);

            edtTxtUsername.setError("Username is required");
        } else {


            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database.getReference("locations");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                boolean flag = false;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> list = dataSnapshot.getChildren();


                    for (DataSnapshot dataSnapshot1 : list) {
                        if (dataSnapshot1.getKey().equals(edtTxtGroupName.getText().toString())) {
                            Toast.makeText(context, "Group name has been already taken.", Toast.LENGTH_SHORT).show();
                            flag = true;
                        }
                    }


                    if (!flag) {
                        SaveSharedPreference.setUserName(CreateGroupActivity.this, edtTxtUsername.getText().toString());
                        SaveSharedPreference.setGroupName(CreateGroupActivity.this, edtTxtGroupName.getText().toString());
                        SaveSharedPreference.setEmojiId(CreateGroupActivity.this, selectedImagePath);



                        Intent intent = new Intent(context, WelcomeScreenActivity.class);
                        String strName = "fromCreate";
                        intent.putExtra("STRING_I_NEED", strName);
                        startActivity(intent);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GRID_REQUEST) {
            if(resultCode == RESULT_OK) {
                selectedImagePath =data.getIntExtra("imagePath",0);
                //TODO: Set the image in your imageview
                Log.d("Olumlu","Gelen resim "+selectedImagePath);
            }
            if (resultCode == RESULT_CANCELED) {
                Log.d("test","The selection was cancelled");
            }
        }
    }
}
