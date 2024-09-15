package com.codebee.water_app.model;




public class Orders {

    private Long id;


    private String latitude;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitiude() {
        return longitiude;
    }

    public void setLongitiude(String longitiude) {
        this.longitiude = longitiude;
    }

    private String longitiude;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    private Customer customer;


    private Status status;



}
