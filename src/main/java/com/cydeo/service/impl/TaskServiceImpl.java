package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // we will always use the interface for the injection. So we need to tell one of the qualified bean for this interface which is right now TaskServiceImpl
public class TaskServiceImpl implements TaskService {

private final TaskRepository taskRepository;
private final TaskMapper taskMapper;
private final ProjectMapper projectMapper;//part-5
private final UserService userService;
private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public List<TaskDTO> listAllTask() {
        return taskRepository.findAll().stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void save(TaskDTO dto) {
        dto.setTaskStatus(Status.OPEN);
        dto.setAssignedDate(LocalDate.now());

        Task task1=taskMapper.convertToEntity(dto);
        taskRepository.save(task1);
    }

    @Override
    public void update(TaskDTO dto) {
      Optional<Task> task = taskRepository.findById(dto.getId());
        //ready Jpa methods returns everything in Optional
      Task convertedTask = taskMapper.convertToEntity(dto);
      if (task.isPresent()){
          convertedTask.setTaskStatus(dto.getTaskStatus()!=null ? dto.getTaskStatus(): task.get().getTaskStatus() ); // in part-5 it is changed and fixed
          convertedTask.setAssignedDate(task.get().getAssignedDate());
          taskRepository.save(convertedTask);
      }
    } // line 59 we use if dto.getTaskStatus is exist we use same taskStatus, if not we assign task's taskStatus
    //when we click on the update button in UI we don't have dto.getStatus but
    //when we call the update() from other place (in here it is calling inside the complete()) so we have dto.getTaskStatus value

    @Override
    public void delete(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);
    //ready Jpa methods returns everything in Optional
        if (foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }

    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        //ready Jpa methods returns everything in Optional

        if (task.isPresent()) {
            return taskMapper.convertToDTO(task.get());
        }
        return null;
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteByProject(ProjectDTO projectDTO) {
        Project  project= projectMapper.convertToEntity(projectDTO); //part-5
        List<Task> tasks = taskRepository.findAllByProject(project); //part-5
        tasks.forEach(task -> delete(task.getId()));
    }

    @Override
    public void completeByProject(ProjectDTO projectDTO) {
        Project project = projectMapper.convertToEntity(projectDTO); //part-5
        List<Task> tasks = taskRepository.findAllByProject(project);
        //part-5, firstly, it couldn't find user id 4-John, when I put where clause as command, and I fixed giving error my tasks should be able to get Employee information from the database
        tasks.stream().map(taskMapper::convertToDTO).forEach(taskDTO -> {
            taskDTO.setTaskStatus(Status.COMPLETE);
            update(taskDTO);
        });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status, userMapper.convertToEntity(loggedInUser));
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        List<Task> tasks = taskRepository.findAllByTaskStatusAndAssignedEmployee(status, userMapper.convertToEntity(loggedInUser));
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
// if you have @Where(clause = "is_deleted=false") on top of the user entity. It's not going to be applied only to this queries you have inside user repository.
//It is going to be applied to all the queries using User. That means, for example, in the project repository or task repository.
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedManager(UserDTO assignedEmployee) {
        List<Task> tasks = taskRepository
                .findAllByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE, userMapper.convertToEntity(assignedEmployee));
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }


}
