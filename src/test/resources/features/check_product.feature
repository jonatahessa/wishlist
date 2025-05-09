@wishlist @check
Feature: Check product in wishlist
  As a customer
  I want to check if a product is in my wishlist
  So that I can avoid duplicates

  Scenario: Product exists in wishlist
    Given the customer has a product with ID "prod-001" in the wishlist
    When I check for product with ID "prod-001" in the wishlist
    Then the response should indicate the product is present