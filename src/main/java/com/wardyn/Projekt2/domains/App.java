package com.wardyn.Projekt2.domains;

import com.wardyn.Projekt2.validatorInterfaces.ValidDomain;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Integer.parseInt;

@Setter
@Getter
public class App {
    public Integer id;

    @NotNull(message = "App name cannot be null")
    @Size(min = 2, max = 50, message = "App name must be between 2 and 50 characters")
    private String name;

    @NotNull(message = "Domain cannot be null")
    @Size(min = 3, max = 50, message = "Domain name must be between 3 and 50 characters")
    @ValidDomain
    private String domain;

    private List<Integer> userList = new ArrayList<>();

    public App() {}

    public App(String id, String name, String domain) {
        this.userList = new ArrayList<>();
        this.id = parseInt(id);
        this.name = name;
        this.domain = domain;
    }
}
