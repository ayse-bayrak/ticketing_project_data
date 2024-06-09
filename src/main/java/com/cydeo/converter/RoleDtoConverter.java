package com.cydeo.converter;

import com.cydeo.dto.RoleDTO;
import com.cydeo.service.RoleService;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

//when I chose to Role in UI, it is coming as a String But I need to pass as an object
//we need convert them
/*
##the drop-down was always saving the values as String.
it's saving them as string. For example, you're saving a user. It's calling all the setters. But the role.
Normally, you have an object for the role. Right? You don't have a String role. You have a roleDTO your role in the user.
but it is just saving it as a string. So it's calling the setter method. It's calling the set role method.
But it's trying to call. It's trying to set it with a string instead of a role.
 */
@Component
//@ConfigurationPropertiesBinding
public class RoleDtoConverter implements Converter<String, RoleDTO> {

    RoleService roleService;

    public RoleDtoConverter(@Lazy RoleService roleService) { // I use @Lazy because I got error about infinite circle create bean, and i say wait until I call RoleService
        this.roleService = roleService;
    }

    @Override
    public RoleDTO convert(String source) {
        if (source == null || source.equals("")) {  //  Select  -> ""
            return null;
        }
        return roleService.findById(Long.parseLong(source));
    }

}
