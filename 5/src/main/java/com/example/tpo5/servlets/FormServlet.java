package com.example.tpo5.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


//@WebServlet(name = "form", value = "/form")
public class FormServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("type").isEmpty()) {
            request.removeAttribute("type");
        }
//        request.setAttribute("type", request.getParameter("type"));
        request.getRequestDispatcher("/db").forward(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
//
//        // Hello
//        PrintWriter out = response.getWriter();
//        out.println("<html><body>");
//        out.println("<h1>" + "message" + "</h1>");
//        out.println("</body></html>");

        request.setAttribute("type", request.getParameter("type"));
        request.getRequestDispatcher("/db").forward(request, response);
    }
}
