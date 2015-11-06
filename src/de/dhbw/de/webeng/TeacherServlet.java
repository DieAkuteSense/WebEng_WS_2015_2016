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

@SuppressWarnings("serial")
public class TeacherServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/text"); // response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Cache-Control", "no-cache, must-revalidate");
        response.setHeader("Access-Control-Allow-Origin", "*");

        String method = request.getParameter("method");

        PrintWriter writer = response.getWriter();
        try {
            switch (method) {
                case "create": {

                    String name = request.getParameter("name");
                    String lastName = request.getParameter("lastName");
                    Teacher teacher = new Teacher(name, lastName);
                    EntityManager em = EMF.createEntityManager();
                    em.persist(teacher);
                    em.close();
                    response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/klassenbuch/mainmenu.html"));
                    writer.write("Neuer Lehrer " + name + " " + lastName + " mit der ID " + teacher.getId() + " angelegt.");
                }
                break;
                case "update": {
                    String id = request.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    Teacher teacher = em.find(Teacher.class, KeyFactory.createKey("Teacher", Long.parseLong(id)));

                    writer.write("found teacher " + teacher.getName() + " " + teacher.getLastname() + " (" + teacher.getId() + ").");

                    teacher.setName(request.getParameter("name"));
                    teacher.setLastname(request.getParameter("lastName"));
                    writer.write("Lehrer umbenannt in " + teacher.getName() + " " + teacher.getLastname() + ".");

                    em.merge(teacher);
                    em.close();
                }
                break;
                case "remove": {
                    String id = request.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    Teacher teacher = em.find(Teacher.class, KeyFactory.createKey("Teacher", Long.parseLong(id)));
                    writer.write("Lehrer " + teacher.getName() + " (ID: " + teacher.getId() + ") gefunden.");

                    em.remove(teacher);
                    writer.write(" Lehrer " + teacher.getName() + " " + teacher.getLastname() + " gel�scht.");
                    em.close();
                }
                break;
                case "search1": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    Query query = em.createQuery("SELECT t FROM Teacher t WHERE t.name='" + searchName + "'");
                    try {
                        Teacher teacher = (Teacher) query.getSingleResult();
                        writer.write("found teacher " + teacher.getName() + " (" + teacher.getId() + ").");
                    } catch (NoResultException e) {
                        writer.write("teacher " + searchName + " not found.");
                    } catch (NonUniqueResultException e) {
                        writer.write("too many teachers found.");
                    }
                    em.close();
                }
                break;
                case "search": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name='" + searchName + "'";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " (" + teacher.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search2": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name=:searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName);

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " (" + teacher.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search3": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Teacher t WHERE t.name LIKE :searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName + "%");

                    @SuppressWarnings("unchecked")
                    List<Teacher> list = (List<Teacher>) query.getResultList();

                    writer.write("\nfound " + list.size() + " teachers.");

                    for (Teacher teacher : list) {
                        writer.write("\nfound teacher " + teacher.getName() + " (" + teacher.getId() + ").");
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

                    writer.write("\n" + list.size() + " Lehrer gefunden.");

                    for (Teacher teacher : list) {
                        writer.write("\n" + teacher.getName() + " " + teacher.getLastname() + " (ID: " + teacher.getId() + ").");
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
                        writer.write("\nLehrer " + teacher.getName() + " (ID: " + teacher.getId() + ") gefunden.");
                        removeList.add(teacher.getId());
                    }
                    em.close();

                    for (Long id : removeList) {
                        em = EMF.createEntityManager();
                        Teacher teacher = em.find(Teacher.class,
                                KeyFactory.createKey("Teacher", id));
                        writer.write("\nLehrer " + teacher.getName() + " (ID: " + teacher.getId() + ") gel�scht.");
                        em.remove(teacher);
                        em.close();
                    }
                }
                break;
            }
        } catch (Exception e) {
            writer.write("The URL must have a \"method\" parameter! \n\n" +
                    "Possible parameters are:\n create (with name&lastname) \n" +
                    "update (with id(needed!) and name and lastname(you can choose both, or just one of them)\n" +
                    "delete (with id) \n" +
                    "allNullTeachers (deletes all Teachers that are null) \n" +
                    "allTeachers (shows all Teachers) \n" +
                    "search1 (with lastname (only prename of the teacher))");
        }
    }
}
