[![Build Status](https://travis-ci.org/trevorgowing/backend-coding-challenge.svg?branch=master)](https://travis-ci.org/trevorgowing/backend-coding-challenge)
[![codecov](https://codecov.io/gh/trevorgowing/backend-coding-challenge/branch/master/graph/badge.svg)](https://codecov.io/gh/trevorgowing/backend-coding-challenge)
# Expense List

A simple expense list web application. A solution to [Engage Technology Partners](http://www.engagetech.com/) [Backend Coding Challenge](https://github.com/engagetech/backend-coding-challenge).

# Developer Documentation
The application is written in [Java](http://openjdk.java.net/) using the [Spring Boot](https://projects.spring.io/spring-boot/) framework and is built with [Gradle](https://gradle.org/).

This repository includes [Gradle's Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) for running and testing the application so no need to install any additional tooling to run or test this application.

Check out the continuous application build status at [Travis](https://travis-ci.org/trevorgowing/backend-coding-challenge) and test reports and coverage at [CodeCov](https://codecov.io/gh/trevorgowing/backend-coding-challenge).

## Running the application

From linux with: `./gradlew bootrun`.
From windows with: `gradlew bootrun`.

## Running the tests

From linux with: `./gradlew test`.
From windows with: `gradlew test`.

## Generating test reports

From linux with: `./gradlew jacocoTestReport`
From windows with: `gradlew jacocoTestReport`