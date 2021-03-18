package com.sohel.drivermanagement.User;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.TransectionAdapter;
import com.sohel.drivermanagement.User.DataModuler.Transection;

import java.util.ArrayList;
import java.util.List;

public class AllTransectionListFragment extends Fragment {

    public AllTransectionListFragment() {
    }


    private RecyclerView recyclerView;
    TransectionAdapter transectionAdapter;
    private List<Transection> transectionList=new ArrayList<>();


    private DatabaseReference transectionRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_all_transection_list, container, false);

       mAuth=FirebaseAuth.getInstance();
       transectionRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Transection");

       progressDialog=new ProgressDialog(getContext());


        recyclerView=view.findViewById(R.id.transectionList_RecyclerViewid);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

       transectionAdapter=new TransectionAdapter(getContext(),transectionList);

       recyclerView.setAdapter(transectionAdapter);

        return  view;
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
                        transectionList.add(transection);

                        transectionAdapter.notifyDataSetChanged();
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