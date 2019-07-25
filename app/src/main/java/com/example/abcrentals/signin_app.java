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

public class signin_app extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
     EditText txtemail, pwd;
    private Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_app);
        txtemail = (EditText)findViewById(R.id.txtEmailID);
        pwd = (EditText)findViewById(R.id.txtSignupPassword);
        firebaseAuth = FirebaseAuth.getInstance();
        btnLogin = (Button)findViewById(R.id.btnSignIn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtemail.getText().toString().trim();
                String password = pwd.getText().toString().trim();
if(TextUtils.isEmpty(email))
{
    Toast.makeText(signin_app.this,"Please Enter Email",Toast.LENGTH_LONG).show();
    return;

}
else if(TextUtils.isEmpty(password))
{
    Toast.makeText(signin_app.this,"Please Enter Password",Toast.LENGTH_LONG).show();
    return;
}
else
{
    firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(signin_app.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {


                        Toast.makeText(  signin_app.this,"Welcome !",Toast.LENGTH_LONG).show();
                        Intent intent1 = new Intent(signin_app.this, myInterests.class);
                        startActivity(intent1);


                    } else {
                        Toast.makeText(signin_app.this,"Authentication Failed!",Toast.LENGTH_LONG).show();
                        return;
                    }

                    // ...
                }
            });
}
            }
        });

    }
}
