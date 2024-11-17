package pe.edu.vallegrande.monitoreo.service;

import pe.edu.vallegrande.monitoreo.model.Persona;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaService {
    Flux<Persona> saveAllStudents(Flux<Persona> personas);

    Mono<Persona> saveSingleStudent(Persona persona);

    Mono<Persona> findStudentById(Integer id);

    Flux<Persona> findAllStudents();

    Mono<Void> deleteAll();

    Mono<Void> deleteById(Integer id);

    Mono<Persona> updatePersona(Integer id, Persona updatedPersona);
}