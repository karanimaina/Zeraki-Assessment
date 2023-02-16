package com.example.zerakiassessment.wrapper;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstitutionWrapper (
        @Size(min = 4,max = 15,message = "Name cannot must be within range 4- 15 characters")
        String name,
        @NotBlank (message = "Tel cannot be empty")
        String tel,
        @NotBlank (message = "Location cannot be empty")
        String location,
        long institutionId
){
}
