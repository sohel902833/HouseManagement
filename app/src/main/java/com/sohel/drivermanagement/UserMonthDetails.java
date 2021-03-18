package com.sohel.drivermanagement;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class UserMonthDetails {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    int todayDate,todayMonth,todayYear,getBalance;



    public UserMonthDetails(int getBalance){
        this.todayDate=todayDate;
        this.todayMonth=todayMonth;
        this.todayYear=todayYear;
        this.getBalance=getBalance;

        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        Calendar calendar=Calendar.getInstance();
        todayYear=calendar.get(Calendar.YEAR);
        todayMonth=calendar.get(Calendar.MONTH)+1;
        todayDate=calendar.get(Calendar.DAY_OF_MONTH);

    }



  public void saveMontheDetails(){
        userRef.child("data").child(String.valueOf(todayYear))
                .child(String.valueOf(todayMonth)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String getBl=snapshot.child("getBalance").getValue().toString();
                    int prevBalance=Integer.parseInt(getBl);
                    int mainTotal=prevBalance+getBalance;
                    userRef.child("data").child(String.valueOf(todayYear))
                            .child(String.valueOf(todayMonth)).child("getBalance")
                            .setValue(String.valueOf(mainTotal));
                }else{
                    userRef.child("data").child(String.valueOf(todayYear))
                            .child(String.valueOf(todayMonth)).child("getBalance")
                            .setValue(String.valueOf(getBalance));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


  };


}
