/***********
Version 1.6
1.6 ->  Support delay query to fix schema not ready issue
************/
function EcTableRender(serverView,renderTagID){
    this.serverView = serverView;
    this.renderTagID = renderTagID;
}
EcTableRender.serverView = undefined;
EcTableRender.renderTagID = undefined;
EcTableRender.main_container = undefined;
EcTableRender.prototype.getMainContainer = function(){return this.main_container;}
EcTableRender.tableTitleCodes = [];
EcTableRender.prototype.getTableTitleCodes = function(){return this.tableTitleCodes;}


EcTableRender.initDone = false;
EcTableRender.prototype.isInitDone = function(){return this.initDone;}
EcTableRender.prototype.markInitDone = function(){this.initDone = true;}


EcTableRender.queryConditonInput = [];
EcTableRender.prototype.getQueryInputByColCode = function(ColCode){
    if(this.queryConditonInput) {
        for(var i in this.queryConditonInput){
            if(this.queryConditonInput[i].getAttribute('CODE') && this.queryConditonInput[i].getAttribute('CODE') == ColCode)
                return this.queryConditonInput[i];
        }
    }
}
EcTableRender.dataRenderSection = undefined;
EcTableRender.prototype.setDataRenderSection = function(dataRenderSection){this.dataRenderSection = dataRenderSection;}
EcTableRender.prototype.getDataRenderSection = function(){return this.dataRenderSection;}

EcTableRender.showColumnAmout = undefined;
EcTableRender.prototype.setShowColumnAmout = function(showColumnAmout){this.showColumnAmout = showColumnAmout;}
EcTableRender.prototype.getShowColumnAmout = function(){return this.showColumnAmout;}



//Query Column Default Value
EcTableRender.qDefaultValue = undefined;
EcTableRender.prototype.getQDefaultValue = function(){return this.qDefaultValue;}
EcTableRender.prototype.registerDefaultQueryValue = function(qDefaultValueOption){
    if(!qDefaultValueOption) {
       this.qDefaultValue = {};
   } else this.qDefaultValue = qDefaultValueOption;
}

//Row Button Extra Functions ,Ex : [{title:'統計數據',fun:openKibana}])
EcTableRender.rowFunctions = [];
EcTableRender.prototype.getRowFunctions = function(){return this.rowFunctions;}

//Table Cach Data
EcTableRender.cachData = undefined;
EcTableRender.prototype.setCachData = function(cachData){
    this.cachData = cachData;
    if(this.cachData) {
        var pageAmt = Math.ceil(this.cachData.length / this.pageOption.amtPerPage);
        this.nowPageIndex = 1;
        if(this.pageInfoViewer) this.pageInfoViewer.innerHTML =  " " + this.nowPageIndex + " / " + pageAmt + " ";
    }
}
EcTableRender.prototype.getCachData = function(){
    return this.cachData;
}

//Table Extra Option
EcTableRender.exOption = undefined;
EcTableRender.prototype.setExOption = function(exOption){this.exOption = exOption;}
EcTableRender.prototype.getExOption = function(){return this.exOption;}
//hideQuerySection:   1 --> Hide Query Section , 0 --> Show (Default)

//Table Page Option
EcTableRender.pageOption = undefined;
EcTableRender.prototype.setPageOption = function(pageOption){this.pageOption = pageOption;} //amtPerPage -> show amt per page,-1 = show all in one page 
EcTableRender.prototype.getPageOption = function(){return this.pageOption;}
EcTableRender.pageInfoViewer = undefined;
EcTableRender.prototype.setPageInfoViewer = function(pageInfoViewer){this.pageInfoViewer = pageInfoViewer;}
EcTableRender.nowPageIndex = undefined;


//Table Loading Block Object
EcTableRender.loadingBlock = undefined;
EcTableRender.prototype.setloadingBlock = function(loadingBlock){this.loadingBlock = loadingBlock;}
EcTableRender.prototype.getloadingBlock = function(){return this.loadingBlock;}


