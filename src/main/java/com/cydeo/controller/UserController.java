package com.cydeo.controller;

import com.cydeo.dto.UserDTO;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final RoleService roleService;
    private final UserService userService;

    public UserController(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/create")
    public String createUser(Model model){

        model.addAttribute("user", new UserDTO());
        model.addAttribute("roles", roleService.listAllRoles());
        model.addAttribute("users", userService.listAllUsers());

        return "/user/create";
//Interview perspective: we are passing some data to my view  by using model structure
    }
// video part 1 stops here.
    @PostMapping("/create")
    public String insertUser(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {
       //BindingResult is the one we use for checking if validations are passed or not on the models (DTOs in our case) coming from UI.
       //BindingResult parameter has to come right after the model object parameter (Which is the DTO, not the "Model model")
       /*
       Because of the design of the BindingResult you need to pass it as a parameter right after the object you want to validate.
       If you were injecting it, then you would need to specify which object you want to validate in each of the controller methods.
       But there is nothing that gives you that kind of manual control over the BindingResult, so that's why we can't inject and use it.
        */

        if (bindingResult.hasErrors()) {

            model.addAttribute("roles", roleService.listAllRoles());
            model.addAttribute("users", userService.listAllUsers());

            return "/user/create";

        }
        userService.save(user);

        return "redirect:/user/create"; // return specific endpoint
    }

    @GetMapping("/update/{username}")
    public String editUser(@PathVariable("username") String username, Model model) {

        model.addAttribute("user", userService.findByUserName(username));
        model.addAttribute("roles", roleService.listAllRoles());
        model.addAttribute("users", userService.listAllUsers());

        return "/user/update";

    }

    @PostMapping("/update")
    public String updateUser( @Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.listAllRoles());
            model.addAttribute("users", userService.listAllUsers());
            return "/user/update";
        }
        userService.update(user);
        return "redirect:/user/create";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable("username") String username) {
      //  userService.deleteByUserName(username);//now we don't use this method because we want to soft delete, only delete from UI without deleting database
        userService.delete(username);
        return "redirect:/user/create";
    }

}
