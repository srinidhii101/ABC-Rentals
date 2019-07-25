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
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
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

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class AddRecord extends AppCompatActivity {

    public int counter=0;
    private Button clickPics;
    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;
//    private EditText enterownerAddress;
//    private EditText ownerName;
//    private EditText houseDescription;
//    private EditText amenities;
//    private EditText rent;
//    private EditText rooms;
//    private EditText contactNumber;
//    private EditText housename;

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
   // List<ImageView> l1 = new ArrayList<>();
    public static String Image ="";
    //private static int counter = 0;
    //List<Bitmap> b = new ArrayList<>();

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
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


        clickPics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(counter<=2)
                {

                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = null;
                // file.mkdirs();

                try {
                    file = getExternalFilesDir(Environment.DIRECTORY_PICTURES); //new File(Environment.getExternalStorageDirectory().getAbsolutePath()+ "Photos");
                    imgFile = File.createTempFile("IMG_", ".jpg", file);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (imgFile != null) {
                    Uri imgUri = FileProvider.getUriForFile(AddRecord.this, "com.example.abcrentals.fileprovider", imgFile);

                    //Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent1, 3);


//                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE,MediaStore.EXTRA_OUTPUT);
//                startActivityForResult(intent2,1 );

//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,1);
                }
            }
                else {
                    clickPics.setEnabled(false);
                }
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
                        Location.setText(ownerAddress);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(AddRecord.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          String Location1 = Location.getText().toString().trim();
                                          String OwnerName1 = OwnerName.getText().toString().trim();
                                          String Description1 = Description.getText().toString().trim();
                                          String Amenities1 = Amenities.getText().toString().trim();
                                          String Rent1 = Rent.getText().toString().trim();
                                          String ContactNumber1 = ContactNumber.getText().toString().trim();
                                          String Name1= Name.getText().toString().trim();
                                          String Rooms1 = Rooms.getText().toString().trim();
                                          if (TextUtils.isEmpty(Location1) || TextUtils.isEmpty(OwnerName1) || TextUtils.isEmpty(Description1 ) || (TextUtils.isEmpty(Image))|| TextUtils.isEmpty(Amenities1 )
                                                  || TextUtils.isEmpty(Rent1)|| TextUtils.isEmpty(ContactNumber1)|| TextUtils.isEmpty(Name1 )|| TextUtils.isEmpty(Rooms1)) {
                                              Toast.makeText(AddRecord.this, "Fields cannot be empty", Toast.LENGTH_LONG).show();

                                          } else {




                                                      Toast.makeText(AddRecord.this, "Pic uploaded", Toast.LENGTH_SHORT).show();

                                                      CollectionReference records = db.collection("Interests");

//                                              Location = enterownerAddress.getText().toString().trim();
//                                              OwnerName = ownerName.getText().toString().trim();
//                                              Description = houseDescription.getText().toString().trim();
//                                              Amenities = amenities.getText().toString().trim();
//                                              Rent = rent.getText().toString().trim();
//                                              ContactNumber = contactNumber.getText().toString().trim();
//                                              Name= housename.getText().toString().trim();
//                                              Rooms = rooms.getText().toString().trim();

                                                      Record record = new Record(Location1, Name1, Description1,Image, Amenities1,Rooms1,ContactNumber1,OwnerName1,Rent1);
                                                      // Record record = new Record(address, name, description);

                                                      records.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                          @Override
                                                          public void onSuccess(DocumentReference documentReference) {
                                                              Toast.makeText(AddRecord.this, "Record added", Toast.LENGTH_LONG).show();
                                                          }
                                                      }).addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              Toast.makeText(AddRecord.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                                          }
                                                      });

                                                  }


//                                              CollectionReference records = db.collection("Records");
//
//                                              Record record = new Record(address, name, description, "");
//                                             // Record record = new Record(address, name, description);
//
//                                              records.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                                  @Override
//                                                  public void onSuccess(DocumentReference documentReference) {
//                                                      Toast.makeText(AddRecord.this, "Record added", Toast.LENGTH_LONG).show();
//                                                  }
//                                              }).addOnFailureListener(new OnFailureListener() {
//                                                  @Override
//                                                  public void onFailure(@NonNull Exception e) {
//                                                      Toast.makeText(AddRecord.this, e.getMessage(), Toast.LENGTH_LONG).show();
//
//                                                  }
//                                              });


                                          }
                                      });


//                submit.setOnClickListener({
//                        String address = enterownerAddress.getText().toString().trim();
//            String name =  ownerName.getText().toString().trim();
//            String description =  houseDescription.getText().toString().trim();
//
//            CollectionReference records = db.collection("Records");
//
//            Record record = new Record(address,name,description);
//
//            records.add(record).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                @Override
//                public void onSuccess(DocumentReference documentReference) {
//                    Toast.makeText(AddRecord.this,"Record added",Toast.LENGTH_LONG).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(AddRecord.this,e.getMessage(),Toast.LENGTH_LONG).show();
//
//                }
//            });
//
//        });


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
                        Location.setText(ownerAddress);
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
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == RESULT_OK) { //&& data != null && data.getData()!=null) {


//            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(bitmap);
            File im=new File(imgFile.getAbsolutePath());


            mImageUri = Uri.fromFile(im);


            //Uri imageUri = data.getData();
            try {

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mImageUri);
                    if(counter ==0) {
                        imageView.setImageBitmap(bitmap);
                        counter++;
                    }else if(counter ==1)
                    {
                        imageView2.setImageBitmap(bitmap);
                        counter++;
                    }
                    else if(counter ==2)
                    {
                        imageView3.setImageBitmap(bitmap);

                        counter++;
                        clickPics.setEnabled(false);
                    }
//                    b.add(bitmap);
//                    l1.add(imageView);
//                    counter++;
//                    if(counter == 3)
//                    {
//                        for(ImageView i: l1)
//                        {
//                            for(Bitmap bMap: b) {
//
//                                i.setImageBitmap(bMap);
//                            }
//                        }
//                    }

//                    for(int i=0;i<2;i++)
//                    {
                        //Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//                    }
//                    imageView2.setImageBitmap(bitmap);
//                    imageView3.setImageBitmap(bitmap);


                final StorageReference filePath = storageReference.child("Photos").child(mImageUri.getLastPathSegment());
                filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final Uri downloadURL = uri;
                                if(Image.isEmpty())
                                {
                                    Image = downloadURL.toString();
                                }
                                else {
                                    Image = Image + ";" + downloadURL.toString();
                                }
                            }
                        });


                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }




//            StorageReference filePath = storageReference.child("Photos").child(mImageUri.getLastPathSegment());
//            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(AddRecord.this, "Pic uploaded", Toast.LENGTH_SHORT).show();
//                }
//            });
        }

    }



}
