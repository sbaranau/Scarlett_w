package com.baranau.sergey.scarlett_w.Entity;

/**
 * Created by sergey on 1/30/16.
 */
public class ParamsEntity {
    private long userId;
    private int date = 0;
    private float weight;
    private float fat;
    private float tdw;
    private float muscle;
    private float bones;
    private float kcal;
    private float bmi;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getTdw() {
        return tdw;
    }

    public void setTdw(float tdw) {
        this.tdw = tdw;
    }

    public float getMuscle() {
        return muscle;
    }

    public void setMuscle(float muscle) {
        this.muscle = muscle;
    }

    public float getBones() {
        return bones;
    }

    public void setBones(float bones) {
        this.bones = bones;
    }

    public float getKcal() {
        return kcal;
    }

    public void setKcal(float kcal) {
        this.kcal = kcal;
    }

    public float getBmi() {
        return bmi;
    }

    public void setBmi(float bmi) {
        this.bmi = bmi;
    }
}
