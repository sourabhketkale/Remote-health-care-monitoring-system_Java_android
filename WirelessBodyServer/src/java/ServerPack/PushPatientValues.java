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
@WebServlet(name = "PushPatientValues", urlPatterns = {"/PushPatientValues"})
public class PushPatientValues extends HttpServlet {

    PatientInfo pi;
    Connection con;
    ResultSet rs;
    Statement stmt;
    String ssql, uid;
    PreparedStatement pre;
    boolean flag;
    Vector<PatientInfo> allPatient;
    Vector<Integer> heart;
    Vector<Integer> temp;
    String str;
    int t;
    int hb;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        try {
            ObjectInputStream in = new ObjectInputStream(request.getInputStream());
            str = (String) in.readObject();
            in.close();
            System.out.println("STRING REC: "+str);
            StringTokenizer st = new StringTokenizer(str, ",");
            if (st.hasMoreTokens()) {
                hb = Integer.parseInt(st.nextToken());
                System.out.println("H VALUES :" + hb);

            }
            if (st.hasMoreTokens()) {
                t = Integer.parseInt(st.nextToken());
                System.out.println("T VALUES :" + t);

            }
            if (st.hasMoreTokens()) {
                uid = st.nextToken();
            }

        } catch (Exception e) {
            System.out.println("ERROR IN Tokenizing " + e);
        }
        initDatabase();
        try {
            ssql = "select * from patient where pid='" + uid + "'";
            System.out.println(ssql);
            stmt = con.createStatement(ResultSet.CONCUR_UPDATABLE, ResultSet.TYPE_SCROLL_INSENSITIVE);
            rs = stmt.executeQuery(ssql);
            if (rs.next()) {
                heart = (Vector<Integer>) stringToObject(rs.getString(11));
                temp = (Vector<Integer>) stringToObject(rs.getString(12));
            } else {
                

                
            }
        } catch (Exception e) {
            System.out.println("ERROR IN READING FROM DB" + e);
        }
        try {
            heart.add(0, hb);
            temp.add(0, t);
            String ssql = "update patient set temp=?,heartbeat=?  where pid='" + uid + "'";
            pre = con.prepareStatement(ssql);
            pre.setString(1, objectToString(heart));
            pre.setString(2, objectToString(temp));
            pre.executeUpdate();
        } catch (Exception e) {
            System.out.println("ERROR IN Putting values " + e);
        }

        try {
            ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
            out.writeObject(flag);
            out.close();
        } catch (Exception e) {
            System.out.println("ERROR IN WRITING " + e);
        }
    }

    private void initDatabase() {
        String connection = "jdbc:mysql://localhost/db6555";
        String user = "root";
        String password = "krishna";
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
