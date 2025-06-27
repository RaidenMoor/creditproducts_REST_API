package com.example.creditproducts.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users")
@ToString(callSuper = true)
@SequenceGenerator(name = "default_gen", sequenceName = "users_seq", allocationSize = 1, initialValue = 12)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "jsonId")
public class User extends GenericModel{
    @Column(name="username", nullable = false)
    private String username;

    @Column(name="password", nullable = false)
    private String password;

    @ManyToOne(optional = false)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_users_roles"), nullable = false)
    private Role role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Client client;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(){

    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public Role getRole(){
        return role;
    }

    public void setRole(Role role){
        this.role = role;
    }



}
