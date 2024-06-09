package com.cydeo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {
//we want to it to be super class and also we want it to be mapped
// .. so we name it like MappedSuperclass
    /*
    we don't want it Table. That's why we don't use the Entity.
    But in order for this mapping, just like you must had to happen.
    We need to include this @MappedSuperclass
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // postgres create this Id
    private Long id;

    @Column(nullable = false, updatable = false) // when we try to update something ignore these fields, ignore what it means just keep the data for the creation time, don't touch it when it's updating
    private LocalDateTime insertDateTime;// we need in the database, need to keep track on some information, but I don't need to UI

    @Column(nullable = false, updatable = false)
    private Long insertUserId;

    @Column(nullable = false)
    private LocalDateTime lastUpdateDateTime;

    @Column(nullable = false)
    private Long lastUpdateUserId;

    private Boolean isDeleted = false;

    @PrePersist
    private void onPrePersist(){
        this.insertDateTime = LocalDateTime.now();
        this.lastUpdateDateTime = LocalDateTime.now();
        this.insertUserId=1L;  // we use 1L for this field until the security portion, now hardcoded in the security I'm gonna change this one to dynamic
        this.lastUpdateUserId=1L;
    }  // we create this method for initialize field
    //this method needs to be executed whenever I create a new user, this function will be done by @PrePersist annotation

    @PreUpdate
    private void onPreUpdate(){
        this.lastUpdateDateTime = LocalDateTime.now();
        this.lastUpdateUserId=1L;
    }//this method needs to be executed whenever I update the  user, this function will be done by @PreUpdate annotation

}
/*
I am creating two different object for same object, DTO and entity.
Why?
Because some information may be not necessary for UI part.
 */