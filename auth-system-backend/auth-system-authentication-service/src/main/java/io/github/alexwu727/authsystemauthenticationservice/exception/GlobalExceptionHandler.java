package io.github.alexwu727.authsystemauthenticationservice.exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public void handleRuntimeException(RuntimeException ex) {
        System.out.println("RuntimeException is handled");
    }

    @ExceptionHandler(BadCredentialsException.class)
    public void handleBadCredentialsException(BadCredentialsException ex) {
        System.out.println("BadCredentialsException is handled");
    }
}
