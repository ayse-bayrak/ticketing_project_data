package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor // we need because of jpql stuff
@Entity
@Table(name="projects")
@Where(clause = "is_deleted=false") // in the interview toy can explain this one, most of the junior developer not aware of this one.
public class Project extends BaseEntity{

    @Column(unique = true) // database is not gonna let we create with same projectCode if it is existing in another project
    private String projectCode;
    private String projectName;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status projectStatus;
    
    private String projectDetail;

    @ManyToOne(fetch = FetchType.LAZY) // one manager many project
    @JoinColumn(name = "manager_id")
    private User assignedManager; // every project has a manager, manager is User

    private Boolean isDeleted=false;
}
/*
error happens because of the manager field in your Project entity class, the name now is assignManager but in ProjectDTO it is assignedManager.

When you save a new project from UI, it is firstly a ProjectDTO, but before you save it to database we convert the dto to entity. We do this conversion using ModelMapper and modelMapper is working based on matching field names. Since dto has assignedManager but entity has assignManager, during this convertion mapper is not able to map this manager field because names are different. Then what happens in database is, your project's manager user is going to be null.

And error happens when it tries to display this new project on project list, it tries to for ex print first and last name of manager: project.manager.firstName, but since manager is null, it will be null.firstName and cause null pointer exception.

So to solve it, you can change your field name in Project class to be assignedManager
 */