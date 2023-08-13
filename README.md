# Filo

This project provides a backend microservice to manage vehicle records and authorization. It supports a Multi-tenancy
structure allowing multiple companies to use the same system.

## Features

- Admin role manages vehicle groups: Create, edit, delete.
- Admin role assigns authorized vehicles to standard users.
- Standard users can view vehicles assigned to them.

## Technologies Used

- Java
- Spring Boot
- JPA
- Flyway
- OpenAPI
- Querydsl
- Spring Boot Actuator

## Installation

#### 1. Clone the project repository:

```
git clone https://github.com/hasangurbuzz/Filo.git
```

#### 2. Navigate to the project directory:

```
cd Filo
```

#### 3. To compile and run the project:

```
./mvnw spring-boot:run
```

#### 4. For API documentation:

#### [openapi.yaml](/src/main/resources/openapi/openapi.yaml)



