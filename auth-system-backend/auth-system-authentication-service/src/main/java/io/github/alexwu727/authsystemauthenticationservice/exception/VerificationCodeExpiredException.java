package io.github.alexwu727.authsystemauthenticationservice.exception;

public class VerificationCodeExpiredException extends RuntimeException{
    public VerificationCodeExpiredException(String message) {
        super(message);
    }
}
