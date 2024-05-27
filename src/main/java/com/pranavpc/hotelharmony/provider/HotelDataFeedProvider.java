package com.pranavpc.hotelharmony.provider;

import com.pranavpc.hotelharmony.model.hotel.Hotel;

import java.util.List;

public interface HotelDataFeedProvider {
    List<Hotel> fetchHotels();
}
