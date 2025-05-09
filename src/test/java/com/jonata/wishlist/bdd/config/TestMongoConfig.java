package com.jonata.wishlist.bdd.config;

import com.mongodb.client.MongoClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;

@TestConfiguration
public class TestMongoConfig {

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(MongoClients.create("mongodb://root:example@localhost:27017/wishlist-test?authSource=admin"), "wishlist-test");
    }
}