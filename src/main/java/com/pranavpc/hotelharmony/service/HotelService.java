package com.pranavpc.hotelharmony.service;

import com.pranavpc.hotelharmony.model.hotel.*;
import com.pranavpc.hotelharmony.provider.HotelDataFeedProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelService {

    @Autowired
    private List<HotelDataFeedProvider> providers;

    public List<Hotel> fetchAndMergeHotelData() {
        List<Hotel> allHotels = providers.stream()
                .flatMap(provider -> provider.fetchHotels().stream())
                .toList();

        Map<String, List<Hotel>> groupedHotels = allHotels.stream()
                .collect(Collectors.groupingBy(Hotel::getId));

        return groupedHotels.values().stream()
                .map(this::mergeHotels)
                .collect(Collectors.toList());
    }

    private Hotel mergeHotels(List<Hotel> hotels) {
        Hotel result = new Hotel();
        result.setId(hotels.get(0).getId());
        result.setDestinationId(hotels.get(0).getDestinationId());

        String name = hotels.stream()
                .map(Hotel::getName)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
        result.setName(name);

        String description = hotels.stream()
                .map(Hotel::getDescription)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
        result.setDescription(description);

        result.setLocation(mergeLocations(hotels.stream().map(Hotel::getLocation).collect(Collectors.toList())));
        result.setAmenities(mergeAmenities(hotels.stream().map(Hotel::getAmenities).collect(Collectors.toList())));
        result.setImages(mergeImages(hotels.stream().map(Hotel::getImages).collect(Collectors.toList())));
        result.setBookingConditions(mergeBookingConditions(hotels.stream().map(Hotel::getBookingConditions).collect(Collectors.toList())));

        return result;
    }

    private Location mergeLocations(List<Location> locations) {
        Location result = new Location();
        locations.forEach(location -> {
            if (location != null) {
                if (location.getLatitude() != null) result.setLatitude(location.getLatitude());
                if (location.getLongitude() != null) result.setLongitude(location.getLongitude());
                if (location.getAddress() != null) result.setAddress(location.getAddress());
                if (location.getCity() != null) result.setCity(location.getCity());
                if (location.getCountry() != null) result.setCountry(location.getCountry());
                if (location.getPostalCode() != null) result.setPostalCode(location.getPostalCode());
            }
        });
        return result;
    }

    private Amenities mergeAmenities(List<Amenities> amenitiesList) {
        Amenities result = new Amenities();
        List<String> general = new ArrayList<>();
        List<String> room = new ArrayList<>();
        amenitiesList.forEach(amenities -> {
            if (amenities != null) {
                general.addAll(amenities.getGeneral());
                room.addAll(amenities.getRoom());
            }
        });
        result.setGeneral(general.stream().distinct().collect(Collectors.toList()));
        result.setRoom(room.stream().distinct().collect(Collectors.toList()));
        return result;
    }

    private Images mergeImages(List<Images> imagesList) {
        Images result = new Images();
        List<Image> rooms = new ArrayList<>();
        List<Image> amenities = new ArrayList<>();
        List<Image> site = new ArrayList<>();
        imagesList.forEach(images -> {
            if (images != null) {
                rooms.addAll(images.getRooms().stream()
                        .filter(img -> img.getLink() != null && img.getDescription() != null)
                        .collect(Collectors.toMap(Image::getDescription, img -> img, (img1, img2) -> img1))
                        .values());

                amenities.addAll(images.getAmenities().stream()
                        .filter(img -> img.getLink() != null && img.getDescription() != null)
                        .collect(Collectors.toMap(Image::getDescription, img -> img, (img1, img2) -> img1))
                        .values());

                site.addAll(images.getSite().stream()
                        .filter(img -> img.getLink() != null && img.getDescription() != null)
                        .collect(Collectors.toMap(Image::getDescription, img -> img, (img1, img2) -> img1))
                        .values());
            }
        });
        result.setRooms(rooms);
        result.setAmenities(amenities);
        result.setSite(site);
        return result;
    }

    private List<String> mergeBookingConditions(List<List<String>> bookingConditionsList) {
        return bookingConditionsList.stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
