package com.codebee.water_app.model;

public class Address {


    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String no;

    private String line1;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public Address(String no, String line1, String line2, City city) {
        this.id = id;
        this.no = no;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    private String line2;

    private City city;
}
