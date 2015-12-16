<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page session="true" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
        <%@include file="WEB-INF/jspf/libIncludeFrg.jspf" %>
        <title>JSP Page</title>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/topmenuFrg.jspf" %>
    <div class="container">
      <div class="row">
        <div class="col-sm-12">
          <h2>Личный кабинет</h2>          
        </div>        
        
      </div> 
      
      <h3>Сообщения</h3>
      <div class="row">
          <div class="col-sm-10">
              <div class="tabbable">
                  <ul class="nav nav-tabs">
                      <li class="active"><a data-toggle="tab" href="#inboxContentPane">Входящие</a></li>
                      <li><a data-toggle="tab" href="#outboxContentPane">Исходящие</a></li>
                  </ul>
                  <div class="tab-content">
                      <div class="tab-pane active" id="inboxContentPane">
                          
                      </div>
                      <div class="tab-pane" id="outboxContentPane">
                          
                      </div>
                  </div>
              </div>
          </div>
          <div class="col-sm-2">
              <p><a id="newMsgBtn" class="btn btn-default">Написать</a></p>
          </div>
      </div>
      <hr>  

    </div> <!-- /container -->    
    <%@include file="WEB-INF/jspf/bottomIncludeFrg.jspf" %>
    <script>
        $(document).ready(function(){
            requesrExecutor("messageList",{},function(data){
                processArray("inboxContentPane",data.in);      
                processArray("outboxContentPane",data.out);  
            });
            initNewMsgBtn();
        });
        function processArray(contName, arr){
            var table=$("<table/>").appendTo($("#"+contName)).addClass("table").css({width:"100%"});
            var row=$("<tr/>").appendTo(table); 
            $("<th/>").appendTo(row).html("От");
            $("<th/>").appendTo(row).html("Тип");
            $("<th/>").appendTo(row).html("Сообщение");
            for(var i=0;i<arr.length;i++){
                row=$("<tr/>").appendTo(table).prop("id",arr[i].id).click(function(){
                    showMessageWindow(this.id);
                });    
                var msg=arr[i];
                $("<td/>").appendTo(row).html(msg.fromid); 
                $("<td/>").appendTo(row).html(msg.type); 
                $("<td/>").appendTo(row).html(msg.message.substring(0,100));      
                
            }
        }
    </script>
  </body>
</html>
