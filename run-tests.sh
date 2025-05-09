#!/bin/bash

docker compose -f docker-compose-test.yml up -d

while ! docker exec mongodb-test-cucumber mongosh --eval "db.adminCommand('ping')" >/dev/null 2>&1; do
    sleep 1
done

./mvnw test

docker compose -f docker-compose-test.yml down