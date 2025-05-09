package com.jonata.wishlist.bdd.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@CucumberContextConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class CucumberSpringConfig {
}