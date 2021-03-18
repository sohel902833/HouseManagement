package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sohel.drivermanagement.LocalDatabase.UserShared;
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.SpinnerCustomAdapter;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class HomeCreateActivity extends AppCompatActivity {

    private EditText homeNameEdittext;
    private Button nextButton,doneButton;

    private  String[] floors={
           "1","2","3","4","5","6","7","8","9","10","11","12"
    };
    String homeName,selectedFloor="Select Floor",selectedUnit="Select Unit";
    private Toolbar toolbar;
    AutoCompleteTextView floorAutoComplete;

    //<---------------Firebase-------------------->
    private DatabaseReference homeRef,floorRef,userRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_create);

        toolbar=findViewById(R.id.homeCreateAppbarid);
        setSupportActionBar(toolbar);
        this.setTitle("Home Create");
        mAuth=FirebaseAuth.getInstance();
        homeRef=FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Home");
        floorRef=FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
        userRef=FirebaseDatabase.getInstance().getReference().child("users");
        progressDialog=new ProgressDialog(this);

        homeNameEdittext=findViewById(R.id.homeCreate_HomeNameEdittext);
        floorAutoComplete=findViewById(R.id.floorAutoCompleteText);
       nextButton=findViewById(R.id.homeCreate_NextButtonid);
        doneButton=findViewById(R.id.homeCreate_DoneButtonid);

        ArrayAdapter floorAdapter=new ArrayAdapter(this,R.layout.item_layout,floors);
        floorAutoComplete.setText(floorAdapter.getItem(0).toString(),false);
        floorAutoComplete.setAdapter(floorAdapter);





        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getAllDataFormUser();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeActivity();
                setValueHomeAdedValue();
            }
        });
    }

    private void setValueHomeAdedValue() {
        userRef.child(mAuth.getCurrentUser().getUid())
                .child("homeAdded").setValue("true");
        UserShared userShared=new UserShared(HomeCreateActivity.this);
        userShared.saveData("true");
    }

    private void getAllDataFormUser() {




        homeName=homeNameEdittext.getText().toString();
         selectedFloor=floorAutoComplete.getText().toString();


        if(homeName.isEmpty()){
            homeNameEdittext.setError("Enter Your Home Name");
            homeNameEdittext.requestFocus();
        }else if(selectedFloor.equals("Select Floor")){
            Toasty.warning(HomeCreateActivity.this, "Please Select Floor", Toast.LENGTH_SHORT, true).show();
        }else{
         saveHomeToDatabase();
        }


    }

    private void saveHomeToDatabase() {
        progressDialog.setTitle("Saving Information..");
        progressDialog.show();

        HashMap<String,Object> homeMap=new HashMap<>();
        String homeId=homeRef.push().getKey()+System.currentTimeMillis();
        homeMap.put("homeName",homeName);
        homeMap.put("totalFloor",selectedFloor);
        homeMap.put("totalUnit",selectedUnit);
        homeMap.put("homeId",homeId);

        homeRef.child(homeId)
                .updateChildren(homeMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            for(int i=1; i<=Integer.parseInt(selectedFloor); i++){
                               HashMap<String,Object> floorMap=new HashMap<>();
                               floorMap.put("homeId",homeId);
                               floorMap.put("alotedPerson","none");
                               floorMap.put("aloted","false");
                               floorMap.put("floorName",String.valueOf(i));
                               String floorId=floorRef.push().getKey()+System.currentTimeMillis();
                               floorMap.put("floorId",floorId);
                               floorMap.put("alotedRenterId","none");
                                floorMap.put("waterBill","0");
                                floorMap.put("electricityBill","0");
                                floorMap.put("gasBill","0");
                                floorMap.put("utilitiesBill","0");
                                floorMap.put("rentAmount","0");
                                floorMap.put("waterIncluded","true");
                                floorMap.put("gasIncluded","true");
                                floorMap.put("utilitsIncluded","true");
                                floorMap.put("electricityIncluded","true");
                                floorMap.put("dueAmount","0");
                                floorMap.put("advanced","0");
                                floorRef.child(floorId)
                                       .updateChildren(floorMap)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if(task.isSuccessful()){

                                               } else{
                                                   progressDialog.dismiss();
                                                   Toasty.warning(HomeCreateActivity.this, "Home Create Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                                               }
                                           }
                                       });
                            }

                            homeNameEdittext.setText("");
                            progressDialog.dismiss();
                            Toasty.success(HomeCreateActivity.this, "Home Create Success", Toast.LENGTH_SHORT, true).show();


                        } else{
                            progressDialog.dismiss();
                            Toasty.warning(HomeCreateActivity.this, "Home Create Failed,Please Try Again", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void sendUserToHomeActivity() {
        Intent intent=new Intent(HomeCreateActivity.this,HomeListActivity.class);
        startActivity(intent);
        finish();
    }
}