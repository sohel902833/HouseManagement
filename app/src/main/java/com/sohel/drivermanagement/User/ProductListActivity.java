package com.sohel.drivermanagement.User;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.Admin.Adapters.ProductAdapter;
import com.sohel.drivermanagement.Admin.DataModuler.Products;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.UserProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Products> productList=new ArrayList<>();
    private DatabaseReference productRef;
    private UserProductAdapter productAdapter;
    private String categoryId,categoryName;
    private ProgressDialog progressBar;


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        categoryId=getIntent().getStringExtra("categoryId");
        categoryName=getIntent().getStringExtra("categoryName");
        toolbar=findViewById(R.id.user_ProductListToolbarid);
        setSupportActionBar(toolbar);
        this.setTitle(categoryName);



        progressBar=new ProgressDialog(this);
        progressBar.setMessage("Loading");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        productRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Products");

        recyclerView=findViewById(R.id.user_productListRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter=new UserProductAdapter(this,productList);
        recyclerView.setAdapter(productAdapter);


        productAdapter.setOnItemClickListner(new UserProductAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Products currentItem=productList.get(position);
                Intent intent=new Intent(ProductListActivity.this,ProductDetailsActivity.class);
               intent.putExtra("productName",currentItem.getProductName());
               intent.putExtra("productImage",currentItem.getImage());
               intent.putExtra("productDescription",currentItem.getProductDescription());
               intent.putExtra("productPhone",currentItem.getPhoneNumber());
                startActivity(intent);



            }
        });




    }


    @Override
    protected void onStart() {
        super.onStart();
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    productList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Products products=snapshot1.getValue(Products.class);
                        if(products.getCategoryId().equals(categoryId)){
                            productList.add(products);
                            productAdapter.notifyDataSetChanged();
                        }

                    }
                    progressBar.dismiss();
                }else{
                    progressBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}