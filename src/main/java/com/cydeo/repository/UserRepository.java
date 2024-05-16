package com.cydeo.repository;

import com.cydeo.entity.Role;
import com.cydeo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    //get user based on username
    User findByUserName(String username);
    @Transactional
    void deleteByUserName(String username); // derived query

    //Commit meaning all the steps  are successful executed
    //rollback meaning if anything error happened in any step, everything is going back, rolling back to the original place

    List<User> findByRoleDescriptionIgnoreCase(String description); // derived query
}
