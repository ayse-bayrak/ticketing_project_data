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
      Task convertedTask = taskMapper.convertToEntity(dto);
      if (task.isPresent()){
          convertedTask.setTaskStatus(dto.getTaskStatus()!=null ? dto.getTaskStatus(): task.get().getTaskStatus() ); // in part-5 it is changed and fixed
          convertedTask.setAssignedDate(task.get().getAssignedDate());
          taskRepository.save(convertedTask);
      }
    } // line 59 we use if getTaskStatus
        /*
      TaskRepository kullanılarak, veritabanında Task varlığı aranır.
      Arama işlemi, TaskDTO'dan gelen id ile yapılır. findById metodu bir Optional nesnesi döndürür,
      çünkü belirtilen id'ye sahip bir görev bulunamayabilir.

      TaskDTO nesnesi, Task varlığına dönüştürülür.
      Bu dönüşüm işlemi genellikle bir Mapper sınıfı aracılığıyla gerçekleştirilir.
      Mapper sınıfı, DTO ve varlık arasında dönüşüm işlemlerini yönetir.

      Optional içindeki Task varlığı mevcut mu diye kontrol edilir.
      Eğer varsa, güncelleme işlemi gerçekleştirilir.

      Task varlığının mevcut durumu (taskStatus) mevcut Task varlığından alınır
      ve güncellenen Task varlığına atanır. Bu, TaskDTO'dan gelen verinin eksik olabileceği
      durumlar için mevcut verinin korunmasını sağlar.

      Son olarak, güncellenen Task varlığı taskRepository üzerinden veritabanına kaydedilir.
      Bu, güncellenen verinin kalıcı olarak saklanmasını sağlar.
       */

    @Override
    public void delete(Long id) {
        Optional<Task> foundTask = taskRepository.findById(id);

        if (foundTask.isPresent()){
            foundTask.get().setIsDeleted(true);
            taskRepository.save(foundTask.get());
        }

    }

    @Override
    public TaskDTO findById(Long id) {
        Optional<Task> task = taskRepository.findById(id);

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
        List<Task> tasks = taskRepository.findAllByProject(project); //part-5
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
    }

    @Override
    public List<TaskDTO> listAllNonCompletedByAssignedManager(UserDTO assignedEmployee) {
        List<Task> tasks = taskRepository
                .findAllByTaskStatusIsNotAndAssignedEmployee(Status.COMPLETE, userMapper.convertToEntity(assignedEmployee));
        return tasks.stream().map(taskMapper::convertToDTO).collect(Collectors.toList());
    }


}
