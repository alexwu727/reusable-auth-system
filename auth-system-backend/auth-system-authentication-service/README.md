# API Documentataion

## Table of Contents

### [1. Introduction](#introduction)
### [2. API Endpoints](#api-endpoints)
### [3. API Request and Response Examples](#api-request-and-response-examples)
### [4. API Error Codes](#api-error-codes)
### [5. API Security](#api-security)
### [6. API Rate Limiting](#api-rate-limiting)
### [7. API Versioning](#api-versioning)
### [8. API Best Practices](#api-best-practices)
### [9. API Testing](#api-testing)
### [10. API Monitoring](#api-monitoring)
### [11. API Documentation](#api-documentation)
### [12. API References](#api-references)

## 1. Introduction

This document provides the API documentation for the Auth System Authentication Service. The Auth System Authentication Service is a RESTful web service that provides authentication and authorization services for the Auth System.

## 2. API Endpoints

Base URL: `http://[host]:[port]/api/v1/auth/`

### 2.1. User Registration

#### 2.1.1. Register a New User

- **URL:** `/register`
- **Method:** `POST`
- **Description:** Register a new user.
- **Request Body:**
```json
{
  "username": "hello",
  "email": "hello@world.com",
  "password": "helloworld"
}
```
- **Response**:
  - **201 Created**
  - ** 400 Bad Request**
    - HttpMessageNotReadableException
    - MethodArgumentNotValidException
  - ** 409 Conflict**
    - UsernameAlreadyExistsException
    - EmailAlreadyExistsException

#### 2.1.2. Verify Email

- **URL:** `/verify`
- **Method:** `POST`
- **Description:** Verify email.
- **Request Body:**
```json
{
  "email": "hello@world.com",
  "verificationCode": "123456"
}
```
- **Response**:
  - **200 OK**
  - **400 Bad Request**
    - HttpMessageNotReadableException
    - MethodArgumentNotValidException
    - VerificationCodeMismatchException
    - VerificationCodeExpiredException
  - **404 Not Found**
    - UserNotFoundException
  - **409 Conflict**
    - UserAlreadyVerifiedException

#### 2.1.3. Resend Verification Email

- **URL:** `/resend-verification-code`
- **Method:** `POST`
- **Description:** Resend verification email.
- **Request Parameters:**
  - `email`: Email address
- **Response**:
  - **200 OK**
  - **400 Bad Request**
    - MissingServletRequestParameterException
  - **404 Not Found**
    - UserNotFoundException
  - **409 Conflict**
    - UserAlreadyVerifiedException

### 2.2. User Authentication

#### 2.2.1. Login

- **URL:** `/login`
- **Method:** `POST`
- **Description:** Login.
- **Request Body:**
```json
{
  "username": "hello",
  "password": "helloworld"
}
```

- **Response**:
  - **200 OK**
    - **Response Body:**
```json
{
  "token": "[JWT]"
}
```
  - **400 Bad Request**
    - HttpMessageNotReadableException
    - MethodArgumentNotValidException
  - **401 Unauthorized**
    - BadCredentialsException
  - **403 Forbidden**
    - UserNotVerifiedException
  - **404 Not Found**
    - UserNotFoundException

### 2.3. Update User Profile

#### 2.3.1. Patch User Profile

- **URL:** `/{id}`
- **Method:** `PATCH`
- **Description:** Update user's username and email using Json Patch.
- **Request Body:**
```json
[
  {
    "op": "replace",
    "path": "/username",
    "value": "hello2"
  },
  {
    "op": "replace",
    "path": "/email",
    "value": "hello2@world.com"
    }
]
```

> **Note:** Content-Type must be `application/json-patch+json`.
- **Response**:
  - **200 OK**
  - **400 Bad Request**
    - HttpMessageNotReadableException
    - ConstraintViolationException
    - JsonPatchException
  - **403 Forbidden**
    - AccessDeniedException
  - **404 Not Found**
    - UserNotFoundException
  - **409 Conflict**
    - UsernameAlreadyExistsException
    - EmailAlreadyExistsException
    