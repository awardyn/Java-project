package com.wardyn.Projekt2.domains;

import com.wardyn.Projekt2.validatorInterfaces.ValidDomain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

@Entity
@Setter
@Getter
public class App {

    @Id
    @GeneratedValue
    public Long id;

    @NotNull(message = "App name cannot be null")
    @Size(min = 2, max = 50, message = "App name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Domain cannot be null")
    @Size(min = 3, max = 50, message = "Domain name must be between 3 and 50 characters")
    @ValidDomain
    private String domain;

    @ManyToMany(fetch = javax.persistence.FetchType.EAGER, mappedBy = "appList")
    private List<User> userList = new ArrayList<>();

    public App() {}

    public App(String name, String domain) {
        this.name = name;
        this.domain = domain;
    }
}
