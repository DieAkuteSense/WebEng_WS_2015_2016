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
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Key key;
    private String teachername;
    private int year;

    @Transient
    private boolean loggedin;

    public SchoolClass() {
    }

    public SchoolClass(String t2,int year2) {
        teachername = t2;
        year = year2;
    }
    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String t2) {
        teachername = t2;
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
