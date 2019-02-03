# RTDPTIcache

[![Build Status](https://travis-ci.com/Lewis-Allen/RTDPTIcache.svg?token=1nzEHxQR3yx7r4oy4QzU&branch=master)](https://travis-ci.com/Lewis-Allen/RTDPTIcache) [![codecov](https://codecov.io/gh/Lewis-Allen/RTDPTIcache/branch/master/graph/badge.svg?token=6H5eGshUMx)](https://codecov.io/gh/Lewis-Allen/RTDPTIcache)

Caching Server for the Real Time Displays for Public Transportation Information application.

## Deployment Instructions

1. Create an empty database instance to store naptan codes.
2. Clone this repository and navigate into the root directory.
3. Make a copy of the *.env.example* file in the root directory named *.env*. Place this file in the root directory and replace the values inside the file with values corresponding to your setup.
4. Make a copy of the *myFlywayConfig.properties.example* file in the root directory named *myFlywayConfig.properties*. Place this file in the root directory and replace the values inside the file with values corresponding to your setup.
5. Run `mvn clean flyway:migrate -Dflyway.configFiles=myFlywayConfig.properties` to create and populate the naptan database.
6. Run `mvn generate-sources`
7. Run `mvn clean install`
8. Run `mvn java:exec`

## To Do
- Add prerequisites section.
- Add mvn wrapper.
