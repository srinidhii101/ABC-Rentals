package com.example.abcrentals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logout extends AppCompatActivity {
private Button logout ;
private  Button back;
private TextView userinfo;
    @Override

    //Method oncreate to initialize variables and click on respective button to logout
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String userid=user.getEmail();
        userinfo = findViewById(R.id.txtLoggedinUser);

        userinfo.setText("Logged In User :\n"+userid);

        logout = findViewById(R.id.btnlogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth fAuth = FirebaseAuth.getInstance();

                fAuth.signOut();
                startActivity(new Intent(Logout.this,MainActivity.class));
                finish();
            }
        });

        back = findViewById(R.id.btnback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Logout.this,SearchPage.class));
                finish();
            }
        });


    }
}
