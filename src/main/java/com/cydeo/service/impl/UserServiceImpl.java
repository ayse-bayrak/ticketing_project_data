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

@Service
@Transactional // when we put it to the class level, use only method with derived Query @Modifying use @Query (JPQL and native query)
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
        userRepository.deleteByUserName(username);
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
    //this is small challenges which is you can explain in you interview about what problem is challenge fro you and how did you fix it?
}
