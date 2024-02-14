package io.github.alexwu727.authsystemuserservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ErrorResponse {
    private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private String error;
    private int status;
    private String path;
}
