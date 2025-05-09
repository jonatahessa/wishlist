@wishlist @add
Feature: Add product to wishlist
  As a customer
  I want to add products to my wishlist
  So that I can save items I'm interested in

  Scenario: Successfully add a product to an empty wishlist
    Given a customer with ID "customer-123" has no wishlist
    When I add a product with ID "prod-001", name "Premium Smartphone" and price 999.99 to the wishlist
    Then the product should be successfully added
    And the response should contain the product with ID "prod-001"

  Scenario: Fail to add a duplicate product
    Given a customer with ID "customer-123" has a product with ID "prod-001" in their wishlist
    When I add a product with ID "prod-001", name "Premium Smartphone" and price 999.99 to the wishlist
    Then the response should be a bad request error
    And the error message should indicate the product already exists

  Scenario: Fail when wishlist is full
    Given a customer with ID "customer-123" has a full wishlist (20 products)
    When I add a product with ID "prod-100", name "Extra Item" and price 1.99 to the wishlist
    Then the response should be an unprocessable entity error
    And the error message should indicate the wishlist is full