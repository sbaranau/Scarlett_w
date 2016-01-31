package com.baranau.sergey.scarlett_w.Global;

/**
 * Created by sergey on 1/30/16.
 */
public class GlobalVars {

    private long id;
    private String name;

    private static GlobalVars globalVars;

    private GlobalVars() {
        id = 0;
        name = "";
    }

    public static GlobalVars getInstance() {
        if (globalVars == null) {
            globalVars = new GlobalVars();
        }
        return globalVars;
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
