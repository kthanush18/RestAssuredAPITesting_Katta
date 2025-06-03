package com.booking.stepdefinitions;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import java.util.HashMap;
import java.util.Map;

public class BookingSteps {

    private Response response;
    private Map<String, Object> bookingData;

    @Given("I have valid booking details")
    public void i_have_valid_booking_details() {
        Map<String, String> bookingDates = new HashMap<>();
        bookingDates.put("checkin", "2025-07-01");
        bookingDates.put("checkout", "2025-07-05");

        bookingData = new HashMap<>();
        bookingData.put("roomid", 1);
        bookingData.put("firstname", "John");
        bookingData.put("lastname", "Doe");
        bookingData.put("depositpaid", true);
        bookingData.put("email", "john.doe@example.com");
        bookingData.put("phone", "01234567890");
        bookingData.put("bookingdates", bookingDates);
    }

    @When("I send a POST request to create a booking")
    public void i_send_post_request_to_create_booking() {

        try {
            String payload = new ObjectMapper().writeValueAsString(bookingData);
            System.out.println("📦 JSON Payload Sent:\n" + payload);
        } catch (Exception e) {
            e.printStackTrace();
        }

        response = RestAssured
                .given()
                .baseUri("https://automationintesting.online/api")
                .basePath("/booking")
                .contentType(ContentType.JSON)
                .body(bookingData)
                .log().all()
                .when()
                .post();

        response.then().log().all();
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer expectedStatusCode) {
        Assertions.assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @Then("the booking should be created with a booking ID")
    public void booking_should_be_created_with_id() {
        Integer bookingId = response.jsonPath().getInt("bookingid");
        if (bookingId == null) {
            // Try fallback if wrapped in 'booking' object
            bookingId = response.jsonPath().getInt("booking.bookingid");
        }
        Assertions.assertNotNull(bookingId, "Booking ID was not returned in the response.");
    }
}
