package com.example.abcrentals;

import java.io.Serializable;

//Model class to fetch data from firestore
public class InterestsClass implements Serializable {


    private String address, amenities, contactNumber,description,imageUri,name,ownerName,rent,rooms;


    public InterestsClass() {

    }

    public InterestsClass(String address, String amenities, String contactNumber, String description, String imageUri,String name,String ownerName, String rent,String rooms) {
        this.address = address;
        this.amenities = amenities;
        this.contactNumber = contactNumber;
        this.description  = description;
        this.imageUri = imageUri;
        this.name = name;
        this.ownerName = ownerName;
        this.ownerName = ownerName;
        this.rent = rent;
        this.rooms = rooms;

    }


    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnersName() {
        return ownerName;
    }

    public String getAddress () {
        return address;
    }

    public String getRent () {
        return rent;
    }
    public String getContactNumber () {
        return contactNumber;
    }
    public String getAmenities () {
        return amenities;
    }



    public String getRooms()
    {
        return rooms;
    }

}


