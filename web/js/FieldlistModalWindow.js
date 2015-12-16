function FieldlistModalWindow(title){    
    if(!$("#defaultModalWindowPanel").length){
        $("<div/>").prop("id","defaultModalWindowPanel").appendTo("body");       
    }
    var cont=$("#defaultModalWindowPanel").empty();
    var wnd=$("<div/>").appendTo(cont).addClass("modal-dialog");    
    var wndBody=$("<div/>").appendTo(wnd).addClass("modal-content");
    var wndHeader=$("<div/>").appendTo(wndBody).addClass("modal-header");
    this.titleLabel=$("<h4 class='modal-title'>");
    wndHeader.append($("<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button>"));
    wndHeader.append(this.titleLabel); 
    
    this.wndContent=$("<div/>").addClass("modal-body").appendTo(wndBody);
    this.wndFooter=$("<div class='modal-footer'/>").appendTo(wndBody);
    
    cont.addClass("modal fade");
    cont.modal("show");
    this.fieldList=$("<div/>").css({display:"table", width:"100%"}).appendTo(this.wndContent);
    this.contentPanel=$("<div/>").appendTo(this.wndContent);
    
    this.cancelBtn=$("<span><a class=\"btn btn-danger\" data-dismiss='modal'>Отмена</a></span>").appendTo(this.wndFooter);
    this.setTitle(title);
}
FieldlistModalWindow.prototype.getContentPanel=function(){
    return this.contentPanel;
};
FieldlistModalWindow.prototype.getFieldList=function(){
    return this.fieldList;
};
FieldlistModalWindow.prototype.addAcceptBtn=function(label,fnc){
    
    this.acceptBtn=$("<span></span>").prependTo(this.wndFooter);
    this.acceptInbtn=$("<a class=\"btn btn-success\">"+label+"</a>").appendTo(this.acceptBtn).click(function(){fnc();});
};
FieldlistModalWindow.prototype.setTitle=function(title){
    this.titleLabel.html(title);
};
FieldlistModalWindow.prototype.addField=function(data){
    var cont=this.fieldList;
    var item=$("<div/>").appendTo(cont).css({display:"table-row"});
    var label=$("<div/>").css({display:"table-cell","vertical-align":"middle"}).html(data.label).appendTo(item);
    var cell=$("<div/>").appendTo(item).css({display:"table-cell"});
    var field=null;
    if(data.type==="select"){
        field=$("<select/>").addClass("form-control");
        cell.addClass("selectContainer");
        //$('.selectpicker').selectpicker({style: 'btn-info', size: 4});
        if(typeof(data.value)==="function"){
            data.value(function(arr){
                for(var i=0;i<arr.length;i++){
                    $("<option/>").html(arr[i]).appendTo(field);
                }
            });
        }else if(typeof(data.value)==="object"){
            if($.isArray(data.value)){
                for(var i=0;i<data.value.length;i++){
                    $("<option/>").html(data.value[i]).appendTo(field);
                }
            }
        }else if(typeof(data.value)==="string"){
            $("<option>").html(data.value).appendTo(field);
        }
    }else if(data.type==="input"){
        field=$("<input/>").css({width:"100%"}).addClass("input-sm");
    }else if(data.type==="textarea"){
        field=$("<textarea/>").css({width:"100%", "height": "90px","resize":"none"}).addClass("form-control");
        
    }
    field.addClass("modalAutoItem");
    field.prop("name",data.name);
    field.prop("id",data.id);
    field.appendTo(cell);
};



