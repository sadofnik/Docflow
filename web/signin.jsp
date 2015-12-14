<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    if(session.getAttribute("userid")!=null){
        response.sendRedirect("index.jsp");
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%@include file="WEB-INF/jspf/libIncludeFrg.jspf" %>
        <style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
    </head>
    <body>
       
        <div class="container">            
            <form class="form-signin" method="POST" action="signin">
                <h2 class="form-signin-heading">Вход в систему</h2>
                <div>
                    <%
    if(session.getAttribute("erroeMsg")!=null){
        out.println(((String)session.getAttribute("errorMsg")));
    }
                    %>
                </div>
                <input name="username" type="text" class="input-block-level" placeholder="Имя пользователя">
                <input name="password" type="password" class="input-block-level" placeholder="Пароль">                
                <button class="btn btn-large btn-primary" type="submit">Войти</button>
            </form> 
        </div>
        <%@include file="WEB-INF/jspf/bottomIncludeFrg.jspf" %>
    </body>
</html>
