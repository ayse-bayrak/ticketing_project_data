package com.cydeo.entity;

import com.cydeo.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="tasks")
@Getter
@Setter
@NoArgsConstructor // FOR my query portion
@AllArgsConstructor
@Where(clause = "is_deleted=false") //for our delete operation, we don't want to do hardcoded deleted from the database, I want to soft delete, mean we just changing the field parameter, whatever repository is using task entity, all those queries belongs to theta repository autimatically concat this clause
public class Task extends BaseEntity{

    // order field is different from the DTO, is it problem for mapper?
    // no, it is checking for fieldName, it looks exact name
    // name needs to match for mapper, it finds the both name same and then getter setter

    private String taskSubject;

    private String taskDetail;

    @Enumerated(EnumType.STRING)
    private Status taskStatus;

    @Column(columnDefinition = "DATE")
    private LocalDate assignedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id")
    private Project project; // one project can have many takes

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assigned_employee_id")
    private User assignedEmployee;

    private Boolean isDeleted=false;
}
