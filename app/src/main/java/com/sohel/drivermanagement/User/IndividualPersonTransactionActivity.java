package com.sohel.drivermanagement.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.LoadingDialog;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.RenterListAdapter;
import com.sohel.drivermanagement.User.Adapter.TransectionAdapter;
import com.sohel.drivermanagement.User.DataModuler.RenterList;
import com.sohel.drivermanagement.User.DataModuler.Transection;

import java.util.ArrayList;
import java.util.List;

public class IndividualPersonTransactionActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    TransectionAdapter transectionAdapter;
    private List<Transection> transectionList=new ArrayList<>();


    private DatabaseReference transectionRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;


    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_person_transaction);

        userId=getIntent().getStringExtra("userId");

        mAuth=FirebaseAuth.getInstance();
        transectionRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Transection");

        progressDialog=new ProgressDialog(this);

        recyclerView=findViewById(R.id.inividul_transaction_list_RecyclerViewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        transectionAdapter=new TransectionAdapter(this,transectionList);
        recyclerView.setAdapter(transectionAdapter);



    }

    @Override
    public void onStart() {
        super.onStart();

        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        transectionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    transectionList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Transection transection=snapshot1.getValue(Transection.class);
                        if(transection.getPersonId().equals(userId)){
                            transectionList.add(transection);
                            transectionAdapter.notifyDataSetChanged();
                        }
                   }
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


}