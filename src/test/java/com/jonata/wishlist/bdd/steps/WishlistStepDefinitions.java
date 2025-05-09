package com.jonata.wishlist.bdd.steps;

import com.jonata.wishlist.domain.entity.Wishlist;
import com.jonata.wishlist.domain.repository.WishlistRepository;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WishlistStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private WishlistRepository wishlistRepository;

    private RestTemplate restTemplate = new RestTemplate();
    private String baseUrl;
    private ResponseEntity<?> response;
    private HttpClientErrorException exception;
    private String currentCustomerId = "customer-123";
    private String currentProductId = "prod-001";

    @Before
    public void setup() {
        baseUrl = "http://localhost:" + port + "/api/wishlist";
        wishlistRepository.deleteAll();
    }

    @Given("a customer with ID {string} has no wishlist")
    public void customer_has_no_wishlist(String customerId) {
        this.currentCustomerId = customerId;
        assertFalse(wishlistRepository.findByCustomerId(customerId).isPresent());
    }

    @Given("a customer with ID {string} has a product with ID {string} in their wishlist")
    public void customer_has_product_in_wishlist(String customerId, String productId) {
        this.currentCustomerId = customerId;
        this.currentProductId = productId;

        Wishlist wishlist = wishlistRepository.findByCustomerId(customerId)
                .orElse(Wishlist.builder().customerId(customerId).build());

        wishlist.addProduct(Wishlist.Product.builder()
                .productId(productId)
                .name("Test Product")
                .price(19.99)
                .build());

        wishlistRepository.save(wishlist);
    }

    @Given("a customer with ID {string} has a full wishlist \\(20 products)")
    public void customer_has_full_wishlist(String customerId) {
        this.currentCustomerId = customerId;
        Wishlist wishlist = Wishlist.builder().customerId(customerId).build();

        for (int i = 1; i <= 20; i++) {
            wishlist.addProduct(Wishlist.Product.builder()
                    .productId("prod-" + i)
                    .name("Product " + i)
                    .price(10.00 + i)
                    .build());
        }

        wishlistRepository.save(wishlist);
    }

    @When("I add a product with ID {string}, name {string} and price {double} to the wishlist")
    public void add_product_to_wishlist(String productId, String name, double price) {
        this.currentProductId = productId;
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
    public void product_added_successfully() {
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertTrue(body.getProducts().stream()
                .anyMatch(p -> p.getProductId().equals(currentProductId)));
    }

    @Then("the response should contain the product with ID {string}")
    public void response_contains_product(String productId) {
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertTrue(body.getProducts().stream()
                .anyMatch(p -> p.getProductId().equals(productId)));
    }

    @Then("the response should be a bad request error")
    public void bad_request_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }

    // ===== Remove Product Steps =====
    @When("I remove the product with ID {string} from the wishlist")
    public void remove_product_from_wishlist(String productId) {
        this.currentProductId = productId;
        try {
            restTemplate.delete(baseUrl + "/" + currentCustomerId + "/products/" + productId);
            response = ResponseEntity.noContent().build();
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @Then("the product should be successfully removed")
    public void product_removed_successfully() {
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Optional<Wishlist> wishlist = wishlistRepository.findByCustomerId(currentCustomerId);
        assertTrue(wishlist.isPresent());
        assertFalse(wishlist.get().containsProduct(currentProductId));
    }

    @Given("a customer with ID {string} has {int} products in their wishlist")
    public void customer_has_products_in_wishlist(String customerId, int count) {
        this.currentCustomerId = customerId;
        Wishlist wishlist = Wishlist.builder().customerId(customerId).build();

        for (int i = 1; i <= count; i++) {
            wishlist.addProduct(Wishlist.Product.builder()
                    .productId("prod-" + i)
                    .name("Product " + i)
                    .price(10.00 + i)
                    .build());
        }

        wishlistRepository.save(wishlist);
    }

    @When("I retrieve the wishlist")
    public void retrieve_wishlist() {
        try {
            response = restTemplate.getForEntity(
                    baseUrl + "/" + currentCustomerId,
                    WishlistResponse.class
            );
        } catch (HttpClientErrorException e) {
            exception = e;
        }
    }

    @Then("the response should contain {int} products")
    public void response_contains_products(int count) {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        WishlistResponse body = (WishlistResponse) response.getBody();
        assertEquals(count, body.getProducts().size());
    }

    @When("I check for product with ID {string} in the wishlist")
    public void check_product_in_wishlist(String productId) {
        this.currentProductId = productId;
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
    public void product_is_present() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductPresenceResponse body = (ProductPresenceResponse) response.getBody();
        assertTrue(body.isPresent());
    }

    @Then("the response should indicate the product is not present")
    public void product_is_not_present() {
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ProductPresenceResponse body = (ProductPresenceResponse) response.getBody();
        assertFalse(body.isPresent());
    }

    @Then("the response should be a not found error")
    public void not_found_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Then("the response should be an unprocessable entity error")
    public void unprocessable_entity_error() {
        assertNotNull(exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatusCode());
    }

    @Then("the error message should indicate the product already exists")
    public void error_message_product_exists() {
        assertTrue(exception.getResponseBodyAsString().contains("already exists"));
    }

    @Then("the error message should indicate the wishlist is full")
    public void error_message_wishlist_full() {
        assertTrue(exception.getResponseBodyAsString().contains("limit exceeded"));
    }

    @Then("the error message should indicate the product was not found")
    public void error_message_product_not_found() {
        assertTrue(exception.getResponseBodyAsString().contains("not found"));
    }
}