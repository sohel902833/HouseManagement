package com.sohel.drivermanagement.User.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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
import com.sohel.drivermanagement.User.Adapter.RenterListAdapter;
import com.sohel.drivermanagement.User.DataModuler.RenterList;
import com.sohel.drivermanagement.User.FloorListActivity;
import com.sohel.drivermanagement.User.FloorPaymentActivity;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private  List<RenterList> renterDataList=new ArrayList<>();
    private RenterListAdapter renterAdapter;


    private FirebaseAuth mAuth;
    private DatabaseReference renterRef;

    LoadingDialog loadingDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
      View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

      mAuth=FirebaseAuth.getInstance();
      renterRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Renter");
      loadingDialog=new LoadingDialog(getContext());
      loadingDialog.showLoadingDiolouge();

        recyclerView=root.findViewById(R.id.renterListRecyclerViewid);
      recyclerView.setHasFixedSize(true);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

      renterAdapter=new RenterListAdapter(getContext(),renterDataList);
      recyclerView.setAdapter(renterAdapter);

      renterAdapter.setOnItemClickListner(new RenterListAdapter.OnItemClickListner() {
          @Override
          public void onItemClick(int position) {
              RenterList currentItem=renterDataList.get(position);
              Intent intent=new Intent(getContext(), FloorPaymentActivity.class);
              intent.putExtra("floorId",currentItem.getFloorId());
              intent.putExtra("homeName",currentItem.getHomeName());
              startActivity(intent);

          }
      });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();


        renterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    renterDataList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        RenterList renter=snapshot1.getValue(RenterList.class);
                        renterDataList.add(renter);
                        renterAdapter.notifyDataSetChanged();
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