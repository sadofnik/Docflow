/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.doverie.docflow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class addUrItem extends HttpServlet {
     private HttpSession session;
    private MySQLConnect con=null;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        request.setCharacterEncoding("UTF-8");
        session=request.getSession();
        if(session.getAttribute("userid")==null){
            response.sendRedirect("index.jsp");
        }
         BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "{}";
        if(br != null){
            json = br.readLine();
        }
        
        try {            
            JSONObject req=new JSONObject(URLDecoder.decode(json, "UTF-8"));
            con=new MySQLConnect("localhost", "docflow", "3306", "writer", "1");
            PreparedStatement prep=null;
            prep=con.getConnection().prepareStatement(
                    "INSERT INTO "
                        + "sprur(`id`,`companyname`, `companytype`,`uradres`, `contactname`,`tel`,`fax`,`email`,`bankname`,`bik`,`kor`,`raschet`,`kpp`,`inn`,`description`)"
                    + " VALUES  (NULL,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            prep.setString(1, req.getString("companyname"));
            prep.setString(2, req.getString("companytype"));
            prep.setString(3, req.getString("uradres"));
            prep.setString(4, req.getString("contactname"));
            prep.setString(5, req.getString("tel"));
            prep.setString(6, req.getString("fax"));
            prep.setString(7, req.getString("email"));
            prep.setString(8, req.getString("bankname"));
            prep.setString(9, req.getString("bik"));
            prep.setString(10, req.getString("kor"));
            prep.setString(11, req.getString("raschet"));
            prep.setString(12, req.getString("kpp"));
            prep.setString(13, req.getString("inn"));
            prep.setString(14, req.getString("description"));
                        
            prep.executeUpdate();
            JSONObject output=new JSONObject();
            output.accumulate("status", "success");
            out.println(output.toString());              
        }catch(Exception e){
            e.printStackTrace();
        } finally {            
            out.close();
            
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
