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
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.DataModuler.FloorList;
import com.sohel.drivermanagement.User.DataModuler.FloorList2;
import com.sohel.drivermanagement.UserMonthDetails;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FloorAloteActivity extends AppCompatActivity {
    private TextView floorAloteDetailsTextview;
    private EditText renterNameEdittext,renterPhoneNumberEdittext,advanceEdittext,rentAmountEdittext;
    private EditText waterBillEdittext,gasBillEdittext,electricityBillEditext,utilitesBillEdittext;
    private RadioGroup waterRadioGroup,gasRadioGroup,electricityRadioGroup,utilitiesRadioGroup;
    private TextView renterDateTextview,paidDateTextview,alarmTimeTextviewid;



    private String renterName,renterPhoneNumber="",renterDue="0",renterAdvance="0",rentAmount="0",waterBill="0",gasBill="0",electricityBill="0",utilitesBill="0";
    private Button saveAloteButton;
    private CheckBox alarmCheckBox;

    String homeName,floorName,floorId,homeId;

    boolean gasEx=true,waterEx=true,electriciyEx=true,utilitsEx=true;

    private DatabaseReference renterRef,floorRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    DatePickerDialog.OnDateSetListener setListener;

    private  int todayDate,todayMonth,todayYear;
    private  int renterDate,renterMonth,renterYear;
    private  int alarmDate=0,alarmMonth=-1,alarmYear=0;
    private  int alarmHour=0,alarmMin=0;
    private String timeTonotify;


    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floor_alote);

        toolbar=findViewById(R.id.floorAloteAppBarid);
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);

        homeName=getIntent().getStringExtra("homeName");
        homeId=getIntent().getStringExtra("homeId");
        floorName=getIntent().getStringExtra("floorName");
        floorId=getIntent().getStringExtra("floorId");


        this.setTitle(homeName+"("+floorName+")");
        // saveAloteButton.setText("Save And Alote Floor "+(floorCounter+2));


        mAuth=FirebaseAuth.getInstance();
        floorRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
        renterRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Renter");

        init();
        floorAloteDetailsTextview.setText("House Name : "+homeName+"\n Floor "+floorName+".  Alote To");
        saveAloteButton.setOnClickListener(new View.OnClickListener() {
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



             /*   String value = (paidDateTextview.getText().toString().trim());
                String date = (paidDateTextview.getText().toString().trim());
                String time = (alarmTimeTextviewid.getText().toString().trim());
                setAlarm( date, time);
*/

                 validate();
            }
        });
        renterDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        FloorAloteActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        renterDate=day;
                        renterYear=year;
                        renterMonth=month;
                        renterDateTextview.setText(renterDate+"/"+renterMonth+"/"+renterYear);
                    }
                },todayYear,todayMonth,todayDate
                );
                datePickerDialog.show();
            }
        });
        paidDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAlarmDate();
              /*  DatePickerDialog datePickerDialog=new DatePickerDialog(
                        FloorAloteActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        alarmTimeTextviewid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });


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
                alarmTimeTextviewid.setText(FormatTime(i, i1));
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



    private void validate() {

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


            if(alarmCheckBox.isChecked()){
                if(alarmDate==0 || alarmMonth==-1 || alarmYear==0 || alarmHour==0 || alarmMin==0){
                    Toasty.warning(FloorAloteActivity.this, "Please Select Alarm Date", Toast.LENGTH_SHORT, true).show();
                }else{

                    String value = (paidDateTextview.getText().toString().trim());
                    String date = (paidDateTextview.getText().toString().trim());
                    String time = (alarmTimeTextviewid.getText().toString().trim());
                    setAlarm( date, time);
                    saveRenterDetails();

                }
            }else {
                saveRenterDetails();
            }



        }
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
    private void saveRenterDetails() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Saving Your Renter Details");
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
        renterMap.put("floorName",floorName);
        renterMap.put("homeName",homeName);
        renterMap.put("enterDate",todayDate+"/"+todayMonth+1+"/"+todayYear);
        renterMap.put("paidDate",paidDateTextview.getText().toString().trim());

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
                                                Toasty.success(FloorAloteActivity.this, "Floor Aloted Success!", Toast.LENGTH_SHORT, true).show();
                                            }
                                        }
                                    });
                        }else{
                            progressDialog.dismiss();
                            Toasty.warning(FloorAloteActivity.this, "Floor Alote Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
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

    public void sendUserToFloorListActivity(){
        Intent intent=new Intent(FloorAloteActivity.this,FloorListActivity.class);
        intent.putExtra("homeName",homeName);
        intent.putExtra("homeId",homeId);
        startActivity(intent);
        finish();
    }


    private void init() {
        floorAloteDetailsTextview=findViewById(R.id.floorAlote_DetailsTextviewid);
        renterNameEdittext=findViewById(R.id.floorAlote_RenterNameEdittextid);
        renterPhoneNumberEdittext=findViewById(R.id.floatAlote_RenterPhoneNumberid);
        advanceEdittext=findViewById(R.id.floorAlote_AdvanceEdittextied);
        rentAmountEdittext=findViewById(R.id.floorAlote_RentAmountEdittextied);
        waterBillEdittext=findViewById(R.id.floorAlote_WaterEdittextid);
        gasBillEdittext=findViewById(R.id.floorAlote_GasEdittextid);
        electricityBillEditext=findViewById(R.id.floorAlote_ElectricityEdittextid);
        utilitesBillEdittext=findViewById(R.id.floorAlote_UtilitsEdittextid);
        saveAloteButton=findViewById(R.id.saveAloteButton);
        alarmTimeTextviewid=findViewById(R.id.floorAlote_AlarmTimeTextviewid);

        renterDateTextview=findViewById(R.id.floorAlote_renterDateTextviewid);
        paidDateTextview=findViewById(R.id.floorAlote_RentPaidLimitDateTextView);
        alarmCheckBox=findViewById(R.id.alarmCheckSwitchId);

        Calendar calendar=Calendar.getInstance();
        todayYear=calendar.get(Calendar.YEAR);
        todayMonth=calendar.get(Calendar.MONTH);
        todayDate=calendar.get(Calendar.DAY_OF_MONTH);

        renterDateTextview.setText(todayDate+"/"+(todayMonth+1)+"/"+todayYear);

        waterRadioGroup=findViewById(R.id.water_RadioGroup);
        electricityRadioGroup=findViewById(R.id.electriciy_RadioGroup);
        gasRadioGroup=findViewById(R.id.gas_RadioGroup);
        utilitiesRadioGroup=findViewById(R.id.utilities_RadioGroup);
    }
}