package com.example.abcrentals;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class myInterests extends AppCompatActivity {

    private Button call;
    private Button addRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_interests);

        call = (Button) findViewById(R.id.call);
        addRecord = (Button) findViewById(R.id.newRecord);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 9999999999"));
                startActivity(intent);

            }
        });

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForm();
            }
        });
    }

    public void openForm() {
        Intent intent1 = new Intent(myInterests.this, AddRecord.class);
        startActivity(intent1);
    }
}
