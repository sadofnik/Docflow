/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.doverie.docflow;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Александр
 */
public class signin extends HttpServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        HttpSession session=request.getSession();
        PrintWriter out = response.getWriter();
        try {
            MySQLConnect con=new MySQLConnect("localhost", "docflow", "3306", "reader", "1");
            PreparedStatement prep=con.getConnection().prepareStatement("SELECT * FROM loginlist WHERE login=? AND password=?");
            prep.setString(1,request.getParameter("username"));
            prep.setString(2,request.getParameter("password"));
            
            ResultSet res=prep.executeQuery();
            session.setAttribute("errorMsg", null);
            if(res.next()){
                session.setAttribute("userid", res.getString("id"));
                session.setAttribute("roles", res.getString("roles"));
            }else{
                session.setAttribute("errorMsg", "authError");
            }
            PreparedStatement userdataSt=con.getConnection().prepareStatement("SELECT * FROM userdata WHERE id=?");
            userdataSt.setString(1, ((String)session.getAttribute("userid")));
            res.close();
            
            res=userdataSt.executeQuery();
            if(res.next()){
                session.setAttribute("userName", res.getString("name"));
            }else{
                session.setAttribute("userName", "Не отпределено");
            }   
            res.close();
            con.getConnection().close();
        }catch(Exception e){
            e.printStackTrace();
            System.err.println("ERROR");
        } finally {  
            response.sendRedirect("index.jsp");
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
