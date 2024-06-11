# Depth Chart Management System

## Overview

The Depth Chart Management System is a Spring Boot application that provides functionality to manage depth charts for various sports teams. A depth chart ranks each player for their position, and this application supports the following features:
- Adding players to the depth chart at a specific position and depth.
- Removing players from the depth chart.
- Retrieving the full depth chart.
- Finding all players below a specified player in the depth chart.

## Features

- **Add Player to Depth Chart**: Add a player to a specific position at a given depth. If no depth is specified, the player is added to the end of the depth chart.
- **Remove Player from Depth Chart**: Remove a player from the depth chart for a specific position.
- **Get Full Depth Chart**: Retrieve the entire depth chart with all players and their positions.
- **Get Players Under a Player**: Retrieve all players below a specified player in the depth chart for a given position.

## Supported Sports and Positions

### NFL (National Football League)
- QB (Quarterback)
- WR (Wide Receiver)
- RB (Running Back)
- TE (Tight End)
- K (Kicker)
- P (Punter)
- KR (Kick Returner)
- PR (Punt Returner)

### MLB (Major League Baseball)
- SP (Starting Pitcher)
- RP (Relief Pitcher)
- C (Catcher)
- 1B (First Baseman)
- 2B (Second Baseman)
- 3B (Third Baseman)
- SS (Shortstop)
- LF (Left Fielder)
- CF (Center Fielder)
- RF (Right Fielder)
- DH (Designated Hitter)

## Technology Stack

- **Java 17**
- **Spring Boot**
- **H2 Database** (In-memory database for easy setup and testing)
- **Spring Data JPA** (Data persistence)
- **Jakarta Bean Validation** (Request object validation)
- **JUnit 5** (Testing framework)
- **Mockito** (Mocking framework for unit tests)

## Getting Started

### Prerequisites

- JDK 17 or higher
- Maven 3.6 or higher

### Setup

1. **Download the Project**
2. **Build the project**
    ```bash
   mvn clean install
   
3. **Run the Application
   ```bash
   mvn spring-boot:run
The application will start on http://localhost:8080.

## API Endpoints

**Add Player to Depth Chart**

* URL: /api/depth-chart/add-player
* Method: POST
* Request Parameter: positionDepth = 2
* Request Body:
```json
{
  "playerId": 1,
  "name": "Bob",
  "position": "WR"
}
```
* **Responses:**

**`200 OK`:** Player added to the depth chart.

**`400 Bad Request`:** Validation error.

**Remove Player from Depth Chart**

* URL: /depth-chart/remove-player
* Method: DELETE
* Request Body:
```json
{
"playerId": 1,
"name": "Bob",
"position": "WR"
}
```

* Query Parameters:

  *    position: The position from which to remove the player.

* Responses:

**`200 OK`**: Player removed from the depth chart.

**`400 Bad Request`**: Validation error.

**`404 Not Found`**: Player not found in the specified position.

**Get Full Depth Chart**

* URL: /depth-chart/full
* 
* Method: GET
* 
* Responses:

**`200 OK`**: Returns the full depth chart.
```json
{
    "KR": [
        1
    ],
    "WR": [
        2,
        1,
        3
    ]
}
```

**Get Players Under a Player**

* URL: /depth-chart/under-player
* 
* Method: GET
* 
* Request Body:
```json
{
"playerId": 1,
"name": "Bob",
"position": "WR"
}
```
**Query Parameters:**

* position: The position to search.
* 
* Responses:

**`200 OK`**: Returns a list of players below the specified player in the depth chart.

**`400 Bad Request`**: Validation error.

## Testing
**Unit Tests**

To run the unit tests, use the following command:


```bash
mvn test
```

## Controller Tests

The project includes controller tests to verify the API endpoints.