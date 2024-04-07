# API Documentation

## Table of Contents

1. [Introduction](#1-introduction)
2. [API Endpoints](#2-api-endpoints)
   1. <details> 
        <summary> <a href="#21-user-registration">User Registration</a> </summary>
        <ol>
          <li> <a href="#211-register-a-new-user">Register a New User</a> </li>
          <li> <a href="#212-verify-email">Verify Email</a> </li>
          <li> <a href="#213-resend-verification-email">Resend Verification Email</a> </li>
        </ol>
      </details>
   2. <details> 
        <summary> <a href="#22-user-authentication">User Authentication</a> </summary>
        <ol>
          <li> <a href="#221-login">Login</a> </li>
          <li> <a href="#222-refresh-token">Refresh Token</a> </li>
        </ol>
        </details>
    3. <details>
          <summary> <a href="#23-update-user-profile">Update User Profile</a> </summary>
            <ol>
            <li> <a href="#231-patch-user-profile">Patch User Profile</a> </li>
            <li> <a href="#232-forgot-password">Forgot Password</a> </li>
            <li> <a href="#233-reset-password">Reset Password</a> </li>
            <li> <a href="#234-update-password">Update Password</a> </li>
            </ol>
          </details>
    4. <details>
            <summary> <a href="#24-user-management">User Management</a> </summary>
                <ol>
                <li> <a href="#241-get-user">Get User</a> </li>
                <li> <a href="#242-delete-user">Delete User</a> </li>
                </ol>
            </details>
  

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

#### 2.2.2. Refresh Token

- **URL:** `/refresh-token`
- **Method:** `POST`
- **Description:** Refresh token.
- **Response**:
  - **200 OK**
    - **Response Body:**
```json
{
    "token": "[JWT]"
}
```
  - **403 Forbidden**

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
    - UserNotVerifiedException
  - **404 Not Found**
    - UserNotFoundException
  - **409 Conflict**
    - UsernameAlreadyExistsException
    - EmailAlreadyExistsException

#### 2.3.2. Forgot Password

- **URL:** `/forgot-password`
- **Method:** `POST`
- **Description:** Send password reset link to user's email.
- **Request Parameters:**
  - `email`: Email address
- **Response**:
  - **200 OK**
  - **400 Bad Request**
    - MissingServletRequestParameterException
  - **403 Forbidden**
    - UserNotVerifiedException
  - **404 Not Found**
    - UserNotFoundException

#### 2.3.3. Reset Password

- **URL:** `/reset-password`
- **Method:** `POST`
- **Description:** Reset user's password.
- **Request Body:**
```json
{
  "email": "hello@world.com",
  "verificationCode": "123456",
  "newPassword": "helloworld2"
}
```
- **Response**:
  - **200 OK**
  - **400 Bad Request**
    - HttpMessageNotReadableException
    - MethodArgumentNotValidException
    - VerificationCodeMismatchException
    - VerificationCodeExpiredException
  - **403 Forbidden**
    - UserNotVerifiedException
  - **404 Not Found**
    - UserNotFoundException
    - VerificationCodeNotFoundException

####  2.3.4. Update Password

- **URL:** `/update-password`
- **Method:** `POST`
- **Description:** Update user's password.
- **Request Body:**
```json
{
  "email": "hello@world.com",
  "oldPassword": "helloworld",
  "newPassword": "helloworld2"
}
```
- **Response**:
  - **200 OK**
  - **400 Bad Request**
    - HttpMessageNotReadableException
    - MethodArgumentNotValidException
    - PasswordMismatchException
  - **403 Forbidden**
    - UserNotVerifiedException
    - AccessDeniedException
  - **404 Not Found**
    - UserNotFoundException

### 2.4. User Management

#### 2.4.1. Get User

- **URL:** `/{id}`
- **Method:** `GET`
- **Description:** Get user by id.
- **Response**:
  - **200 OK**
  - **403 Forbidden**
    - AccessDeniedException
  - **404 Not Found**
    - UserNotFoundException

#### 2.4.2. Delete User

- **URL:** `/{id}`
- **Method:** `DELETE`
- **Description:** Delete user by id.
- **Response**:
  - **200 OK**
  - **403 Forbidden**
    - AccessDeniedException
  - **404 Not Found**
    - UserNotFoundException

## 3. Data Models

### 3.1. User

- **Properties:**
  - `id`: User ID
  - `enabled`: User status
  - `username`: Username
  - `email`: Email address
  - `password`: Password
  - `verified`: Email verification status
  - `verification_code`: Email verification code
  - `verification_code_expiration`: Email verification code expiration date
  - `roles`: User roles
  - `registration_date`: User registration date
  - `last_login_date`: User last login date
  - `last_land_date`: User last land date
  - `last_update_date`: User last update date

### 3.2. Password Reset Verification Code

- **Properties:**
  - `id`: Verification code ID
  - `user_id`: User ID
  - `verification_code`: Verification code
  - `expiration_date`: Verification code expiration date
- **Relationships:**
  - `user_id`: User

## 4. Security

The Auth System Authentication Service uses JWT (JSON Web Token) for authentication. The service generates a JWT token upon successful login and refreshes the token upon successful token refresh.

### Secured Endpoints

The following endpoints are secured and require a valid JWT token.

- POST `/refresh-token`

The following endpoints are secured and require a valid JWT token with the `ADMIN` role or the user's own JWT token.

- GET `/{id}`
- PATCH `/{id}`
- DELETE `/{id}`
- POST `/update-password`
