package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Task;
import com.cydeo.enums.Status;
import com.cydeo.mapper.TaskMapper;
import com.cydeo.repository.TaskRepository;
import com.cydeo.service.TaskService;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
      /*
      TaskRepository kullanılarak, veritabanında Task varlığı aranır.
      Arama işlemi, TaskDTO'dan gelen id ile yapılır.
      findById metodu bir Optional nesnesi döndürür,
      çünkü belirtilen id'ye sahip bir görev bulunamayabilir.
       */
      Task convertedTask = taskMapper.convertToEntity(dto);
      /*
      TaskDTO nesnesi, Task varlığına dönüştürülür.
      Bu dönüşüm işlemi genellikle bir Mapper sınıfı aracılığıyla gerçekleştirilir.
      Mapper sınıfı, DTO ve varlık arasında dönüşüm işlemlerini yönetir.
       */

      if (task.isPresent()){
          /*
          Optional içindeki Task varlığı mevcut mu diye kontrol edilir.
          Eğer varsa, güncelleme işlemi gerçekleştirilir.
           */
          convertedTask.setTaskStatus(task.get().getTaskStatus());
          /*
          Task varlığının mevcut durumu (taskStatus) mevcut Task varlığından alınır
          ve güncellenen Task varlığına atanır. Bu, TaskDTO'dan gelen verinin eksik olabileceği
          durumlar için mevcut verinin korunmasını sağlar.


           */
          convertedTask.setAssignedDate(task.get().getAssignedDate());
          taskRepository.save(convertedTask);
          /*
          Son olarak, güncellenen Task varlığı taskRepository üzerinden veritabanına kaydedilir.
          Bu, güncellenen verinin kalıcı olarak saklanmasını sağlar.
           */
      }

    }

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
}
