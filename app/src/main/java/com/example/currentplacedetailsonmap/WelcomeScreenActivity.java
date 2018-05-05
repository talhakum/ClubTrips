package com.example.currentplacedetailsonmap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WelcomeScreenActivity extends AppCompatActivity {
    int emoji;
   // private List<Integer> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);



        final TextView textView1  = (TextView)findViewById(R.id.textView);
        final ImageView imageView1 = (ImageView)findViewById(R.id.imageView);


        String createMi = getIntent().getExtras().getString("STRING_I_NEED","hata");
        String loginMi = getIntent().getExtras().getString("STRING_I_NEED","hata");

        if( createMi.equals("fromCreate")){
            Log.d("Nereden","Create");

            String name = SaveSharedPreference.getUserName(WelcomeScreenActivity.this);
            int emoji = SaveSharedPreference.getEmojiId(WelcomeScreenActivity.this);

            textView1.setText(name);

            if(emoji==0){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster0));
            }
            else if(emoji==1){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster1));
            }
            else if(emoji==2){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster2));
            }
            else if(emoji==3){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster3));
            }
            else if(emoji==4){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster4));
            }
            else if(emoji==5){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster5));
            }
            else if(emoji==6){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster6));
            }
            else if(emoji==7){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster7));
            }
            else if(emoji==8){
                imageView1.setImageDrawable(getDrawable(R.drawable.monster8));
            }

        }else if(loginMi.equals("fromLogin")){
            Log.d("Nereden","Login");

            final String name = SaveSharedPreference.getUserName(WelcomeScreenActivity.this);


            FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference myRef = database.getReference("locations").child(SaveSharedPreference.getGroupName(WelcomeScreenActivity.this).toString()).child("emoji").child("id");

            Log.d("NeredeTest","listenerdan once");

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                   int emoji1 = dataSnapshot.getValue(int.class);
                    Log.d("emojiTest", String.valueOf(emoji1));
                //    list.add(emoji1);
                 //   Log.d("emojiTestt", String.valueOf(list.get(0)));

                    emoji = emoji1;
                    textView1.setText(name);
                    // emoji=list.get(0);
                    Log.d("emojiTest1", String.valueOf(emoji));
                    if(emoji==0){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster0));
                    }
                    else if(emoji==1){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster1));
                    }
                    else if(emoji==2){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster2));
                    }
                    else if(emoji==3){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster3));
                    }
                    else if(emoji==4){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster4));
                    }
                    else if(emoji==5){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster5));
                    }
                    else if(emoji==6){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster6));
                    }
                    else if(emoji==7){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster7));
                    }
                    else if(emoji==8){
                        imageView1.setImageDrawable(getDrawable(R.drawable.monster8));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }




        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {



                    Log.d("tag","sleep");
                    sleep(3600);
                    Intent intent = new Intent(getApplicationContext(),ManageGroupActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
