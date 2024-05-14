package com.cydeo.entity;

import lombok.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // postgres create this Id
    private Long id;
    private LocalDateTime insertDateTime;// we need in the database, need to keep track on some information, but I don't need to UI
    private Long insertUserId;
    private LocalDateTime lastUpdateDateTime;
    private Long lastUpdateUserId;

}
/*
I am creating two different object fro same object, DTO and entity.
Why?
Because some information may be not necessary for UI part.
 */