package com.example.zerakiassessment.wrapper;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CourseWrapper(
        @Max(value = 15, message = "Course name cannot exceed 15 characters")
        @NotBlank(message = "Course name cannot be empty")
        String name,
        long institutionId,
        long courseId,
        @NotEmpty(message = "Course description cannot be empty")
        String description,
        @Min(value = 1, message = "Course duration cannot be less than 1 month")
        long monthDuration,

        boolean desc

) {
}
