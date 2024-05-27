# Hotel Harmony App

Hotel Harmony is a data aggregation service that fetches hotel data from multiple suppliers, merges and sanitizes the data, and provides a unified API endpoint to query the hotels. The project is built using Spring Boot and provides caching to optimize performance.

## Data Merging Strategy

The application fetches hotel data from three different suppliers and merges them into a unified model. Here are the strategies used:

1. **Normalization**:
   - Data values are stripped of leading and trailing spaces.
   - Data values are converted to a consistent case depending on the attribute.

2. **Sanitization**:
   - Null and empty values are filtered out.
   - Duplicate images based on descriptions are removed.

3. **Merging**:
   - The merging process ensures completeness by selecting the longest strings when merging fields.
   - For amenities and facilities, duplicates are removed and the values are normalized.
   - Location data is consolidated into a single object.

4. **Handling Missing Values**:
   - If any attributes are missing or invalid, they are set to null in API output.

## Caching Strategy

To optimize performance and reduce load on the backend services, the application implements caching with the following strategy:

- **Cache Duration**: Data is cached for 1 hour using Cache-Control header values with s-maxage and max-age directives.
- **Cache Scope**: High-level infrastructure components like load balancers and API gateways cache the response to improve efficiency and reduce latency.

## Testing Strategy

The testing strategy for Hotel Harmony ensures that major application flows are covered comprehensively. The test file, located at `src/test/java/com/pranavpc/hotelharmony/HotelHarmonyApplicationTests.java`, includes several key tests:

### Test Coverage

1. **Context Load Test**:
   - Ensures that the Spring application context loads successfully.

2. **Fetch and Merge Hotel Data**:
   - Verifies that hotel data is fetched and merged correctly.
   - Ensures the hotel list is not empty and contains valid data.

3. **Fetch Hotels by Destination**:
   - Tests fetching hotels by a specific destination ID.
   - Confirms that the hotel list is not empty and that all hotels have the expected destination ID.

4. **Fetch Hotels by IDs**:
   - Validates fetching hotels by a list of specific IDs.
   - Ensures the hotel list is not empty and that all hotels have the expected IDs.

These tests cover major application flows, including data fetching, merging, and filtering by different criteria, ensuring that Hotel Harmony's core functionalities are tested.

## How to Run and Deploy

### Prerequisites

- JDK 17
- Maven
- Docker (optional, for containerized deployment)

### Running Locally

1. **Extract the Zip File**:
   - Extract the provided zip file to a desired location on your machine.

2. **Import the Project into IntelliJ IDEA**:
   - Open IntelliJ IDEA.
   - Go to `File > New > Project from Existing Sources`.
   - Select the extracted project folder.
   - Choose `Import project from external model` and select `Maven`.
   - Click `Finish`.

3. **Build the Project**:
    ```bash
    mvn clean install
    ```

4. **Run the Application**:
    ```bash
    mvn spring-boot:run
    ```

### Running with Docker

1. **Build the Docker Image**:
    ```bash
    mvn dockerfile:build
    ```

2. **Run the Docker Container**:
    ```bash
    docker run -p 8080:8080 docker-repo/hotel-data-merge:0.0.1-SNAPSHOT
    ```

### Deployment Strategy
As the application is stateless and multiple instances of it can run simultaneously, we can use the built container artifact to deploy them on Kubernetes or any OCI container platform.


### API Documentation

The API provides a unified endpoint to query hotel data. Detailed API documentation can be accessed at `/swagger-ui.html` after running the application.

#### Endpoint: `/api/hotels`

This endpoint allows you to fetch hotel data based on either a list of hotel IDs or a destination ID. Each hotel will be returned only once, ensuring the data is uniquely merged.

**Method**: `GET`

**Parameters**:

- `destination`: (Optional) The ID of the destination to fetch hotels for.
- `hotels`: (Optional) A comma-separated list of hotel IDs to fetch specific hotels.

**Example Requests**:

1. **Fetch hotels by destination ID**:
    ```bash
    GET /api/hotels?destination=12345
    ```

2. **Fetch hotels by hotel IDs**:
    ```bash
    GET /api/hotels?hotels=1,2,3,4,5
    ```

**Response**:

The response will be a JSON array of hotel objects, with each hotel object containing the following fields:

