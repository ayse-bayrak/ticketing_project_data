package com.cydeo.dto;
import com.cydeo.enums.Status;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id; // we need to add in Data portion,
    // because when we try to map to entity, since the id is not exist in the UI,
    // it gives error and we add this one therefore
    // This can map to entity

    @NotBlank
    private String projectName;

    @NotBlank
    private String projectCode;

    @NotNull
    private UserDTO assignedManager;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotBlank
    private String projectDetail;

    private Status projectStatus;

    private int completeTaskCounts; // These fields are no need in database portion. So we add only UI portion object which is DTO
    private int unfinishedTaskCounts;

    public ProjectDTO(String projectName, String projectCode, UserDTO assignedManager, LocalDate startDate, LocalDate endDate, String projectDetail, Status projectStatus) {
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.assignedManager = assignedManager;
        this.startDate = startDate;
        this.endDate = endDate;
        this.projectDetail = projectDetail;
        this.projectStatus = projectStatus;
    }

}
/*
error happens because of the manager field in your Project entity class, the name now is assignManager but in ProjectDTO it is assignedManager.

When you save a new project from UI, it is firstly a ProjectDTO, but before you save it to database we convert the dto to entity. We do this conversion using ModelMapper and modelMapper is working based on matching field names. Since dto has assignedManager but entity has assignManager, during this convertion mapper is not able to map this manager field because names are different. Then what happens in database is, your project's manager user is going to be null.

And error happens when it tries to display this new project on project list, it tries to for ex print first and last name of manager: project.manager.firstName, but since manager is null, it will be null.firstName and cause null pointer exception.

So to solve it, you can change your field name in Project class to be assignedManager
 */