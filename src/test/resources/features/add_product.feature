@wishlist @add
Feature: Add products to wishlist
  As a customer
  I want to add products to my wishlist
  So that I can save items for later

  Scenario: Successfully add a product to empty wishlist
    Given the customer has no wishlist
    When I add a product with ID "prod-001", name "Smartphone" and price 999.99 to the wishlist
    Then the product should be added successfully
    And the response should contain the product with ID "prod-001"

  Scenario: Fail to add duplicate product
    Given the customer has a product with ID "prod-001" in the wishlist
    When I add a product with ID "prod-001", name "Smartphone" and price 999.99 to the wishlist
    Then the response should return a bad request error