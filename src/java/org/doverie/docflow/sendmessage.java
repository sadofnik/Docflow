/*
 * To change this template, choose Tools | Templates
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Александр
 */
public class sendmessage extends HttpServlet {
    private HttpSession session;
    private MySQLConnect con=null;
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
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
        System.out.println("asdasdasd");
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
            JSONObject output=new JSONObject();
            //JSONArray output=new JSONArray();
            JSONObject req=new JSONObject(URLDecoder.decode(json, "UTF-8"));
            con=new MySQLConnect("localhost", "docflow", "3306", "writer", "1");
            PreparedStatement statement=con.getConnection().prepareStatement(
                    "INSERT INTO `msgbox`(`id`,`fromid`, `toid`, `type`, `deleteFlag`, `message`, `itemlist`) "
                            + "VALUES (NULL,?,?,?,?,?,?)"
            );
            statement.setString(1, (String)session.getAttribute("userid"));
            statement.setString(2, req.getString("sendTo"));
            statement.setString(3, req.getString("type"));
            statement.setBoolean(4, false);
            statement.setString(5, req.getString("msgText"));
            statement.setString(6, req.getString("itemList"));
            
            statement.executeUpdate();
            
            output.accumulate("status", "success");
            out.println(output.toString());
            con.getConnection().close();
        }catch(Exception e){
            e.printStackTrace();
        } finally {            
            out.close();
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
        //processRequest(request, response);
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
