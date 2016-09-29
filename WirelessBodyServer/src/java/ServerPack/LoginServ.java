/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import JavaLib.LoadForm;
import LibPack.DoctorInfo;
import LibPack.PatientInfo;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ravee
 */
@WebServlet(name = "LoginServ", urlPatterns = {"/LoginServ"})
public class LoginServ extends HttpServlet {

    boolean flag;
    PatientInfo pi;
    Connection con;
    ResultSet rs;
    Statement stmt;
    String ssql, uid;
    DoctorInfo di;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            System.out.println("Servlet called ");
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            di = (DoctorInfo) in.readObject();
            in.close();
            flag = true;
        } catch (Exception e) {
            System.out.println("ERROR IN READING " + e);
        }
        initDatabase();
        try {
            ssql = "select * from doctor where did='" + di.did + "' and password='" + di.password + "'";
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(ssql);
            di = new DoctorInfo();
            if (rs.next()) {
                di.did = rs.getString(1);
                di.fname = rs.getString(2);
                di.mname = rs.getString(3);
                di.lname = rs.getString(4);
                di.password = rs.getString(5);
                di.gender = rs.getString(6);
                di.mobile = rs.getString(7);
                di.add = rs.getString(8);
                di.email = rs.getString(9);
                di.specialization = rs.getString(10);
                di.rating = rs.getString(11);
            }

        } catch (Exception e) {
            System.out.println("ERROR IN READING " + e);
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            out.writeObject(di);
            out.close();
        } catch (Exception e) {
            System.out.println("ERROR IN WRITING " + e);
        }
    }

    private void initDatabase() {
        String connection = "jdbc:mysql://localhost/db6555";
        String user = "root";
        String password = "root";
        String ssql;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(connection, user, password);
            System.out.println("Database Connection OK");
        } catch (Exception e) {
            System.out.println("Error opening database : " + e);

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        new LoadForm();
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        new LoadForm();
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
