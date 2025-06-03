Feature: Booking API Tests

  Scenario: Create a new booking with valid data
    Given I have valid booking details
    When I send a POST request to create a booking
    Then the response status code should be 200
    And the booking should be created with a booking ID
