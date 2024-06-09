package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByIsDeletedOrderByFirstNameDesc(Boolean deleted);
    //get user based on username
    User findByUserNameAndIsDeleted(String username, Boolean deleted); //part-5 fixed

    @Transactional
    void deleteByUserName(String username); // derived query
//@Transactional --> we use it for sensitive database operations.

    //Commit meaning all the steps  are successful executed
    //rollback meaning if anything error happened in any step, everything is going back, rolling back to the original place

    List<User> findByRoleDescriptionIgnoreCaseAndIsDeleted(String description, Boolean deleted); // derived query
}
