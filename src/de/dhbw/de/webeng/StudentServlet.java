package de.dhbw.de.webeng;

import com.google.appengine.api.datastore.KeyFactory;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.servlet.RequestDispatcher;
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
public class StudentServlet extends HttpServlet{
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
                    int year = Integer.parseInt(request.getParameter("year"));
                    Student student = new Student(name, lastName, year);
                    EntityManager em = EMF.createEntityManager();
                    em.persist(student);
                    em.close();
                    response.sendRedirect(response.encodeRedirectURL("http://localhost:8080/klassenbuch/mainmenu.html"));
                    writer.write("Neuer Schüler " + name + " " + lastName + " mit der ID " + student.getId() + " angelegt.");
                }
                break;
                case "update": {
                    String id = request.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    Student student = em.find(Student.class, KeyFactory.createKey("Student", Long.parseLong(id)));

                    writer.write("found student " + student.getName() + " (" + student.getId() + ").");

                    student.setName(request.getParameter("name"));
                    student.setLastName(request.getParameter("lastname"));
                    student.setYear(Integer.parseInt(request.getParameter("year")));
                    writer.write("renamed student to " + student.getName() + student.getLastName() + " in year " + student.getYear() + ".");

                    em.merge(student);
                    em.close();
                }
                break;
                case "remove": {
                    String id = request.getParameter("id");

                    EntityManager em = EMF.createEntityManager();
                    Student student = em.find(Student.class, KeyFactory.createKey("Student", Long.parseLong(id)));
                    writer.write("found student " + student.getName() + " (" + student.getId() + ").");

                    em.remove(student);
                    writer.write("removed student " + student.getName() + ".");
                    em.close();
                }
                break;
                case "search1": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    Query query = em.createQuery("SELECT s FROM Student s WHERE s.name='" + searchName + "'");
                    try {
                        Student student = (Student) query.getSingleResult();
                        writer.write("found student " + student.getName() + " ("
                                + student.getId() + ").");
                    } catch (NoResultException e) {
                        writer.write("student " + searchName + " not found.");
                    } catch (NonUniqueResultException e) {
                        writer.write("too many students found.");
                    }
                    em.close();
                }
                break;
                case "search": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM Student s WHERE s.name='" + searchName + "'";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Student> list = (List<Student>) query.getResultList();

                    writer.write("\nfound " + list.size() + " students.");

                    for (Student student : list) {
                        writer.write("\nfound student " + student.getName() + " (" + student.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search2": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM Student s WHERE s.name=:searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName);

                    @SuppressWarnings("unchecked")
                    List<Student> list = (List<Student>) query.getResultList();

                    writer.write("\nfound " + list.size() + " students.");

                    for (Student student : list) {
                        writer.write("\nfound student " + student.getName() + " (" + student.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "search3": {
                    String searchName = request.getParameter("searchName");

                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM Student s WHERE s.name LIKE :searchName";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);
                    query.setParameter("searchName", searchName + "%");

                    @SuppressWarnings("unchecked")
                    List<Student> list = (List<Student>) query.getResultList();

                    writer.write("\nfound " + list.size() + " students.");

                    for (Student student : list) {
                        writer.write("\nfound student " + student.getName() + " (" + student.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allStudents": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT s FROM Student s";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Student> list = (List<Student>) query.getResultList();

                    writer.write("\nfound " + list.size() + " students.");

                    for (Student student : list) {
                        writer.write("\nfound student " + student.getName() + student.getLastName() + student.getYear() + " (" + student.getId() + ").");
                    }

                    em.close();
                }
                break;
                case "allNullStudents": {
                    EntityManager em = EMF.createEntityManager();
                    String s = "SELECT t FROM Student t WHERE t.name IS NULL";
                    writer.write("query " + s);
                    Query query = em.createQuery(s);

                    @SuppressWarnings("unchecked")
                    List<Student> list = (List<Student>) query.getResultList();

                    writer.write("\nfound " + list.size() + " students.");

                    List<Long> removeList = new ArrayList<Long>();
                    for (Student student : list) {
                        writer.write("\nfound student " + student.getName() + " ("
                                + student.getId() + ").");
                        removeList.add(student.getId());
                    }
                    em.close();

                    for (Long id : removeList) {
                        em = EMF.createEntityManager();
                        Student student = em.find(Student.class,
                                KeyFactory.createKey("Student", id));
                        writer.write("\nremove student " + student.getName() + " ("
                                + student.getId() + ").");
                        em.remove(student);
                        em.close();
                    }
                }
                break;
            }

        } catch (Exception e) {
            writer.write("The URL must have a \"method\" parameter! \n\n" +
                    "Possible parameters are:\n create (with name & lastname & year) \n" +
                    "update (with id(needed!) and name/lastname/year(you can choose all/two/just one)\n" +
                    "delete (with id) \n" +
                    "allNullStudents (deletes all Students that are null) \n" +
                    "allStudents (shows all Students) \n" +
                    "search1 (with lastname (only prename of the student)) ");
        }
    }
}
