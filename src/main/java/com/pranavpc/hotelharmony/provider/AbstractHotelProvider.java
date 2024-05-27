package com.pranavpc.hotelharmony.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pranavpc.hotelharmony.model.hotel.Hotel;
import com.pranavpc.hotelharmony.model.hotel.Image;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class AbstractHotelProvider implements HotelDataFeedProvider {

    protected final RestTemplate restTemplate = new RestTemplate();
    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected List<Hotel> fetchHotelsFromUrl(String url, boolean enabled) {
        if (!enabled) return new ArrayList<>();

        List<Hotel> hotels = new ArrayList<>();
        try {
            JsonNode nodes = restTemplate.getForObject(url, JsonNode.class);
            if (nodes != null) {
                hotels = StreamSupport.stream(nodes.spliterator(), false)
                        .map(this::mapToHotel)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hotels;
    }

    protected abstract Hotel mapToHotel(JsonNode node);

    protected String getTextValue(JsonNode node, String key) {
        JsonNode valueNode = node.get(key);
        return valueNode != null ? normalize(valueNode.asText()) : null;
    }

    protected int getIntValue(JsonNode node, String key) {
        JsonNode valueNode = node.get(key);
        return valueNode != null ? valueNode.asInt() : 0;
    }

    protected Double getDoubleValue(JsonNode node, String key) {
        JsonNode valueNode = node.get(key);
        if (valueNode != null && !valueNode.asText().isEmpty()) {
            Double result = valueNode.asDouble();
            return result != 0.0 ? result : null;
        }
        return null;
    }

    protected List<String> getListValue(JsonNode node, String key) {
        JsonNode valueNode = node.get(key);
        if (valueNode != null && valueNode.isArray()) {
            List<String> values = new ArrayList<>();
            for (JsonNode element : valueNode) {
                String value = normalize(element.asText());
                if (value != null && !value.isEmpty()) {
                    values.add(value);
                }
            }
            return values;
        }
        return new ArrayList<>();
    }

    protected List<Image> getImageListValue(JsonNode node, String key) {
        JsonNode valueNode = node.get(key);
        if (valueNode != null && valueNode.isArray()) {
            List<Image> images = new ArrayList<>();
            for (JsonNode element : valueNode) {
                String url = getTextValue(element, "url");
                String description = getTextValue(element, "description");
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

    protected String normalize(String value) {
        return value != null ? value.trim() : null;
    }

    protected String transformToLowerCasedWords(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        // Replace underscores with spaces and convert to lowercase
        return input.replaceAll("_", " ").toLowerCase();
    }
}
