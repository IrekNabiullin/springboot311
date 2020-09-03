package ru.javamentor.springboot311.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.javamentor.springboot311.model.Roles;
import ru.javamentor.springboot311.service.UserService;
import ru.javamentor.springboot311.model.User;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller

@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String printWelcome(Authentication authentication, ModelMap modelMap) {
        User user = userService.getUserByName(authentication.getName());

        modelMap.addAttribute("users", userService.listUsers());
        modelMap.addAttribute("user", user);
//        return "admin";
        return "users";
    }

    @GetMapping(value = "/profile")
    public String getProfile(Authentication authentication, ModelMap modelMap) {
        User user = userService.getUserByName(authentication.getName());
        System.out.println("Profile. Current authority:" + authentication.getAuthorities());
        modelMap.addAttribute("user", user);
        return "profile";
    }


//    @GetMapping(value = "/users")
//    public String getUsers(ModelMap modelMap, Authentication authentication) {
//        User user = userService.getUserByName(authentication.getName());
//        modelMap.addAttribute("user", user);
//        modelMap.addAttribute("users", userService.listUsers());
//        return "users";
//    }

    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(name = "locale", defaultValue = "en", required = false) String locale, ModelMap modelMap,
                           Authentication authentication, HttpServletRequest request) {
        modelMap.addAttribute("users", userService.listUsers());
        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        User user = userService.getUserByName(authentication.getName());
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        return "users";
    }

    @GetMapping(value = "/users/edit")
    public String editPage(@RequestParam(value = "id") String id, ModelMap modelMap) {
        Long userId = Long.parseLong(id);
        User user = userService.getUserById(userId);
        modelMap.addAttribute("user", user);
        return "editPage";
    }

    @PostMapping(value = "/users/edit")
    public String editUser(@RequestParam(value = "id") String id,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "last_name") String last_name,
                           @RequestParam(value = "email") String email,
                           @RequestParam(value = "login") String login,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "role") String roleOfUser,
                           ModelMap modelMap, Authentication authentication,
                           @RequestParam(name = "locale", defaultValue = "en", required = false) String locale) {

        Long userId = Long.parseLong(id);
        Collection<Roles> roles2 = new ArrayList<>(userService.getUserByName("USER").getRoles());
        if (roleOfUser.contains("ADMIN") || roleOfUser.contains("admin")) {
            roles2.clear();
            roles2.addAll(userService.getUserByName("ADMIN").getRoles());
            System.out.println("User editing. Roles of user " + name + ": " + roles2.toString());
        }
        System.out.println("User editing. Roles of user " + name + ": " + roles2.toString());
        User tempUser = new User(userId, name, last_name, email, login, password, roles2);
        tempUser.setId(userId);
        userService.updateUser(tempUser);

        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        modelMap.addAttribute("users", userService.listUsers());
        User user = userService.getUserByName(authentication.getName());
        modelMap.addAttribute("user", user);
        return "users";
    }

    @GetMapping(value = "/users/add")
    public String addForm(ModelMap modelMap, Authentication authentication) {
        User user = userService.getUserByName(authentication.getName());
        modelMap.addAttribute("user", user);
        return "addPage";
    }

    @PostMapping(value = "/users/add", produces = "text/html; charset=utf-8")
    public String addNewUser(@RequestParam(value = "name") String name,
                             @RequestParam(value = "last_name") String last_name,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "login") String login,
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "role") String roleOfNewUser,
                             ModelMap modelMap, Authentication authentication) {

        Collection<Roles> rolesNewUser = new ArrayList<>(userService.getUserByName("USER").getRoles());
        rolesNewUser.clear();
        rolesNewUser.addAll(userService.getUserByName("USER").getRoles());
        System.out.println("Adding new user. Roles of user " + name + ": " + rolesNewUser.toString());
        if (roleOfNewUser.contains("ADMIN") || roleOfNewUser.contains("admin")) {
            rolesNewUser.clear();
            rolesNewUser.addAll(userService.getUserByName("ADMIN").getRoles());
            System.out.println("Adding new user. Roles of user " + name + ": " + rolesNewUser.toString());
        }
        userService.addUser(new User(name, last_name, email, login, password, rolesNewUser));
        User user = userService.getUserByName(authentication.getName());
        modelMap.addAttribute("user", user);
        modelMap.addAttribute("users", userService.listUsers());
        return "addPage";
    }

    @GetMapping("/users/delete")
    public String deleteUser(@RequestParam(value = "id") String id, ModelMap modelMap,
//                             @RequestParam(name = "locale", defaultValue = "en", required = false) String locale,
                             Authentication authentication) {
        System.out.println("Delete user with id = " + id);
        Long userId = Long.parseLong(id);
        userService.deleteUser(userId);
//        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
//        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        modelMap.addAttribute("users", userService.listUsers());
        User user = userService.getUserByName(authentication.getName());
        modelMap.addAttribute("user", user);
        return "users";
    }


    @GetMapping(value = "/logout")
    public String logOut(Authentication authentication, ModelMap modelMap) {
        return "logout";
    }

    @GetMapping(value = "/error")
    public String getError() {
        return "success";
    }

}
