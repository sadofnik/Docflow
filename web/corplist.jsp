<%-- 
    Document   : corplist
    Created on : 07.11.2015, 11:25:43
    Author     : Александр
--%>

<%@page import="java.util.Vector"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="WEB-INF/jspf/libIncludeFrg.jspf" %>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/topmenuFrg.jspf" %>
        <div class="container">
            <br>
            <div class="row">
                <div class="form-horizontal">
                    <div class="col-sm-11"><input placeholder="Поиск" class="input-sm" style="width: 100%" name="searchTxt" id="searchTxt"/></div>
                    <div class="col-sm-1"><p><a id="searchBtn" class="btn btn-default">Поиск</a></p></div> 
                </div>
            </div>
            <br>
            <div class="row">
                <div class="col-sm-11">
                    <div id="resultList"></div>
                </div>
                <div class="col-sm-1">
                    <%
                        if(session.getAttribute("roles")!=null){
                            String[] roleArr=((String)session.getAttribute("roles")).split(";");
                            for(String s:roleArr){
                                if(s.equals("admin")){
                                    out.println("<p><a id=\"addCorpSprItem\" class=\"btn btn-default\">Создать</a></p>");
                                    //out.println("<script>$(document).ready(function(){initAddCorpSprButton();});</script>");
                                }
                            }
                        }
                    %>                    
                </div>
            </div>
        </div>
        <%@include file="WEB-INF/jspf/bottomIncludeFrg.jspf" %>
        <script>
            function tablePrinter(data){
                $("#resultList").empty();
                var table=$("<table/>").appendTo("#resultList").addClass("table");
                //table header
                
                var row=$("<tr/>").appendTo(table).css({"font-weight": "bold"});
                $("<td/>").appendTo(row).html("ФИО");
                $("<td/>").appendTo(row).html("Должность");
                $("<td/>").appendTo(row).html("E-Mail");
                for(var i=0;i<data.length;i++){
                    var item=data[i];
                    row=$("<tr/>").appendTo(table);
                    $("<td/>").appendTo(row).html(item.name);
                    $("<td/>").appendTo(row).html(item.post);
                    $("<td/>").appendTo(row).html(item.email);
                }
            }
            function clientSearchFnc(str){
                requesrExecutor("searchcorplist",{searchquery:str},tablePrinter);
            }
            $(document).ready(function(){
                
                $("#searchTxt").on("keyup",function(){
                    if($(this).val().trim().length>3){
                        clientSearchFnc($(this).val().trim());
                    }
                });
                $("#searchBtn").click(function(){
                    clientSearchFnc($(this).val().trim());
                });
            });
            $(document).ready(function(){
                requesrExecutor("searchcorplist",{searchquery:""},tablePrinter);
            });
            </script>  
             <%
                        if(session.getAttribute("roles")!=null){
                            String[] roleArr=((String)session.getAttribute("roles")).split(";");
                            for(String s:roleArr){
                                if(s.equals("admin")){
                                    
                                    out.println("<script>$(document).ready(function(){initAddCorpSprButton();});</script>");
                                }
                            }
                        }
                    %>
                  
            
    </body>
</html>
