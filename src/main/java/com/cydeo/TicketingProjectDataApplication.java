package com.cydeo;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication // this includes @Configuration so we can put @Bean for method to create ModelMapper bean
public class TicketingProjectDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingProjectDataApplication.class, args);
    }

    //I am trying to add bean in the container through @Bean annotation
    //Becasue it is not our class so we don't put @Component anywhere
    //create a class annotated with @Configuration
    //Write a method which return the object that you trying to add in the container
    //Annotate this method with @Bean

    @Bean
    public ModelMapper mapper() {
        return new ModelMapper();
    }
}
