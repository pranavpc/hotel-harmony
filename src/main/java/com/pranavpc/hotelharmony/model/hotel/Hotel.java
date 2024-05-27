package com.pranavpc.hotelharmony.model.hotel;

import java.util.List;

public class Hotel {
    private String id;
    private int destinationId;
    private String name;
    private String description;
    private Location location;
    private Amenities amenities;
    private Images images;
    private List<String> bookingConditions;

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Amenities getAmenities() {
        return amenities;
    }

    public void setAmenities(Amenities amenities) {
        this.amenities = amenities;
    }

    public Images getImages() {
        return images;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public List<String> getBookingConditions() {
        return bookingConditions;
    }

    public void setBookingConditions(List<String> bookingConditions) {
        this.bookingConditions = bookingConditions;
    }
}
