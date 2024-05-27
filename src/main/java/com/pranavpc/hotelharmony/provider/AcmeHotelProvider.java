package com.pranavpc.hotelharmony.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.pranavpc.hotelharmony.model.hotel.Hotel;
import com.pranavpc.hotelharmony.model.hotel.Amenities;
import com.pranavpc.hotelharmony.model.hotel.Images;
import com.pranavpc.hotelharmony.model.hotel.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AcmeHotelProvider extends AbstractHotelProvider {

    @Value("${provider.acme.url}")
    private String url;

    @Value("${provider.acme.enabled}")
    private boolean enabled;

    @Override
    public List<Hotel> fetchHotels() {
        return fetchHotelsFromUrl(url, enabled);
    }

    @Override
    protected Hotel mapToHotel(JsonNode node) {
        Hotel hotel = new Hotel();
        Location location = new Location();

        hotel.setId(getTextValue(node, "Id"));
        hotel.setDestinationId(getIntValue(node, "DestinationId"));
        hotel.setName(getTextValue(node, "Name"));
        hotel.setDescription(getTextValue(node, "Description"));

        location.setLatitude(getDoubleValue(node, "Latitude"));
        location.setLatitude(getDoubleValue(node, "Longitude"));
        location.setAddress(getTextValue(node, "Address"));
        location.setCity(getTextValue(node, "City"));
        location.setCountry(getTextValue(node, "Country"));
        location.setPostalCode(getTextValue(node, "PostalCode"));
        hotel.setLocation(location);

        List<String> facilities = getListValue(node, "Facilities");

        Amenities amenitiesObj = new Amenities();
        amenitiesObj.setGeneral(facilities.stream()
                .filter(facility -> facility != null && !facility.isEmpty())
                .map(this::transformToLowerCasedWords)
                .collect(Collectors.toList()));
        hotel.setAmenities(amenitiesObj);

        Images images = new Images();
        images.setRooms(getImageListValue(node, "Images.Rooms"));
        images.setAmenities(getImageListValue(node, "Images.Amenities"));
        images.setSite(getImageListValue(node, "Images.Site"));
        hotel.setImages(images);

        hotel.setBookingConditions(getListValue(node, "BookingConditions"));

        return hotel;
    }
}