//Data Editor Parameters
EcTableRender.isAllowEditor = undefined;
EcTableRender.prototype.setAllowEditor = function(allowOpen){this.isAllowEditor = allowOpen;}
EcTableRender.prototype.getAllowEditor = function(){return this.isAllowEditor;}
EcTableRender.editorContainer = undefined;
EcTableRender.prototype.seteEditorContainer = function(editorContainer){this.editorContainer = editorContainer;}
EcTableRender.prototype.geteEditorContainer = function(){return this.editorContainer;}
EcTableRender.editorInput = undefined;
EcTableRender.prototype.seteEditorInput = function(editorInput){this.editorInput = editorInput;}
EcTableRender.prototype.geteEditorInput = function(){return this.editorInput;}

EcTableRender.isAllowDelete = undefined;
EcTableRender.prototype.setAllowDelete = function(isAllowDelete){this.isAllowDelete = isAllowDelete;}
EcTableRender.prototype.getAllowDelete = function(){return this.isAllowDelete;}


//EventFunction
EcTableRender.event = undefined;
EcTableRender.prototype.triggerEvent = function(){
    if(!this.event) this.registerEvent();
    return this.event;
}
EcTableRender.prototype.registerEvent = function(eventOption){
    if(!eventOption) {
       this.event = {};
       this.event.onPressQueryButton = undefined;
       this.event.onRenderInitialStart = undefined;
       this.event.onRenderInitialFinish = undefined;
       this.event.beforeQueryLoading = undefined;
       this.event.afterQueryLoading = undefined;
       this.event.onSchemaLoadOver = undefined;
   } else this.event = eventOption;
}



EcTableRender.prototype.pushColumnTitle = function(title){
    if(!this.tableTitleCodes) this.tableTitleCodes = [];
    this.tableTitleCodes.push(title);
} 

EcTableRender.prototype.pushConditionInput = function(input){
    if(!this.queryConditonInput) this.queryConditonInput = [];
    this.queryConditonInput.push(input);
} 



EcTableRender.prototype.registerRowFunction = function(funs){
    this.rowFunctions = funs;
}


EcTableRender.prototype.startRender = function(qDefaultValueOption){
   if(!this.event) this.event = {};
   if(!this.pageOption) this.pageOption = {amtPerPage:10};
   this.registerDefaultQueryValue(qDefaultValueOption);
    
   if(this.event.onRenderInitialStart) this.event.onRenderInitialStart();
   this.initialContainer();
   this.loadSchema();
   if(this.event.onRenderInitialFinish) this.event.onRenderInitialFinish();
   this.showBackdrop();
   this.hideBackdrop();
}

EcTableRender.prototype.initialContainer = function(){
   var pageContainer = document.getElementById(this.renderTagID);
   if(pageContainer) {
      this.main_container = document.createElement("div");
      pageContainer.appendChild(this.main_container);
   }
}

