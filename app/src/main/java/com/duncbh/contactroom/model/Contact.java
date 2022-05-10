package com.duncbh.contactroom.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contact_table")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "occupation")
    private String occupation;
    @ColumnInfo(name = "age")
    private int age;
    @ColumnInfo(name = "gender")
    private String gender;

    public Contact() {
    }

    public Contact(@NonNull String name, String occupation) {
        this.name = name;
        this.occupation = occupation;
    }

    public Contact(@NonNull String name, String occupation, int age, String gender) {
        this.name = name;
        this.occupation = occupation;
        this.age = age;
        this.gender = gender;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
