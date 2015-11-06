package de.dhbw.de.webeng;

import com.google.appengine.api.datastore.KeyFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

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
                    SchoolClass schoolclass = new SchoolClass(teachername, year);

                    EntityManager em = EMF.createEntityManager();
                    em.persist(schoolclass);
                    em.close();
                    writer.write("new Schoolclass " + teachername + " in the year " + String.valueOf(year) + " (" + schoolclass.getId() + ").");
                }
                break;
                case "update": {
                    String id = req.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    SchoolClass schoolClass = em.find(SchoolClass.class, KeyFactory.createKey("Schoolclass", Long.parseLong(id)));

                    writer.write("found schoolClass " + schoolClass.getTeachername() + " (" + schoolClass.getId() + ").");

                    schoolClass.setTeachername(req.getParameter("teachername"));
                    schoolClass.setYear(Integer.parseInt(req.getParameter("year")));
                    writer.write("renamed schoolClass to " + schoolClass.getTeachername() + " in year " + schoolClass.getYear() + ".");

                    em.merge(schoolClass);
                    em.close();
                }
                break;
                case "remove": {
                    String id = req.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    SchoolClass schoolClass = em.find(SchoolClass.class, KeyFactory.createKey("Schoolclass", Long.parseLong(id)));
                    writer.write("found schoolClass " + schoolClass.getTeachername() + " ("
                            + schoolClass.getId() + ").");

                    em.remove(schoolClass);
                    writer.write("removed schoolClass " + schoolClass.getTeachername() + ".");
                    em.close();
                }
                break;
                case "search1": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    Query query = em
                            .createQuery("SELECT s FROM SchoolClass s WHERE s.name='" + searchName + ":");
                    try {
                        SchoolClass schoolClass = (SchoolClass) query.getSingleResult();
                        writer.write("found schoolclass " + schoolClass.getTeachername() + " (" + schoolClass.getId() + ").");
                    } catch (NoResultException e) {
                        writer.write("schoolclass " + searchName + " not found.");
                    } catch (NonUniqueResultException e) {
                        writer.write("too many schoolclasses found.");
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

                    writer.write("\nfound " + list.size() + " schoolclasses.");

                    for (SchoolClass schoolClass : list) {
                        writer.write("\nfound schoolclass " + schoolClass.getTeachername() + " ("
                                + schoolClass.getId() + ").");
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

                    writer.write("\nfound " + list.size() + " schoolclasses.");

                    for (SchoolClass schoolClass : list) {
                        writer.write("\nfound schoolclass " + schoolClass.getTeachername() + " ("
                                + schoolClass.getId() + ").");
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

                    writer.write("\nfound " + list.size() + " schoolclasses.");

                    for (SchoolClass schoolClass : list) {
                        writer.write("\nfound schoolclass " + schoolClass.getTeachername() + " ("
                                + schoolClass.getId() + ").");
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

                    writer.write("\nfound " + list.size() + " schoolclasses.");

                    for (SchoolClass schoolClass : list) {
                        writer.write("\nfound schoolclass " + schoolClass.getTeachername() + " in year " + schoolClass.getYear() + " ("
                                + schoolClass.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allNullSchoolclasses": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM SchoolClass t WHERE t.name IS NULL";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<SchoolClass> list = (List<SchoolClass>) query.getResultList();

                    writer.write("\nfound " + list.size() + " schoolclasses.");

                    List<Long> removeList = new ArrayList<Long>();
                    for (SchoolClass schoolClass : list) {
                        writer.write("\nfound schoolclass " + schoolClass.getTeachername() + " ("
                                + schoolClass.getId() + ").");
                        removeList.add(schoolClass.getId());
                    }
                    em.close();

                    for (Long id : removeList) {
                        em = EMF.createEntityManager();
                        SchoolClass schoolClass = em.find(SchoolClass.class,
                                KeyFactory.createKey("Schoolclass", id));
                        writer.write("\nremove schoolclass " + schoolClass.getTeachername() + " ("
                                + schoolClass.getId() + ").");
                        em.remove(schoolClass);
                        em.close();
                    }
                }
                break;
            }
        } catch (Exception e) {
            writer.write("The URL must have a \"method\" parameter! \n\n" +
                    "Possible parameters are:\n create (with teachername & year) \n" +
                    "update (with id(needed!) and teachername/year(you can choose two/just one)\n" +
                    "delete (with id) \n" +
                    "allNullSchoolclasses (deletes all Schoolclasses that are null) \n" +
                    "allSchoolclasses (shows all Schoolclasses)");
        }
    }
}
