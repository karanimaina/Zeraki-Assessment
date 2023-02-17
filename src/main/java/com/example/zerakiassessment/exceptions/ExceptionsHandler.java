package com.example.zerakiassessment.exceptions;


import com.example.zerakiassessment.wrapper.UniversalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@ControllerAdvice
@Slf4j
public class ExceptionsHandler {

    @ExceptionHandler({StudentException.class,CourseException.class,InstitutionException.class})
    public Mono<ResponseEntity<UniversalResponse>> handleExceptions(Exception e) {
        return Mono.fromCallable(() -> {
                    log.error("Error occurred ", e);
                    return UniversalResponse.builder().status(400).message(e.getMessage())
                            .build();
                }).map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<UniversalResponse>> handleArgumentException(Exception e) {
        return Mono.fromCallable(() -> {
                    log.error("Error occurred ", e);
                    return UniversalResponse.builder().status(400).message(e.getMessage())
                            .build();
                }).map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<UniversalResponse>> handleGlobalException(Exception e){
        return Mono.fromCallable(() -> {
                    log.error("Error occurred ", e);
                    return UniversalResponse.builder().status(400).message("server error")
                            .build();
                }).map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }
}
