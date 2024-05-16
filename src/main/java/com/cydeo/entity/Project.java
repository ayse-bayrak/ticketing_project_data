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
    private User assignManager; // every project has a manager, manager is User

}
