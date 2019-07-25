package com.example.abcrentals;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class signup extends AppCompatActivity {
     EditText txtemail,pwd,confirmpassword;
    private Button btnSignUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        txtemail = (EditText)findViewById(R.id.txtSignupEmail);
        pwd = findViewById(R.id.txtSignupPassword);
        confirmpassword = (EditText)findViewById(R.id.txtsignupCnfPassword) ;
        firebaseAuth = FirebaseAuth.getInstance();
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txtemail.getText().toString().trim();
                String pass = pwd.getText().toString().trim();
                String confPass = confirmpassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(signup.this,"Please Enter Email",Toast.LENGTH_LONG).show();
                    return;
                }
                else  if(TextUtils.isEmpty(pass))
                {
                    Toast.makeText(signup.this,"Please Enter Password",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(TextUtils.isEmpty(confPass))
                {
                    Toast.makeText(signup.this,"Please Confirm Password",Toast.LENGTH_LONG).show();
                    return;
                }

                if(pass.length()<6)
                {
                    Toast.makeText(signup.this,"Password is too short !",Toast.LENGTH_LONG).show();
                    return;
                }

                if(pass.equals(confPass))
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                        Toast.makeText(signup.this,"Registration Complete !",Toast.LENGTH_LONG).show();


                                    } else {
                                        Toast.makeText(signup.this,"Authentication Failed!",Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    // ...
                                }
                            });
                }
                else
                {
                    Toast.makeText(signup.this,"Passwords do not match!",Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }
}
