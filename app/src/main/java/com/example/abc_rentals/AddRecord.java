package com.example.abc_rentals;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddRecord extends AppCompatActivity {

    private Button clickPics;
    private ImageView imageView;
    private EditText enterownerAddress;
    private Button getLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        clickPics = (Button) findViewById(R.id.clickPhotos);
        imageView = (ImageView) findViewById(R.id.imageView);
        enterownerAddress = (EditText) findViewById(R.id.enterownerAddress);
        getLocation = (Button) findViewById(R.id.getLocation);


        clickPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent2,0 );
            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                       // checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
//                }
                else {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                 //   Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    try {
                         String ownerAddress = fetchLocation(location.getLatitude(), location.getLongitude());
                        enterownerAddress.setText(ownerAddress);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddRecord.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
  //      super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case 1000: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                   // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    try {
                        String ownerAddress = fetchLocation(location.getLatitude(), location.getLongitude());
                        enterownerAddress.setText(ownerAddress);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddRecord.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                    // String ownerAddress = fetchLocation(location.getLatitude(), location.getLongitude());
                    //enterownerAddress.setText(ownerAddress);

                }
                else {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private String fetchLocation(double latitute, double longitude) {

        String city = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
         List<Address> addressList;
         try {
          addressList = geocoder.getFromLocation(latitute,longitude, 10);

          if (addressList.size() > 0)
          {
          for (Address address : addressList)
          {
              if(address.getLocality() != null && address.getLocality().length() >0 ) {
                 // city = address.getLocality();//+ address.getAddressLine(0) + address.getPostalCode();
                  city = address.getAddressLine(0);
                //  city = address.getPostalCode();
                  break;
              }
          }
          }
         }
         catch (IOException e) {
             e.printStackTrace();
         }

         return city;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);

    }
}
