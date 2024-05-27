package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.mapper.ProjectMapper;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // this bean is gonna be used for the interface
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final TaskService taskService;
    private final MapperUtil mapperUtil;


    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService, MapperUtil mapperUtil) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project project = projectRepository.findByProjectCode(code);
        return mapperUtil.convert(project, new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll(Sort.by("projectCode")).stream().map(projectMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN); // since status is not exist in UI form
        Project project = mapperUtil.convert(dto, new Project());
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {

        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedProject = mapperUtil.convert(dto, new Project());
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        projectRepository.save(convertedProject);


    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);

        project.setProjectCode(project.getProjectCode() + "-" + project.getId()); //part-5 // SP00-1
        //at the end (part-5) we add this line, because i want to use same code SP00 to create another project after I deleted the project, soft deleting
        projectRepository.save(project);

        taskService.deleteByProject(mapperUtil.convert(project, new ProjectDTO())); //part-5
        //at the end (part-5) we add this line, because I want to delete all task related with soft deleting project
        //I will create deleteByProject() in TaskService
    }

    @Override
    public void deleteByUserName(String code) {

    }

    @Override
    public void complete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);

        taskService.completeByProject(mapperUtil.convert(project, new ProjectDTO())); //part-5
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        // hey db give me all projects assign to manager login in the system
        UserDTO currentUserDTO = userService.findByUserName("harold@manager.com");// this portion is login, now we do hardcoded,
        // after i got this information i need to convert to entity
        User user = mapperUtil.convert(currentUserDTO, new User());
        List<Project> list = projectRepository.findAllByAssignedManager(user);
        return list.stream().map(project -> {
            ProjectDTO obj = mapperUtil.convert(project, new ProjectDTO());
            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {

        List<Project> projects = projectRepository
                .findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, userMapper.convertToEntity(assignedManager));
        return projects.stream().map(project -> mapperUtil.convert(project, new ProjectDTO())).collect(Collectors.toList());

    }

}
