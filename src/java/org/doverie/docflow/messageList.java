/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.doverie.docflow;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author admin
 */
public class messageList extends HttpServlet {

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
        HttpSession session = request.getSession();
       
        if(session.getAttribute("userid")==null){
            response.sendRedirect("index.jsp");
        }
        
        try {
            JSONObject output=new JSONObject();
            JSONArray inB=new JSONArray();
            JSONArray outB=new JSONArray();
            
            MySQLConnect con=new MySQLConnect("localhost", "docflow", "3306", "reader", "1");
            
            PreparedStatement outSt=con.getConnection().prepareStatement("SELECT * FROM `msgbox` WHERE fromid = ? ");
            outSt.setInt(1, Integer.parseInt((String)session.getAttribute("userid")));
            ResultSet outRes=outSt.executeQuery();
            ResultSetMetaData rsmd = outRes.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while(outRes.next()){
                JSONObject msg=new JSONObject();                
                for(int i=1;i<=columnsCount;i++){
                    msg.accumulate(rsmd.getColumnName(i), outRes.getString(i));
                }                
                outB.put(msg);                
            }
            outRes.close();
            
            PreparedStatement inSt=con.getConnection().prepareStatement("SELECT * FROM `msgbox` WHERE toid = ? ");
            inSt.setInt(1, Integer.parseInt((String)session.getAttribute("userid")));
            ResultSet inRes=inSt.executeQuery();
            
            rsmd = inRes.getMetaData();
            columnsCount = rsmd.getColumnCount();
            while(inRes.next()){
                JSONObject msg=new JSONObject();                
                for(int i=1;i<=columnsCount;i++){
                    msg.accumulate(rsmd.getColumnName(i), inRes.getString(i));
                }                
                inB.put(msg);                
            }
            inRes.close();
            
            
            output.accumulate("in", inB);
            output.accumulate("out", outB);
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
        //processRequest(request, response);
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
