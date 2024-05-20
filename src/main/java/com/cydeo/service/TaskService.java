package com.cydeo.service;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;

import java.util.List;

public interface TaskService {
    //save, update, delete, show all task, give me certain task

    List<TaskDTO> listAllTask(); // we need all Task why? because there is table in the task,
    void save(TaskDTO task);
    void update(TaskDTO task);
    void delete(Long id); // we don't have something unique in UI so we use id from the database portion
    TaskDTO findById(Long id); // for update, delete we need to find certain task

}
