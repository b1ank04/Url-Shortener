# URL Shortening API

## Overview

The URL Shortening API provides a reactive service to shorten URLs with expiration date support. 

It is built using Kotlin with Ktor, and utilizes Redis for data storage.

## Features

- Shorten long URLs.
- Optional expiration date for shortened URLs.
- Simple and intuitive API design.

## API Documentation

### Base URL

The base URL for the API is:

```
http://localhost:8080
```
### Swagger

You can access the Swagger documentation for the API at:
```
http://localhost:8080/swagger
```

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/get-started) and [Docker Compose](https://docs.docker.com/compose/) installed on your machine.

### Running the Application

1. Clone this repository:

   ```bash
   git clone https://github.com/b1ank04/Url-Shortener
   cd Url-Shortener
   ```

2. Build and run the services using Docker Compose:

   ```bash
   docker-compose up --build
   ```

## Configuration

### Redis Configuration

You can customize the Redis configuration by modifying the `redis.conf` file located in the /infra package of the project.

## Volume Persistence

The Redis service uses a Docker volume named `redis_data` to persist data. This ensures that your data remains intact even when the containers are stopped.
