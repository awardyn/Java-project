package com.wardyn.Projekt2.domains;

import com.wardyn.Projekt2.validatorInterfaces.ValidPassword;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Country cannot be null")
    @Size(min = 2, max = 50, message = "Country name must be between 2 and 50 characters")
    private String country;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String username;

    @ValidPassword
    @NotNull(message = "Password cannot be null")
    private String userPassword;

    @ManyToMany
    private List<App> appList;

    public User() {}

    public User(String firstName, String lastName, String email, String country, String username, String userPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.username = username;
        this.userPassword = userPassword;
    }
}
