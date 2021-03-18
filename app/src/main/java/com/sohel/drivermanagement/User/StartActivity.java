package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.LocalDatabase.UserShared;
import com.sohel.drivermanagement.MainActivity;
import com.sohel.drivermanagement.R;

public class StartActivity extends AppCompatActivity {
    private Button homeCreateButton;
    private TextView seeVideoTutorialTextview;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth= FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users");



        homeCreateButton=findViewById(R.id.start_EnterAllRentButton);
        seeVideoTutorialTextview=findViewById(R.id.videoTutorialLink);
        homeCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeCreateButton();
            }
        });


        seeVideoTutorialTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void sendUserToHomeCreateButton() {
        Intent intent=new Intent(StartActivity.this, HomeCreateActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        UserShared userShared=new UserShared(StartActivity.this);
        String value=userShared.isHomeAded();
        if(value.equals("true")){
            sendUserToMainActivity();
        }else{
            userRef.child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String value=snapshot.child("homeAdded").getValue().toString();
                                if(value.equals("true")){
                                    sendUserToMainActivity();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        }




    }

    private void sendUserToMainActivity() {
        Intent intent=new Intent(StartActivity.this,MainBottomNavigationActivity.class);
        finish();
        startActivity(intent);
    }


}