package com.example.zerakiassessment.wrapper;

import lombok.Builder;

@Builder
public record UniversalResponse (int status, String message, Object data) {
    public static UniversalResponseBuilder builder() {
        return new UniversalResponseBuilder();

    }
}
