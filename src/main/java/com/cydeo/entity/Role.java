package com.cydeo.entity;

import jdk.jfr.Enabled;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="roles")//plural is best practice in the table
public class Role extends BaseEntity {

    private String description;

}
