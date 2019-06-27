package com.example.abc_rentals;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

   // private TextView homePage;
    private Button submit;
    private EditText userName;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

userName = (EditText) findViewById(R.id.emailID);
password = (EditText) findViewById(R.id.password);
submit = (Button) findViewById(R.id.submit);

submit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        validateCredentials(userName.getText().toString(), password.getText().toString());


    }
});
    }

    public void validateCredentials (String username, String password) {

        if (username.equals("admin") && password.equals("admin")) {
            Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
            startActivity(intent);
        }
        else if (!username.equals("admin") && password.equals("admin")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Username is invalid", Toast.LENGTH_LONG);
            toast.show();
        }
        else if (username.equals("admin") && !password.equals("admin")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Password is invalid", Toast.LENGTH_LONG);
            toast.show();
        }
        else if (!username.equals("admin") && !password.equals("admin")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Username and Password are invalid", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
