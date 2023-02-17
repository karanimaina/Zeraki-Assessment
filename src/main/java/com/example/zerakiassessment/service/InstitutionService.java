package com.example.zerakiassessment.service;


import com.example.zerakiassessment.wrapper.InstitutionNameWrapper;
import com.example.zerakiassessment.wrapper.InstitutionWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import reactor.core.publisher.Mono;

public interface InstitutionService {
    Mono<UniversalResponse> createInstitution(InstitutionWrapper institutionWrapper);
    Mono<UniversalResponse> getAllInstitution(boolean desc);
    Mono<UniversalResponse> searchInstitution(String name);
    Mono<UniversalResponse> deleteInstitution(long id);
    Mono<UniversalResponse> updateInstitutionName(InstitutionNameWrapper institutionWrapper);


}
