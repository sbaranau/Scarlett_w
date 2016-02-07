package com.baranau.sergey.scarlett_w.Global;

import android.app.Activity;

import com.baranau.sergey.scarlett_w.dao.DataBaseHelper;

/**
 * Created by sergey on 1/30/16.
 */
public class GlobalVars {

    private long id;
    private String name;
    private float weight;
    private int age;
    private int gender;

    private static GlobalVars globalVars;

    private GlobalVars() {
        id = 0;
        name = "";
        weight = 0;
        age = 0;
        gender = 0;
    }

    public static GlobalVars getInstance() {
        if (globalVars == null) {
            globalVars = new GlobalVars();
        }
        return globalVars;
    }


    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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
}
