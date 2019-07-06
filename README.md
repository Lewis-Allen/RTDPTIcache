# RTDPTI
README UNDER CONSTRUCTION.

[![Build Status](https://travis-ci.com/Lewis-Allen/RTDPTIcache.svg?token=1nzEHxQR3yx7r4oy4QzU&branch=master)](https://travis-ci.com/Lewis-Allen/RTDPTIcache) [![codecov](https://codecov.io/gh/Lewis-Allen/RTDPTIcache/branch/master/graph/badge.svg?token=6H5eGshUMx)](https://codecov.io/gh/Lewis-Allen/RTDPTIcache)

Real Time Displays for Public Transportation Information application.


## OUTDATED - Deployment Instructions (With Maven)

1. Create an empty database instance to store bus and train codes.
2. Clone this Repository and navigate into the root directory.
3. Make a copy of the *.env.example* file in the root directory named *.env*. Place this file in the root directory and replace the values inside the file with values corresponding to your setup.
4. Populate the bus database by running the provided migrations. - Run from root directory `./mvnw flyway:migrate -Dflyway.user=<DB_USERNAME> -Dflyway.password=<DB_PASSWORD> -Dflyway.schemas=<DB_SCHEMA> -Dflyway.url=<DB_URL> -Dflyway.locations=filesystem:db/migration`
6. Run `./mvn generate-sources`
7. Run `./mvn clean install`
8. Run `./mvn java:exec`