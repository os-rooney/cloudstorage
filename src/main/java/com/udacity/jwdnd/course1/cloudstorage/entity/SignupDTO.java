package com.udacity.jwdnd.course1.cloudstorage.entity;

public class SignupDTO {
    private String firstname;
    private String lastname;
    private String username;
    private String password;
    private boolean usernameExistsAlready;
    private boolean signupSucess;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isUsernameExistsAlready() {
        return usernameExistsAlready;
    }

    public void setUsernameExistsAlready(boolean usernameExistsAlready) {
        this.usernameExistsAlready = usernameExistsAlready;
    }

    public boolean isSignupSucess() {
        return signupSucess;
    }

    public void setSignupSucess(boolean signupSucess) {
        this.signupSucess = signupSucess;
    }
}
