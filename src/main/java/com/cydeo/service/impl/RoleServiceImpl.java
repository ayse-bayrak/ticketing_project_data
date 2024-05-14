package com.cydeo.service.impl;

import com.cydeo.dto.RoleDTO;
import com.cydeo.entity.Role;
import com.cydeo.mapper.RoleMapper;
import com.cydeo.repository.RoleRepository;
import com.cydeo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public List<RoleDTO> listAllRoles() {
        //Controller called me and requesting all RoleDTOs so it can show in the drop-down in the ui
        //I need to make a call to db and all the roles from table
        // go to repository find a service(method ) which gives me the roles from db
        List<Role> roleList = roleRepository.findAll();

        //I have Role entities from DB
        //I need to convert those Role entities to DTOs
        //I need to use Modelmapper
        //I already created a class called RoleMapper and there are methods for me that will make this conversion

        return roleList.stream().map(roleMapper::convertToDto).collect(Collectors.toList());

    }

    //it is looking for RoleDTO but it is returning Role, so we need to convert it
    //we use modelmapper dependency to convert, if we din't  use third part library which is modelmapper
    //we need to one by one get it and set it, all the field we need to do. this is too much work.
    //so we are using a third party library

    @Override
    public RoleDTO findById(Long id) {
        return null;
    }
}