EcTableRender.prototype.loadSchema = function(){
    var ajaxer = new Ajaxer();
    var self = this;
    ajaxer.postJsonRes(this.serverView,"action=querySchema",function(ResJsonObj){
        if(ResJsonObj.result && ResJsonObj.result == 'ok') {
            if(ResJsonObj.data && ResJsonObj.data != "empty"){
                if(ResJsonObj.allowupdate && ResJsonObj.allowupdate == 'true') self.setAllowEditor(true);
                if(ResJsonObj.allowdelete && ResJsonObj.allowdelete == 'true') self.setAllowDelete(true);
                if(true){
                    //Search Section
                    for(var i in ResJsonObj.data){
                        if(!ResJsonObj.data[i].TYPE) continue;
                        if(ResJsonObj.data[i].FORM_ONLY && ResJsonObj.data[i].FORM_ONLY ==  '1') continue;
                        
                        var colQueryContainer = document.createElement('div');
                        colQueryContainer.setAttribute('class','row');
                       
                        if(self.getExOption() && self.getExOption().hideQuerySection)  
                            colQueryContainer.setAttribute('style','margin : 10px; display:none;');
                        else  
                            colQueryContainer.setAttribute('style','margin : 10px;');
                        if(self.getMainContainer()) self.getMainContainer().appendChild(colQueryContainer);
                        
                        if(true){
                            //label section
                            var e_lable = document.createElement('label');
                            e_lable.setAttribute('for','inputType');
                            e_lable.setAttribute('class','col-sm-3 control-label');
                            e_lable.setAttribute('style','padding-top:5px;text-align:right;');
                            e_lable.innerHTML = ResJsonObj.data[i].ALIAS + ":";
                            colQueryContainer.appendChild(e_lable);
                        }
                        var hasQuery = false;
                        if(true){
                            //input section
                            if(ResJsonObj.data[i].TYPE == 'TEXT'){
                                hasQuery = true;
                                var queryInContainer = document.createElement('div');
                                queryInContainer.setAttribute('class','col-sm-2');
                                var inTxt = document.createElement('input');
                                inTxt.setAttribute('type','text');
                                inTxt.setAttribute('class','form-control');
                                inTxt.setAttribute('CODE',ResJsonObj.data[i].ID);
                                if(self.getQDefaultValue()[ResJsonObj.data[i].ID]) {
                                    inTxt.value = self.getQDefaultValue()[ResJsonObj.data[i].ID];
                                }
                                    
                                self.pushConditionInput(inTxt);
                                queryInContainer.appendChild(inTxt);
                                colQueryContainer.appendChild(queryInContainer); 
                            } else if(ResJsonObj.data[i].TYPE == 'RANGE'){
                                hasQuery = true;
                                var rangeDefaultS = undefined;
                                var rangeDefaultE = undefined;
                                if(true){
                                    //Range Default Value Parsing
                                    if(self.getQDefaultValue()[ResJsonObj.data[i].ID]){
                                        var v = self.getQDefaultValue()[ResJsonObj.data[i].ID];
                                        if(v.indexOf(",") >= 0) {
                                            var a = v.split(",");
                                            rangeDefaultS = a[0].trim();
                                            rangeDefaultE = a[1].trim();
                                        }
                                    } 
                                }
                                
                                if(true){
                                    //Range Query Input For Start Section
                                    var queryInContainer = document.createElement('div');
                                    queryInContainer.setAttribute('class','col-sm-2');
                                    var inTxt = document.createElement('input');
                                    inTxt.setAttribute('type','text');
                                    inTxt.setAttribute('class','form-control');
                                    inTxt.setAttribute('CODE',ResJsonObj.data[i].ID + "@RANGE_START");
                                    if(rangeDefaultS) inTxt.setAttribute('value',rangeDefaultS);
                                    self.pushConditionInput(inTxt);
                                    queryInContainer.appendChild(inTxt);
                                    colQueryContainer.appendChild(queryInContainer);
                                }
                                if(true){
                                    var e_lable = document.createElement('label');
                                    e_lable.setAttribute('for','inputType');
                                    e_lable.setAttribute('style','text-align:center;');
                                    e_lable.setAttribute('class','col-sm-1 control-label');
                                    e_lable.setAttribute('style','padding-top:5px;');
                                    e_lable.innerHTML = "~";
                                    colQueryContainer.appendChild(e_lable);
                                }
                                if(true){
                                    //Range Query Input For End Section
                                    var queryInContainer = document.createElement('div');
                                    queryInContainer.setAttribute('class','col-sm-2');
                                    var inTxt = document.createElement('input');
                                    inTxt.setAttribute('type','text');
                                    inTxt.setAttribute('class','form-control');
                                    inTxt.setAttribute('CODE',ResJsonObj.data[i].ID + "@RANGE_END");
                                    if(rangeDefaultE) inTxt.setAttribute('value',rangeDefaultE);
                                    self.pushConditionInput(inTxt);
                                    queryInContainer.appendChild(inTxt);
                                    colQueryContainer.appendChild(queryInContainer);
                                }
                                
                            } else if(ResJsonObj.data[i].TYPE == 'SELECT'){
                                hasQuery = true;
                                var queryInContainer = document.createElement('div');
                                queryInContainer.setAttribute('class','col-sm-2');
                                var inSelect = document.createElement('select');
                                //inSelect.setAttribute('type','text');
                                inSelect.setAttribute('type','select');
                                inSelect.setAttribute('class','form-control');
                                inSelect.setAttribute('CODE',ResJsonObj.data[i].ID);
                                if(self.getQDefaultValue()[ResJsonObj.data[i].ID]) inSelect.setAttribute('value',self.getQDefaultValue()[ResJsonObj.data[i].ID]);
                                self.pushConditionInput(inSelect);
                                queryInContainer.appendChild(inSelect);
                                colQueryContainer.appendChild(queryInContainer);
                                
                                
                                if(true){
                                  var option = document.createElement('option');
                                  option.innerHTML = "ALL";
                                  inSelect.appendChild(option);
                                }
                                
                                if(ResJsonObj.data[i].OPTION){
                                   var optionAr = ResJsonObj.data[i].OPTION.split(',');
                                   for(var j in optionAr){
                                       var option = document.createElement('option');
                                       option.innerHTML = optionAr[j];
                                       inSelect.appendChild(option);
                                   }
                                }
                                
                            }
                        }
                    }
                    
                    if(true && hasQuery){
                        //Operation Button Section
                        var operationContainer = document.createElement('div');
                        operationContainer.setAttribute('class','row');
                        if(self.getExOption() && self.getExOption().hideQuerySection)  
                            operationContainer.setAttribute('style','margin : 10px; display:none;');
                        else  
                            operationContainer.setAttribute('style','margin : 10px;');
                        
                        if(self.getMainContainer()) self.getMainContainer().appendChild(operationContainer);
                        
                        
                        var sp = document.createElement('div');
                        sp.setAttribute('class','col-sm-3');
                        operationContainer.appendChild(sp);
                        
                        var _btnContainer = document.createElement('div');
                        _btnContainer.setAttribute('class','col-sm-5');
                        operationContainer.appendChild(_btnContainer);
                        var e_btn = document.createElement('button');
                        e_btn.setAttribute('class','btn btn-default');
                        e_btn.innerHTML = "查詢";
                        _btnContainer.appendChild(e_btn);
                        e_btn.onclick = function(){
                            self.query();
                        }
                        
                    }
                }
                
                if(true && self.getAllowEditor()){
                    //Data Editor Section
                    var editor_container = document.createElement('div');
                    editor_container.setAttribute('class','row');
                    editor_container.setAttribute('style','display:none;');
                    if(self.getMainContainer()) self.getMainContainer().appendChild(editor_container);
                    self.seteEditorContainer(editor_container);
                    var updaters = {};
                    for(var i in ResJsonObj.data){
                        var updateColContainer = document.createElement('div');
                        updateColContainer.setAttribute('class','row');
                        updateColContainer.setAttribute('style','margin : 10px;');
                        editor_container.appendChild(updateColContainer);
                        if(true){
                            //label section
                            var e_lable = document.createElement('label');
                            e_lable.setAttribute('for','inputType');
                            e_lable.setAttribute('class','col-sm-3 control-label');
                            e_lable.setAttribute('style','padding-top:5px;text-align:right;');
                            e_lable.innerHTML = ResJsonObj.data[i].ALIAS + ":";
                            updateColContainer.appendChild(e_lable);
                        }
                        
                        if(true){
                           var updateInContainer = document.createElement('div');
                           updateInContainer.setAttribute('class','col-sm-2');
                           var inTxt = undefined;
                            //alert(ResJsonObj.data[i].ID);
                           if(ResJsonObj.data[i].TYPE == 'SELECT'){
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
                            if(ResJsonObj.data[i].UPDATE == '1'){
                                //can update
                            } else {
                                //read only
                                inTxt.setAttribute('readonly','');
                            }
                            inTxt.setAttribute('CODE',ResJsonObj.data[i].ID);
                            updateInContainer.appendChild(inTxt);
                            updateColContainer.appendChild(updateInContainer);
                            updaters[ResJsonObj.data[i].ID] = inTxt;
                           
                           
                           
                        }
                    }
                    
                    
                    if(true){
                        var updateOperationContainer = document.createElement('div');
                        updateOperationContainer.setAttribute('class','row');
                        updateOperationContainer.setAttribute('style','margin : 10px;');
                        editor_container.appendChild(updateOperationContainer);
                        
                        var sp = document.createElement('div');
                        sp.setAttribute('class','col-sm-3');
                        updateOperationContainer.appendChild(sp);
                        
                        var _btnContainer = document.createElement('div');
                        _btnContainer.setAttribute('class','col-sm-5');
                        updateOperationContainer.appendChild(_btnContainer);
                        if(true){
                           var e_btn = document.createElement('button');
                            e_btn.setAttribute('class','btn btn-default');
                            e_btn.setAttribute('style','margin-right:5px;');
                            e_btn.innerHTML = "更新";
                            _btnContainer.appendChild(e_btn);
                            e_btn.onclick = function(){
                                self.commitUpdateData();
                            }
                        }
                        
                        if(true){
                           var e_btn = document.createElement('button');
                           e_btn.setAttribute('class','btn btn-default');
                           e_btn.innerHTML = "取消";
                           _btnContainer.appendChild(e_btn);
                           e_btn.onclick = function(){
                               self.cancelUpdateData();
                           }
                        }
                       
                    }
                    
                    self.seteEditorInput(updaters);
                }
                
                if(true){
                    //Table Title Section
                    var e_table = document.createElement("table");
                    e_table.setAttribute('class','table table-striped');
                    if(self.getMainContainer()) self.getMainContainer().appendChild(e_table);
                    var e_thead = document.createElement("thead");
                    e_table.appendChild(e_thead);
                    var e_tr = document.createElement("tr");
                    e_thead.appendChild(e_tr);
                    for(var i in ResJsonObj.data){
                        if(ResJsonObj.data[i].FORM_ONLY && ResJsonObj.data[i].FORM_ONLY ==  '1') continue;
                        if(!ResJsonObj.data[i].NOSHOW){ 
                            var e_th = document.createElement("th");
                            e_th.innerHTML = ResJsonObj.data[i].ALIAS;
                            self.pushColumnTitle(ResJsonObj.data[i].ID);
                            e_tr.appendChild(e_th);
                        }
                    }
                    if(self.getRowFunctions() && self.getRowFunctions().length > 0){
                        var e_th = document.createElement("th");
                        e_th.innerHTML = "&nbsp;";
                        e_tr.appendChild(e_th);
                    }
                    self.setShowColumnAmout(ResJsonObj.data.length);
                    var e_tbody = document.createElement("tbody");
                    e_table.appendChild(e_tbody);
                    self.setDataRenderSection(e_tbody);
                    self.showDataResultMessage("尚未取得資料");
                }
                
                if(true){
                   //Page Operation Section
                    var pageOperationContainer = document.createElement('div');
                    pageOperationContainer.setAttribute('class','row');
                    if(self.getPageOption().amtPerPage == -1){
                       pageOperationContainer.setAttribute('style','display:none;');
                    }
                    if(self.getMainContainer()) self.getMainContainer().appendChild(pageOperationContainer);
                    if(true){
                        var padding = document.createElement('div');
                        padding.setAttribute('class','col-sm-2');
                        pageOperationContainer.appendChild(padding);
                    }
                    var mainOpcontainer = document.createElement('div');
                    pageOperationContainer.appendChild(mainOpcontainer);
                    mainOpcontainer.setAttribute('class','col-sm-8');
                    mainOpcontainer.setAttribute('style','text-align:center;');
                    
                    if(true){
                       //main page operation section
                        var buttonLastPage = document.createElement('button');
                        mainOpcontainer.appendChild(buttonLastPage);
                        buttonLastPage.setAttribute('class','btn btn-default');
                        buttonLastPage.innerHTML = "<<";
                        buttonLastPage.onclick = function(){
                           self.onclickLastPage();   
                        }
                        
                        var pageInfoViewer = document.createElement('span');
                        mainOpcontainer.appendChild(pageInfoViewer);
                        pageInfoViewer.innerHTML = " 0 / 0 ";
                        self.setPageInfoViewer(pageInfoViewer);
                        
                        var buttonNextPage = document.createElement('button');
                        mainOpcontainer.appendChild(buttonNextPage);
                        buttonNextPage.setAttribute('class','btn btn-default');
                        buttonNextPage.innerHTML = ">>";
                        buttonNextPage.onclick = function(){
                            self.onclickNextPage();   
                        }
                    }
                    
                    
                    if(true){
                        var padding = document.createElement('div');
                        padding.setAttribute('class','col-sm-2');
                        pageOperationContainer.appendChild(padding);
                    }
                }
               
            }
        } else alert('Render EcTable Fail,Target Tag ID = ' + self.renderTagID);
        if(self.triggerEvent().onSchemaLoadOver) self.triggerEvent().onSchemaLoadOver();
        self.markInitDone();
    });
}

EcTableRender.prototype.showDataResultMessage = function(message){
    var e_tmp = document.createElement("th");
    e_tmp.setAttribute('style','text-align:center');
    e_tmp.setAttribute('colspan',this.showColumnAmout);
    e_tmp.innerHTML = message;
    this.dataRenderSection.appendChild(e_tmp);
}

EcTableRender.prototype.query = function(queryOption){
    if(!this.isInitDone()){
        var self = this;
        setTimeout(function(){
            self.query(queryOption);        
        },500);
        return;
    }
    if(this.event.onPressQueryButton) this.event.onPressQueryButton();
    var queryText = "";
    if(queryOption){
       for (var i in queryOption){
           queryText += i + "=" + queryOption[i] + "&";
       }
    } else {
        for(var i in this.queryConditonInput){
            if(this.queryConditonInput[i].value != '') {
                var inputType = this.queryConditonInput[i].getAttribute('type');
                if(inputType && inputType == 'select') {
                    if(this.queryConditonInput[i].value != 'ALL')
                        queryText += this.queryConditonInput[i].getAttribute('CODE') + "=" + this.queryConditonInput[i].value + "&";
                } else  
                    queryText += this.queryConditonInput[i].getAttribute('CODE') + "=" + this.queryConditonInput[i].value + "&";
            }
        }
    }
    
   
    if(queryText.length > 0)  queryText = queryText.substring(0,queryText.length - 1);
    
    var ajaxer = new Ajaxer();
    var self = this;
    this.showBackdrop();
    if(this.event.beforeQueryLoading) this.event.beforeQueryLoading();
    ajaxer.postJsonRes(this.serverView,(queryText && queryText.length > 0) ? queryText : null,function(ResJsonObj){
        self.getDataRenderSection().innerHTML = "";
        self.setCachData(undefined);
        if(ResJsonObj.result && ResJsonObj.result == 'ok') {
            if(ResJsonObj.data && ResJsonObj.data != "empty"){
                 self.setCachData(ResJsonObj.data);
                 self.renderQueryContent();
            } else {
                self.showDataResultMessage("沒有符合的資料!!");
            }
        }
        self.hideBackdrop();
        if(self.triggerEvent().afterQueryLoading) self.triggerEvent().afterQueryLoading();
    });
 }

EcTableRender.prototype.renderQueryContent = function(){
   if(this.cachData){ 
       this.getDataRenderSection().innerHTML = "";
       var showCount = 0;
       var startIndex = 0;
       if(this.pageOption.amtPerPage && this.pageOption.amtPerPage != -1) {
           startIndex = (this.nowPageIndex - 1) * this.pageOption.amtPerPage;
       }
       var datas = this.cachData;
       var self = this;
       var columnAmount = undefined;
       for(var i = startIndex;i < datas.length; i++){
            var e_tr = document.createElement("tr");
            this.getDataRenderSection().appendChild(e_tr);
            var temp = 0;
            for(var renderCode in this.tableTitleCodes){
                 for(var code in datas[i]){
                    if(this.getTableTitleCodes()[renderCode].toUpperCase() == code.toUpperCase()){
                        var e_td = document.createElement("td");
                        e_td.innerHTML = datas[i][code];
                        e_tr.appendChild(e_td);
                        temp++;
                        break;
                    }
                }
            }
            if(true){
               var e_td = undefined;
               
               if(this.getAllowEditor()) {
                   if(!e_td) {
                       e_td = document.createElement("td");
                       e_tr.appendChild(e_td);  
                       temp++;
                   }
                   var btn = document.createElement("button");
                   btn.setAttribute('class','btn btn-default');
                   btn.setAttribute('rowindex',i);
                   btn.setAttribute('style','margin:3px;');
                   btn.innerHTML = "編輯";
                   btn.onclick = function(){
                      self.onclickEditDataButton(this.getAttribute('rowindex'));
                   }
                   e_td.appendChild(btn);
               } 
                
               if(this.getAllowDelete()) {
                   if(!e_td) {
                       e_td = document.createElement("td");
                       e_tr.appendChild(e_td);  
                       temp++;
                   }
                   var btn = document.createElement("button");
                   btn.setAttribute('class','btn btn-default');
                   btn.setAttribute('rowindex',i);
                   btn.setAttribute('style','margin:3px;');
                   btn.innerHTML = "刪除";
                   btn.onclick = function(){
                      self.onclickDeleteDataButton(this.getAttribute('rowindex'));
                   }
                   e_td.appendChild(btn);
               } 
                
               
               
               if(this.getRowFunctions() && this.getRowFunctions().length > 0){
                    if(!e_td) {
                       e_td = document.createElement("td");
                       e_tr.appendChild(e_td);  
                       temp++;
                    }
                    //Row Button Section
                    for(var fi in this.rowFunctions){
                        var btn = document.createElement("button");
                        btn.setAttribute('class','btn btn-default');
                        btn.setAttribute('rowindex',i);
                        btn.setAttribute('funindex',fi);
                        btn.setAttribute('style','margin:3px;');
                        btn.innerHTML = this.getRowFunctions()[fi].title;
                        btn.onclick = function(){
                            self.onclickRowFunctionButton(this.getAttribute('rowindex'),this.getAttribute('funindex'));
                        }
                        e_td.appendChild(btn);
                    }
                }
            }
            
           if(!columnAmount) columnAmount = temp;
           showCount++;
           if(this.pageOption.amtPerPage != -1 && showCount >= this.pageOption.amtPerPage) {
               break;
           }
       }
       
       if(showCount < this.pageOption.amtPerPage) {
            for(var j = 0 ;j <= this.pageOption.amtPerPage - showCount;j++){
                var e_tr = document.createElement("tr");
                var e_td = document.createElement("td");
                e_td.innerHTML = "&nbsp;";
                e_td.setAttribute('colspan',columnAmount);
                e_tr.appendChild(e_td);
                this.getDataRenderSection().appendChild(e_tr);
            }
        }
       
       
   }
}

EcTableRender.prototype.onclickLastPage = function(){
    this.nowPageIndex--;
    var pageAmt = Math.ceil(this.cachData.length / this.pageOption.amtPerPage);
    if(this.nowPageIndex <= 1) this.nowPageIndex = 1;
    if(this.pageInfoViewer) this.pageInfoViewer.innerHTML =  " " + this.nowPageIndex + " / " + pageAmt + " ";
    this.renderQueryContent();
}

EcTableRender.prototype.onclickNextPage = function(){
   this.nowPageIndex++;
   var pageAmt = Math.ceil(this.cachData.length / this.pageOption.amtPerPage);
   if(this.nowPageIndex >= pageAmt)  this.nowPageIndex =  pageAmt;
   if(this.pageInfoViewer) this.pageInfoViewer.innerHTML =  " " + this.nowPageIndex + " / " + pageAmt + " ";
    this.renderQueryContent();
}
EcTableRender.prototype.onclickRowFunctionButton = function(clickRowIndex,triggerFunIndex){
   if(this.rowFunctions && this.rowFunctions[triggerFunIndex]){
       if(this.cachData[clickRowIndex])  this.rowFunctions[triggerFunIndex].fun((this.cachData) ? this.cachData[clickRowIndex] : undefined);
   }
}
EcTableRender.prototype.onclickEditDataButton = function(clickRowIndex){
   if(this.cachData[clickRowIndex])  {
       this.showEditor(this.cachData[clickRowIndex]);
   }
}



EcTableRender.prototype.onclickDeleteDataButton = function(clickRowIndex){
   if(this.cachData[clickRowIndex])  {
        this.commitDeleteData(this.cachData[clickRowIndex]);
   }
}


EcTableRender.prototype.showEditor = function(rowData){
    if(this.getAllowEditor()) {
       var inputs = this.geteEditorInput();
       if(inputs) {
            for(var i in inputs){
              inputs[i].value = '';
           }
           for(var i in inputs){
              var code = inputs[i].getAttribute("CODE");
              
              if(rowData[code]) {
                 inputs[i].value = rowData[code];
              }
           }
           this.geteEditorContainer().setAttribute('style','');
       }
    }
}

EcTableRender.prototype.cancelUpdateData = function(){
     if(this.getAllowEditor()) {
         this.geteEditorContainer().setAttribute('style','display:none;');
     }
}

EcTableRender.prototype.commitUpdateData = function(){
    if(this.getAllowEditor()) {
        var para = "";
        var inputs = this.geteEditorInput();
        for(var i in inputs){
            var code = inputs[i].getAttribute("CODE");
            var value = inputs[i].value;
            para += code + "=" + encodeURIComponent(value) + "&";
        }
        para += "action=commitUpdate";
        
        if(confirm("是否確定要更新資料？")) {
            var ajaxer = new Ajaxer();
            var self = this;
            ajaxer.postJsonRes(this.serverView,para,function(ResJsonObj){
                if(ResJsonObj.result && ResJsonObj.result == 'ok') {
                    alert("更新成功!!");
                    self.cancelUpdateData();
                    self.query();
                } else {
                    if(ResJsonObj.message) alert(ResJsonObj.message);
                    else alert("update fail!!");
                }
            });
            
        }
        
    }
}


EcTableRender.prototype.commitDeleteData = function(rowData){
    if(this.getAllowDelete()) {
        var para = "";
        for(var i in rowData){
            var code = i;
            var value = rowData[i];
            para += code + "=" + encodeURIComponent(value) + "&";
        }
        para += "action=commitDelete";
        
        if(confirm("是否確定要刪除資料？")) {
            var ajaxer = new Ajaxer();
            var self = this;
            ajaxer.postJsonRes(this.serverView,para,function(ResJsonObj){
                if(ResJsonObj.result && ResJsonObj.result == 'ok') {
                    alert("刪除成功!!");
                    self.query();
                } else {
                    if(ResJsonObj.message) alert(ResJsonObj.message);
                    else alert("delete fail!!");
                }
            });
            
        }
        
    }
}






EcTableRender.prototype.showBackdrop = function(){
   if(!this.getloadingBlock()) {
       var smain = document.createElement('div');
       smain.setAttribute('style','position:relative;z-index:1050;margin:30px auto;width:200px;');
       this.main_container.appendChild(smain);

       var s1 = document.createElement('div');
       s1.setAttribute('style','position:relative;background-color:#FFF;text-align:center;border-radius: 5px;');
       s1.innerHTML =  "讀取中！！ ";
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

EcTableRender.prototype.hideBackdrop = function(){
    if(this.getloadingBlock()) {
        this.getloadingBlock().smain.setAttribute('class','hide');
        this.getloadingBlock().backDrop.setAttribute('class','hide');
    }
}   

