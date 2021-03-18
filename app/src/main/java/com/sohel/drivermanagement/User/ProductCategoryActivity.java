package com.sohel.drivermanagement.User;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.Admin.Adapters.CategoryAdapter;
import com.sohel.drivermanagement.Admin.DataModuler.Category;
import com.sohel.drivermanagement.R;
import com.sohel.drivermanagement.User.Adapter.UserCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductCategoryActivity extends AppCompatActivity {

    private List<Category> categoryList=new ArrayList<>();
    private UserCategoryAdapter categoryAdapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressBar;
    private DatabaseReference productCategoryRef;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_category);

        toolbar=findViewById(R.id.product_category_Toolbarid);
        setSupportActionBar(toolbar);
        this.setTitle("Category");


        progressBar=new ProgressDialog(this);
        productCategoryRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Category");

        recyclerView=findViewById(R.id.productCategoryRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        categoryAdapter=new UserCategoryAdapter(this,categoryList);
        recyclerView.setAdapter(categoryAdapter);


        categoryAdapter.setOnItemClickListner(new UserCategoryAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(int position) {
                Category selectedItem=categoryList.get(position);

                Intent intent=new Intent(ProductCategoryActivity.this,ProductListActivity.class);
                 intent.putExtra("categoryId",selectedItem.getCategoryId());
                 intent.putExtra("categoryName",selectedItem.getCategoryName());
                startActivity(intent);



            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setMessage("Loading");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();
        productCategoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    categoryList.clear();
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        Category category=snapshot1.getValue(Category.class);
                        categoryList.add(category);
                        categoryAdapter.notifyDataSetChanged();
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