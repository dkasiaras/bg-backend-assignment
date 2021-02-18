#!/usr/bin/env bash
docker-compose up -d && ./gradlew clean build &&  ./gradlew test && ./gradlew flywayClean && ./gradlew flywayMigrate && ./gradlew bootRun
