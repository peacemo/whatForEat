package com.carlchenxq.zidong;

public class Drink {
    private static int count;
    private int id;
    private String name;
    private String loc;

    public Drink() {
        this.id = ++count;
        this.name = "";
        this.loc = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    @Override
    public String toString() {
        return " name: " + name + " Loc: " + loc;
    }
}
