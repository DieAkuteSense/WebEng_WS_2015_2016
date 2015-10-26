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

@SuppressWarnings("serial")
public class TeacherServlet extends HttpServlet {
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

                    String name = req.getParameter("name");
                    String lastname = req.getParameter("lastname");
                    Teacher teacher = new Teacher(name,lastname);

                    EntityManager em = EMF.createEntityManager();
                    em.persist(teacher);
                    em.close();
                    writer.write("new teacher " + name + " " + lastname + " (" + teacher.getId()
                            + ").");
                }
                break;
                case "update": {
                    String id = req.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    Teacher teacher = em.find(Teacher.class,
                            KeyFactory.createKey("Teacher", Long.parseLong(id)));

                    writer.write("found teacher " + teacher.getName() +  " " + teacher.getLastname() +" ("
                            + teacher.getId() + ").");

                    teacher.setName(req.getParameter("name"));
                    teacher.setLastname(req.getParameter("lastname"));
                    writer.write("renamed teacher to " + teacher.getName() +" "+  teacher.getLastname() + ".");

                    em.merge(teacher);
                    em.close();
                }
                break;
                case "remove": {
                    String id = req.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    Teacher teacher = em.find(Teacher.class,
                            KeyFactory.createKey("Teacher", Long.parseLong(id)));
                    writer.write("found teacher " + teacher.getName() + " ("
                            + teacher.getId() + ").");

                    em.remove(teacher);
                    writer.write("removed teacher " + teacher.getName() +" "+  teacher.getLastname() + ".");
                    em.close();
                }
                break;
                case "search1": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    Query query = em
                            .createQuery("SELECT t FROM Teacher t WHERE t.name='"
                                    + searchName + "'");
                    try {
                        Teacher teacher = (Teacher) query.getSingleResult();
                        writer.write("found teacher " + teacher.getName() + " ("
                                + teacher.getId() + ").");
                    } catch (NoResultException e) {
                        writer.write("teacher " + searchName + " not found.");
                    } catch (NonUniqueResultException e) {
                        writer.write("too many teachers found.");
                    }
                    em.close();
                }
                break;
                case "search": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name='"
                            + searchName + "'";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " ("
                                + teacher.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search2": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name=:searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName);

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " ("
                                + teacher.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search3": {
                    String searchName = req.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name LIKE :searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName + "%");

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " ("
                                + teacher.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allTeachers": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() +" "+  teacher.getLastname() + " ("
                                + teacher.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allNullTeachers": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name IS NULL";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    List<Long> removeList = new ArrayList<Long>();
                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " ("
                                + teacher.getId() + ").");
                        removeList.add(teacher.getId());
                    }
                    em.close();

                    for (Long id : removeList) {
                        em = EMF.createEntityManager();
                        Teacher teacher = em.find(Teacher.class,
                                KeyFactory.createKey("Teacher", id));
                        writer.write("\nremove teacher " + teacher.getName() + " ("
                                + teacher.getId() + ").");
                        em.remove(teacher);
                        em.close();
                    }
                }
                break;
            }
        } catch (Exception e) {
            writer.write("The URL must have a \"method\" parameter! \n\n Possible parameters are:\n create (with name&lastname) \n update (with id(needed!) and name and lastname(you can choose both, or just one of them)\n delete (with id) \n allNullTeachers (deletes all Teachers that are null) \n allTeachers (shows all Teachers)");
        }
    }
}