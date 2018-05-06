package com.example.currentplacedetailsonmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class PopUp extends Activity {

    int selectedImagePath = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("popup","Pop up acildi");
        setContentView(R.layout.pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width* .8) ,(int) (height*.6));

        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(PopUp.this, "" + position,
                        Toast.LENGTH_SHORT).show();
                selectedImagePath = position;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("imagePath", selectedImagePath);
                setResult(RESULT_OK,returnIntent);
                finish();
            }


        });



    }



}
