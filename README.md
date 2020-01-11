# Aplikasi Keuangan Keluarga
[![pipeline status](https://gitlab.com/aplikasi-keuangan-keluarga/apkeukel-core/badges/master/pipeline.svg)](https://gitlab.com/aplikasi-keuangan-keluarga/apkeukel-core/commits/master)
[![coverage report](https://gitlab.com/aplikasi-keuangan-keluarga/apkeukel-core/badges/master/coverage.svg)](https://gitlab.com/aplikasi-keuangan-keluarga/apkeukel-core/commits/master)

A simple family finance management software that I develop as the assignment for my "GUI Programming" class final project.

Within this project, I try to learn and use various frameworks, tools, design patterns, as well as to follow great principles and best practices in software development.

## About this repository
This particular repository is the core package of the software. It contains the APIs of the "business" logic of the software. The data access logic also lies within this package.

I separate the presentation logic (the UI) from this package therefore I can reuse this core package in my future classes' assignment (e.g. Web and Mobile programming).

## Methodology
I use the [test-driven development (TDD)](https://en.wikipedia.org/wiki/Test-driven_development) technique in combination with [continuous integration (CI)](https://en.wikipedia.org/wiki/Continuous_integration) to develop the software.

## Technologies
*  Platform: Java 8
*  Build tool: Gradle 5+
*  Continuous integration: GitLab CI
*  Unit test framework: JUnit 4.12
*  Mocking framework: Mockito 1.+
*  Dependency injection framework: Guice 4.2.2
*  Database: SQLite 3.30.1
*  Micro-ORM framework: Norm 0.8.6

## Project status
The core package is now in 1.0-beta version. See the package's [milestones](https://gitlab.com/aplikasi-keuangan-keluarga/apkeukel-core/-/milestones) to see what is currently going on.

See Aplikasi Keuangan Keluarga's [group page](https://gitlab.com/aplikasi-keuangan-keluarga) to see other activities related to the software.