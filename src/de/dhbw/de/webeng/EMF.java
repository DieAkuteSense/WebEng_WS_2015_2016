package de.dhbw.de.webeng;

import javax.persistence.*;

public final class EMF {
    public static EntityManagerFactory emfInstance = Persistence.createEntityManagerFactory("transactions-optional");

    synchronized public static EntityManager createEntityManager() {
        return emfInstance.createEntityManager();

    }
}
