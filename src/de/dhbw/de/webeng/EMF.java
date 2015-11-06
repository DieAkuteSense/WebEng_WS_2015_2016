package de.dhbw.de.webeng;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    public static EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");

    synchronized public static EntityManager createEntityManager() {
        return emfInstance.createEntityManager();

    }
}
