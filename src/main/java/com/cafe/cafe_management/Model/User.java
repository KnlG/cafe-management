package com.cafe.cafe_management.Model;

import jakarta.persistence.*;
//import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.io.Serializable;

@NamedQuery(name = "User.findByEmailId", query = "select u from User u where u.email =:email")
@NamedQuery(name="User.getAllUsers",
        query = "select new com.cafe.cafe_management.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) " +
                "from User u where u.role='user'")
@NamedQuery(name="User.getAllAdmins", query = "select u.email from User u where u.role='admin'")
@NamedQuery(name ="User.updateStatus", query = "update User u set u.status =: status where u.id =:id")

//@Data //getter, setter, equals, tostring ...
@Entity //create a corresponding table
@Table(name="user") // set table name in the database
@DynamicInsert //will generate SQL INSERT statements that only include the columns with non-null values
@DynamicUpdate //ensures that only the fields that have changed are included in the SQL UPDATE statement
public class User implements Serializable {
    @Serial //to indicate specific elements in a class related to serialization
    private static final long serialVersionUID = 42L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //relies on the database to auto-generate the primary key upon inserting a new row, usually through an auto-increment column
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name="number")
    private String contactNumber;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }
}
