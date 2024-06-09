package com.cydeo.repository;

import com.cydeo.dto.TaskDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
//@Repository
public interface ProjectRepository extends JpaRepository <Project, Long> {
    Project findByProjectCode (String code);
    List<Project> findAllByAssignedManager(User assignedManager); // derived query
    List<Project> findAllByProjectStatusIsNotAndAssignedManager(Status status, User assignedManager);
}
/*
we use this repository to create beans.
and we put it on top of interface. But we cannot create beans or objects from interface.
So how does it work?
-implementation is hidden
for repository we did not create any implementation class right? We only created the interfaces.
And then we were adding, everything inside the interface. But you were using some methods without an implementation.
So, for example, we were creating some drive queries, You were writing some method, names following some pattern,
but we were not exactly giving an implementation for it. But how it was it working? So It was all working behind the scene by Spring right?
So what's happening actually is on Runtime. there's going to be one implementation class created. And all these queries are going to be run for us,
for example, from this dried query again, as some sequel queries are going to be created, and so on. So on the Runtime.
There is going to be one implementation class, and Spring is creating the bean from that one.
 not from interface, because interfaces cannot have cannot be created as objects.
So that's why we're able to use it on top of interface. But that doesn't mean the object is getting created directly from the interface.
 */