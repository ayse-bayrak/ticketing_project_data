package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


//@Transactional--> when you write a query this is drive or jpql or native query in the repository, if this query is doing either insert or delete or update (it is called ddl stuff)
// if doing this kind of transaction in the database, we need to @Transactional, either method level either class level
// if derived @Transactional
// if jpql or native @Transactional and @Modifying
//@Transactional --> we use it for sensitive database operations.
//Commit meaning all the steps  are successful executed
//rollback meaning if anything error happened in any step, everything is going back, rolling back to the original place

@Service
@Transactional // when we put it to the class level, use only method with derived Query,  use @Query (JPQL and native query), based on the needs either class level or method
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, @Lazy ProjectService projectService, @Lazy TaskService taskService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList =userRepository.findAllByIsDeletedOrderByFirstNameDesc(false);
        return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        return userMapper.convertToDTO(userRepository.findByUserNameAndIsDeleted(username, false));
    }

    @Override
    public void save(UserDTO user) {
    userRepository.save(userMapper.convertToEntity(user));
    }

    @Override
    public void deleteByUserName(String username) {
//        User user = userRepository.findAll().stream().filter(u->u.getUserName().equals(username)).findFirst().get();
//        userRepository.delete(user);  --> he says long way
        userRepository.deleteByUserName(username); // instead of long way we create derived query in repository
    }//  har deletion: now we don't use this method because we want to delete UI without deleting database

    @Override
    public void delete(String username) {
        //username comes from controller but we need to find a user
    // go to db and get the user with username
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        if (checkIfCanBeDeleted(user)) {
            // change the isDeleted field to true
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" +user.getId()); // harold@manager.com-2, if I want, I can use this part to create
           //To be able to create new user with same name if it was deleted
            userRepository.save(user); //we don't use delete method from the crud repository, because it is hard deletion
            // because we use soft delete via changing some field and by saving
            // save the object in the db

        }

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCaseAndIsDeleted(role, false);
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public UserDTO update(UserDTO user) {
       // User user1 = userRepository.findAll().stream().filter(u->u.getUserName().equals(user.getUserName())).findAny().get();
        //return userMapper.convertToDTO(userRepository.updateUserById(user1.getId()));

        //Find current user
        User user1 = userRepository.findByUserNameAndIsDeleted(user.getUserName(), false); // has id yes
        //Mapper Model update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user); // has id? no
        //set id to the converted object
        convertedUser.setId(user1.getId()); //avoiding create one more new user
        //save the update user in the db
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());
    }
    // When we want to update I need to id, we need to setId as actual id
    // otherwise if we use save method, it is created new object in the DB, duplicate obj
    // this is small challenges which is you can explain in you interview about what problem is challenge for you and how did you fix it?

    //we  use the save method, both for saving and updating.
    // and how does it know whether it should do an update or save is basically based on primary key.
    //we are saving technically in updating with same unique id with some diffrence
        /*
        if the data does not have any primary key yet, it means this is a new data. It wasn't saved earlier.
        So let me just create and generate a primary key for it, and then put it in the database.
        But if it already has a primary key, that means the data already isn't a database.
       implementation of this save method:
           @Transactional
    public <S extends T> S save(S entity) {
        Assert.notNull(entity, "Entity must not be null.");
        if (this.entityInformation.isNew(entity)) {
            this.em.persist(entity);
            return entity;
        } else {
            return this.em.merge(entity);
        }
    }
If condition to check.If this entity is new, and if it is new, it is persisting, it meaning it is saving it to the database,
else, if it is not new, it is merging it.
It means it is going to merge this entity with the existing row in the database, meaning it is going to update it.
that's why we use sustain same method receiving and updating.
         */
private boolean checkIfCanBeDeleted(User user) {
        // we can not delete user who have the uncompleted task (for employee) or the uncompleted project (for manager)
switch (user.getRole().getDescription()) {
    case "Manager":
        List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(userMapper.convertToDTO(user));
        return projectDTOList.size() == 0; // if i need to create function related with other object -need to other repository- i can not use directly other repository, i should use related service,
// we can use other service not it's repository, Don't directly  use a other Service's repo without calling them first  it is rude (edited)
    /*if you need some functionality related with other objects, maybe like coming from other services.
    you will be keeping it on the same layer. You will again communicate with a different service.
     */

    case "Employee":
        List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedManager(userMapper.convertToDTO(user));
        return taskDTOList.size() == 0;
    default:
        return true;
}
}
//we did private this method, because we don't need to this method from the other class
    //and we use User user because this is a private method, this is not connected to our controller
    //so it does not have to get some dto from the controller and it doesn't have to send something to our back

}
