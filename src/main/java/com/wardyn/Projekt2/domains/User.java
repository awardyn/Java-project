package com.wardyn.Projekt2.domains;

import com.wardyn.Projekt2.validatorInterfaces.ValidPassword;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Getter
@Setter
public class User {
    public Integer id;

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

    private List<Integer> appList = new ArrayList<>();
}
