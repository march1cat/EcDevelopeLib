/***********
Version 1.3
新增readonly
************/
function EcFormRender(serverView,renderTagID){
    this.serverView = serverView;
    this.renderTagID = renderTagID;
}

EcFormRender.serverView = undefined;
EcFormRender.renderTagID = undefined;
EcFormRender.main_container = undefined;
EcFormRender.formInputs = undefined;


EcFormRender.custAfterSubmitFunction = undefined;



EcFormRender.prototype.startRender = function(){
   var pageContainer = document.getElementById(this.renderTagID);
   if(pageContainer) {
      this.main_container = document.createElement("div");
      pageContainer.appendChild(this.main_container);
   }
   this.loadSchema();
} 


EcFormRender.prototype.loadSchema = function(){
    var ajaxer = new Ajaxer();
    var self = this;
    ajaxer.postJsonRes(this.serverView,"action=queryFormDefinition",function(ResJsonObj){
        if(ResJsonObj.result && ResJsonObj.result == 'ok') {
            for(var i in ResJsonObj.data) {
                if(ResJsonObj.data[i].VIEW_ONLY && ResJsonObj.data[i].VIEW_ONLY ==  '1') continue;
                var colInputContainer = document.createElement('div');
                colInputContainer.setAttribute('class','row');
                colInputContainer.setAttribute('style','margin : 10px;');
                if(self.getMainContainer()) self.getMainContainer().appendChild(colInputContainer);
                if(true){
                    //label section
                    var e_lable = document.createElement('label');
                    e_lable.setAttribute('for','inputType');
                    e_lable.setAttribute('class','col-sm-3 control-label');
                    e_lable.setAttribute('style','padding-top:5px;text-align:right;');
                    e_lable.innerHTML = ResJsonObj.data[i].ALIAS + ":";
                    colInputContainer.appendChild(e_lable);
                }
                
                if(true){
                    var formInContainer = document.createElement('div');
                    formInContainer.setAttribute('class','col-sm-5');
                    var inTxt = undefined;
                    if(ResJsonObj.data[i].TYPE == 'SELECT') {
                         inTxt = document.createElement('select');
                         inTxt.setAttribute('type','select');
                         if(ResJsonObj.data[i].OPTION){
                            var optionAr = ResJsonObj.data[i].OPTION.split(',');
                            for(var j in optionAr){
                               var option = document.createElement('option');
                               option.innerHTML = optionAr[j];
                               inTxt.appendChild(option);
                            }
                          }
                    } else {
                         inTxt = document.createElement('input');
                         inTxt.setAttribute('type','text');
                    }
                    inTxt.setAttribute('class','form-control');
                    inTxt.setAttribute('CODE',ResJsonObj.data[i].ID);
                    if(ResJsonObj.data[i].READ_ONLY && ResJsonObj.data[i].READ_ONLY ==  '1') inTxt.setAttribute('readonly','');
                    
                    self.getformInputs().push(inTxt);
                    formInContainer.appendChild(inTxt);
                    colInputContainer.appendChild(formInContainer); 
                }
            }
            
            if(true ){
                //Operation Button Section
                 var operationContainer = document.createElement('div');
                 operationContainer.setAttribute('class','row');
                 operationContainer.setAttribute('style','margin : 10px;');
                 if(self.getMainContainer()) self.getMainContainer().appendChild(operationContainer);
                        
                  var sp = document.createElement('div');
                  sp.setAttribute('class','col-sm-3');
                  operationContainer.appendChild(sp);
                        
                  var _btnContainer = document.createElement('div');
                  _btnContainer.setAttribute('class','col-sm-5');
                  operationContainer.appendChild(_btnContainer);
                
                  if(true){
                     var e_btn = document.createElement('button');
                      e_btn.setAttribute('class','btn btn-default');
                      e_btn.innerHTML = "送出";
                      _btnContainer.appendChild(e_btn);
                      e_btn.onclick = function(){
                         self.submit();
                      }
                  }
            }
        }
    });
} 


EcFormRender.prototype.submit = function(){
        var para = "";
        var inputs = this.getformInputs();
        for(var i in inputs){
            var code = inputs[i].getAttribute("CODE");
            var value = inputs[i].value;
            para += code + "=" + encodeURIComponent(value) + "&";
        }
        para += "action=commitInsert";
        
        if(confirm("是否確定要提交資料？")) {
            var ajaxer = new Ajaxer();
            var self = this;
            this.showBackdrop();
            ajaxer.postJsonRes(this.serverView,para,function(ResJsonObj){
                if(ResJsonObj.result && ResJsonObj.result == 'ok') {
                    alert("提交成功!!");
                } else {
                    if(ResJsonObj.message) alert(ResJsonObj.message);
                    else alert("submit fail!!");
                }
                self.hideBackdrop();
                self.afterSubmitFunction();
            });
            
        }
}



EcFormRender.prototype.fillForm = function(data){
    if(data) {
        for(var i in this.formInputs){
            this.formInputs[i].value = '';
        }
        for (var code in data){
            for(var i in this.formInputs){
                if(code == this.formInputs[i].getAttribute('CODE')) {
                    this.formInputs[i].value  = data[code];
                    break;
                }
            }
        }
    }
}

EcFormRender.prototype.afterSubmitFunction = function(){
    if(this.custAfterSubmitFunction) this.custAfterSubmitFunction();
}

EcFormRender.prototype.getMainContainer = function(){
    return this.main_container;
}

EcFormRender.prototype.getformInputs = function(){
    if(!this.formInputs) this.formInputs = [];
    return this.formInputs;
}


EcFormRender.prototype.getformInputs = function(){
    if(!this.formInputs) this.formInputs = [];
    return this.formInputs;
}

EcFormRender.prototype.setAfterSubmit = function(fun){
   if(fun) this.custAfterSubmitFunction = fun;
}

//Table Loading Block Object
EcFormRender.loadingBlock = undefined;
EcFormRender.prototype.setloadingBlock = function(loadingBlock){this.loadingBlock = loadingBlock;}
EcFormRender.prototype.getloadingBlock = function(){return this.loadingBlock;}
EcFormRender.prototype.showBackdrop = function(){
   if(!this.getloadingBlock()) {
       var smain = document.createElement('div');
       smain.setAttribute('style','position:relative;z-index:1050;margin:30px auto;width:200px;');
       this.main_container.appendChild(smain);

       var s1 = document.createElement('div');
       s1.setAttribute('style','position:relative;background-color:#FFF;text-align:center;border-radius: 5px;');
       s1.innerHTML = "讀取中！！";
       smain.appendChild(s1);

       var backDrop = document.createElement('div');
       backDrop.setAttribute('style','position:absolute;top:0;right:0;bottom:0;left:0;z-index:1040;background-color:#000;opacity:0.5;');
       this.main_container.appendChild(backDrop);
       
       
       this.setloadingBlock({
           smain:smain,
           backDrop:backDrop
       });
   } else {
       if(this.getloadingBlock()) {
            this.getloadingBlock().smain.setAttribute('class','show');
            this.getloadingBlock().backDrop.setAttribute('class','show');
        }
   }
}

EcFormRender.prototype.hideBackdrop = function(){
    if(this.getloadingBlock()) {
        this.getloadingBlock().smain.setAttribute('class','hide');
        this.getloadingBlock().backDrop.setAttribute('class','hide');
    }
}   

