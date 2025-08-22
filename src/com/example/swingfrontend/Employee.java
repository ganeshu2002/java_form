// package com.example.swingfrontend;

public class Employee {
    private Long userId;
    private String firstName;
    private String lastName;
    private String dob;
    private String doj; // Date of Joining
    private Integer age;
    private String address;
    private String mobile;
    private String city;
    private String state;
    private String country;
    private Integer education10;
    private Integer education12;
    private Integer graduation;
    private Double percentage; // auto-calculated

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getDoj() { return doj; }
    public void setDoj(String doj) { this.doj = doj; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public Integer getEducation10() { return education10; }
    public void setEducation10(Integer education10) { this.education10 = education10; }
    public Integer getEducation12() { return education12; }
    public void setEducation12(Integer education12) { this.education12 = education12; }
    public Integer getGraduation() { return graduation; }
    public void setGraduation(Integer graduation) { this.graduation = graduation; }
    public Double getPercentage() { return percentage; }
    public void setPercentage(Double percentage) { this.percentage = percentage; }
}
