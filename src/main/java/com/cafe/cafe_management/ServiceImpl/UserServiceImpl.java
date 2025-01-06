package com.cafe.cafe_management.ServiceImpl;

import com.cafe.cafe_management.JWT.CustomUserDetailsService;
import com.cafe.cafe_management.JWT.JWTFilter;
import com.cafe.cafe_management.JWT.JWTUtils;
import com.cafe.cafe_management.Model.User;
import com.cafe.cafe_management.DAO.UserDAO;
import com.cafe.cafe_management.Service.UserService;
import com.cafe.cafe_management.constants.CafeConstants;
import com.cafe.cafe_management.utils.CafeUtils;
import com.cafe.cafe_management.utils.EmailUtils;
import com.cafe.cafe_management.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {


    private UserDAO userDAO;

    private AuthenticationManager authenticationManager;

    private CustomUserDetailsService customUserDetailsService;

    private JWTUtils jwtUtils;

    private PasswordEncoder passwordEncoder;

    private JWTFilter jwtFilter;

    private EmailUtils emailUtils;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, AuthenticationManager authenticationManager,
                           CustomUserDetailsService customUserDetailsService, JWTUtils jwtUtils,
                           PasswordEncoder passwordEncoder, EmailUtils emailUtils){
        this.userDAO = userDAO;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.emailUtils = emailUtils;
    }

    @Autowired
    public void setJwtFilter(JWTFilter jwtFilter){
        this.jwtFilter = jwtFilter;
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try{
            if(validateSignUpMap(requestMap)){
                User user = userDAO.findByEmailId(requestMap.get("email"));
                if(Objects.isNull(user)){
                    userDAO.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully registered.", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateSignUpMap(Map<String, String> requestMap){
        return requestMap.containsKey("name")&&requestMap.containsKey("contactNumber")
                &&requestMap.containsKey("email")&&requestMap.containsKey("password");
    }

    private User getUserFromMap(Map <String, String> requestMap){
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncoder.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            System.out.println(requestMap.get("email") + " " + requestMap.get("password"));
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),
                            requestMap.get("password")));
            if(auth.isAuthenticated()){
                if(customUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtils.generateToken(
                            customUserDetailsService.getUserDetail().getEmail(),
                            customUserDetailsService.getUserDetail().getRole())+"\"}", HttpStatus.OK);
                }
                return new ResponseEntity<String>("{\"message\":\"Wait for admin approval\"}", HttpStatus.BAD_REQUEST);
            }
        }catch(Exception ex){
            log.error("{}", ex);
        }

        return new ResponseEntity<String>("{\"message\":\"Bad credentials.\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUsers() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDAO.getAllUsers(), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
                Optional<User> optional = userDAO.findById(Integer.parseInt(requestMap.get("id")));
                if (optional.isPresent()){
                    userDAO.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendEmailToAllAdmins(requestMap.get("status"), optional.get().getEmail(), userDAO.getAllAdmins());
                    return CafeUtils.getResponseEntity("User status updated successfully.", HttpStatus.OK);
                }else{
                    return CafeUtils.getResponseEntity("User id doesn't exist.", HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            User userObj = userDAO.findByEmailId(jwtFilter.getCurrentUser());
            if(userObj != null){
                if(passwordEncoder.matches(requestMap.get("oldPassword"), userObj.getPassword())){
                    userObj.setPassword(passwordEncoder.encode(requestMap.get("newPassword")));
                    userDAO.save(userObj);
                    return CafeUtils.getResponseEntity("Password updated successfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect old password.", HttpStatus.BAD_REQUEST);
            }
            CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDAO.findByEmailId(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())){
                emailUtils.forgotPassword(user.getEmail(), "Crededntials for Cafe Management System", user.getPassword());
            }
            return CafeUtils.getResponseEntity("Check your email for credentials.", HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendEmailToAllAdmins(String status, String user, List<String> allAdmins) {
        allAdmins.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account approved.",
                    "USER:- " + user + "\n is approved by \n ADMIN:-"+jwtFilter.getCurrentUser(), allAdmins);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account disabled.",
                    "USER:- " + user + "\n is disabled by \n ADMIN:-"+jwtFilter.getCurrentUser(), allAdmins);
        }
    }


}
