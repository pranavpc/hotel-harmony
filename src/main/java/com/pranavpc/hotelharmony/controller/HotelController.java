package com.pranavpc.hotelharmony.controller;

import com.pranavpc.hotelharmony.model.hotel.Hotel;
import com.pranavpc.hotelharmony.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/api/hotels")
    public ResponseEntity<List<Hotel>> getHotels(
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) List<String> hotels) {

        List<Hotel> allHotels = hotelService.fetchAndMergeHotelData();
        List<Hotel> filteredHotels;

        if (destination != null) {
            filteredHotels = allHotels.stream()
                    .filter(hotel -> hotel.getDestinationId() == Integer.parseInt(destination))
                    .collect(Collectors.toList());
        } else if (hotels != null && !hotels.isEmpty()) {
            filteredHotels = allHotels.stream()
                    .filter(hotel -> hotels.contains(hotel.getId()))
                    .collect(Collectors.toList());
        } else {
            filteredHotels = allHotels;
        }

        return ResponseEntity.ok()
                .cacheControl(
                        CacheControl.maxAge(1, TimeUnit.HOURS)
                                .sMaxAge(1, TimeUnit.HOURS)
                )
                .body(filteredHotels);
    }
}
