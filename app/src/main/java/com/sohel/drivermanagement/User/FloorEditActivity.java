package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.AlarmBrodcast;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.FloorList2;
import com.sohel.drivermanagement.User.DataModuler.RenterList;
import com.sohel.drivermanagement.UserMonthDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class FloorEditActivity extends AppCompatActivity {


    private TextView floorAloteDetailsTextview;
    private EditText renterNameEdittext,renterPhoneNumberEdittext,advanceEdittext,rentAmountEdittext;
    private EditText waterBillEdittext,gasBillEdittext,electricityBillEditext,utilitesBillEdittext;
    private RadioGroup waterRadioGroup,gasRadioGroup,electricityRadioGroup,utilitiesRadioGroup;
    private TextView renterDateTextview,paidDateTextview,paidTimeTextview;




    private String renterName,renterPhoneNumber="",renterDue="0",renterAdvance="0",rentAmount="0",waterBill="0",gasBill="0",electricityBill="0",utilitesBill="0";
    private Button updateValueButton,updateRenterButton;
    private CheckBox alarmCheckBox;

    String homeName,floorName,floorId,homeId;

    boolean gasEx=true,waterEx=true,electriciyEx=true,utilitsEx=true;

    private DatabaseReference renterRef,floorRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener setListener;

    private  int todayDate,todayMonth,todayYear;
    private  int renterDate,renterMonth,renterYear;
    private  int alarmDate=0,alarmMonth=-1,alarmYear=0,alarmHour=0,alarmMin;


    FloorList2 floor;
    RenterList renter;
    String timeTonotify;

    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_edit);


        toolbar=findViewById(R.id.floorEdit_ToolbarId);
        setSupportActionBar(toolbar);

        floorId=getIntent().getStringExtra("floorId");
        homeName=getIntent().getStringExtra("homeName");

        this.setTitle("Edit Floor("+homeName+")");


        init();
        progressDialog=new ProgressDialog(this);
        updateValueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int waterSelectedId = waterRadioGroup.getCheckedRadioButtonId();
                int utilitSelectedId = utilitiesRadioGroup.getCheckedRadioButtonId();
                int electricitySelectedId=electricityRadioGroup.getCheckedRadioButtonId();
                int gasSelectedId=gasRadioGroup.getCheckedRadioButtonId();
                RadioButton waterRadioButton = (RadioButton) findViewById(waterSelectedId);
                RadioButton electricityRadioButton = (RadioButton) findViewById(electricitySelectedId);
                RadioButton gasRadioButton = (RadioButton) findViewById(gasSelectedId);
                RadioButton utilitiRadioButton = (RadioButton) findViewById(utilitSelectedId);

                waterBill=waterBillEdittext.getText().toString();
                gasBill=gasBillEdittext.getText().toString();
                electricityBill=electricityBillEditext.getText().toString();
                utilitesBill=utilitesBillEdittext.getText().toString();


                waterEx=isIncluded(waterRadioButton.getText().toString());
                gasEx=isIncluded(gasRadioButton.getText().toString());
                electriciyEx=isIncluded(electricityRadioButton.getText().toString());
                utilitsEx=isIncluded(utilitiRadioButton.getText().toString());
                renterName=renterNameEdittext.getText().toString();
                renterPhoneNumber=renterPhoneNumberEdittext.getText().toString();
                renterAdvance=advanceEdittext.getText().toString();
                rentAmount=rentAmountEdittext.getText().toString();


                validate(1);
            }
        });
         updateRenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int waterSelectedId = waterRadioGroup.getCheckedRadioButtonId();
                int utilitSelectedId = utilitiesRadioGroup.getCheckedRadioButtonId();
                int electricitySelectedId=electricityRadioGroup.getCheckedRadioButtonId();
                int gasSelectedId=gasRadioGroup.getCheckedRadioButtonId();
                RadioButton waterRadioButton = (RadioButton) findViewById(waterSelectedId);
                RadioButton electricityRadioButton = (RadioButton) findViewById(electricitySelectedId);
                RadioButton gasRadioButton = (RadioButton) findViewById(gasSelectedId);
                RadioButton utilitiRadioButton = (RadioButton) findViewById(utilitSelectedId);

                waterBill=waterBillEdittext.getText().toString();
                gasBill=gasBillEdittext.getText().toString();
                electricityBill=electricityBillEditext.getText().toString();
                utilitesBill=utilitesBillEdittext.getText().toString();


                waterEx=isIncluded(waterRadioButton.getText().toString());
                gasEx=isIncluded(gasRadioButton.getText().toString());
                electriciyEx=isIncluded(electricityRadioButton.getText().toString());
                utilitsEx=isIncluded(utilitiRadioButton.getText().toString());
                renterName=renterNameEdittext.getText().toString();
                renterPhoneNumber=renterPhoneNumberEdittext.getText().toString();
                renterAdvance=advanceEdittext.getText().toString();
                rentAmount=rentAmountEdittext.getText().toString();
                validate(2);
            }
        });



        renterDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        FloorEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        renterDate=day;
                        renterYear=year;
                        renterMonth=month+1;
                        renterDateTextview.setText(renterDate+"/"+renterMonth+"/"+renterYear);
                    }
                },todayYear,todayMonth,todayDate
                );
                datePickerDialog.show();
            }
        });


        paidTimeTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });

        paidDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAlarmDate();

           /*     DatePickerDialog datePickerDialog=new DatePickerDialog(
                        FloorEditActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        alarmDate=day;
                        alarmYear=year;
                        alarmMonth=month+1;
                        paidDateTextview.setText(alarmDate+"/"+alarmMonth+"/"+alarmYear);
                    }
                },todayYear,todayMonth,todayDate
                );
                datePickerDialog.show();*/
            }
        });




    }
    private void selectAlarmDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                paidDateTextview.setText(day + "-" + (month + 1) + "-" + year);
                alarmYear=year;
                alarmMonth=month;
                alarmDate=day;
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void validate(int check) {

        if(renterName.isEmpty()){
            renterNameEdittext.setError("Write Renter Name");
            renterNameEdittext.requestFocus();
        }else if(rentAmount.isEmpty()){
            rentAmountEdittext.setError("Please Write Rent amount");
            rentAmountEdittext.requestFocus();
        }else{
            waterBill= waterBill.isEmpty()?"0":waterBill;
            electricityBill= electricityBill.isEmpty()?"0":electricityBill;
            gasBill= gasBill.isEmpty()?"0":gasBill;
            utilitesBill = utilitesBill.isEmpty() ? "0" : utilitesBill;
            renterDue=String.valueOf(rentAmount);
            renterAdvance=renterAdvance.isEmpty()?"0":renterAdvance;

            if(check==1){
                if(alarmCheckBox.isChecked()){
                    if(alarmDate==0 || alarmMonth==-1 || alarmYear==0 || alarmHour==0){
                        Toasty.warning(FloorEditActivity.this, "Please Select Alarm Date And Time", Toast.LENGTH_SHORT, true).show();
                    }else{
                        String alarmDateString=alarmDate+"/"+alarmMonth+1+"/"+alarmYear;
                        String currentDateString=todayDate+"/"+todayMonth+1+"/"+todayYear;
                        String date = (paidDateTextview.getText().toString().trim());
                        String time = (paidTimeTextview.getText().toString().trim());
                        setAlarm( date, time);
                        updateRenterDetails(alarmDateString,currentDateString);

                    }
                }else{
                    String currentDateString=todayDate+"/"+todayMonth+1+"/"+todayYear;
                   updateRenterDetails("none",currentDateString);

                }
           }else if(check==2){
                if(alarmCheckBox.isChecked()){
                    if(alarmDate==0 || alarmMonth==-1 || alarmYear==0 || alarmHour==0){
                        Toasty.warning(FloorEditActivity.this, "Please Select Alarm Date And Time", Toast.LENGTH_SHORT, true).show();
                    }else{
                        String alarmDateString=alarmDate+"/"+alarmMonth+1+"/"+alarmYear;
                        String currentDateString=todayDate+"/"+todayMonth+1+"/"+todayYear;
                        String date = (paidDateTextview.getText().toString().trim());
                        String time = (paidTimeTextview.getText().toString().trim());
                        setAlarm( date, time);
                        saveRenterDetails(alarmDateString,currentDateString);
                    }
                }else{
                    String currentDateString=todayDate+"/"+todayMonth+1+"/"+todayYear;
                    saveRenterDetails("none",currentDateString);

                }

            }
        }




    }


    private void setAlarm(String date, String time) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", "Md Sohrab Hossain Toal Due Balance: 4000");
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("floorId", floorId);
        intent.putExtra("homeName", homeName);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        String dateandtime = date + " " + timeTonotify;
        DateFormat formatter = new SimpleDateFormat("d-M-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(dateandtime);
            am.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                alarmMin=i1;
                alarmHour=i;
                timeTonotify = i + ":" + i1;
                paidTimeTextview.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();
    }

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    public boolean isIncluded(String value){
        boolean check=true;
        if(value.equals("Include")) {
            check=true;
        }
        else if(value.equals("Exclude")){
            check= false;
        }
        return  check;
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
                            getPersonDetails();
                        }else{
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getPersonDetails() {
        renterRef.child(floor.getAlotedRenterId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                             renter=snapshot.getValue(RenterList.class);
                             submitValue();
                             progressDialog.dismiss();
                        }else{
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void saveRenterDetails(String paidDate,String enterDate) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating Floor Data");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        String renterUserId=renterRef.push().getKey()+System.currentTimeMillis();
        HashMap<String,Object> renterMap=new HashMap<>();
        renterMap.put("renterId",renterUserId);
        renterMap.put("homeId",homeId);
        renterMap.put("name",renterName);
        renterMap.put("phone",renterPhoneNumber);
        renterMap.put("floorId",floorId);
        renterMap.put("advanced",renterAdvance);
        renterMap.put("dueAmount",renterDue);
        renterMap.put("floorName",floor.getFloorName());
        renterMap.put("homeName",homeName);
        renterMap.put("enterDate",enterDate);
        renterMap.put("paidDate",paidDate);

        UserMonthDetails userMonthDetails=new UserMonthDetails(Integer.parseInt(renterAdvance));
        userMonthDetails.saveMontheDetails();




        HashMap<String,Object> floorMap=new HashMap<>();
        floorMap.put("alotedPerson",renterName);
        floorMap.put("alotedRenterId",renterUserId);
        floorMap.put("waterBill",waterBill);
        floorMap.put("electricityBill",electricityBill);
        floorMap.put("gasBill",gasBill);
        floorMap.put("utilitiesBill",utilitesBill);
        floorMap.put("rentAmount",rentAmount);
        floorMap.put("waterIncluded",String.valueOf(waterEx));
        floorMap.put("aloted","true");
        floorMap.put("gasIncluded",String.valueOf(gasEx));
        floorMap.put("utilitsIncluded",String.valueOf(utilitsEx));
        floorMap.put("electricityIncluded",String.valueOf(electriciyEx));
        floorMap.put("dueAmount",renterDue);
        floorMap.put("advanced",renterAdvance);
        floorMap.put("enterDate",enterDate);
        floorMap.put("paidDate",paidDate);
        renterRef.child(floor.getAlotedRenterId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            renterRef.child(renterUserId)
                                    .setValue(renterMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                floorRef.child(floorId)
                                                        .updateChildren(floorMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    sendUserToFloorListActivity();
                                                                    progressDialog.dismiss();
                                                                    Toasty.success(FloorEditActivity.this, "Floor Aloted Success!", Toast.LENGTH_SHORT, true).show();
                                                                }
                                                            }
                                                        });
                                            }else{
                                                progressDialog.dismiss();
                                                Toasty.warning(FloorEditActivity.this, "Floor Alote Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                                    });
                        }
                    }
                });

    }

    public void submitValue(){
        renterNameEdittext.setText(floor.getAlotedPerson());
        floorAloteDetailsTextview.setText(homeName+",Floor: "+floor.getFloorName()+" Aloteted to "+floor.getAlotedPerson());
        renterPhoneNumberEdittext.setText(renter.getPhone());
        rentAmountEdittext.setText(floor.getRentAmount());
        waterBillEdittext.setText(floor.getWaterBill());
        gasBillEdittext.setText(floor.getGasBill());
        electricityBillEditext.setText(floor.getElectricityBill());
        utilitesBillEdittext.setText(floor.getUtilitiesBill());


        if(floor.getWaterIncluded().equals("false")){
            utilitiesRadioGroup.check(R.id.floorEdit_utilities_Exclude_RadioButton);
        }
        if(floor.getElectricityIncluded().equals("false")){
            electricityRadioGroup.check(R.id.floorEdit_electriciy_Exclude_RadioButton);
        }
        if(floor.getGasIncluded().equals("false")){
            gasRadioGroup.check(R.id.floorEdit_gas_Exclude_RadioButton);
        }
        if(floor.getUtilitsIncluded().equals("false")){
            utilitiesRadioGroup.check(R.id.floorEdit_utilities_Exclude_RadioButton);
        }
    }
    private void init() {
        mAuth=FirebaseAuth.getInstance();
        floorRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
        renterRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Renter");



        floorAloteDetailsTextview=findViewById(R.id.floorEdit_DetailsTextviewid);
        renterNameEdittext=findViewById(R.id.floorEdit_RenterNameEdittextid);
        renterPhoneNumberEdittext=findViewById(R.id.floorEdit_RenterPhoneNumberid);
        advanceEdittext=findViewById(R.id.floorEdit_AdvanceEdittextied);
        rentAmountEdittext=findViewById(R.id.floorEdit_RentAmountEdittextied);
        waterBillEdittext=findViewById(R.id.floorEdit_WaterEdittextid);
        gasBillEdittext=findViewById(R.id.floorEdit_GasEdittextid);
        electricityBillEditext=findViewById(R.id.floorEdit_ElectricityEdittextid);
        utilitesBillEdittext=findViewById(R.id.floorEdit_UtilitsEdittextid);
        updateValueButton=findViewById(R.id.floorEdit_updateValue);
        updateRenterButton=findViewById(R.id.floorEdit_UpdateRenterButton);
        paidTimeTextview=findViewById(R.id.floorEdit_RentPaidTimeTextviewid);


        renterDateTextview=findViewById(R.id.floorEdit_renterDateTextviewid);
        paidDateTextview=findViewById(R.id.floorEdit_RentPaidLimitDateTextView);
        alarmCheckBox=findViewById(R.id.floorEdit_alarmCheckSwitchId);

        Calendar calendar=Calendar.getInstance();
        todayYear=calendar.get(Calendar.YEAR);
        todayMonth=calendar.get(Calendar.MONTH);
        todayDate=calendar.get(Calendar.DAY_OF_MONTH);

        renterDateTextview.setText(todayDate+"/"+(todayMonth+1)+"/"+todayYear);

        waterRadioGroup=findViewById(R.id.floorEdit_water_RadioGroup);
        electricityRadioGroup=findViewById(R.id.floorEdit_electriciy_RadioGroup);
        gasRadioGroup=findViewById(R.id.floorEdit_gas_RadioGroup);
        utilitiesRadioGroup=findViewById(R.id.floorEdit_utilities_RadioGroup);










     }
    private void updateRenterDetails(String alarmDate,String currentDate) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating Floor Details");
        progressDialog.setTitle("Please Wait..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        HashMap<String,Object> renterMap=new HashMap<>();
        renterMap.put("homeId",homeId);
        renterMap.put("name",renterName);
        renterMap.put("phone",renterPhoneNumber);
        renterMap.put("floorId",floorId);
        renterMap.put("advanced",renterAdvance);
        renterMap.put("dueAmount",renterDue);
        renterMap.put("floorName",floorName);
        renterMap.put("homeName",homeName);
        renterMap.put("enterDate",currentDate);
        renterMap.put("paidDate",alarmDate);


        UserMonthDetails userMonthDetails=new UserMonthDetails(Integer.parseInt(renterAdvance));
        userMonthDetails.saveMontheDetails();




        HashMap<String,Object> floorMap=new HashMap<>();
        floorMap.put("alotedPerson",renterName);
        floorMap.put("alotedRenterId",floor.getAlotedRenterId());
        floorMap.put("waterBill",waterBill);
        floorMap.put("electricityBill",electricityBill);
        floorMap.put("gasBill",gasBill);
        floorMap.put("utilitiesBill",utilitesBill);
        floorMap.put("rentAmount",rentAmount);
        floorMap.put("waterIncluded",String.valueOf(waterEx));
        floorMap.put("aloted","true");
        floorMap.put("gasIncluded",String.valueOf(gasEx));
        floorMap.put("utilitsIncluded",String.valueOf(utilitsEx));
        floorMap.put("electricityIncluded",String.valueOf(electriciyEx));
        floorMap.put("dueAmount",renterDue);
        floorMap.put("advanced",renterAdvance);
        floorMap.put("enterDate",currentDate);
        floorMap.put("paidDate",alarmDate);


        renterRef.child(renter.getRenterId())
                .updateChildren(renterMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            floorRef.child(floorId)
                                    .updateChildren(floorMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                sendUserToFloorListActivity();
                                                progressDialog.dismiss();
                                                Toasty.success(FloorEditActivity.this, "Floor Updated Success!", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                                    });
                        }else{
                            progressDialog.dismiss();
                            Toasty.warning(FloorEditActivity.this, "Floor Updated Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    public void sendUserToFloorListActivity(){
        Intent intent=new Intent(FloorEditActivity.this,FloorListActivity.class);
        intent.putExtra("homeName",homeName);
        intent.putExtra("homeId",floor.getHomeId());
        startActivity(intent);
        finish();
    }


}