package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor // we need because of jpql stuff
@Entity
@Table(name="users")
//@Where(clause = "is_deleted=false")  // select * from users Where id = 4 and is_deleted = false;
// in the interview you can explain this one, most of the junior developer not aware of this one.
public class User extends BaseEntity {
//@Where(clause = "is_deleted=false")
    //whatever repository which is using the User entity (in my case UserRepository)
    //whatever queries in side (findByUsername, deleteByUserName...) include where(clause  = "?") all queries is gonna be combine,
    //concatenate in my case where is_deleted=false
    //we want to delete UI without deleting database and for this we use this annotation
    /*
    if you have this on top of the user entity. It's not going to be applied only to this queries you have inside user repository.
It is going to be applied to all the queries using User. That means, for example, in the project repository or task repository.
It says, find all,It's finding all the projects right. We're not in user repository. Now find all by assigned manager.
and it is trying to use the user entity. It is trying to find the project based on an assigned manager user.
Since this is also using this user entity or table, it is also going to apply this same condition for that query as well.
So it is dangerous to put @Where(clause = "is_deleted=false") to top op level
     */
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;

    @ManyToOne //always many is the owner
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
