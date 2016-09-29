/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerPack;

import JavaLib.LoadForm;
import LibPack.PatientInfo;
import java.io.*;
import java.sql.*;
import java.util.StringTokenizer;
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
@WebServlet(name = "GetPatientValues", urlPatterns = {"/GetPatientValues"})
public class GetPatientValues extends HttpServlet {

    PatientInfo pi;
    Connection con;
    ResultSet rs;
    Statement stmt;
    String ssql, uid;
    PreparedStatement pre;
    boolean flag;
    Vector<PatientInfo> allPatient;
    String str;
    int t;
    int hb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            str = (String) in.readObject();
            in.close();
            System.out.println("STRING REC: " + str);
        } catch (Exception e) {
            System.out.println("ERROR IN Tokenizing " + e);
        }
        initDatabase();
        try {
            ssql = "select * from patient where pid='" + str + "'";
            System.out.println(ssql);
            stmt = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_INSENSITIVE);
            rs = stmt.executeQuery(ssql);
            pi = new PatientInfo();
            if (rs.next()) {
                pi.pid = rs.getString(1);
                pi.fname = rs.getString(2);
                pi.mname = rs.getString(3);
                pi.lname = rs.getString(4);
                pi.gender = rs.getString(5);
                pi.mobile = rs.getString(6);
                pi.add = rs.getString(7);
                pi.email = rs.getString(8);
                pi.mediHist = rs.getString(9);
                pi.city = rs.getString(10);
                pi.temp = (Vector<Integer>) stringToObject(rs.getString(11));
                System.out.println("temp size :" +pi.temp.size());
                pi.heart = (Vector<Integer>) stringToObject(rs.getString(12));
            }
        } catch (Exception e) {
            System.out.println("ERROR IN READING FROM DB" + e);
        }
        try {
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            out.writeObject(pi);
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

    Object stringToObject(String inp) {
        byte b[] = Base64.decode(inp);
        Object ret = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            ObjectInput in = new ObjectInputStream(bis);
            ret = (Object) in.readObject();
            bis.close();
            in.close();
        } catch (Exception e) {
            System.out.println("NOT DE-SERIALIZABLE: " + e);
        }
        return ret;
    }

    String objectToString(Object obj) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            b = bos.toByteArray();
        } catch (Exception e) {
            System.out.println("NOT SERIALIZABLE: " + e);
        }
        return Base64.encode(b);
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
