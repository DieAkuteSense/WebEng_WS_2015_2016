package de.dhbw.de.webeng;

import com.google.appengine.api.datastore.Key;

import javax.persistence.*;

/**
 * Created by Andreas on 24.10.2015.
 */


//name nachname 


@Entity
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;

    private String name;
    private String lastName;

    @Transient
    private boolean loggedin;

    public Teacher() {
    }

    public Teacher(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return key.getId();
    }
}
