package com.example.zerakiassessment.controller;

import com.example.zerakiassessment.service.InstitutionService;
import com.example.zerakiassessment.wrapper.InstitutionNameWrapper;
import com.example.zerakiassessment.wrapper.InstitutionWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequestMapping("/api/v1/institution")
@RestController
@RequiredArgsConstructor
public class InstitutionController {
    public InstitutionService institutionService;

    @PostMapping("/create")
    public Mono<ResponseEntity<UniversalResponse>> createInstitution(@RequestBody InstitutionWrapper institutionWrapper){
        return institutionService.createInstitution(institutionWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @GetMapping("/all")
    public Mono<ResponseEntity<UniversalResponse>> getAllInstitution(@RequestParam boolean desc){
        return institutionService.getAllInstitution(desc)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @GetMapping("/search")
    public Mono<ResponseEntity<UniversalResponse>> searchInstitution(@RequestParam String name){
        return institutionService.searchInstitution(name)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @DeleteMapping("/delete")
    public Mono<ResponseEntity<UniversalResponse>> deleteInstitution(@RequestBody long institutionWrapper){
        return institutionService.deleteInstitution(institutionWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }

    @PutMapping("/update")
    public Mono<ResponseEntity<UniversalResponse>> updateInstitutionName(@RequestBody InstitutionNameWrapper institutionWrapper){
        return institutionService.updateInstitutionName(institutionWrapper)
                .map(ResponseEntity::ok)
                .publishOn(Schedulers.boundedElastic());
    }
}
