package com.pranavpc.hotelharmony.model.hotel;

import java.util.ArrayList;
import java.util.List;

public class Amenities {
    private List<String> general = new ArrayList<>();
    private List<String> room = new ArrayList<>();

    // Getters and Setters

    public List<String> getGeneral() {
        return general;
    }

    public void setGeneral(List<String> general) {
        this.general = general;
    }

    public List<String> getRoom() {
        return room;
    }

    public void setRoom(List<String> room) {
        this.room = room;
    }
}
