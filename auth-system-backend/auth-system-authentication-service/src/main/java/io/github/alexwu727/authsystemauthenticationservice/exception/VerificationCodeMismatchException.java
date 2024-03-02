package io.github.alexwu727.authsystemauthenticationservice.exception;

public class VerificationCodeMismatchException extends RuntimeException{
    public VerificationCodeMismatchException(String message) {
        super(message);
    }
}
