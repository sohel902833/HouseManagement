package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.FloorList;
import com.sohel.drivermanagement.User.DataModuler.FloorList2;
import com.sohel.drivermanagement.UserMonthDetails;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class FloorPaymentActivity extends AppCompatActivity {

    private EditText paymentEdittext;
    private TextView detailsTextView,
            renterNameTextview,
            dueBalanceTextView,
            gasTextView,
            electricityTextView,
            paymentTextview,
            advanceTextView,
            rentAmountTextView,
            waterTextview,
            utilsTextview;

    private Button paymentButton;

    String floorId,homeName;
    private FirebaseAuth mAuth;
    private DatabaseReference floorRef,renterRef,transectionRef;


    FloorList2 floor;
    int calculatedDueBalance;

    private ProgressDialog progressDialog;

    String payment;
    int newDue=0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_payment);


        toolbar=findViewById(R.id.floorPaymentAppBarId);
        setSupportActionBar(toolbar);

        floorId=getIntent().getStringExtra("floorId");
        homeName=getIntent().getStringExtra("homeName");
        mAuth=FirebaseAuth.getInstance();
        floorRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
        renterRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Renter");
        transectionRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Transection");
        progressDialog=new ProgressDialog(this);


        init();

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 payment=paymentEdittext.getText().toString();

                 if(payment.isEmpty()){
                     Toasty.warning(FloorPaymentActivity.this, "Enter Payment", Toast.LENGTH_SHORT, true).show();
                 }else {


                     int p = Integer.parseInt(payment);
                     if (calculatedDueBalance == 0) {
                         Toasty.warning(FloorPaymentActivity.this, "No Payment Due For Paid", Toast.LENGTH_SHORT, true).show();
                     } else if (p == calculatedDueBalance) {
                         int newDue = 0;
                         updateDatabase(newDue);
                     }
                     else if (p < calculatedDueBalance) {
                         int newDue =calculatedDueBalance-p;
                         updateDatabase(newDue);
                     }else if(p>calculatedDueBalance){
                         int newDue = 0;
                         updateDatabase(newDue);

                     }

                 }


               /*     if(prevAdvancedBalance>0){
                        int totalGivenBalance=prevAdvancedBalance+paymentBalance;
                        if(totalGivenBalance>calculatedDueBalance){
                            newDue=0;
                            newAdvance=totalGivenBalance-calculatedDueBalance;
                        }else if(calculatedDueBalance>totalGivenBalance){
                            newDue=calculatedDueBalance-totalGivenBalance;
                            newAdvance=0;
                        }else if(calculatedDueBalance==totalGivenBalance){
                            newDue=0;
                            newAdvance=0;
                        }
                    }else{
                        if(paymentBalance>calculatedDueBalance){
                            newAdvance=paymentBalance-calculatedDueBalance;
                        }else if(paymentBalance<calculatedDueBalance){
                            newDue=calculatedDueBalance-paymentBalance;
                        }
                    }*/


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        floorRef.child(floorId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                             floor=snapshot.getValue(FloorList2.class);
                             submitValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





    }
    private void init() {
        detailsTextView=findViewById(R.id.floorPayment_DetailsTextViewid);
        renterNameTextview=findViewById(R.id.floorPayment_RenterNameTextviewid);
        rentAmountTextView=findViewById(R.id.floorPayment_RentBalanceTextviewid);
        advanceTextView=findViewById(R.id.floorPayment_RentAdvancedTextviewid);
        gasTextView=findViewById(R.id.floorPayment_gasTextviewid);
        electricityTextView=findViewById(R.id.floorPayment_ElectriciyTextviewid);
        waterTextview=findViewById(R.id.floorPayment_WaterTextviewid);
        utilsTextview=findViewById(R.id.floorPayment_UtilsTextviewid);
        dueBalanceTextView=findViewById(R.id.floorPayment_previousDueTextviewid);
        paymentEdittext=findViewById(R.id.floorPayment_paymentEdittextid);
        paymentButton=findViewById(R.id.floorPaymentButtonid);
    }
    public void submitValue(){

       int prevDueBalance= Integer.parseInt(floor.getDueAmount());
        calculatedDueBalance=prevDueBalance;


        this.setTitle("Payment Floor :"+floor.getFloorName()+"("+homeName+")");

        detailsTextView.setText(homeName+" Floor: "+floor.getFloorName());
        renterNameTextview.setText("Renter Name: "+floor.getAlotedPerson());
        rentAmountTextView.setText("Rent Amount: "+floor.getRentAmount());
        advanceTextView.setText("Advanced: "+floor.getAdvanced());
        gasTextView.setText("Gas:"+floor.getGasBill()+" ("+getStringToBoolean(floor.getGasIncluded())+")");
        electricityTextView.setText("Electricity:"+floor.getElectricityBill()+" ("+getStringToBoolean(floor.getElectricityIncluded())+")");
        waterTextview.setText("Water:"+floor.getWaterBill()+" ("+getStringToBoolean(floor.getWaterIncluded())+")");
        utilsTextview.setText("Utills:"+floor.getUtilitiesBill()+" ("+getStringToBoolean(floor.getUtilitsIncluded())+")");


        if(Integer.parseInt(floor.getDueAmount())>0){
            if(floor.getUtilitsIncluded().equals("false")){
                calculatedDueBalance+=Integer.parseInt(floor.getUtilitiesBill());
            }
            if(floor.getWaterIncluded().equals("false")){
                calculatedDueBalance+=Integer.parseInt(floor.getWaterBill());
            }
            if(floor.getElectricityIncluded().equals("false")){
                calculatedDueBalance+=Integer.parseInt(floor.getElectricityBill());
            }
            if(floor.getGasIncluded().equals("false")){
                calculatedDueBalance+=Integer.parseInt(floor.getGasBill());
            }
        }
        else{
            calculatedDueBalance=0;
        }

       dueBalanceTextView.setText("Due: "+calculatedDueBalance);

        progressDialog.dismiss();




    }
    public String getStringToBoolean(String txt){
        String d="";
        if(txt.equals("true")){
            d= "Included";
        }else if(txt.equals("false")){
            d="Excluded";
        }
        return d;
    }


    public void updateDatabase(int nDueAmount){
        progressDialog.setMessage("Update Payment");
        progressDialog.show();

        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyy");
        String todayDate=currentDate.format(calForDate.getTime());

        Calendar callForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");
        String todayTime=currentTime.format(callForTime.getTime());





        HashMap<String,Object> updatePayment=new HashMap<>();
       updatePayment.put("dueAmount",String.valueOf(nDueAmount));

        String transactionId=floor.getFloorId()+System.currentTimeMillis();

        HashMap<String,Object> transactionMap=new HashMap<>();
        transactionMap.put("transactionId",transactionId);
        transactionMap.put("personId",floor.getAlotedRenterId());
        transactionMap.put("PersonName",floor.getAlotedPerson());
        transactionMap.put("floorId",floor.getFloorId());
        transactionMap.put("payment",payment);
        transactionMap.put("date",todayDate);
        transactionMap.put("time",todayTime);

        floorRef.child(floorId).updateChildren(updatePayment)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        renterRef.child(floor.getAlotedRenterId()).updateChildren(updatePayment)
                                .addOnCompleteListener(task1 -> {
                                    if(task1.isSuccessful()){
                                        transectionRef.child(transactionId).setValue(transactionMap)
                                                        .addOnCompleteListener(task11 -> {
                                                                if(task11.isSuccessful()){
                                                                    UserMonthDetails userMonthDetails=new UserMonthDetails(Integer.parseInt(payment));
                                                                    userMonthDetails.saveMontheDetails();
                                                                    progressDialog.dismiss();
                                                                    sendUserToFloorListActivity();
                                                                    Toasty.success(FloorPaymentActivity.this, "Payment Success!", Toast.LENGTH_SHORT, true).show();
                                                                }else{
                                                                    progressDialog.dismiss();
                                                                    Toasty.warning(FloorPaymentActivity.this, "Floor Payment Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                                                                }
                                                        });
                                   }else{
                                        progressDialog.dismiss();
                                        Toasty.warning(FloorPaymentActivity.this, "Floor Payment Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                                    }
                                });
                    }else{
                        progressDialog.dismiss();
                        Toasty.warning(FloorPaymentActivity.this, "Floor Payment Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                    }
                });



    }

    public void sendUserToFloorListActivity(){
        Intent intent=new Intent(FloorPaymentActivity.this,FloorListActivity.class);
        intent.putExtra("homeName",homeName);
        intent.putExtra("homeId",floor.getHomeId());
        startActivity(intent);
        finish();
    }


}