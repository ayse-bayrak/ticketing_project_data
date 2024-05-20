package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.entity.Project;

import java.util.List;

public interface ProjectService {
    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects ();
    void save(ProjectDTO dto);
    void update(ProjectDTO dto);
    void delete(String code);

    void deleteByUserName(String code); //delete
    void complete(String code);
}
//UI something needs to be unique over there.
//database is doing with the primary key is separate
//what is better case? we should have projectId or projectCode something like this in the UI
//it is better to separate.