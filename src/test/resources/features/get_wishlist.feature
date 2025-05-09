@wishlist @get
Feature: View wishlist contents
  As a customer
  I want to view my wishlist
  So that I can see all saved items

  Scenario: Get wishlist with products
    Given the customer has 3 products in the wishlist
    When I request the wishlist
    Then the response should contain 3 products