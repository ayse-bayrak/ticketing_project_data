package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TaskRepository extends JpaRepository <Task, Long> {

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus <> 'COMPLETE'")
    int totalNonCompletedTasks(String projectCode);

    @Query(value = "SELECT COUNT(*) " +
            "FROM tasks t JOIN projects p on t.project_id=p.id " +
            "WHERE p.project_code=?1 AND t.task_status='COMPLETE'",nativeQuery = true)
    int totalCompletedTasks(String projectCode);

    List<Task> findAllByProject(Project project); // I am using derived query // part-5
    // if i useing derive queries i can work with entities directly inside my paramaters,
    //i can pass another entity or some old entities.
    //But I can not pass entities directly if i am using native or jpql queries,
    // i need to pass project id or string

}
