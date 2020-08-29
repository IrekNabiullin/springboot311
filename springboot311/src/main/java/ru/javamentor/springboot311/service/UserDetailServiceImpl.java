package ru.javamentor.springboot311.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javamentor.springboot311.model.Roles;
import ru.javamentor.springboot311.model.User;
import ru.javamentor.springboot311.repository.UserDao;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserDetailServiceImpl implements UserDetailsService {
    //    private UserRepository userRepository;
    private UserDao userDao;

    @Autowired
    public UserDetailServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.getUserByName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        System.out.println("Login in process.User.login: " + user.getLogin());
        System.out.println("Login in process.User password: " + user.getPassword());
        System.out.println("Login in process.User login: " + user.getLogin());
        System.out.println("Login in process.User roles: " + mapRolesToAuthorities(user.getRoles()));
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Roles> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
}
