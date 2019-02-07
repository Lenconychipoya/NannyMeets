package zw.co.appsareus.nannymeets.models;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String surname;
    private String dob;
    private String gender;
    private String id;
    private String email;
    private String phone;
    private String address;
    private String natID;
    private String password;
    private String accountType;
    private String username;
    private String city;
    private String country;
    private ProfilePicture profilePic;

    public User() {
    }

    public User(String name, String surname, String dob, String gender, String id, String email, String phone, String address, String natID, String password, String accountType, String username, String city, String country) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.gender = gender;
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.natID = natID;
        this.password = password;
        this.accountType = accountType;
        this.username = username;
        this.city = city;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public User setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getDob() {
        return dob;
    }

    public User setDob(String dob) {
        this.dob = dob;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getNatID() {
        return natID;
    }

    public User setNatID(String natID) {
        this.natID = natID;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAccountType() {
        return accountType;
    }

    public User setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public User setUsername(String username) {
        this.username = username;
        return this;
    }

    public ProfilePicture getProfilePic() {
        return profilePic;
    }

    public User setProfilePic(ProfilePicture profilePic) {
        this.profilePic = profilePic;
        return this;
    }

    public String getCity() {
        return city;
    }

    public User setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public User setCountry(String country) {
        this.country = country;
        return this;
    }
}
