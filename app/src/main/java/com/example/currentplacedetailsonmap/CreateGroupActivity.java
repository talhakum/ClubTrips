package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CreateGroupActivity extends AppCompatActivity {

    EditText edtTxtGroupName;
    EditText edtTxtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

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

            SaveSharedPreference.setUserName(CreateGroupActivity.this, edtTxtUsername.getText().toString());
            SaveSharedPreference.setGroupName(CreateGroupActivity.this, edtTxtGroupName.getText().toString());

            Intent intent = new Intent(this, ManageGroupActivity.class);
            startActivity(intent);
        }
    }
}
