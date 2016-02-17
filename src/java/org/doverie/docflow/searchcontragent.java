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
 * @author Александр
 */
public class searchcontragent extends HttpServlet {

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
        
        HttpSession session = request.getSession();
        if(session.getAttribute("userid")==null){
            response.sendRedirect("index.jsp");
        }
        String regstr="{}";
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        if(br != null){
            regstr = br.readLine(); 
        }
        try {
            JSONObject output=new JSONObject();
            JSONArray fizB=new JSONArray();
            JSONArray urB=new JSONArray();
            
            System.out.println("asdasdasdsd");
            
            JSONObject req=new JSONObject(URLDecoder.decode(regstr, "UTF-8"));
            MySQLConnect con=new MySQLConnect("localhost", "docflow", "3306", "reader", "1");
            PreparedStatement prepfiz=null;
            PreparedStatement prepur=null;
            if(req.getString("searchquery").equals("")){
                prepfiz=con.getConnection().prepareStatement("SELECT * FROM sprfiz ORDER BY name desc");
                prepur=con.getConnection().prepareStatement("SELECT * FROM sprur ORDER BY contactname desc"); 
            }else{
                prepfiz=con.getConnection().prepareStatement("SELECT * FROM sprfiz WHERE name LIKE ? ORDER BY name DESC");
                  prepur=con.getConnection().prepareStatement("SELECT * FROM sprur WHERE contactname LIKE ? ORDER BY contactname DESC");
                prepfiz.setString(1, "%"+req.getString("searchquery")+"%");
                prepur.setString(1, "%"+req.getString("searchquery")+"%");
            }
            ResultSet rs= prepfiz.executeQuery();            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while(rs.next()){
                JSONObject ob=new JSONObject();
                
                for(int i=1;i<=columnsCount;i++){
                    ob.accumulate(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));                    
                }
                fizB.put(ob);               
            }
            rs.close();
            rs= prepur.executeQuery();
            rsmd =rs.getMetaData();
            columnsCount = rsmd.getColumnCount();
            while(rs.next()){
                JSONObject ob=new JSONObject();
                
                for(int i=1;i<=columnsCount;i++){
                    ob.accumulate(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));                    
                }
                urB.put(ob);
            }
            rs.close();
            
           output.accumulate("fiz",fizB);
            output.accumulate("ur", urB);
            out.println(output.toString());
            out.flush();            
            con.getConnection().close();
          
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
