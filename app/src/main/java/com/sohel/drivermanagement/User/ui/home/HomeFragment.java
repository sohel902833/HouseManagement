package com.sohel.drivermanagement.User.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.TransectionDetailsActivity;
import com.sohel.drivermanagement.User.DataModuler.HomeList;
import com.sohel.drivermanagement.User.HomeCreateActivity;
import com.sohel.drivermanagement.User.HomeListActivity;
import com.sohel.drivermanagement.User.ProductCategoryActivity;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;

    private Button paymentEditButton,moreHomeCreateButton,logoutButton;
    TextView utilitsButton;
    private TextView dateTextView;
    private  TextView totalMoneyTextview;
    private  TextView totalRentAmountTextView;
    private TextView getMoneyTextView;
    private  int todayYear,todayMonth,todayDate;
    private DatabaseReference floorRef,balanceRef;

    private ProgressDialog progressDialog;

    int totalGetMoney=0;

    private  TextView moreDetailsTextview;
    private  int totalRentAmount=0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root);





        paymentEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeListActivity();
            }
        });

        moreHomeCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToHomeCreateActivity();
            }
        });
        utilitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToProductCategoryActivity();
            }
        });
      /*  logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                sendUsertoLoginActivity();

            }
        });*/

        moreDetailsTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), TransectionDetailsActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        floorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    totalGetMoney=0;
                    totalRentAmount=0;
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        String currentCount=snapshot1.child("dueAmount").getValue().toString();
                        String currentRentAmount=snapshot1.child("rentAmount").getValue().toString();
                        totalGetMoney+=Integer.parseInt(currentCount);
                        totalRentAmount+=Integer.parseInt(currentRentAmount);
                    }
                    progressDialog.dismiss();
                    totalRentAmountTextView.setText(""+String.valueOf(totalRentAmount));
                    totalMoneyTextview.setText(""+String.valueOf(totalGetMoney));
                }else{
                    progressDialog.dismiss();
                    totalMoneyTextview.setText(""+String.valueOf(totalGetMoney));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        balanceRef.child("data").child(String.valueOf(todayYear))
                .child(String.valueOf(todayMonth+1)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String getMoney=snapshot.child("getBalance").getValue().toString();
                    getMoneyTextView.setText(""+getMoney);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void init(View root) {
        mAuth=FirebaseAuth.getInstance();
        floorRef= FirebaseDatabase.getInstance().getReference().child("userData").child(mAuth.getCurrentUser().getUid()).child("Floor");
        balanceRef= FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        progressDialog=new ProgressDialog(getContext());
       progressDialog.setMessage("Loading");
       progressDialog.show();


        paymentEditButton=root.findViewById(R.id.f_home_paymentEditButton);
        moreHomeCreateButton=root.findViewById(R.id.f_home_moreHomeCreateButton);
        utilitsButton=root.findViewById(R.id.f_home_UtilitisIndex);
        getMoneyTextView=root.findViewById(R.id.f_Home_TotalGetBalance);
        totalMoneyTextview=root.findViewById(R.id.f_Home_TotalDueBalance);
        dateTextView=root.findViewById(R.id.f_home_DateTextviewid);
        moreDetailsTextview=root.findViewById(R.id.moreDetailsTextviewid);
        totalRentAmountTextView=root.findViewById(R.id.f_Home_TotalRentBalance);

        Calendar calendar=Calendar.getInstance();
        todayYear=calendar.get(Calendar.YEAR);
        todayMonth=calendar.get(Calendar.MONTH);
        todayDate=calendar.get(Calendar.DAY_OF_MONTH);


        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        dateTextView.setText(month_name+","+todayYear);


    }



    private void sendUserToProductCategoryActivity() {

        Intent intent=new Intent(getContext(), ProductCategoryActivity.class);
        intent.putExtra("check","edit");
        startActivity(intent);


    }

    private void sendUserToHomeCreateActivity() {
        Intent intent=new Intent(getContext(), HomeCreateActivity.class);
        intent.putExtra("check","edit");
        startActivity(intent);
    }

    private void sendUserToHomeListActivity() {
        Intent intent=new Intent(getContext(), HomeListActivity.class);
        intent.putExtra("check","edit");
        startActivity(intent);


    }
}