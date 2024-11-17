// Education.java
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
@Table(name = "education")
public class Education {

    @Id
    @Column("id_education")
    private Integer idEducation;

    @Column("grade_book")
    private String gradeBook;

    @Column("grade_average")
    private Integer gradeAverage;

    @Column("full_notebook")
    private String fullNotebook;

    @Column("educational_assitence")
    private String educationalAssitence;

    @Column("academic_tutorias")
    private String academicTutorias;

    @Column("degree_study")
    private String degreeStudy; 
}
