package pe.edu.vallegrande.monitoreo.service.impl;

import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.monitoreo.dto.PersonaUpdateDTO;
import pe.edu.vallegrande.monitoreo.dto.PersonaWithDetailsDTO;
import pe.edu.vallegrande.monitoreo.model.Education;
import pe.edu.vallegrande.monitoreo.model.Health;
import pe.edu.vallegrande.monitoreo.model.Persona;
import pe.edu.vallegrande.monitoreo.repository.EducationRepository;
import pe.edu.vallegrande.monitoreo.repository.HealthRepository;
import pe.edu.vallegrande.monitoreo.repository.PersonaRepository;
import pe.edu.vallegrande.monitoreo.service.PersonaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PersonaServiceImpl implements PersonaService {

    @Autowired
    PersonaRepository personaRepository;

    @Autowired
    private EducationRepository educationRepository;

    @Autowired
    private HealthRepository healthRepository;

    @Override
    public Flux<Persona> saveAllStudents(Flux<PersonaWithDetailsDTO> personaWithDetailsDTO) {
        log.info("Request to save batch of students with education and health");

        return personaWithDetailsDTO.flatMap(dto -> {
            Persona persona = dto.getPersona();
            Education education = dto.getEducation();
            Health health = dto.getHealth();

          
            return healthRepository.save(health)
                    .flatMap(savedHealth -> {
                        persona.setHealthIdHealth(savedHealth.getIdHealth()); 
                        return educationRepository.save(education)
                                .flatMap(savedEducation -> {
                                    persona.setEducationIdEducation(savedEducation.getIdEducation()); 
                                    return personaRepository.save(persona);
                                });
                    });
        })
                .doOnComplete(() -> log.info("Successfully saved all students with education and health"))
                .doOnError(error -> log.error("Error saving batch of students with education and health", error));
    }

    @Override
    public Mono<Persona> saveSingleStudent(Persona persona) {
        log.info("Request to save student: {}", persona);
        return personaRepository.save(persona)
                .doOnSuccess(savedStudent -> log.info("Successfully saved persona: {}", savedStudent))
                .doOnError(error -> log.error("Error saving student: {}", persona, error));
    }

    @Override
    public Mono<Persona> findStudentById(Integer id) {
        log.info("Request to find student by ID: {}", id);
        return personaRepository.findById(id)
                .doOnSuccess(persona -> log.info("Found student: {}", persona))
                .doOnError(error -> log.error("Error finding student by ID: {}", id, error));
    }

    @Override
    public Flux<Persona> findAllStudents() {
        log.info("Request to find all students");
        return personaRepository.findAll()
                .doOnComplete(() -> log.info("Successfully retrieved all students"))
                .doOnError(error -> log.error("Error finding all students", error));
    }

    @Override
    public Mono<Void> deleteById(Integer id) {
        log.info("Request to delete student by ID: {}", id);
        return personaRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Successfully deleted student with ID: {}", id))
                .doOnError(error -> log.error("Error deleting student by ID: {}", id, error));
    }

    @Override
    public Mono<Void> deleteAll() {
        log.info("Request to delete all students");
        return personaRepository.deleteAll()
                .doOnSuccess(unused -> log.info("Successfully deleted all students"))
                .doOnError(error -> log.error("Error deleting all students", error));
    }

    @Override
    public Mono<Persona> updatePersona(Integer id, PersonaUpdateDTO updateDTO) {
        return personaRepository.findById(id)
                .flatMap(persona -> {
                    persona.setName(updateDTO.getPersona().getName());
                    persona.setSurname(updateDTO.getPersona().getSurname());
                    persona.setTypeDocument(updateDTO.getPersona().getTypeDocument());
                    persona.setDocumentNumber(updateDTO.getPersona().getDocumentNumber());
                    persona.setTypeKinship(updateDTO.getPersona().getTypeKinship());
                    persona.setFamiliaId(updateDTO.getPersona().getFamiliaId());

                    persona.setEducationIdEducation(updateDTO.getEducation().getIdEducation());
                    persona.setHealthIdHealth(updateDTO.getHealth().getIdHealth());

                    return personaRepository.save(persona);
                })
                .switchIfEmpty(Mono.empty()); 
    }

    @Override
    public Mono<Persona> registerPersona(PersonaWithDetailsDTO personaWithDetailsDTO) {
        Persona persona = personaWithDetailsDTO.getPersona();
        Education education = personaWithDetailsDTO.getEducation();
        Health health = personaWithDetailsDTO.getHealth();

        return healthRepository.save(health)
                .flatMap(savedHealth -> {
                    persona.setHealthIdHealth(savedHealth.getIdHealth());

                    return educationRepository.save(education)
                            .flatMap(savedEducation -> {
                                persona.setEducationIdEducation(savedEducation.getIdEducation());

                                return personaRepository.save(persona);
                            });
                });
    }
}