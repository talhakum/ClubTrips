package com.example.currentplacedetailsonmap;

import android.content.ContentValues;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText groupname;

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

        username = (EditText) findViewById(R.id.username);
        groupname = (EditText) findViewById(R.id.groupname);
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

        if (TextUtils.isEmpty(username.getText())) {
            Toast.makeText(this, "User name is required", Toast.LENGTH_SHORT);
            username.setError("User name is required!");
        } else if (TextUtils.isEmpty(groupname.getText())) {
            Toast.makeText(this, "Group is required", Toast.LENGTH_SHORT);
            username.setError("Group name is required!");
        } else {
            SaveSharedPreference.setUserName(LoginActivity.this, username.getText().toString());
            SaveSharedPreference.setGroupName(LoginActivity.this, groupname.getText().toString());
            Intent intent = new Intent(this, MapsActivityCurrentPlace.class);
            startActivity(intent);
        }
    }
}
