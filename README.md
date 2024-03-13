# reusable-auth-system
`Reusable Auth System` is a scalable user authentication system built with Spring Boot and React, featuring user registration, login, OAuth integration, and admin functions. Designed as a reusable microservice, it streamlines adding secure user management to any application.

## Getting Started

These instructions will guide you through setting up the project locally using Docker Compose.

### Prerequisites

- Docker and Docker Compose installed on your machine.

### Running the Project

1. Clone the repository

```bash
git clone https://github.com/alexwu727/reusable-auth-system.git
```

2. Modify backend configuration files.

    1. If you want to run on docker, create a file named `application-container.properties` in `auth-system-backend/auth-system-authentication-service/src/main/resources/`. Based on the `application-container.template.properties` file, fill in the necessary information.

    2. If you want to run on your local machine, create a file named `application-dev.properties` in `auth-system-backend/auth-system-authentication-service/src/main/resources/`. Based on the `application-dev.template.properties` file, fill in the necessary information.



3. Use Docker Compose to build and run the backend containers

```bash
cd auth-system-backend
docker-compose up --build
```

4. Use Docker Compose to build and run the frontend containers

```bash
cd auth-system-frontend
docker build -t auth-system-frontend .
docker run -it -p 3000:3000 auth-system-frontend
```

5. Access the frontend at `http://localhost:3000` and the backend at `http://localhost:8080`

## Backend

The backend system is built using Spring Boot and Spring Security, structured into microservices to ensure scalability and maintainability.

### Microservices
- **API Gateway (Spring Reactive):** Acts as the entry point for all requests, routing them to the appropriate microservice.
- **Service Discovery (Eureka):** Manages service registration and discovery, facilitating communication between microservices.
- **UserService:** Manages user information and provides endpoints for user management.
- **AuthService:** Responsible for authentication and authorization, issuing JWTs for secure access to services.

## Frontend
The frontend is developed with React and Redux, offering a dynamic and responsive user interface for login, registration, and user management.

### Features
- **React:** Utilizes functional components and hooks for efficient state management.
- **Redux:** Centralizes state management, enabling efficient data flow between components.
- **React Router:** Enables navigation between different pages and components.