# Wishlist Service

A REST API for managing customer wishlists built with Spring Boot and MongoDB.

## Prerequisites

- Java 17+
- Docker 20.10+
- Docker Compose 2.5+
- Maven 3.8+

## Getting Started

### 1. Clone the repository
```bash
  git clone https://github.com/your-repo/wishlist.git
```
cd wishlist
### 2. Build the project
```bash
  ./mvnw clean package
```
### 3. Running the Application
#### Option 1: Using Docker Compose (Recommended)
```bash
  docker-compose up -d --build
```
The API will be available at:
http://localhost:8080

Option 2: Running Locally
```bash
  ./mvnw spring-boot:run
```
Running Tests

#### All Tests
```bash
  ./mvnw test
```
Only Unit Tests

```bash
  ./mvnw test -Dtest=*UnitTest
```
### Only BDD Tests (Cucumber)
```bash
  ./mvnw test -Dtest=CucumberIT
```
#### Test Reports
After running tests, view reports at:
target/cucumber-reports/cucumber.html

### API Documentation
Swagger UI is available at:
http://localhost:8080/swagger-ui.html

### Test Structure
BDD features are located in:

src/test/resources/features/

    features/
    ├── add_product.feature
    ├── remove_product.feature
    ├── get_wishlist.feature
    └── check_product.feature
### Debugging in IntelliJ
#### Install plugins:

Cucumber for Java

JUnit

To run BDD tests:

Right-click CucumberIT.java → "Run"

To debug:

Set breakpoints in step definitions

Run in debug mode

Environment Variables
Configuration can be modified in:

application.properties (main config)

application-test.properties (test config)

Cleanup
To stop and remove all containers:

```bash
  docker-compose down -v
```
License
This project is licensed under the MIT License.