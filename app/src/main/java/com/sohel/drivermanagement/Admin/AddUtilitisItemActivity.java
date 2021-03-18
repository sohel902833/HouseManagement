package com.sohel.drivermanagement.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sohel.drivermanagement.Admin.DataModuler.Products;
import com.sohel.drivermanagement.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class AddUtilitisItemActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText productNameEdittext,productDescriptionEdittext,phoneNumberEdittext;
    private Button addUtillsButton;
    private Uri imageUri;
    String categoryId,categoryName;



    private ProgressDialog progressBar;



    //Firebase
    private DatabaseReference productRef;
    private StorageReference storageReference;

    int checker;

    String uProductName,uProductDescription,uPhoneNumber,phoneNumber,uProductCategoryId,uProductId,uProductImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_utilitis_item);
        checker=getIntent().getIntExtra("checker",0);
        if(checker==1){
            categoryId=getIntent().getStringExtra("categoryId");
            categoryName=getIntent().getStringExtra("categoryName");
        }else if(checker==2){
            uProductName=getIntent().getStringExtra("productName");
            uProductDescription=getIntent().getStringExtra("productDescription");
            categoryId=getIntent().getStringExtra("categoryId");
            uProductImage=getIntent().getStringExtra("productImage");
            uProductId=getIntent().getStringExtra("productId");
            uPhoneNumber=getIntent().getStringExtra("phoneNumber");
            categoryName=getIntent().getStringExtra("categoryName");

        }


        progressBar=new ProgressDialog(this);



        storageReference= FirebaseStorage.getInstance().getReference().child("ProductImages");
        productRef= FirebaseDatabase.getInstance().getReference().child("Admin").child("Utilites").child("Products");




        imageView=findViewById(R.id.addUtils_ImageViewid);
        productNameEdittext=findViewById(R.id.add_Utils_headingEdittextid);
        productDescriptionEdittext=findViewById(R.id.add_Utils_DescriptionEdittext);
        phoneNumberEdittext=findViewById(R.id.add_Utils_brandPhoneNumber);
        addUtillsButton=findViewById(R.id.utils_SaveButtonid);

        if(checker==2){
            Picasso.get().load(uProductImage).placeholder(R.drawable.select_image).into(imageView);
            productNameEdittext.setText(uProductName);
            productDescriptionEdittext.setText(uProductDescription);
            phoneNumberEdittext.setText(uPhoneNumber);
        }


       if(checker==2){
            addUtillsButton.setText("Update Product");
        }


        addUtillsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String productName=productNameEdittext.getText().toString();
                     phoneNumber=phoneNumberEdittext.getText().toString();
                    String productDescription=productDescriptionEdittext.getText().toString();
                    if(checker==1 && imageUri==null){
                        Toasty.warning(AddUtilitisItemActivity.this, "Please Select An Image..", Toast.LENGTH_SHORT, true).show();
                    }else if(productName.isEmpty()){
                        productNameEdittext.setError("Write Your Product Name");
                        productNameEdittext.requestFocus();

                    }else if(phoneNumber.isEmpty()){
                        phoneNumberEdittext.setError("Write Company Phone Number");
                        phoneNumberEdittext.requestFocus();

                    }else if(productDescription.isEmpty()){
                        productDescriptionEdittext.setError("Write Something About Your Product");
                        productDescriptionEdittext.requestFocus();
                    }else{
                        if(checker==1){
                            saveProduct(productName,productDescription);
                        }else if(checker==2){
                            updateProduct(productName,productDescription);
                        }

                    }
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openfilechooser();
            }
        });








    }

    private void updateProduct(String productName,String productDescription) {
        progressBar.setMessage("Saving Product.");
        progressBar.setTitle("Please Wait");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();

        if(imageUri==null){
            HashMap<String,Object> updateProductmap=new HashMap<>();
            updateProductmap.put("productName",productName);
            updateProductmap.put("productDescription",productDescription);
            updateProductmap.put("phoneNumber",phoneNumber);
            productRef.child(uProductId)
                        .updateChildren(updateProductmap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressBar.dismiss();
                                    sendUserToUtitiliListActivity();
                                    Toasty.success(AddUtilitisItemActivity.this, "Product Updated", Toast.LENGTH_SHORT, true).show();
                                }else{
                                    progressBar.dismiss();
                                    Toasty.warning(AddUtilitisItemActivity.this, "Product Updated Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        });
        }else{
            String key=productRef.push().getKey();

            StorageReference filePath=storageReference.child(key+productRef.push().getKey()+"."+getFileExtension(imageUri));
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                    while(!urlTask.isSuccessful());
                    Uri downloaduri=urlTask.getResult();
                    HashMap<String,Object> updateProductmap=new HashMap<>();
                    updateProductmap.put("productName",productName);
                    updateProductmap.put("phoneNumber",phoneNumber);
                    updateProductmap.put("image",downloaduri.toString());
                    updateProductmap.put("productDescription",productDescription);

                    productRef.child(uProductId)
                            .updateChildren(updateProductmap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressBar.dismiss();
                                        sendUserToUtitiliListActivity();
                                        Toasty.success(AddUtilitisItemActivity.this, "Product Updated", Toast.LENGTH_SHORT, true).show();
                                    }else{
                                        progressBar.dismiss();
                                        Toasty.warning(AddUtilitisItemActivity.this, "Product Update Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                                    }
                                }
                            });
                }
            });

        }


    }

    public void sendUserToUtitiliListActivity(){
        Intent intent=new Intent(AddUtilitisItemActivity.this,UtilitiesListActivity.class);
        intent.putExtra("categoryId",categoryId);
        intent.putExtra("categoryName",categoryName);
        startActivity(intent);
        finish();
}
    private void saveProduct(String productName,String productDescription) {
        progressBar.setMessage("Saving Product.");
        progressBar.setTitle("Please Wait");
        progressBar.setCanceledOnTouchOutside(false);
        progressBar.show();


        String key=productRef.push().getKey();

        StorageReference filePath=storageReference.child(key+productRef.push().getKey()+"."+getFileExtension(imageUri));
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> urlTask=taskSnapshot.getStorage().getDownloadUrl();
                while(!urlTask.isSuccessful());
                Uri downloaduri=urlTask.getResult();


                String productId=key+System.currentTimeMillis()+System.currentTimeMillis();
                Products products=new Products(productId,categoryId,productName,productDescription,downloaduri.toString(),phoneNumber);

                productRef.child(productId)
                        .setValue(products)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    progressBar.dismiss();
                                    sendUserToUtitiliListActivity();
                                   Toasty.success(AddUtilitisItemActivity.this, "Product Saved", Toast.LENGTH_SHORT, true).show();
                                }else{
                                    progressBar.dismiss();
                                    Toasty.warning(AddUtilitisItemActivity.this, "Product Save Failed,Please Try again Later.", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        });

            }
        });



    }

    public String getFileExtension(Uri imageuri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageuri));
    }

    private void openfilechooser() {
        Intent intentf=new Intent();
        intentf.setType("image/*");
        intentf.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentf,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            imageView.setImageURI(data.getData());
        }

    }
}