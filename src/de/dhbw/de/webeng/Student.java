package de.dhbw.de.webeng;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Key;

/**
 * Created by Andreas on 24.10.2015.
 */

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Key key;
    private String name;
    private String lastname;
    private int year;

    @Transient
    private boolean loggedin;

    public Student() {
    }

    public Student(String name2, String lastname2, int year2) {
        name = name2;
        lastname=lastname2;
        year = year2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name2) {
        name = name2;
    }

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String name2) {
        lastname = name2;
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year2) {
        year = year2;
    }
    public long getId() {
        return key.getId();

    }
}
