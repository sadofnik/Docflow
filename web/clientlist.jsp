<%-- 
    Document   : clientlist
    Created on : 07.11.2015, 11:16:57
    Author     : Александр
--%>

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
                    <div class="col-sm-11"><input class="input-sm" style="width: 100%" name="SearchTxt" id="fizSearchTxt"/></div>
                    <div class="col-sm-1"><p><a id="fizSearchBtn" class="btn btn-default">Поиск</a></p></div> 
                </div>
            </div>
            <br>
            <div class="row">
                <div class="tabbable">  
                    <ul class="nav nav-tabs"> 
                        <li class="active"><a href="#fizTabContent" data-toggle="tab">Физические лица</a></li>
                        <li><a href="#urTabContent" data-toggle="tab">Юридические лица</a></li>                        
                    </ul>
                    <div class="tab-content"> 
                        <div class="tab-pane active" id="fizTabContent"> 
                            <br>
                            <div class="row">
                                <div class="col-sm-11"></div>
                            </div>
                            <div class="row" >
                                <div class="col-sm-11" id="fizTable"></div>
                                <div class="col-sm-1" id="fizTableCmdPanel">
                                    <p><a id="addFizListItem" class="btn btn-default">Добавить</a></p>
                                </div>
                            </div>
                            
                        </div>
                        <div class="tab-pane" id="urTabContent">
                            <br>
                            <div class="row">
                                <div class="col-sm-12">
                                    
                                </div>
                            </div>
                            <div class="row" id="">
                                <div class="col-sm-11" id="urTable"></div>
                                <div class="col-sm-1" id="urTableCmdPanel">
                                    <p><a id="addUrListItem" class="btn btn-default">Добавить</a></p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%@include file="WEB-INF/jspf/bottomIncludeFrg.jspf" %>
        
        <script>
            $(document).ready(function(){
                initAddFizItemBtn();
                initAddUrItemBtn()
            });
            function clientSearchFnc(type, stxt){
            }
            $(document).ready(function(){
                $("#urSearchTxt").on("keyup",function(){
                    if($(this).val().trim().length>3){
                        clientSearchFnc("ur",$(this).val().trim());
                    }
                });
                $("#fizSearchTxt").on("keyup",function(){
                    if($(this).val().trim().length>3){
                        clientSearchFnc("fiz",$(this).val().trim());
                    }
                });
                $("#urSearchBtn").click(function(){
                    clientSearchFnc("ur",$(this).val().trim());
                });
                $("#fizSearchBtn").click(function(){
                    clientSearchFnc("fiz",$(this).val().trim());
                });
            });
            $(document).ready(function(){
                requesrExecutor("searchcontragent",{searchquery:""},function(data){
                    contrtableprocessfiz("fizTable",data.fiz);
                    contrtableprocessur("urTable",data.ur);
                });
            });
            function contrtableprocessfiz(contname,arr){
                var cont=$("#"+contname).empty();
                var table=$("<table/>").appendTo(cont).addClass("table");
                var row=$("<tr/>").appendTo(table);
                $("<th>").appendTo(row).html("Имя");
                $("<th>").appendTo(row).html("Дата регистрации");
                $("<th>").appendTo(row).html("Адрес");
                $("<th>").appendTo(row).html("Раб.адрес");
                $("<th>").appendTo(row).html("Телефон");
                $("<th>").appendTo(row).html("E-mail");
                $("<th>").appendTo(row).html("Компания");
                $("<th>").appendTo(row).html("Должность");
                $("<th>").appendTo(row).html("Комментарии");
                for(var i=0;i<arr.length;i++){
                    var item=arr[i];
                    var row=$("<tr/>").appendTo(table);
                    $("<td/>").appendTo(row).html(item.name);
                    $("<td>").appendTo(row).html(item.regdate);
                    $("<td>").appendTo(row).html(item.homeadres);
                    $("<td>").appendTo(row).html(item.rabadres);
                    $("<td>").appendTo(row).html(item.tel);
                    $("<td>").appendTo(row).html(item.Email);
                    $("<td>").appendTo(row).html(item.workname);
                    $("<td>").appendTo(row).html(item.post);
                    $("<td>").appendTo(row).html(item.komment);
            }
        }
            function contrtableprocessur(contname,arr){
                var cont=$("#"+contname).empty();
                var table=$("<table/>").appendTo(cont).addClass("table");
                var row=$("<tr/>").appendTo(table);
                $("<th>").appendTo(row).html("Название компании");
                $("<th>").appendTo(row).html("Тип компании");
                $("<th>").appendTo(row).html("Юр.адрес");
                $("<th>").appendTo(row).html("ФИО представителя");
                $("<th>").appendTo(row).html("Телефон");
                $("<th>").appendTo(row).html("Факс");
                $("<th>").appendTo(row).html("E-mail");
                $("<th>").appendTo(row).html("Название банка");
                $("<th>").appendTo(row).html("БИК");
                $("<th>").appendTo(row).html("Корсчёт");
                $("<th>").appendTo(row).html("Рсчётный счёт");
                $("<th>").appendTo(row).html("КПП");
                $("<th>").appendTo(row).html("ИНН");
                $("<th>").appendTo(row).html("Описание");
                for(var i=0;i<arr.length;i++){
                    var item=arr[i];
                    var row=$("<tr/>").appendTo(table);
                    $("<td>").appendTo(row).html(item.companyname);
                    $("<td>").appendTo(row).html(item.companytype);
                    $("<td>").appendTo(row).html(item.uradres);
                    $("<td>").appendTo(row).html(item.contactname);
                    $("<td>").appendTo(row).html(item.tel);
                    $("<td>").appendTo(row).html(item.fax);
                    $("<td>").appendTo(row).html(item.email);
                    $("<td>").appendTo(row).html(item.bankname);
                    $("<td>").appendTo(row).html(item.bik);
                    $("<td>").appendTo(row).html(item.kor);
                    $("<td>").appendTo(row).html(item.raschet);
                    $("<td>").appendTo(row).html(item.kpp);
                    $("<td>").appendTo(row).html(item.inn);
                    $("<td>").appendTo(row).html(item.description);
                    }
            }
        </script>
    </body>
</html>
