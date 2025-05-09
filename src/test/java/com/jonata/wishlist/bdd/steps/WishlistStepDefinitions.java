package com.jonata.wishlist.bdd.steps;

import com.jonata.wishlist.infrastructure.dto.ProductPresenceResponse;
import com.jonata.wishlist.infrastructure.dto.ProductRequest;
import com.jonata.wishlist.infrastructure.dto.WishlistResponse;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WishlistStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    private ResponseEntity<?> response;
    private HttpClientErrorException exception;
    private String currentCustomerId;
    private String currentProductId;
    private String baseUrl;

    @Before
    public void setup() {
        currentCustomerId = "customer-" + System.currentTimeMillis();
        baseUrl = "http://localhost:" + port + "/api/wishlist";
    }

    @Given("the customer has no wishlist")
    public void the_customer_has_an_empty_wishlist() {
        // Implícito pelo setup
    }

    @Given("the customer has a product with ID {string} in the wishlist")
    public void the_customer_has_product_in_wishlist(String productId) {
        currentProductId = productId;
        addProductToWishlist(productId, "Existing Product", 19.99);
    }

    @Given("the customer has {int} products in the wishlist")
    public void customer_has_products_in_wishlist(int count) {
        for (int i = 1; i <= count; i++) {
            addProductToWishlist("prod-" + i, "Product " + i, 10.00 + i);
        }
    }

    @When("I add a product with ID {string}, name {string} and price {double} to the wishlist")
    public void i_add_a_product_to_wishlist(String productId, String name, double price) {
        currentProductId = productId;
        ProductRequest request = ProductRequest.builder()
                .productId(productId)
                .name(name)
                .price(price)
                .build();

        try {
            response = restTemplate.postForEntity(
                    baseUrl + "/" + currentCustomerId + "/products",
                    request,
                    WishlistResponse.class
            );
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @When("I remove the product with ID {string} from the wishlist")
    public void i_remove_product_from_wishlist(String productId) {
        currentProductId = productId;
        try {
            restTemplate.delete(baseUrl + "/" + currentCustomerId + "/products/" + productId);
            response = ResponseEntity.noContent().build();
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @When("I request the wishlist")
    public void i_request_the_wishlist() {
        try {
            response = restTemplate.getForEntity(
                    baseUrl + "/" + currentCustomerId,
                    WishlistResponse.class
            );
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @When("I check if product with ID {string} is in the wishlist")
    public void i_check_product_in_wishlist(String productId) {
        currentProductId = productId;
        try {
            response = restTemplate.getForEntity(
                    baseUrl + "/" + currentCustomerId + "/products/" + productId,
                    ProductPresenceResponse.class
            );
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @Then("the product should be added successfully")
    public void product_should_be_added() {
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertTrue(body.getProducts().stream()
                .anyMatch(p -> p.getProductId().equals(currentProductId)));
    }

    @Then("the response should contain the product with ID {string}")
    public void response_should_contain_product(String productId) {
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertTrue(body.getProducts().stream()
                .anyMatch(p -> p.getProductId().equals(productId)));
    }

    @Then("the response should return a bad request error")
    public void response_should_return_bad_request_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    @Then("the product should be removed successfully")
    public void product_should_be_removed_successfully() {
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Then("the response should contain {int} products")
    public void response_should_contain_products(int expectedCount) {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertEquals(expectedCount, body.getProducts().size());
    }

    @Then("the response should indicate the product is present")
    public void response_should_indicate_product_is_present() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductPresenceResponse body = (ProductPresenceResponse) response.getBody();
        assertTrue(body.isPresent());
    }

    private void addProductToWishlist(String productId, String name, double price) {
        ProductRequest request = ProductRequest.builder()
                .productId(productId)
                .name(name)
                .price(price)
                .build();
        restTemplate.postForEntity(
                baseUrl + "/" + currentCustomerId + "/products",
                request,
                WishlistResponse.class
        );
    }
}