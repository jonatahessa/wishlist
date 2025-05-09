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
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Given("the customer has an empty wishlist")
    public void the_customer_has_an_empty_wishlist() {
        // Implícito pelo setup
    }

    @Given("the customer already has a product with ID {string} in the wishlist")
    public void the_customer_has_product_in_wishlist(String productId) {
        currentProductId = productId;
        addProductToWishlist(productId, "Existing Product", 19.99);
    }

    @Given("the customer has a full wishlist")
    public void the_customer_has_a_full_wishlist() {
        for (int i = 1; i <= 20; i++) {
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

    @Then("the product should be successfully added")
    public void product_should_be_added() {
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertTrue(body.getProducts().stream()
                .anyMatch(p -> p.getProductId().equals(currentProductId)));
    }

    @Then("I should receive a duplicate product error")
    public void should_receive_duplicate_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertTrue(exception.getResponseBodyAsString().contains("already exists"));
    }

    @Then("I should receive a wishlist full error")
    public void should_receive_wishlist_full_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
        assertTrue(exception.getResponseBodyAsString().contains("limit exceeded"));
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

    @Then("the product should be successfully removed")
    public void product_should_be_removed() {
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Then("I should receive a product not found error")
    public void should_receive_product_not_found_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Given("the customer has {int} products in the wishlist")
    public void customer_has_products_in_wishlist(int count) {
        for (int i = 1; i <= count; i++) {
            addProductToWishlist("prod-" + i, "Product " + i, 10.00 + i);
        }
    }

    @When("I retrieve the customer's wishlist")
    public void i_retrieve_wishlist() {
        try {
            response = restTemplate.getForEntity(
                    baseUrl + "/" + currentCustomerId,
                    WishlistResponse.class
            );
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @Then("I should receive the wishlist with {int} products")
    public void should_receive_wishlist_with_products(int count) {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertEquals(count, body.getProducts().size());
    }

    // ========== CHECK PRODUCT STEPS ==========
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

    @Then("the response should indicate the product is present")
    public void response_should_indicate_product_present() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductPresenceResponse body = (ProductPresenceResponse) response.getBody();
        assertTrue(body.isPresent());
    }

    @Then("the response should indicate the product is not present")
    public void response_should_indicate_product_not_present() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductPresenceResponse body = (ProductPresenceResponse) response.getBody();
        assertFalse(body.isPresent());
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