package io.github.alexwu727.authsystemauthenticationservice.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("USER")
    USER,
    @JsonProperty("ADMIN")
    ADMIN
}
