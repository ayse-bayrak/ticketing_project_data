package com.cydeo.service;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;

import java.util.List;

public interface TaskService {
    //save, update, delete, show all task, give me certain task

    List<TaskDTO> listAllTask(); // we need all Task why? because there is table in the task,
    void save(TaskDTO dto);
    void update(TaskDTO dto);
    void delete(Long id); // we don't have something unique in UI so we use id from the database portion
    TaskDTO findById(Long id); // for update, delete we need to find certain task
    int totalNonCompletedTask(String projectCode);
    int totalCompletedTask(String projectCode);
    void deleteByProject(ProjectDTO projectDTO);//part-5
    void completeByProject(ProjectDTO projectDTO);
    List<TaskDTO> listAllTasksByStatusIsNot(Status status);
    List<TaskDTO> listAllTasksByStatus(Status status);
    List<TaskDTO> listAllNonCompletedByAssignedManager(UserDTO assignedEmployee);

}
