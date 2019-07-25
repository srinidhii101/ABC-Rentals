package com.example.abcrentals;

import android.media.Image;
import android.net.Uri;

import java.net.URL;

public class Record {

    private String Location;
    private String Name;
    private String Description;
    private String Image;
    private String Amenities;
    private String Rooms;
    private String Rent;
    private String ContactNumber;
    private String OwnerName;






    public Record() {
    }

    public Record(String Location, String Name, String Description, String Image,String Amenities,String Rooms, String ContactNumber,String OwnerName, String Rent) {
        this.Location = Location;
        this.Name = Name;
        this.Description = Description;
        this.Image = Image;
        this.Amenities = Amenities;
        this.Rooms = Rooms;
        this.ContactNumber = ContactNumber;
        this.OwnerName = OwnerName;
        this.Rent = Rent;
    }

    public String getRent() {
        return Rent;
    }

    public void setRent(String Rent) {
        this.Rent = Rent;
    }


    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String OwnerName) {
        this.OwnerName = OwnerName;
    }


    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String ContactNumber) {
        this.ContactNumber = ContactNumber;
    }


    public String getRooms() {
        return Rooms;
    }

    public void setRooms(String Rooms) {
        this.Rooms = Rooms;
    }


    public String getAmenities() {
        return Amenities;
    }

    public void setAmenities(String Amenities) {
        this.Amenities = Amenities;
    }


    public String getAddress() {
        return Location;
    }

    public void setAddress(String Location) {
        this.Location = Location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getImageUri() {
        return Image;
    }

    public void setImageUri(String Image) {
        this.Image = Image;
    }
}
