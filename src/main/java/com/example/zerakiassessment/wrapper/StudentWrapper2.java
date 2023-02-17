package com.example.zerakiassessment.wrapper;

import jakarta.validation.constraints.NotEmpty;

public record StudentWrapper2(@NotEmpty(message = "Student name cannot be empty")
                              String name,
                              @NotEmpty(message = "Admission number cannot be empty")
                              String admissionNo, long courseId,
                              long institutionId) {
}
