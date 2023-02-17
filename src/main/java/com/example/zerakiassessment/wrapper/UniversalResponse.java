package com.example.zerakiassessment.wrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UniversalResponse (int status, String message, Object data) {
    public static UniversalResponseBuilder builder() {
        return new UniversalResponseBuilder();

    }
}
