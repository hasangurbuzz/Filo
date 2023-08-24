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
- QueryDSL
- Spring Boot Actuator

## Application Components

### Domain Classes
Domain classes represent the core entities of the application, defining the fundamental data structures and relationships within the system's data model.

Each class includes an 'isDeleted' field. When a deletion process occurs, this field is set to 'true', causing the object to be hidden from users. This approach prevents inadvertent deletions, facilitates data recovery and auditing, maintains database performance, and supports archiving historical data.

These classes could also provide several benefits in the future. They offer adaptability, allowing easy expansion of data to meet evolving business needs. This data could be leveraged for insightful analytics, trend tracking, and supporting data-driven decisions. The classes might facilitate seamless integration across applications, enabling efficient data sharing. They could also automate processes like maintenance scheduling based on vehicle age.

#### Vehicle

The `Vehicle` class serves as an entity in the database schema, defining the structure for storing vehicle-related data. This class is mapped to a corresponding table and offers the schema for storing vehicle records.

#### VehicleAuthority

The `VehicleAuthority` class is utilized for storing role-based vehicle access permissions in the database. This enables the determination of which user has what role-based authorization for accessing specific vehicles.

#### Group

The `Group` class represents an entity in the database schema for managing groups. It offers the schema for storing group data.

#### GroupAuthority

The `GroupAuthority` class is employed to record the authorization of users for specific groups in the database. This allows users to interact with groups for which they possess authorization.

#### UserRole

The `UserRole` class represents a role type assigned to users when granting vehicle permissions. This role type helps determine the level of access a user has to specific vehicles.

### Service Classes

Service classes encapsulate the business logic and operations related to specific domain entities. They act as intermediaries between the API controllers and the database, providing functionalities such as creation, modification, deletion, and authorization.

In order for a user to carry out operations on groups or vehicles, they must have the appropriate authorization for the particular group or vehicle. These authorizations are verified by the AuthorityServices. This ensures a uniform approach to authorization control, which can be extended to new domain classes in the future with minimal adjustments, maintaining consistency through the same methodology.

#### VehicleService

The `VehicleService` class is responsible for handling the create, read, update, and delete operations involving Vehicle objects.

#### VehicleAuthorityService

The `VehicleAuthorityService` class provides functionalities related to user authorization for viewing specific vehicles. It enables the granting and revoking of permissions, as well as the retrieval of authorized vehicles for users. This service facilitates controlled access to vehicle information based on user permissions.

#### GroupService

The `GroupService` class responsible for creating groups and checking their existence.

#### GroupAuthorityService

The `GroupAuthorityService` class manages user authorization for viewing specific groups. It enables users to access groups they are authorized to see by controlling permissions. This service enhances data security by controlling user-based access to groups.

#### PagedResults

The `PagedResults` class is a generic class utilized for storing paginated query results. Due to its generic structure, it can be applied to domain objects of any type.

### API Classes

API classes responsible for handling client requests and interactions.

#### ApiConstant

The `ApiConstant` class holds constants used in the API layer.

#### ApiContext

The `ApiContext` class is responsible for storing user information set by the `SecurityInterceptor` during each request's lifecycle. It ensures that user information is accessible and consistent throughout the request's processing.

#### ApiException

The `ApiException` class captures errors that occur during the request lifecycle and provides a mechanism for handling and managing exceptions.

#### ApiExceptionCode

The `ApiExceptionCode` class contains constants representing error codes for exceptions that occur within the request lifecycle.

#### ApiExceptionHandler

The `ApiExceptionHandler` class captures and handles `ApiException` errors, providing meaningful error messages and codes to the user during the request lifecycle.

#### ApiValidator

The `ApiValidator` class is responsible for validating vehicle information that requires validation within the API. It ensures that the provided vehicle information adheres to specified rules and constraints.

### API Controller Classes

The following controller classes manage the API endpoints, validating requests, interacting with the service layer, and returning appropriate responses to the client.

#### VehicleApiController

The `VehicleApiController` class handles requests related to vehicles from the client. It is responsible for validating requests, calling necessary methods from the service layer to perform CRUD operations on vehicles, and returning the appropriate response to the client.

#### VehicleUsersApiController

The `VehicleUsersApiController` class manages requests related to vehicle users from the client. It validates incoming requests, interacts with the service layer to handle actions related to vehicle users, and responds to the client with relevant information.

#### GroupApiController

The `GroupApiController` class deals with requests related to groups from the client. It validates incoming requests, communicates with the service layer to execute actions related to groups, and provides responses to the client based on the performed operations.

### Security Classes

#### SecurityInterceptor

The `SecurityInterceptor` class is responsible for validating user information present in the "X-User" header of each incoming request. It ensures that the user information is valid and sets it in the `ApiContext` for the duration of the request's lifecycle.

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



