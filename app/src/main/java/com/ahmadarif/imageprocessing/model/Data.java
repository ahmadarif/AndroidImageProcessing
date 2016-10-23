package com.ahmadarif.imageprocessing.model;

import io.realm.RealmObject;

public class Data extends RealmObject {

    private int id;
    private String character;
    private String pattern;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id=" + id +
                ", character='" + character + '\'' +
                ", pattern='" + pattern + '\'' +
                '}';
    }
}
