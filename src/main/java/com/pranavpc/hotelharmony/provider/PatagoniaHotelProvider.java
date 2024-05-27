package com.pranavpc.hotelharmony.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.pranavpc.hotelharmony.model.hotel.Hotel;
import com.pranavpc.hotelharmony.model.hotel.Amenities;
import com.pranavpc.hotelharmony.model.hotel.Images;
import com.pranavpc.hotelharmony.model.hotel.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatagoniaHotelProvider extends AbstractHotelProvider {

    @Value("${provider.patagonia.url}")
    private String url;

    @Value("${provider.patagonia.enabled}")
    private boolean enabled;

    @Override
    public List<Hotel> fetchHotels() {
        return fetchHotelsFromUrl(url, enabled);
    }

    @Override
    protected Hotel mapToHotel(JsonNode node) {
        Hotel hotel = new Hotel();
        Location location = new Location();

        hotel.setId(getTextValue(node, "id"));
        hotel.setDestinationId(getIntValue(node, "destination"));
        hotel.setName(getTextValue(node, "name"));
        hotel.setDescription(getTextValue(node, "info"));

        location.setLatitude(getDoubleValue(node, "lat"));
        location.setLongitude(getDoubleValue(node, "lng"));
        location.setAddress(getTextValue(node, "address"));
        hotel.setLocation(location);

        Amenities amenities = new Amenities();
        if (node.has("amenities")) {
            amenities.setRoom(getListValue(node, "amenities"));
            amenities.setGeneral(new ArrayList<>());
        }
        hotel.setAmenities(amenities);

        Images images = new Images();
        if (node.has("images")) {
            JsonNode imagesNode = node.get("images");
            images.setRooms(getImageListValue(imagesNode, "rooms"));
            images.setAmenities(getImageListValue(imagesNode, "amenities"));
            images.setSite(new ArrayList<>());
        }
        hotel.setImages(images);

        return hotel;
    }
}
