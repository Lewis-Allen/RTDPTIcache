-- README UNDER CONSTRUCTION --

[![Build Status](https://travis-ci.com/Lewis-Allen/RTDPTIcache.svg?token=1nzEHxQR3yx7r4oy4QzU&branch=master&kill_cache=1)](https://travis-ci.com/Lewis-Allen/RTDPTIcache) [![codecov](https://codecov.io/gh/Lewis-Allen/RTDPTIcache/branch/master/graph/badge.svg?token=6H5eGshUMx&kill_cache=1)](https://codecov.io/gh/Lewis-Allen/RTDPTIcache)
<br />

<p align="center">
  <!--<a href="https://github.com/othneildrew/Best-README-Template">
    <img src="logo.png" alt="Logo" width="80" height="80">
  </a>-->

  <h3 align="center">Real Time Displays for Public Transportation Information</h3>
  <p align="center">
    A Transport Information Dashboard Creation System
    <br />
      <!--
    <a href="https://github.com/othneildrew/Best-README-Template"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://github.com/othneildrew/Best-README-Template">View Demo</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Report Bug</a>
    ·
    <a href="https://github.com/othneildrew/Best-README-Template/issues">Request Feature</a>-->
  </p>

</p>

## Table of Contents

* [About the Project](#about-the-project)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Terminology](#terminology)
* [License](#license)
* [Contact](#contact)
* [Acknowledgements](#acknowledgements)

## About The Project

RTDPTI is a digital signage project with a focus on promoting the use of sustainable transport. A website that allows users to create and generate dashboards containing bus and train information in the Brighton and Hove area. Created as a Computer Science final year project and developed for the University of Brighton Environmental Team to allow for the creation of screens to display around university locations.

### Built With
* [Spring](https://spring.io/)
* [MySQL](https://www.mysql.com/)

## Getting Started

To get a local copy up and running follow these simple example steps. Ensure you have permission to use to data acquired from the Brighton and Hove SIRI server and National Rail API.

### Prerequisites

- Java 8
- MySQL

### Installation

Run using the maven wrapper. Use `./mvnw` for Batch or `./mvnw.cmd` for Windows in the following commands when specified.

1. Request an API key from the [National Rail Darwin Lite Webservice](http://realtime.nationalrail.co.uk/OpenLDBWSRegistration/)
2. Using MySQL, create an empty database instance.
3. Clone this Repository and navigate into the root directory.
4. In the `src/main/resources` directory - rename `application-template.properties` to `application.properties`
5. Fill in the fields in your new `application.properties` file with the appropriate values corresponding to your newly created database, and your API key, and the URL for the bus data.
6. Using the maven wrapper, populate the database using the provided migrations by running the following from the root directory - `./mvnw clean flyway:migrate -Dflyway.configFiles=src/main/resources/application.properties`
7. Run `./mvn clean package` to install and package the application. <!-- Run `./mvn generate-sources`-->
8. Run `./mvn java:exec` to start the application.

## Usage

To Do!

## Terminology

To Do!

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Contact

Email Lewis @ publiclraa@outlook.com

Project Link: [https://github.com/Lewis-Allen/RTDPTIcache](https://github.com/Lewis-Allen/RTDPTIcache)

## Acknowledgements
* ReadMe Template - [Best-README-Template](https://github.com/othneildrew/Best-README-Template)
