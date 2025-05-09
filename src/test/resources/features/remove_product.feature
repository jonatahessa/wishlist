@wishlist @remove
Feature: Remove products from wishlist
  As a customer
  I want to remove products from my wishlist
  So that I can keep my list updated

  Scenario: Successfully remove a product
    Given the customer has a product with ID "prod-001" in the wishlist
    When I remove the product with ID "prod-001" from the wishlist
    Then the product should be removed successfully