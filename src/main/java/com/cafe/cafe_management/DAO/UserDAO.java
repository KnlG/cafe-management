package com.cafe.cafe_management.DAO;

import com.cafe.cafe_management.Model.User;
import com.cafe.cafe_management.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends JpaRepository <User, Integer> {

    User findByEmailId(@Param("email") String email);

    List<UserWrapper> getAllUsers();

    List<String> getAllAdmins();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);
}
