# API Documentataion

## Table of Contents

1. [Introduction](#1-introduction)
2. [API Endpoints](#2-api-endpoints)
   1. [User Registration](#21-user-registration)
      1. [Register a New User](#211-register-a-new-user)
      2. [Verify Email](#212-verify-email)
      3. [Resend Verification Email](#213-resend-verification-email)
   2. [User Authentication](#22-user-authentication)
      1. [Login](#221-login)
      2. [Refresh Token](#222-refresh-token)
   3. [Update User Profile](#23-update-user-profile)
      1. [Patch User Profile](#231-patch-user-profile)
      2. [Forgot Password](#232-forgot-password)
      3. [Reset Password](#233-reset-password)
      4. [Update Password](#234-update-password)
   4. [User Management](#24-user-management)
      1. [Get User](#241-get-user)
      2. [Delete User](#242-delete-user)

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
  