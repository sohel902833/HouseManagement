package com.sohel.drivermanagement.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sohel.drivermanagement.LocalDatabase.UserShared;
import com.sohel.drivermanagement.LoginActivity;
import com.sohel.drivermanagement.R;

public class MainBottomNavigationActivity extends AppCompatActivity {

    
    
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bottom_navigation);
        
        toolbar=findViewById(R.id.bottomActivityToolbar);
        setSupportActionBar(toolbar);
        mAuth= FirebaseAuth.getInstance();
       userRef=FirebaseDatabase.getInstance().getReference().child("users");


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_transection)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    protected void onStart() {
        super.onStart();

        UserShared userShared=new UserShared(MainBottomNavigationActivity.this);
        String value=userShared.isHomeAded();

            userRef.child(mAuth.getCurrentUser().getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String value=snapshot.child("homeAdded").getValue().toString();
                                if(value.equals("false")){
                                    sendUserToHomeCreateActivity();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
    }

    private void sendUserToHomeCreateActivity() {
        Intent intent=new Intent(MainBottomNavigationActivity.this,HomeCreateActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.logoutId){
            mAuth.signOut();
            sendUsertoLoginActivity();

        }

        return super.onOptionsItemSelected(item);
    }
    private void sendUsertoLoginActivity() {
        Intent intent=new Intent(MainBottomNavigationActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.logout_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }
}