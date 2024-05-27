package com.pranavpc.hotelharmony;

import com.pranavpc.hotelharmony.model.hotel.Hotel;
import com.pranavpc.hotelharmony.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HotelHarmonyApplicationTests {

	@Autowired
	private HotelService hotelService;

	@Test
	void contextLoads() {
		// Test to check if the context loads successfully
	}

	@Test
	void fetchAndMergeHotelData() {
		// Fetch and merge hotel data
		List<Hotel> hotels = hotelService.fetchAndMergeHotelData();

		// Check that the hotel list is not empty
		assertThat(hotels).isNotEmpty();

		// Check that the hotels have valid data
		for (Hotel hotel : hotels) {
			assertThat(hotel.getId()).isNotNull();
			assertThat(hotel.getName()).isNotNull();
			assertThat(hotel.getLocation()).isNotNull();
		}
	}

	@Test
	void fetchHotelsByDestination() {
		// Fetch hotels by destination ID
		List<Hotel> hotels = hotelService.fetchAndMergeHotelData()
				.stream().filter(i -> i.getDestinationId() == 5432)
				.toList();

		// Check that the hotel list is not empty
		assertThat(hotels).isNotEmpty();

		// Check that all hotels have the expected destination ID
		for (Hotel hotel : hotels) {
			assertThat(hotel.getDestinationId()).isEqualTo(5432);
		}
	}

	@Test
	void fetchHotelsByIds() {
		// Fetch hotels by a list of IDs
		List<String> hotelIds = List.of("iJhz", "SjyX");

		List<Hotel> hotels = hotelService.fetchAndMergeHotelData()
				.stream()
				.filter((i) -> hotelIds.contains(i.getId()))
				.toList();

		// Check that the hotel list is not empty
		assertThat(hotels).isNotEmpty();

		// Check that all hotels have the expected IDs
		for (Hotel hotel : hotels) {
			assertThat(hotelIds).contains(hotel.getId());
		}
	}
}
