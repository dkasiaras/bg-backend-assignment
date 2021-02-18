# MARS-REAL-ESTATE

mars-real-estate is a simple spring rest api that provides the endpoints for 

## Installation

Under the root folder there is the run.sh script to run the application<br />
This script is responsible for starting the db (MySql) build the project run the flyway script and run the spring application
```bash
docker-compose up -d && ./gradlew clean build &&  ./gradlew test && ./gradlew flywayClean && ./gradlew flywayMigrate && ./gradlew bootRun
```

## Usage

* GET /units &rarr; retrieve all units
    * request param sortByReviews=DESC/ASC &rarr; sorting by the best review rate
    * request param region=Thessalia/Macedonia/... &rarr; filter by region
    * request param title=Kalamaki/Volos/... &rarr; filter by title
* GET /units/{id} &rarr; retrieve unit by id 
* PUT /units/{unitId}/review &rarr; update review for a unit
    * body &rarr;  { "rate": 4, "comment": "comment" }
    * rate must be between 0 - 5 
    * comment is optional
* POST /authenticate &rarr; authenticate and retrieve jwt token
     * body &rarr;  {
                        "username": "george",
                        "password": "password1"
                    }

## Stack

* Java 11
* MySql 5.7
* Spring Boot 2.4.2
* flyway 7.5.3

## Test Data
In the flyway scripts there is the creation of the database schema and the insertion of some test data 

### USERS
    george password1 hasAuthorities  READ_UNITS,  WRITE_UNITS, READ_REVIEWS, WRITE_REVIEWS
    kostas password1 hasAuthorities  READ_UNITS,  WRITE_UNITS, READ_REVIEWS, WRITE_REVIEWS
    eleni  password1 hasAuthorities  READ_UNITS,  WRITE_UNITS, READ_REVIEWS, WRITE_REVIEWS
    maria  password1 hasAuthorities  READ_UNITS,  WRITE_UNITS, READ_REVIEWS, WRITE_REVIEWS
    nefeli password1 hasAuthorities  READ_UNITS, READ_REVIEWS
    
### UNITS
    you can check the units data in from the file src/main/resources/db/migration/V3__add-test-data-script.sql
    
## Postman Collection

Use the mars.postman_collection.json for some sample requests
