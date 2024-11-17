package pe.edu.vallegrande.monitoreo.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
@Table(name = "person")
public class Persona {

    @Id
    private Integer idPerson;

    private String name;
    private String surname;
    private String typeDocument;
    private String documentNumber;
   
    @Column(value = "type_kinship")
    private String typeKinship;
    private Integer educationIdEducation;
    @Column(value = "health_id_headlth")
    private Integer healthIdHealth;
    @Column(value = "family_id")
    private Integer familiaId;
}
