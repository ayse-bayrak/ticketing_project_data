package com.cydeo.service.impl;

import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


//@Transactional--> when you write a query this is drive or jpql or native query in the repository, if this query is doing either insert or delete or update (it is called ddl stuff)
// if doing this kind of transaction in the database, we need to @Transactional, either method level either class level
// if derived @Transactional
//if jpql or native @Transactional and @Modifying
@Service
@Transactional // when we put it to the class level, use only method with derived Query  use @Query (JPQL and native query), based on the needs either class level or method
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        List<User> userList =userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(userMapper::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {

        return userMapper.convertToDTO(userRepository.findByUserName(username));
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
    }//  // now we don't use this method because we want to delete UI without deleting database

    @Override
    public void delete(String username) {
    // go to db and get the user with username
        User user = userRepository.findByUserName(username);
        // change the isDeleted field to true
        user.setIsDeleted(true);
        userRepository.save(user);
        // save the object in the db
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        List<User> users = userRepository.findByRoleDescriptionIgnoreCase(role);
        return users.stream().map(userMapper::convertToDTO).collect(Collectors.toList());

    }

    @Override
    public UserDTO update(UserDTO user) {
       // User user1 = userRepository.findAll().stream().filter(u->u.getUserName().equals(user.getUserName())).findAny().get();
        //return userMapper.convertToDTO(userRepository.updateUserById(user1.getId()));

        //Find current user
        User user1 = userRepository.findByUserName(user.getUserName()); // has id yes
        //Mapper Model update user dto to entity object
        User convertedUser = userMapper.convertToEntity(user); // has id? no
        //set id to the converted object
        convertedUser.setId(user1.getId());
        //save the update user in the db
        userRepository.save(convertedUser);
        return findByUserName(user.getUserName());
    }
    //When we want to update I need to id, otherwise if we use save method, it is created new object in the DB, duplicate obj
    //this is small challenges which is you can explain in you interview about what problem is challenge for you and how did you fix it?
}
