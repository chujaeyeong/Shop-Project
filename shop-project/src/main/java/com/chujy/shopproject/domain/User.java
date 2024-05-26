package com.chujy.shopproject.domain;

import com.chujy.shopproject.constant.Role;

public interface User {

    Long getId();
    String getName();
    String getEmail();
    String getAddress();
    Role getRole();

    void setName(String name);
    void setEmail(String email);
    void setAddress(String address);
    void setRole(Role role);

}
