function requesrExecutor(fncName, data, callable){
    $.ajax({
        url:fncName,
        contentType: 'application/json; charset=utf-8',
        data:encodeURIComponent(JSON.stringify(data)),
        type:"post",
        success:callable
    });     
}
function initAddCorpSprButton(){
    $("#addCorpSprItem").click(function(){wnd.modal('show');});
    var wnd=$("<div id=\"addItemForm\"/>").addClass("modal fade");
        var modal=$("<div/>").addClass("modal-dialog").appendTo(wnd);
        var modalContent=$("<div/>").appendTo(modal).addClass("modal-content");
        var header=$("<div/>").appendTo(modalContent).addClass("modal-header");
        header.append($("<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>"));
        header.append($("<h4 class='modal-title'>Добавить нового пользователя</h4>"));
        
        var content=$("<div/>").addClass("modal-body").appendTo(modalContent);
        
        
        var footer=$("<div class='modal-footer'/>").appendTo(modalContent);        
        footer.append($("<button/>").addClass("btn btn-success").html("Сохранить").click(function(){sendItem();}));
        footer.append($("<button data-dismiss='modal' />").addClass("btn btn-danger").html("Отмена"));
        
        var frm=$("<form id='newitem'>").appendTo(content);
        var table=$("<table/>").appendTo(frm).css({width:"100%"});
        
        var fieldData=[
            {label:"Имя пользователя",name:"login"},
            {label:"Пароль",name:"password"},
            {label:"Роли",name:"roles"},
            {label:"ФИО",name:"name"},
            {label:"Должность",name:"post"},
            {label:"Номер телефона",name:"tel1"},
            {label:"Номер мобильного",name:"tel2"},
            {label:"E-mail",name:"email"},
            {label:"Дата приема",name:"empldate"},
            {label:"Дата рождения",name:"drsdate"}            
        ];
        content.css({"text-align":"center"});
        for(var i=0;i<fieldData.length;i++){
            var n=fieldData[i];
            var row=$("<tr/>").appendTo(table);
            $("<td/>").appendTo(row).html(n.label).css({"text-align":"right"});
            $("<td/>").appendTo(row).append($("<input name='"+n.name+"'/>"));
        }
        
        wnd.appendTo("body");
        wnd.modal('hide');
    
};
function sendItem(){
    var itemdata={};
    $("#newitem input").each(function(i, elem){
        itemdata[$(this).prop("name")]=$(this).val();
    });
    requesrExecutor("addnewuser",itemdata,function(data){
       if(data.status==="Error"){
           alert(data.msg);
       }else if(data.status==="success"){
           location.reload();
       }
       
    });
}
function showAddRealtyModal(){
    var modal=new FieldlistModalWindow("Добавить объект недвижимости");
    modal.addField({label:"Тип", type:"select", value:["Жилая", "Не жилая","Земельный участок"],name:"type", id:"realtyId"});
    modal.addField({label:"Площадь", type:"input",name:"area", id:"realtyArea"});
    modal.addField({label:"Цена", type:"input",name:"cost", id:"realtyCost"});
    modal.addField({label:"Операция", type:"select", value:["Аренда", "Продажа"],name:"operation", id:"realtyOperation"});
    modal.addField({label:"Регион", type:"select", value:["Север", "Юг", "Запад", "Восток", "Центр", "Н.Москва"],name:"region", id:"realtyRegion"});
    modal.addField({label:"Адрес", type:"input",name:"adres", id:"realtyAdres"});
    modal.addField({label:"Описание", type:"textarea",name:"description", id:"realtyDescription"});
    modal.addAcceptBtn("Сохранить",function(){
        var itemDataList={};
        modal.getFieldList().find(".modalAutoItem").each(function(item, i){
            //alert($(this).prop("tagName")=="SELECT");
            var value="";
            if($(this).prop("tagName")==="INPUT"){
                value=$(this).val();
            }
            if($(this).prop("tagName")==="TEXTAREA"){
                value=$(this).val();
            }
            if($(this).prop("tagName")==="SELECT"){                
                value=$(this).find("option:selected").text();
            }            
            itemDataList[$(this).prop("name")]=value;            
        });
        
        requesrExecutor("addnewrealty",itemDataList,function(data){
            if(data.status==="success"){
                location.reload();
            } 
        });        
    });
}
function showSendSelectedRealtyModal(){
    var modal=new FieldlistModalWindow("Отправить выделеные объекты...");
    var panel=$("<div/>").appendTo(modal.getContentPanel());
    var adresPanel=$("<div/>").appendTo(panel).addClass("ui-widget");
    $("<span/>").appendTo(adresPanel).html("Сотрудник:").css({"margin-right":"30px"});
    $("<input/>").prop("id","adresStr").appendTo(adresPanel).addClass("input-sm").css({"width":"400px"});
    
    
    var msgPanel=$("<div/>").appendTo(panel);
    var itemPanel=$("<div/>").appendTo(panel);
    $("<div/>").appendTo(msgPanel).html("Сообщение");
    $("<textarea/>").appendTo(msgPanel).addClass("form-control").prop("id","messageText").css({resize:"none", "height": "100px"});
    
    modal.addAcceptBtn("Отправить", function(){
        var msgdata={};
        msgdata.sendTo=sendToId;
        msgdata.msgText=$("#messageText").val();
        msgdata.itemList=selected;
        msgdata.type="system";
        requesrExecutor("/sendmessage", msgdata, function(data){
            if(data.status==="success"){
                location.reload();
            }
        });
    });
    $("#adresStr").autocomplete(
            {
        open: function(){
            $(this).autocomplete('widget').css('z-index', 1060);
            return false;
        },
        autoFocus:true,
        source: function(request, response){
            $.ajax({
                type:"POST",
                url: "searchcorplist", 
                data:encodeURIComponent(JSON.stringify({searchquery: request.term})),
                success: function(data){
                    var arr=[];
                    for(var i=0;i<data.length;i++){
                        arr.push({
                            label: data[i].name+" - "+data[i].post,
                            value: data[i].name,
                            id:data[i].id                            
                        });
                    }
                    response(arr);
                }             
            });
        },
        select:function (e, ui) {
            window.sendToId=ui.item.id;
        }
    }
);
}
function initAddFizItemBtn(){
    $("#addFizListItem").click(function(){
        var modal=new FieldlistModalWindow("Добавить данные физЛица");
        modal.addField({label:"Имя", type:"input", name:"name", id:"fizName"});
        modal.addField({label:"Домашний адрес", type:"input", name:"homeadres", id:"fizName"});
        modal.addField({label:"Рабочий адрес", type:"input", name:"rabadres", id:"fizName"});
        modal.addField({label:"tel", type:"input", name:"tel", id:"fizName"});
        modal.addField({label:"Emqil", type:"input", name:"Email", id:"fizName"});
        modal.addField({label:"workname", type:"input", name:"workname", id:"fizName"});
        modal.addField({label:"dolgnost", type:"input", name:"post", id:"fizName"});
        modal.addField({label:"comment", type:"input", name:"komment", id:"fizName"});
        modal.addAcceptBtn("Сохранить",function(){
            var itemDataList={};
            modal.getFieldList().find(".modalAutoItem").each(function(item, i){
                //alert($(this).prop("tagName")=="SELECT");
                var value="";
                if($(this).prop("tagName")==="INPUT"){
                    value=$(this).val();
                }
                if($(this).prop("tagName")==="TEXTAREA"){
                    value=$(this).val();
                }
                if($(this).prop("tagName")==="SELECT"){                
                    value=$(this).find("option:selected").text();
                }            
                itemDataList[$(this).prop("name")]=value;            
            });
        
            requesrExecutor("addFizItem",itemDataList,function(data){
                if(data.status==="success"){
                    location.reload();
                } 
            });        
        });
    });
}
function initAddUrItemBtn(){
    $("#addUrListItem").click(function(){
         var modal=new FieldlistModalWindow("Добавить данные ЮрЛица");
        modal.addField({label:"Название компании", type:"input", name:"companyname", id:"urName"});
        modal.addField({label:"Тип компании", type:"input", name:"companytype", id:"urName"});
        modal.addField({label:"Юр адрес", type:"input", name:"uradres", id:"urName"});
        modal.addField({label:"Имя контакт лица", type:"input", name:"contactname", id:"urName"});
        modal.addField({label:"Телефон", type:"input", name:"tel", id:"urName"});
        modal.addField({label:"Факс", type:"input", name:"fax", id:"urName"});
        modal.addField({label:"E-mail", type:"input", name:"email", id:"urName"});
        modal.addField({label:"Название банка", type:"input", name:"bankname", id:"urName"});
        modal.addField({label:"БИК", type:"input", name:"bik", id:"urName"});
        modal.addField({label:"КорСчет", type:"input", name:"kor", id:"urName"});
        modal.addField({label:"РСчет", type:"input", name:"raschet", id:"urName"});
        modal.addField({label:"КПП", type:"input", name:"kpp", id:"urName"});
        modal.addField({label:"ИНН", type:"input", name:"inn", id:"urName"});
        modal.addField({label:"Ком", type:"input", name:"description", id:"urName"});
        modal.addAcceptBtn("Сохранить",function(){
            var itemDataList={};
            modal.getFieldList().find(".modalAutoItem").each(function(item, i){
                //alert($(this).prop("tagName")=="SELECT");
                var value="";
                if($(this).prop("tagName")==="INPUT"){
                    value=$(this).val();
                }
                if($(this).prop("tagName")==="TEXTAREA"){
                    value=$(this).val();
                }
                if($(this).prop("tagName")==="SELECT"){                
                    value=$(this).find("option:selected").text();
                }            
                itemDataList[$(this).prop("name")]=value;            
            });
        
            requesrExecutor("addUrItem",itemDataList,function(data){
                if(data.status==="success"){
                    location.reload();
                };
            });
        });
    });    
}
function initNewMsgBtn(){
    $("#newMsgBtn").click(function(){
        
        var modal=new FieldlistModalWindow("Отправить выделеные объекты...");
        var panel=$("<div/>").appendTo(modal.getContentPanel());
        var adresPanel=$("<div/>").appendTo(panel).addClass("ui-widget");
        $("<span/>").appendTo(adresPanel).html("Сотрудник:").css({"margin-right":"30px"});
        $("<input/>").prop("id","adresStr").appendTo(adresPanel).addClass("input-sm").css({"width":"400px"});
    
    
        var msgPanel=$("<div/>").appendTo(panel);
        var itemPanel=$("<div/>").appendTo(panel);
        $("<div/>").appendTo(msgPanel).html("Сообщение");
        $("<textarea/>").appendTo(msgPanel).addClass("form-control").prop("id","messageText").css({resize:"none", "height": "100px"});
        
        modal.addAcceptBtn("Отправить", function(){
            var msgdata={};
            msgdata.sendTo=sendToId;
            msgdata.itemList=[];
            msgdata.msgText=$("#messageText").val();
            msgdata.type="msg";
            requesrExecutor("/sendmessage", msgdata, function(data){
                if(data.status==="success"){
                    location.reload();
                }
            });
        });
        $("#adresStr").autocomplete({
            open: function(){
                $(this).autocomplete('widget').css('z-index', 1060);
                return false;
            },
            autoFocus:true,
            source: function(request, response){
                $.ajax({
                    type:"POST",
                    url: "searchcorplist", 
                    data:encodeURIComponent(JSON.stringify({searchquery: request.term})),
                    success: function(data){
                        var arr=[];
                        for(var i=0;i<data.length;i++){
                            arr.push({
                                label: data[i].name+" - "+data[i].post,
                                value: data[i].name,
                                id:data[i].id 
                            });
                        }
                        response(arr);
                    }
                });
            },
            select:function (e, ui) {
                window.sendToId=ui.item.id;
            }
        });
    });
}
function showMessageWindow(msgId){
    var modal=new FieldlistModalWindow("Загрузка...");
    var body=$("<div/>").appendTo(modal.contentPanel);
    requesrExecutor("messageData",{id:msgId},function(msgdata){
        modal.setTitle("Сообщение: "+msgdata.subject);
    });
    
}