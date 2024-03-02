package io.github.alexwu727.authsystemauthenticationservice.exception;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(String message) {
        super(message);
    }
}
