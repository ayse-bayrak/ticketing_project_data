package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // we will always use the interface for the injection. So we need to tell one of the qualified bean for this interface which is right now TaskServiceImpl
public class TaskServiceImpl implements TaskService {

private final TaskRepository taskRepository;
private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskDTO> listAllTask() {
        return null;
    }

    @Override
    public void save(TaskDTO task) {

    }

    @Override
    public void update(TaskDTO task) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public TaskDTO findById(Long id) {
        return null;
    }
}
