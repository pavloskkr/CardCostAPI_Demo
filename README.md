
# CardCostAPI w/ a draft UI

The following is the src code and everything used to create a Debit/Credit card clearing cost calculator where it includes the matrix to view, add, edit and remove the country costs.

## Tech Stack

#### Implementation

- **Spring Boot 3.3.2**
- **Java 17**
- **Redis**
- **Thymeleaf**

#### Testing

- **SpringBootTest**
- **WebMvcTest**
- **Mockito**

#### Deployment

- **Docker for containerization**
- **K3S Cluster for orchestration (w/ Traefik & Let's Encrypt)**
- **or locally using IntelliJ's Spring Boot runtime environment**


## API Reference

#### Create a card clearing cost

```http
  POST /api/v1/clearing-costs
```

| Body Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `countryCode` | `string` | ISO2 Country Code (GR, ES etc.), **Required**  |
| `clearingCost` | `integer` |  More than 0,  **Required**  |

#### Get a clearing cost by ISO2 country code

```http
  GET /api/v1/clearing-costs/{countryCode}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `countryCode`      | `string` | ISO2 Country Code (GR, ES etc.),  **Required** |

#### Get all clearing costs

```http
  GET /api/v1/clearing-costs/
```

| No params required |
| :-------------------------------- |

#### Update a card clearing cost

```http
  PUT /api/v1/clearing-costs/{countryCode}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `countryCode`      | `string` | ISO2 Country Code (GR, ES etc.),  **Required** |

| Body Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `countryCode` | `string` | ISO2 Country Code (GR, ES etc.), **Required**  |
| `clearingCost` | `integer` |  More than 0,  **Required**  |

#### Delete a card clearing cost

```http
  DELETE /api/v1/clearing-costs/{countryCode}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `countryCode`      | `string` | ISO2 Country Code (GR, ES etc.),  **Required** |

#### Retrieve a card clearing cost from the external API

```http
  POST /api/v1/payment-card-cost
```

| Body Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `card_number` | `string` | 8-19 digits of your credit card, **Required**  |

## Deployment

#### Requirements:

- Redis server

If you are using Windows 10/11 make sure WSL (Windows Subsystem for Linux) is enabled and run this:

```sh
curl -fsSL https://packages.redis.io/gpg | sudo gpg --dearmor -o /usr/share/keyrings/redis-archive-keyring.gpg

echo "deb [signed-by=/usr/share/keyrings/redis-archive-keyring.gpg] https://packages.redis.io/deb $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/redis.list

sudo apt-get update
sudo apt-get install redis
```
and then this:
```sh
sudo service redis-server start
```

To deploy clone this code to your IDE and run it in a SpringBoot runtime environment

Otherwise you can always use the deployed API with the embedded UI to test it, from the following link:

```bash
https://cardcostapi.rndv-testing.xyz/matrix
```


## Documentation

The backend consists mainly of this tree structure:

```
cardcostapi/
│
├── .idea/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── etraveli/
│   │   │           └── cardcostapi/
│   │   │               ├── config/
│   │   │               ├── controller/
│   │   │               ├── dto/
│   │   │               ├── exception/
│   │   │               ├── model/
│   │   │               ├── repository/
│   │   │               ├── service/
│   │   │               └── CardcostapiApplication.java
│   │   └── resources/
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── etraveli/
│       │           └── cardcostapi/
│       │               ├── controller/
│       │               ├── integration/
│       │               ├── repository/
│       │               ├── service/
│       │               └── CardcostapiApplicationTests.java

```
where in order to breakdown the main parts of the backend, we have the:
- config (where it includes application configurations that need to be started with the application e.g. Redis)
- controller (where contains the REST API controllers that is the interface where forms and routes the requests to our application or to external APIs)
- dto (it is data transfer objects that form an object the way we need it to transfer specific information)
- exception (contains specific exception handlers/classes tailored to our needs)
- model (consists of the objects that transfer back and forth the data to our storage)
- repository (the communication layer with Redis storage)
- service (the services that contain the logic and recall the repo)
- test/ (there you will find the necessary unit tests for each function and e2e tests to ensure integration)


## Further Information

**Docker image can be found here: https://hub.docker.com/repository/docker/pavloskkr/cardcostapi**

-------

**Included in the email you will find the Postman collection with localhost endpoints but you can use https://cardcostapi.rndv-testing.xyz also to test.**

-------

**In this repository you will also find the .yaml files that have been used to make the deployment to my k3s cluster**


