package com.example.currentplacedetailsonmap;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.currentplacedetailsonmap.SaveSharedPreference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {


    EditText edtTxtGroupName;
    EditText edtTxtUsername;
    Context context = this;


    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogin);

        //setting the title
        toolbar.setTitle("Join A Group");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar);

        edtTxtUsername = (EditText) findViewById(R.id.username);
        edtTxtGroupName = (EditText) findViewById(R.id.groupname);
    }

    public void login(View v) {
//        SaveSharedPreference.setUserName(LoginActivity.this, username.getText().toString());

//        Username'in db'ye yazıldığı kısım
//        DatabaseHelper dbHelper = new DatabaseHelper(this);
//
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put("username", username.getText().toString());
//
//        db.insert("groups", null, values);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        DatabaseReference myRef = database.getReference("locations");
//
//        myRef.child(username.getText().toString()).setValue("");
//

        if (TextUtils.isEmpty(edtTxtUsername.getText())) {
            Toast.makeText(this, "User name is required", Toast.LENGTH_SHORT);
            edtTxtUsername.setError("User name is required!");
        } else if (TextUtils.isEmpty(edtTxtGroupName.getText())) {
            Toast.makeText(this, "Group is required", Toast.LENGTH_SHORT);
            edtTxtUsername.setError("Group name is required!");
        } else {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference myRef = database.getReference("locations");

            final DatabaseReference myUser = database.getReference("locations").child(SaveSharedPreference.getGroupName(context).toString()).child("users");


            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                boolean flag = false;

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                    for (DataSnapshot dataSnapshot1 : list) {
                        if (dataSnapshot1.getKey().equals(edtTxtGroupName.getText().toString())) {

                            flag = true;
                        }
                    }


                    if (flag) {

                        myUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            boolean flag1 = false;

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Iterable<DataSnapshot> list = dataSnapshot.getChildren();

                                for (DataSnapshot dataSnapshot1 : list) {
                                    if (dataSnapshot1.getKey().equals(edtTxtUsername.getText().toString())) {

                                        flag1 = true;
                                    }

                                }

                                if (flag1) {
                                    Toast.makeText(context, "Username is taken in your group.Change it!", Toast.LENGTH_SHORT).show();
                                } else {
                                    SaveSharedPreference.setUserName(context, edtTxtUsername.getText().toString());
                                    SaveSharedPreference.setGroupName(context, edtTxtGroupName.getText().toString());

                                    Intent intent = new Intent(context, WelcomeScreenActivity.class);
                                    String strName = "fromLogin";
                                    intent.putExtra("STRING_I_NEED", strName);
                                    startActivity(intent);
                                }
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    } else {
                        Toast.makeText(context, "There is no group with that name!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
        }
    }
}
