package com.cafe.cafe_management.JWT;

import com.cafe.cafe_management.DAO.UserDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;


@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    UserDAO userDAO;

    @Autowired
    public CustomUserDetailsService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    private com.cafe.cafe_management.Model.User user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Inside loadUserByUsername {}", username);
        user = userDAO.findByEmailId(username);
        if(!Objects.isNull(user)){
            return new User(user.getEmail(),user.getPassword(), new ArrayList<>());
        }else {
            throw new UsernameNotFoundException("User not found.");
        }
    }

    public com.cafe.cafe_management.Model.User getUserDetail(){
        com.cafe.cafe_management.Model.User userDetail = user;
        userDetail.setPassword(null);
        return userDetail;
    }


}
