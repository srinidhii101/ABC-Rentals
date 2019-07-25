package com.example.abcrentals;

import java.io.Serializable;

public class Searches implements Serializable {


    private String Description, Image, Name,OwnerName,Location,Rent,ContactNumber,Amenities,Image2,Rooms;


    public Searches() {

    }

    public Searches(String Description, String Image, String Name, String OwnerName, String Location,String Rent,String ContactNumber, String Amenities,String Image2,String Rooms) {
        this.Description = Description;
        this.Image = Image;
        this.Name = Name;
        this.OwnerName = OwnerName;
        this.Location = Location;
        this.Rent = Rent;
        this.ContactNumber = ContactNumber;
        this.Amenities = Amenities;
        this.Image2 = Image2;
        this.Rooms = Rooms;

    }


    public String getName() {
        return Name;
    }

    public String getImage() {
        return Image;
    }

    public String getDescription() {
        return Description;
    }

    public String getOwnersName() {
        return OwnerName;
    }

    public String getLocation () {
        return Location;
    }

    public String getRent () {
        return Rent;
    }
    public String getContactNumber () {
        return ContactNumber;
    }
    public String getAmenities () {
        return Amenities;
    }

    public String getImage2()
    {
        return Image2;
    }

    public String getRooms()
    {
        return Rooms;
    }

}


