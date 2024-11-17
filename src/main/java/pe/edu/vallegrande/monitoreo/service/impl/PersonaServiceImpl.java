package pe.edu.vallegrande.monitoreo.service.impl;

import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.monitoreo.model.Persona;
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

    @Override
    public Flux<Persona> saveAllStudents(Flux<Persona> persona) {
        log.info("Request to save batch of students");
        return personaRepository.saveAll(persona)
                .doOnComplete(() -> log.info("Successfully saved all students"))
                .doOnError(error -> log.error("Error saving batch of students", error));
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
    public Mono<Persona> updatePersona(Integer id, Persona updatedPersona) {
        return personaRepository.findById(id)
                .flatMap(existingPersona -> {
                    existingPersona.setName(updatedPersona.getName());
                    existingPersona.setSurname(updatedPersona.getSurname());
                    existingPersona.setTypeDocument(updatedPersona.getTypeDocument());
                    existingPersona.setDocumentNumber(updatedPersona.getDocumentNumber());
                    existingPersona.setTypeKinship(updatedPersona.getTypeKinship());
                    existingPersona.setEducationIdEducation(updatedPersona.getEducationIdEducation());
                    existingPersona.setHealthIdHealth(updatedPersona.getHealthIdHealth());
                    existingPersona.setFamiliaId(updatedPersona.getFamiliaId());

                    return personaRepository.save(existingPersona);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Persona no encontrada con ID: " + id)));
    }
}