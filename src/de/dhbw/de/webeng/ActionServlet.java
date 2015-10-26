package de.dhbw.de.webeng;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

/**
 * Created by Olli on 12.10.2015.
 */
public class ActionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("text/plain");
        out.println("Das ist das Action-Servlet");
        out.println("Die Telefonnummer " + request.getParameter("tel") + " hat " + request.getParameter("user") + " eingegeben!");
    }
}
