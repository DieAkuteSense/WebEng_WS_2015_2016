package de.dhbw.de.webeng;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.KeyFactory;

/**
 * Created by Andreas on 24.10.2015.
 */


//name Nachname Klasse 
@SuppressWarnings("serial")
public class SchoolClassServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/text"); // resp.setContentType("application/json");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Cache-Control", "no-cache, must-revalidate");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        String method = req.getParameter("method");

        PrintWriter writer = resp.getWriter();
        try {
            switch (method) {
                case "create": {


                    String teachername = req.getParameter("teachername");
                    int year = Integer.parseInt(req.getParameter("year"));
                    SchoolClass shoolclass = new SchoolClass(teachername,year);

                    EntityManager em = EMF.createEntityManager();
                    em.persist(shoolclass);
                    em.close();
                    writer.write("new Shoolclass " + teachername +  " in the year " + String.valueOf(year) + " (" + shoolclass.getId()+ ").");
                }
                break;
                case "update": {
                    String id = req.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    SchoolClass shoolclass = em.find(SchoolClass.class, KeyFactory.createKey("Shoolclass", Long.parseLong(id)));

                    writer.write("found shoolclass " + shoolclass.getTeachername() + " ("+ shoolclass.getId() + ").");

                    shoolclass.setTeachername(req.getParameter("teachername"));
                    shoolclass.setYear(Integer.parseInt(req.getParameter("year")));
                    writer.write("renamed shoolclass to " + shoolclass.getTeachername() + " in year "+ shoolclass.getYear() +  ".");

                    em.merge(shoolclass);
                    em.close();
                }
                break;
                case "remove": {
                    String id = req.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    SchoolClass shoolclass = em.find(SchoolClass.class, KeyFactory.createKey("Shoolclass", Long.parseLong(id)));
                    writer.write("found shoolclass " + shoolclass.getTeachername() + " ("
                            + shoolclass.getId() + ").");

                    em.remove(shoolclass);
                    writer.write("removed shoolclass " + shoolclass.getTeachername() + ".");
                    em.close();
                }
                break;
                case "search1": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    Query query = em
                            .createQuery("SELECT s FROM Shoolclass s WHERE s.name='" + searchName + ":");
                    try {
                        SchoolClass shoolclass = (SchoolClass) query.getSingleResult();
                        writer.write("found shoolclass " + shoolclass.getTeachername() + " (" + shoolclass.getId() + ").");
                    } catch (NoResultException e) {
                        writer.write("shoolclass " + searchName + " not found.");
                    } catch (NonUniqueResultException e) {
                        writer.write("too many shoolclasses found.");
                    }
                    em.close();
                }
                break;
                case "search": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM SchoolClass s WHERE s.name='"
                            + searchName + "'";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<SchoolClass> list = (List<SchoolClass>) query.getResultList();

                    writer.write("\nfound " + list.size() + " shoolclasses.");

                    for (SchoolClass shoolclass : list) {
                        writer.write("\nfound shoolclass " + shoolclass.getTeachername() + " ("
                                + shoolclass.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search2": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM SchoolClass s WHERE s.name=:searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName);

                    @SuppressWarnings("unchecked")
                    List<SchoolClass> list = (List<SchoolClass>) query.getResultList();

                    writer.write("\nfound " + list.size() + " shoolclasses.");

                    for (SchoolClass shoolclass : list) {
                        writer.write("\nfound shoolclass " + shoolclass.getTeachername() + " ("
                                + shoolclass.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search3": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM SchoolClass s WHERE s.name LIKE :searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName + "%");

                    @SuppressWarnings("unchecked")
                    List<SchoolClass> list = (List<SchoolClass>) query.getResultList();

                    writer.write("\nfound " + list.size() + " shoolclasses.");

                    for (SchoolClass shoolclass : list) {
                        writer.write("\nfound shoolclass " + shoolclass.getTeachername() + " ("
                                + shoolclass.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allSchoolclasses": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM SchoolClass s";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<SchoolClass> list = (List<SchoolClass>) query.getResultList();

                    writer.write("\nfound " + list.size() + " shoolclasses.");

                    for (SchoolClass shoolclass : list) {
                        writer.write("\nfound shoolclass "  +shoolclass.getTeachername()+" in year " +shoolclass.getYear() + " ("
                                + shoolclass.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allNullShoolclasses": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM SchoolClass t WHERE t.name IS NULL";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<SchoolClass> list = (List<SchoolClass>) query.getResultList();

                    writer.write("\nfound " + list.size() + " shoolclasses.");

                    List<Long> removeList = new ArrayList<Long>();
                    for (SchoolClass shoolclass : list) {
                        writer.write("\nfound shoolclass " + shoolclass.getTeachername() + " ("
                                + shoolclass.getId() + ").");
                        removeList.add(shoolclass.getId());
                    }
                    em.close();

                    for (Long id : removeList) {
                        em = EMF.createEntityManager();
                        SchoolClass shoolclass = em.find(SchoolClass.class,
                                KeyFactory.createKey("Shoolclass", id));
                        writer.write("\nremove shoolclass " + shoolclass.getTeachername() + " ("
                                + shoolclass.getId() + ").");
                        em.remove(shoolclass);
                        em.close();
                    }
                }
                break;
            }
        } catch (Exception e) {
            writer.write("The URL must have a \"method\" parameter! \n\n Possible parameters are:\n create (with teachername & year) \n update (with id(needed!) and teachername/year(you can choose two/just one)\n delete (with id) \n allNullShoolclasses (deletes all Shoolclasses that are null) \n allShoolclasses (shows all Shoolclasses)");
        }
    }
}
