#!/bin/bash

# Start test containers
docker compose -f docker-compose-test.yml up -d

# Wait for MongoDB to be ready
while ! docker exec mongodb-test-cucumber mongosh --eval "db.adminCommand('ping')" >/dev/null 2>&1; do
    sleep 1
done

# Run tests (adjust as needed for your build tool)
./mvnw test

# Stop and remove containers
docker compose -f docker-compose-test.yml down