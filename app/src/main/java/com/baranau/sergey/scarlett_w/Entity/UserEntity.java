package com.baranau.sergey.scarlett_w.Entity;

/**
 * Created by sergey on 1/30/16.
 */
public class UserEntity {
    private long id = -1;
    private String name = "User";
    private int age = 18;
    private int regDate = 0;
    private int height = 170;
    private int gender = 0; // 0- female, 1 - man

    public int getRegDate() {
        return regDate;
    }

    public void setRegDate(int regDate) {
        this.regDate = regDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getGender() {
        return gender;
    }

    public String getGenderValue() {
        return gender == 1? "Man" : "Woman";
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}
