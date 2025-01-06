package com.cafe.cafe_management.wrapper;

import lombok.Data;

@Data
public class UserWrapper {
    private Integer id;

    private String name;

    private String email;

    private String contactNumber;

    private String status;

    public UserWrapper(){}

    public UserWrapper(Integer id, String name, String contactNumber, String email, String status) {
        this.id = id;
        this.name = name;
        this.contactNumber = contactNumber;
        this.email = email;
        this.status = status;
    }
}
