package pe.edu.vallegrande.monitoreo.rest;


import lombok.extern.slf4j.Slf4j;
import pe.edu.vallegrande.monitoreo.model.Persona;
import pe.edu.vallegrande.monitoreo.service.impl.PersonaServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/persona")
public class PersonaRest {

    @Autowired
    private PersonaServiceImpl personaService;

    @PostMapping
    public Mono<Persona> saveStudent(@RequestBody Persona persona) {
        log.info("Request to save student: {}", persona);
        return personaService.saveSingleStudent(persona)
                .doOnSuccess(savedPersona -> log.info("Exito persona registrada: {}", savedPersona))
                .doOnError(error -> log.error("Error al guardar persona: {}", persona, error));
    }

    @PostMapping("/batch")
    public Flux<Persona> saveAllStudents(@RequestBody Flux<Persona> persona) {
        log.info("Request to save batch of students");
        return personaService.saveAllStudents(persona)
                .doOnComplete(() -> log.info("Successfully saved all students"))
                .doOnError(error -> log.error("Error saving batch of students", error));
    }

    @GetMapping("/{id}")
    public Mono<Persona> findStudentById(@PathVariable Integer id) {
        log.info("Request to find student by ID: {}", id);
        return personaService.findStudentById(id)
                .doOnSuccess(persona -> log.info("Found student: {}", persona))
                .doOnError(error -> log.error("Error finding student by ID: {}", id, error));
    }

    @GetMapping
    public Flux<Persona> findAllStudents() {
        log.info("Request to find all students");
        return personaService.findAllStudents()
                .doOnComplete(() -> log.info("Successfully retrieved all students"))
                .doOnError(error -> log.error("Error finding all students", error));
    }

    @DeleteMapping("/{id}")
    public Mono<String> deleteStudentById(@PathVariable Integer id) {
        log.info("Request to delete student by ID: {}", id);
        return personaService.findStudentById(id)
                .flatMap(persona -> personaService.deleteById(id)
                        .then(Mono.just("Persona con el ID: " + id + " eliminado"))
                        .doOnSuccess(msg -> log.info(msg))
                        .doOnError(error -> log.error("Error deleting student by ID: {}", id, error)));
    }

    @DeleteMapping
    public Mono<Void> deleteAllStudents() {
        log.info("Request to delete all students");
        return personaService.deleteAll()
                .doOnSuccess(unused -> log.info("Successfully deleted all students"))
                .doOnError(error -> log.error("Error deleting all students", error));
    }



    @PutMapping("/{id}")
    public Mono<Persona> updateStudent(@PathVariable Integer id, @RequestBody Persona updatedPersona) {
        log.info("Request to update student with ID: {}", id);
        
        return personaService.findStudentById(id)
                .flatMap(existingPersona -> {
                    existingPersona.setName(updatedPersona.getName());
                    existingPersona.setSurname(updatedPersona.getSurname());
                    existingPersona.setTypeDocument(updatedPersona.getTypeDocument());
                    existingPersona.setDocumentNumber(updatedPersona.getDocumentNumber());
                    existingPersona.setTypeKinship(updatedPersona.getTypeKinship());
                    existingPersona.setEducationIdEducation(updatedPersona.getEducationIdEducation());
                    existingPersona.setHealthIdHealth(updatedPersona.getHealthIdHealth());
                    existingPersona.setFamiliaId(updatedPersona.getFamiliaId());
                    
                    // Guardar la persona actualizada
                    return personaService.saveSingleStudent(existingPersona)
                            .doOnSuccess(savedPersona -> log.info("Persona con ID: {} actualizada: {}", id, savedPersona))
                            .doOnError(error -> log.error("Error actualizando persona con ID: {}", id, error));
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Persona no encontrada con ID: " + id)));
    }
}