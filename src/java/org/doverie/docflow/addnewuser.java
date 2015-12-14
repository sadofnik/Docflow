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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author Игорь
 */
public class addnewuser extends HttpServlet {
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
            JSONObject result=new JSONObject();
            
            JSONObject req=new JSONObject(URLDecoder.decode(json, "UTF-8"));
            
            con=new MySQLConnect("localhost", "docflow", "3306", "writer", "1");  
            PreparedStatement eq=con.getConnection().prepareStatement("SELECT count(*) FROM loginList WHERE login = ?");
            eq.setString(1,req.getString("login"));
            ResultSet eqRes=eq.executeQuery();
            int count=0;
            while (eqRes.next()) {
                 count=eqRes.getInt(1);
            }            
            eqRes.close();
            
            if(count>0){
                result.accumulate("status", "Error");
                result.accumulate("msg", "Такой логин уже есть");
            }else{
            PreparedStatement prep1=null;
            prep1=con.getConnection().prepareStatement(
                    "INSERT INTO "
                    + "loginlist(`id`, `login`,`password`, `roles`)"
                    + " VALUES  (NULL, ?,?,?)",Statement.RETURN_GENERATED_KEYS);
            prep1.setString(1, req.getString("login"));
            prep1.setString(2, req.getString("password"));
            prep1.setString(3, req.getString("roles"));
            prep1.executeUpdate();
            
            String id="";
            ResultSet generatedKeys = prep1.getGeneratedKeys();
            if (generatedKeys.next()) {
               id=generatedKeys.getString(1);
            }
            prep1.close();
            generatedKeys.close();
            
            PreparedStatement prep2=con.getConnection().prepareStatement("INSERT INTO "
                    + "userdata(`id`,`name`,`email`,`tel1`,`tel2`,`empldate`,`drsdate`,`post`)"
                    + "VALUES (?,?,?,?,?,?,?,?)");
            prep2.setString(1, id);
            prep2.setString(2, req.getString("name"));
            prep2.setString(3, req.getString("email"));
            prep2.setString(4, req.getString("tel1"));
            prep2.setString(5, req.getString("tel2"));
            prep2.setString(6, req.getString("empldate"));
            prep2.setString(7, req.getString("drsdate"));
            prep2.setString(8, req.getString("post"));
            
            prep2.executeUpdate();
            result.accumulate("status", "success");
            }
            
            
            out.println(result.toString());
        }catch(Exception e){
            e.printStackTrace();
        } finally {
            try {
                con.getConnection().close();
            } catch (SQLException ex) {
                Logger.getLogger(addnewuser.class.getName()).log(Level.SEVERE, null, ex);
            }
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
