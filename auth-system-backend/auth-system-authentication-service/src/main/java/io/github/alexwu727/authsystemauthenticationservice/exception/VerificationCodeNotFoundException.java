package io.github.alexwu727.authsystemauthenticationservice.exception;

public class VerificationCodeNotFoundException extends RuntimeException{
    public VerificationCodeNotFoundException(String message) {
        super(message);
    }
}
