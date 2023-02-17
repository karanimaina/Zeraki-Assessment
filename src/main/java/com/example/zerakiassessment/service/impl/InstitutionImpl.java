package com.example.zerakiassessment.service.impl;


import com.example.zerakiassessment.exceptions.InstitutionException;
import com.example.zerakiassessment.model.Institution;
import com.example.zerakiassessment.repository.InstitutionCourseRepository;
import com.example.zerakiassessment.repository.InstitutionRepository;
import com.example.zerakiassessment.service.InstitutionService;
import com.example.zerakiassessment.wrapper.InstitutionNameWrapper;
import com.example.zerakiassessment.wrapper.InstitutionWrapper;
import com.example.zerakiassessment.wrapper.UniversalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InstitutionImpl implements InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionCourseRepository institutionCourseRepository;

    /**
     *
     * @param institutionWrapper
     * verify if institution  exist
     * save a new institution
     *
     */
    @Override
    public Mono<UniversalResponse> createInstitution(InstitutionWrapper institutionWrapper) {
        return Mono.fromCallable(()-> {
            institutionRepository.findTopByNameEqualsIgnoreCase(institutionWrapper.name())
                    .ifPresent(inst-> {
                     throw new InstitutionException("Institution already exist");
                    });
            Institution institution= Institution.builder()
                    .location(institutionWrapper.location())
                    .telephone(institutionWrapper.tel())
                    .name(institutionWrapper.name()).build();
            Institution saved=institutionRepository.save(institution);
            return UniversalResponse.builder().status(200).message("Institution saved successfully").data(saved)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }
    /**
   * List all institutions in descending order
   */

    @Override
    public Mono<UniversalResponse> getAllInstitution(boolean desc) {
        return Mono.fromCallable(()-> {
            List<Institution> institutions= desc? institutionRepository
                    .findAllByOrderByNameDesc(): institutionRepository.findAll();
            return UniversalResponse.builder().status(200).message("Institutions").data(institutions).build();
        }).publishOn(Schedulers.boundedElastic());
    }
/**
 * Search an institution by Name
 * return a list of institution
 */

    @Override
    public Mono<UniversalResponse> searchInstitution(String name) {
        return Mono.fromCallable(()-> {
            List<Institution> institutions= institutionRepository.findAllByNameContainingIgnoreCase(name);
            return UniversalResponse.builder().status(200).message("Institution search results").data(institutions).build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> deleteInstitution(long institutionId) {
        return Mono.fromCallable(()-> {
            Institution institution= institutionRepository.findById(institutionId)
                    .orElseThrow(()-> new InstitutionException("Institution not found"));
            boolean existInstitutionCourse= institutionCourseRepository.existsByInstitution(institutionId);
            if(existInstitutionCourse){
                return UniversalResponse.builder().status(400).message("Cannot delete, Institution has been assigned to course|s").build();
            }
            institution.setSoftDelete(true);
            institutionRepository.save(institution);
            return UniversalResponse.builder().status(200).message("Institution deleted successfully").build();
        }).publishOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UniversalResponse> updateInstitutionName(InstitutionNameWrapper institutionWrapper) {
        return Mono.fromCallable(()->{
            Institution institution=institutionRepository.findById(institutionWrapper.id())
                            .orElseThrow(()-> new InstitutionException("Institution does not exist"));
            boolean exists=institutionRepository.existsByNameEqualsIgnoreCaseAndIdNot(institutionWrapper.name(), institutionWrapper.id());
            if(exists){
                return UniversalResponse.builder().status(400).message("Cannot update institution, name already exists")
                        .build();
            }
            institution.setName(institutionWrapper.name());
            Institution saved=institutionRepository.save(institution);
            return UniversalResponse.builder().status(200).message("Institution updated successfully").data(saved)
                    .build();
        }).publishOn(Schedulers.boundedElastic());
    }
}
