
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">   
        <%@include file="WEB-INF/jspf/libIncludeFrg.jspf" %>
    </head>
    <body>
        <%@include file="WEB-INF/jspf/topmenuFrg.jspf"%>
        <div class="container">
            <br>
            <div class="row">
                <div class="col-sm-3">
                    <h4>Тип:</h4>
                    <div class="checkbox"><label><input name="type" class="realtyparam" type="checkbox" value="Жилая"/> Жилая</label></div>
                    <div class="checkbox"><label><input name="type" class="realtyparam" type="checkbox" value="Не жилая"/> Не жилая</label></div>
                    <div class="checkbox"><label><input name="type" class="realtyparam" type="checkbox" value="Земельный участок"/> Земельный участок</label></div>                    
                </div>
                <div class="col-sm-3" >
                    <h4>Площадь:</h4>
                    <div class="checkbox"><label><input name="area" class="realtyparam" type="checkbox" value="0:100"/> 0-100м</label></div>
                    <div class="checkbox"><label><input name="area" class="realtyparam" type="checkbox" value="101:500"/> 101-500м</label></div>
                    <div class="checkbox"><label><input name="area" class="realtyparam" type="checkbox" value="501:1000"/> 501-1000м</label></div>                    
                </div>  
                <div class="col-sm-3" >
                    <h4>Цена:</h4>
                    <input style="width:100px;" class="input-sm radiantBorder realtyparam" name="costmin" placeholder="От">-
                    <input style="width:100px;" class="input-sm radiantBorder realtyparam" name="costmax" placeholder="До"> 
                    <br>
                    <div>
                    <label class="checkbox-inline"><input name="operation" class="realtyparam" type="checkbox" value="Продажа"/>Покупка</label>
                    <label class="checkbox-inline"><input name="operation" class="realtyparam" type="checkbox" value="Аренда"/>Аренда</label>
                    </div><div>
                    <label class="checkbox-inline"><input name="saleStatus" class="realtyparam" type="checkbox" value="Показать проданное"/>Показать проданное</label>
                    </div>
                </div>  
                <div class="col-sm-3">
                    <h4>Округ:</h4>
                    <div>
                    <label class="checkbox-inline"><input name="region" class="realtyparam" type="checkbox" value="Север"/>Север</label>
                    <label class="checkbox-inline"><input name="region" class="realtyparam" type="checkbox" value="Юг"/>Юг</label>
                    </div>
                    <div>
                    <label class="checkbox-inline"><input name="region" class="realtyparam" type="checkbox" value="Запад"/>Запад</label>
                    <label class="checkbox-inline"><input name="region" class="realtyparam" type="checkbox" value="Восток"/>Восток</label>
                    </div>
                    <div>
                    <label class="checkbox-inline"><input name="region" class="realtyparam" type="checkbox" value="Центр"/>Центр</label>
                    <label class="checkbox-inline"><input name="region" class="realtyparam" type="checkbox" value="Н.Москва"/>Н.Москва</label>
                    </div>
                 </div>                 
            </div>
            <div class="row">
                <div class="col-sm-11">
                    <div id="resultPager"></div>
                    <div id="resultTable"></div>
                </div>
                <div class="col-sm-1">
                    <p><a id="addNewRealtyItem" class="btn btn-default">Добавить</a></p>
                    <p><a id="sendSelected" class="btn btn-default disabled">Отправить</a></p>
                </div>                    
            </div>    
        </div>
        <%@include file="WEB-INF/jspf/bottomIncludeFrg.jspf"%>
        <script>
            function getData(){
                window.realtyParam={};
                $(".realtyparam").each(function(i,elem){
                    if(typeof(window.realtyParam[$(this).prop("name")])==="undefined"){
                        window.realtyParam[$(this).prop("name")]=[];
                    }
                    if($(this).prop("type")==="checkbox"){
                        if($(this).prop("checked")){
                            window.realtyParam[$(this).prop("name")].push($(this).val());
                        }
                    }else if($(this).prop("type")==="text"){
                        if($(this).val().trim()!==""){
                            window.realtyParam[$(this).prop("name")].push($(this).val()); 
                        }
                    }                        
                });
                
                requesrExecutor("searchrealty", realtyParam, function(data){
                    tablePrinter(data); 
                });
            }
            
            $(document).ready(function(){
                $("#addNewRealtyItem").click(function(){showAddRealtyModal();});
                $("#sendSelected").click(function(){showSendSelectedRealtyModal();});
            });
            
            $(document).ready(function(){
                $(".realtyparam").keyup(function(){
                    getData(); 
                });
                $(".realtyparam").change(function(){
                   getData(); 
                });
             });
             selected=[];
             function tablePrinter(data){
                var cont=$("#resultTable").empty();
                var table=$("<table/>").appendTo(cont).addClass("table");
                //table header
                
                var row=$("<tr/>").appendTo(table).css({"font-weight": "bold"});
        $("<td/>").appendTo(row).html("");
        $("<td/>").appendTo(row).html("#");
        $("<td/>").appendTo(row).html("Площадь");
        $("<td/>").appendTo(row).html("Цена");
        $("<td/>").appendTo(row).html("Тип");
        $("<td/>").appendTo(row).html("Регион");
        $("<td/>").appendTo(row).html("Адрес");
        $("<td/>").appendTo(row).html("Описание");
        $("<td/>").appendTo(row).html("sale");
        for(var i=0;i<data.length;i++){
            var item=data[i];
            row=$("<tr/>").appendTo(table);
            $("<td/>").appendTo(row).append($("<input/>").prop("type","checkbox")
                    .prop("id",item.id).change(function(){
                if($(this).prop("checked")){
                    
                    selected.push(this.id);
                }else{
                    
                    var tmp=[];
                    for(var i=0;i<selected.length;i++){
                        if(selected[i]!==this.id){
                            tmp.push(selected[i]);
                        }
                    }
                    selected=tmp;
                    
                } 
                if(selected.length===0){
                    $("#sendSelected").addClass("disabled")
                }else{
                    $("#sendSelected").removeClass("disabled")
                }
                }));
            $("<td/>").appendTo(row).html(item.id);
            $("<td/>").appendTo(row).html(item.area);
            $("<td/>").appendTo(row).html(item.cost);
            $("<td/>").appendTo(row).html(item.type+" / "+item.operation);
            $("<td/>").appendTo(row).html(item.region);
            $("<td/>").appendTo(row).html(item.adres);
            $("<td/>").appendTo(row).html(item.description);
            $("<td/>").appendTo(row).append(function(){
                var checkbox=$("<input type=\"checkbox\"/>").attr("id",item.id);
                if(typeof(item.salestatus)!=="undefined"){   
                    if(item.salestatus!=""){
                        checkbox.prop("checked", true);
                    }
                }
                checkbox.change(function(){
                    var data={};
                    data.id=this.id;
                    data.status=$(this).prop("checked");
                    
                    requesrExecutor("setRealtySaleStatus",data, function(){});
                });
                return checkbox;
            });
         
        }
            }
        </script>
    </body>
</html>
