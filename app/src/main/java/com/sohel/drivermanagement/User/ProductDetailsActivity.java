
package com.sohel.drivermanagement.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sohel.drivermanagement.R;
import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImageView,callButton;
    private TextView productNameTextView,phoneNumberTextView,descriptionTextview;

    private Toolbar toolbar;

    String productName,productImage,productDescription,productPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        toolbar=findViewById(R.id.productDescriptionAppBarid);
        setSupportActionBar(toolbar);
        productName=getIntent().getStringExtra("productName");
        productImage=getIntent().getStringExtra("productImage");
        productDescription=getIntent().getStringExtra("productDescription");
        productPhone=getIntent().getStringExtra("productPhone");
        this.setTitle(productName);


        productImageView=findViewById(R.id.cardDescription_ImageViewid);
        callButton=findViewById(R.id.productDescriptionCallButtonid);
        productNameTextView=findViewById(R.id.productDescriptionProductNameTextviewid);
        phoneNumberTextView=findViewById(R.id.productDescriptionPhoneNumberid);
        descriptionTextview=findViewById(R.id.productDescription_ProductDescriptionTextviewid);


        Picasso.get().load(productImage).placeholder(R.drawable.select_image).into(productImageView);
        phoneNumberTextView.setText("Phone: "+productPhone);
        productNameTextView.setText(""+productName);
        descriptionTextview.setText(" "+productDescription);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+88"+productPhone));
                startActivity(intent);
           }
        });



    }
}