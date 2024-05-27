package com.pranavpc.hotelharmony.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.pranavpc.hotelharmony.model.hotel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaperfliesHotelProvider extends AbstractHotelProvider {

    @Value("${provider.paperflies.url}")
    private String url;

    @Value("${provider.paperflies.enabled}")
    private boolean enabled;

    @Override
    public List<Hotel> fetchHotels() {
        return fetchHotelsFromUrl(url, enabled);
    }

    @Override
    protected Hotel mapToHotel(JsonNode node) {
        Hotel hotel = new Hotel();
        Location location = new Location();

        hotel.setId(getTextValue(node, "hotel_id"));
        hotel.setDestinationId(getIntValue(node, "destination_id"));
        hotel.setName(getTextValue(node, "hotel_name"));
        hotel.setDescription(getTextValue(node, "details"));

        if (node.has("location")) {
            location.setAddress(getTextValue(node.get("location"), "address"));
            location.setCountry(getTextValue(node.get("location"), "country"));
        }
        hotel.setLocation(location);

        Amenities amenities = new Amenities();
        if (node.has("amenities")) {
            amenities.setGeneral(getListValue(node.get("amenities"), "general"));
            amenities.setRoom(getListValue(node.get("amenities"), "room"));
        }
        hotel.setAmenities(amenities);

        Images images = new Images();
        if (node.has("images")) {
            JsonNode imagesNode = node.get("images");
            images.setRooms(getImageListValue(imagesNode, "rooms"));
            images.setAmenities(getImageListValue(imagesNode, "amenities"));
            images.setSite(getImageListValue(imagesNode, "site"));
        }
        hotel.setImages(images);

        if (node.has("booking_conditions")) {
            hotel.setBookingConditions(getListValue(node, "booking_conditions"));
        }

        return hotel;
    }

    @Override
    protected List<Image> getImageListValue(JsonNode node, String key) {
        JsonNode valueNode = node.get(key);
        if (valueNode != null && valueNode.isArray()) {
            List<Image> images = new ArrayList<>();
            for (JsonNode element : valueNode) {
                String url = getTextValue(element, "link"); // For Paperflies
                String description = getTextValue(element, "caption");
                if (url != null && !url.isEmpty() && description != null && !description.isEmpty()) {
                    Image image = new Image();
                    image.setLink(url);
                    image.setDescription(description);
                    images.add(image);
                }
            }
            return images;
        }
        return new ArrayList<>();
    }
}
