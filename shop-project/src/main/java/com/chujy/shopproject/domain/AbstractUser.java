package com.chujy.shopproject.domain;

import com.chujy.shopproject.constant.Role;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type")
@Table(name = "abstract_user")
public abstract class AbstractUser extends BaseEntity implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public Role getRole() {
        return role;
    }


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setRole(Role role) {
        this.role = role;
    }
}
