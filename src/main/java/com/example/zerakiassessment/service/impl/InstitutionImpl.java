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
     *  creates a new Institution object with the data provided in the InstitutionWrapper and saves it to the database.
     *  checks if an institution with the same name already exists in the database by calling the findTopByNameEqualsIgnoreCase method of the institutionRepository. If it exists, the method throws an InstitutionException with an appropriate message.
     *  If the institution does not exist, a new Institution object is created using the data provided in the InstitutionWrapper.
     *  object is saved to the database using the institutionRepository's save method,
     * @return  UniversalResponse object with a status of 200
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
   * This method retrieves a list of all institutions.
     *  takes a boolean flag 'desc' that determines whether the institutions should be sorted in descending order or not.
     *  retrieves the institutions from the repository and creates a UniversalResponse object with the retrieved institutions as data.
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
 * searches for institutions in the database that match a given name.
 * method takes in a String parameter name,
 * creates a List of Institution objects that match the given name by calling institutionRepository.findAllByNameContainingIgnoreCase(name).
 * @return  UniversalResponse object with a status code of 200
 */

    @Override
    public Mono<UniversalResponse> searchInstitution(String name) {
        return Mono.fromCallable(()-> {
            List<Institution> institutions= institutionRepository.findAllByNameContainingIgnoreCase(name);
            return UniversalResponse.builder().status(200).message("Institution search results").data(institutions).build();
        }).publishOn(Schedulers.boundedElastic());
    }

    /**
     * accepts a long parameter institutionId
     * @param institutionId
     * finds the institution with the specified institutionId using the institutionRepository.findById method. If the institution is not found, it throws an InstitutionException with a "Institution not found" message.
     * checks if the institution has been assigned to any course by invoking the institutionCourseRepository.existsByInstitution method. If the institution has been assigned to any course, it returns a UniversalResponse object with a status code of 400 and a "Cannot delete, Institution has been assigned to course|s" message.
     * If the institution has not been assigned to any course, it sets the softDelete attribute of the institution to true and saves the updated institution using the institutionRepository.save method.
     * @return UniversalResponse object with a status code of 200
     */

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

    /**
     *
     * @param institutionWrapper
     * check if institution exist by the id;
     *  checks if the new name already exists for another institution in the database, by calling the existsByNameEqualsIgnoreCaseAndIdNot() method on the institutionRepository.
     * updates the name of the Institution object with the new name passed in the InstitutionNameWrapper object.
     * @return UniversalResponse object with a status code of 200 and a message indicating that the institution was updated successfully, along with the updated Institution object.
     */
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
