package de.dhbw.de.webeng;

import com.google.appengine.api.datastore.Key;

import javax.persistence.*;

/**
 * Created by Andreas on 24.10.2015.
 */

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Key key;
    private String name;
    private String lastName;
    private int year;

    @Transient
    private boolean loggedin;

    public Student() {
    }

    public Student(String name, String lastName, int year) {
        this.name = name;
        this.lastName = lastName;
        this.year = year;
    }

    public Student(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getId() {
        return key.getId();

    }
}
