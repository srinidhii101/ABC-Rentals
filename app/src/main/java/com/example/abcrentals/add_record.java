package com.example.abcrentals;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

public class add_record extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public int counter = 0;
    private Button clickPics;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
    private EditText Location;
    private EditText OwnerName;
    private EditText Description;
    private EditText Amenities;
    private EditText Rent;
    private EditText Rooms;
    private EditText ContactNumber;
    private EditText Name;
    private Button getLocation;
    private Button submit;
    private Uri mImageUri;
    private Bitmap compressor;
    private ProgressDialog progressDialog;
    private static final int CAMERA_REQUEST_CODE = 1;

    public static String Image = "";
    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private DrawerLayout mDrawerLayout;
    String user_id;
    File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        db = FirebaseFirestore.getInstance();
//        firebaseAuth = FirebaseAuth.getInstance();
//        user_id = firebaseAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        // storageReference = db.
        NavigationView navigationView2 = findViewById(R.id.nav_view_add);
        mDrawerLayout = findViewById(R.id.drawer_layout_two);


        //Code to navigate between the activities using the navigation drawer
        navigationView2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.profile) {
                    Intent intent = new Intent(getApplication(), Logout.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.Home) {
                    Intent intent = new Intent(getApplication(), SearchPage.class);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.AddInterests) {
                    Intent intent = new Intent(getApplication(), add_record.class);
                    startActivity(intent);
                }

                menuItem.setChecked(true);

                // TODO: handle navigation
                // Closing drawer on item click
                mDrawerLayout.closeDrawers();
                return true;
            }

        });

        //Initializing the objects
        clickPics = (Button) findViewById(R.id.clickPhotos);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);
        Location = (EditText) findViewById(R.id.enterownerAddress);
        getLocation = (Button) findViewById(R.id.getLocation);
        OwnerName = (EditText) findViewById(R.id.enterOwnerName);
        Description = (EditText) findViewById(R.id.enterhouseDescription);
        submit = (Button) findViewById(R.id.submitDetails);
        Amenities = findViewById(R.id.Amenneties);
        ContactNumber = findViewById(R.id.ContactNumber);
        Rent = findViewById(R.id.Rent);
        Rooms = findViewById(R.id.Rooms);
        Name = findViewById(R.id.placename);

        //Code to click photos from phone camera

        clickPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (counter <= 2) {

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = null;


                    try {
                        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "Photos");
                        imgFile = File.createTempFile("IMG_", ".jpg", file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    if (imgFile != null) {
                        //this will fetch the photo
                        Uri imgUri = FileProvider.getUriForFile(add_record.this, "com.example.abcrentals.fileprovider", imgFile);

                        //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent1.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                        startActivityForResult(intent1, 3);

                    }
                } else {
                    clickPics.setEnabled(false);
                }
            }
        });

        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
                } else {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    android.location.Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                    try {
                        String ownerAddress = fetchLocation(location.getLatitude(), location.getLongitude());
                        Location.setText(ownerAddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(add_record.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        //submitting the interests
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Location1 = Location.getText().toString().trim();
                String OwnerName1 = OwnerName.getText().toString().trim();
                String Description1 = Description.getText().toString().trim();
                String Amenities1 = Amenities.getText().toString().trim();
                String Rent1 = Rent.getText().toString().trim();
                String ContactNumber1 = ContactNumber.getText().toString().trim();
                String Name1 = Name.getText().toString().trim();
                String Rooms1 = Rooms.getText().toString().trim();
                //Checking if all the values are not empty
                if (TextUtils.isEmpty(Location1) || TextUtils.isEmpty(OwnerName1) || TextUtils.isEmpty(Description1) || (TextUtils.isEmpty(Image)) || TextUtils.isEmpty(Amenities1)
                        || TextUtils.isEmpty(Rent1) || TextUtils.isEmpty(ContactNumber1) || TextUtils.isEmpty(Name1) || TextUtils.isEmpty(Rooms1)) {
                    Toast.makeText(add_record.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(add_record.this, "Pic uploaded", Toast.LENGTH_SHORT).show();

                    CollectionReference records = db.collection("Interests");

                    Record record = new Record(Location1, Name1, Description1, Image, Amenities1, Rooms1, ContactNumber1, OwnerName1, Rent1);
                    // Record record = new Record(address, name, description);

                    //Creating a new interest and inserting values into the firestore DB
                    records.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(add_record.this, "Record added", Toast.LENGTH_LONG).show();
                            Image = "";
                            counter = 0;
                            clickPics.setEnabled(true);
                            Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_record.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    });

                }


            }
        });


//

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_two);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //      super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    // Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    try {
                        String ownerAddress = fetchLocation(location.getLatitude(), location.getLongitude());
                        Location.setText(ownerAddress);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(add_record.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                    // String ownerAddress = fetchLocation(location.getLatitude(), location.getLongitude());
                    //enterownerAddress.setText(ownerAddress);

                } else {
                    Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    //Fetching current location of the user
    private String fetchLocation(double latitute, double longitude) {

        String city = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(latitute, longitude, 10);

            if (addressList.size() > 0) {
                for (Address address : addressList) {
                    if (address.getLocality() != null && address.getLocality().length() > 0) {
                        // city = address.getLocality();//+ address.getAddressLine(0) + address.getPostalCode();
                        city = address.getAddressLine(0);
                        //  city = address.getPostalCode();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return city;
    }

    //Code to add images to the imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { //&& data != null && data.getData()!=null) {


//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bitmap);
            File im = new File(imgFile.getAbsolutePath());


            mImageUri = Uri.fromFile(im);


            //Uri imageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                if (counter == 0) {
                    imageView.setImageBitmap(bitmap);
                    counter++;
                } else if (counter == 1) {
                    imageView2.setImageBitmap(bitmap);
                    counter++;
                } else if (counter == 2) {
                    imageView3.setImageBitmap(bitmap);

                    counter++;
                    clickPics.setEnabled(false);
                }

                final StorageReference filePath = storageReference.child("Photos").child(mImageUri.getLastPathSegment());
                filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadURL = uri;
                                if (Image.isEmpty()) {
                                    Image = downloadURL.toString();
                                } else {
                                    Image = Image + ";" + downloadURL.toString();
                                }
                            }
                        });


                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_two);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout_two);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
