package com.pranavpc.hotelharmony.model.hotel;

import java.util.List;

public class Images {
    private List<Image> rooms;
    private List<Image> amenities;
    private List<Image> site;

    // Getters and Setters

    public List<Image> getRooms() {
        return rooms;
    }

    public void setRooms(List<Image> rooms) {
        this.rooms = rooms;
    }

    public List<Image> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<Image> amenities) {
        this.amenities = amenities;
    }

    public List<Image> getSite() {
        return site;
    }

    public void setSite(List<Image> site) {
        this.site = site;
    }
}
