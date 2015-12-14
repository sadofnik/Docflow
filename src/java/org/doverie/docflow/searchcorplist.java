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
 * @author Игорь
 */
public class searchcorplist extends HttpServlet {
    private HttpSession session;
    String json = "{}";

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
        session=request.getSession();
        if(session.getAttribute("userid")==null){
            response.sendRedirect("index.jsp");
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        if(br != null){
            json = br.readLine(); 
        }
        
        //System.out.println();
        try {
            JSONObject req=new JSONObject(URLDecoder.decode(json, "UTF-8"));
            System.out.println(req.toString());
            //JSONObject req=new JSONObject(json);
            MySQLConnect con=new MySQLConnect("localhost", "docflow", "3306", "reader", "1");
            PreparedStatement prep=null;
            if(req.getString("searchquery").equals("")){
                prep=con.getConnection().prepareStatement("SELECT * FROM userdata ORDER BY name desc");
            }else{
                prep=con.getConnection().prepareStatement("SELECT * FROM userdata WHERE name LIKE ? ORDER BY name DESC");
                prep.setString(1, "%"+req.getString("searchquery")+"%");
            }
            ResultSet rs= prep.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //String name = rsmd.getColumnName(1);
            int columnsCount = rsmd.getColumnCount();
            JSONArray output=new JSONArray();
            while(rs.next()){
                JSONObject ob=new JSONObject();
                
                for(int i=1;i<=columnsCount;i++){
                    ob.accumulate(rsmd.getColumnName(i), rs.getString(rsmd.getColumnName(i)));
                }
                output.put(ob);
            }
            out.print(output.toString());
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
        //this.json="{searchquery:\""+URLDecoder.decode(request.getParameter("searchquery"), "UTF-8")+"\"}";
        //this.json=request.getParameter("searchquery");
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
