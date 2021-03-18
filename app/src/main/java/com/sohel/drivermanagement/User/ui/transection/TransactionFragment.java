package com.sohel.drivermanagement.User.ui.transection;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.LoadingDialog;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.HomeListAdapter;
import com.sohel.drivermanagement.User.DataModuler.HomeList;
import com.sohel.drivermanagement.User.FloorListActivity;
import com.sohel.drivermanagement.User.HomeListActivity;
import com.sohel.drivermanagement.User.MainBottomNavigationActivity;

import java.util.ArrayList;
import java.util.List;


public class TransactionFragment extends Fragment {
    private RecyclerView homeListRecyclerview;
    private HomeListAdapter homeListAdapter;
    private List<HomeList> homeDataList=new ArrayList<>();

    private DatabaseReference homeRef;
    private FirebaseAuth mAuth;

    LoadingDialog loadingDialog;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_transaction, container, false);


        loadingDialog=new LoadingDialog(getContext());
        loadingDialog.showLoadingDiolouge();
        mAuth=FirebaseAuth.getInstance();
        homeRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Home");

        homeListRecyclerview=root.findViewById(R.id.f_Main_HomeLIstRecyclerviewid);
       homeListRecyclerview.setHasFixedSize(true);
        homeListRecyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        homeListAdapter=new HomeListAdapter(getContext(),homeDataList);
        homeListRecyclerview.setAdapter(homeListAdapter);

        homeListAdapter.setOnItemClickListner(new HomeListAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                HomeList currentItem=homeDataList.get(position);

                Intent intent=new Intent(getContext(), FloorListActivity.class);
                intent.putExtra("homeName",currentItem.getHomeName());
                intent.putExtra("homeId",currentItem.getHomeId());
                startActivity(intent);
            }
        });


        return root;
    }


    private void sendUserToMainActivity() {
        Intent intent=new Intent(getContext(), MainBottomNavigationActivity.class);
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();
        homeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    homeDataList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        HomeList homes=snapshot1.getValue(HomeList.class);
                        homeDataList.add(homes);
                        homeListAdapter.notifyDataSetChanged();
                    }
                    loadingDialog.dismiss();
                }else{
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}