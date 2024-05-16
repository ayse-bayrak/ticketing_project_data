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
@Where(clause = "is_deleted=false") // in the interview toy can explain this one, most of the junior developer not aware of this one.
public class User extends BaseEntity {
//@Where(clause = "is_deleted=false")
    //whatever repository which is using the User entity (in my case UserRepository)
    //whatever queries inside (findByUsername, deleteByUserName...) include where(clause  = "?") all queries is gonna be combine,
// concatenate in my case where is_deleted=false
    //we want to delete UI without deleting database and for this we use this annotation
    private String firstName;
    private String lastName;
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;

    @ManyToOne //always many is the owner
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;


}
