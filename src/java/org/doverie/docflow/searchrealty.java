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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Игорь
 */
public class searchrealty extends HttpServlet {

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
        //response.setContentType("text/html;charset=UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String json = "";
        if(br != null){
            json = br.readLine();
        }
        
        try {
            JSONObject req=new JSONObject(URLDecoder.decode(json, "UTF-8"));
            MySQLConnect con=new MySQLConnect("localhost", "docflow", "3306", "reader", "1");
            String query="SELECT * FROM realtylist WHERE ";
            String queryParam="";
            //areaparametr
            JSONArray areas=req.getJSONArray("area");
            String areaSql="";
            for(int i=0;i<areas.length();i++){
                if(!areaSql.equals("")){ areaSql+=" OR ";}else{areaSql+="(";}
                String[] areaArr=areas.getString(i).split(":");
                areaSql+="(area>"+areaArr[0]+" AND area<"+areaArr[1]+")";
            }
            if(!areaSql.equals("")){areaSql+=")";}
            queryParam+=areaSql;
            
            //typeparametr
            JSONArray types=req.getJSONArray("type");
            String typeSql="";
            for(int i=0;i<types.length();i++){ 
                if(!typeSql.equals("")){typeSql+=" OR ";}else{typeSql+="(";}
                typeSql+="(type='"+types.getString(i)+"')";                
            }
            if(!typeSql.equals("")){typeSql+=")";}
            if(!queryParam.equals("")& !typeSql.equals("")){queryParam+=" AND ";}
            queryParam+=typeSql;
            //costparametr
            JSONArray costMax=req.getJSONArray("costmax");
            JSONArray costMin=req.getJSONArray("costmin");
            String costSql="";
            
            
            if(costMin.length()!=0){
                costSql+="(";
                if(costMin.length()!=0){costSql+="cost>"+costMin.getInt(0);}
            }
            if(!costSql.equals("")){costSql+=")";}
            if(!queryParam.equals("") & !costSql.equals("")){queryParam+=" AND ";}
            queryParam+=costSql;
            
            costSql="";
            if(costMax.length()!=0){
                costSql+="(";
                if(costMax.length()!=0){costSql+="cost<"+costMax.getInt(0);}
            }
            if(!costSql.equals("")){costSql+=")";}
            if(!queryParam.equals("") & !costSql.equals("")){queryParam+=" AND ";}
            queryParam+=costSql;
            
            //operationParam
            String operSql="";
            JSONArray operats=req.getJSONArray("operation");
            for(int i=0;i<operats.length();i++){
                if(operSql.equals("")){operSql+="(";}else{operSql+=" OR ";}
                operSql+="operation='"+operats.getString(i)+"'";
            }
            if(!operSql.equals("")){operSql+=")";}
            if(!queryParam.equals("")& !operSql.equals("")){queryParam+=" AND ";}
            queryParam+=operSql;
            
            //regionParam
            String regionSql="";
            JSONArray regions=req.getJSONArray("region");
            for(int i=0;i<regions.length();i++){
                if(regionSql.equals("")){regionSql+="(";}else{regionSql+=" OR ";}
                regionSql+="(region='"+regions.getString(i)+"')";
            }
            if(!regionSql.equals("")){regionSql+=")";}
            if(!queryParam.equals("") & !regionSql.equals("")){queryParam+=" AND ";}
            queryParam+=regionSql;
            
            
            
            //compileQuery
            query+=queryParam;
            System.out.println(query);
            
            Statement st=con.getConnection().createStatement();
            
            ResultSet res=st.executeQuery(query);            
            JSONArray output=new JSONArray();
            ResultSetMetaData rsmd = res.getMetaData();
            int columnsCount = rsmd.getColumnCount();
            while(res.next()){
                JSONObject ob=new JSONObject();                
                for(int i=1;i<=columnsCount;i++){
                    ob.accumulate(rsmd.getColumnName(i), res.getString(rsmd.getColumnName(i)));
                } 
                output.put(ob);
            }
            
            st.close();
            con.getConnection().close();            
          
            out.println(output.toString());
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