- `id`: The unique identifier of the hotel.
- `destination_id`: The ID of the destination where the hotel is located.
- `name`: The name of the hotel.
- `location`: An object containing the latitude, longitude, address, city, and country of the hotel.
- `description`: A detailed description of the hotel.
- `amenities`: An object containing lists of general and room amenities offered by the hotel.
- `images`: An object containing lists of image URLs categorized by rooms, site, and amenities.
- `booking_conditions`: A list of booking conditions for the hotel.

**Example Response**:
```json
[
  {
    "id": "iJhz",
    "destination_id": 5432,
    "name": "Beach Villas Singapore",
    "location": {
      "lat": 1.264751,
      "lng": 103.824006,
      "address": "8 Sentosa Gateway, Beach Villas, 098269",
      "city": "Singapore",
      "country": "Singapore"
    },
    "description": "Surrounded by tropical gardens, these upscale villas in elegant Colonial-style buildings are part of the Resorts World Sentosa complex and a 2-minute walk from the Waterfront train station. Featuring sundecks and pool, garden or sea views, the plush 1- to 3-bedroom villas offer free Wi-Fi and flat-screens, as well as free-standing baths, minibars, and tea and coffeemaking facilities. Upgraded villas add private pools, fridges and microwaves; some have wine cellars. A 4-bedroom unit offers a kitchen and a living room. There's 24-hour room and butler service. Amenities include posh restaurant, plus an outdoor pool, a hot tub, and free parking.",
    "amenities": {
      "general": ["outdoor pool", "indoor pool", "business center", "childcare", "wifi", "dry cleaning", "breakfast"],
      "room": ["aircon", "tv", "coffee machine", "kettle", "hair dryer", "iron", "bathtub"]
    },
    "images": {
      "rooms": [
        { "link": "https://d2ey9sqrvkqdfs.cloudfront.net/0qZF/2.jpg", "description": "Double room" },
        { "link": "https://d2ey9sqrvkqdfs.cloudfront.net/0qZF/3.jpg", "description": "Double room" },
        { "link": "https://d2ey9sqrvkqdfs.cloudfront.net/0qZF/4.jpg", "description": "Bathroom" }
      ],
      "site": [
        { "link": "https://d2ey9sqrvkqdfs.cloudfront.net/0qZF/1.jpg", "description": "Front" }
      ],
      "amenities": [
        { "link": "https://d2ey9sqrvkqdfs.cloudfront.net/0qZF/0.jpg", "description": "RWS" }
      ]
    },
    "booking_conditions": [
      "All children are welcome. One child under 12 years stays free of charge when using existing beds. One child under 2 years stays free of charge in a child's cot/crib. One child under 4 years stays free of charge when using existing beds. One older child or adult is charged SGD 82.39 per person per night in an extra bed. The maximum number of children's cots/cribs in a room is 1. There is no capacity for extra beds in the room.",
      "Pets are not allowed.",
      "WiFi is available in all areas and is free of charge.",
      "Free private parking is possible on site (reservation is not needed).",
      "Guests are required to show a photo identification and credit card upon check-in. Please note that all Special Requests are subject to availability and additional charges may apply. Payment before arrival via bank transfer is required. The property will contact you after you book to provide instructions. Please note that the full amount of the reservation is due before arrival. Resorts World Sentosa will send a confirmation with detailed payment information. After full payment is taken, the property's details, including the address and where to collect keys, will be emailed to you. Bag checks will be conducted prior to entry to Adventure Cove Waterpark. === Upon check-in, guests will be provided with complimentary Sentosa Pass (monorail) to enjoy unlimited transportation between Sentosa Island and Harbour Front (VivoCity). === Prepayment for non refundable bookings will be charged by RWS Call Centre. === All guests can enjoy complimentary parking during their stay, limited to one exit from the hotel per day. === Room reservation charges will be charged upon check-in. Credit card provided upon reservation is for guarantee purpose. === For reservations made with inclusive breakfast, please note that breakfast is applicable only for number of adults paid in the room rate. Any children or additional adults are charged separately for breakfast and are to paid directly to the hotel."
    ]
  }
]
```
**Notes**:

- Either the `destination` or `hotels` parameter must be provided.
- If both parameters are provided, the `hotels` parameter will take precedence.
- The response will ensure each hotel is returned only once.

### Further considerations / optimizations

- Handling API failures from providers - Rate limiting and circuit breakers
- Caching strategy per provider response
- Caching strategy for API Consumers
- Running workers jobs to fetch data from providers and performing the merge / sanitization

