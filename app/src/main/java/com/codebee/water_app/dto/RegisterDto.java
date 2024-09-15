package com.codebee.water_app.dto;

import com.codebee.water_app.model.Address;
import com.codebee.water_app.model.City;

public class RegisterDto {

    private String firstName;

    public RegisterDto(String firstName, String lastName, String mobile, String email, String password, String no, String line1, String line2, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.no = no;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
    }

    private String lastName;

    private String mobile;
    private String email;
    private String password;


    private String no;

    private String line1;

    private String line2;

    private String city;

}
