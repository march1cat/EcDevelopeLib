function EcBatchProcessor(renderTagID,postUrl){
    this.renderTagID = renderTagID;
    this.postUrl = postUrl;
}

EcBatchProcessor.main_container = undefined;
EcBatchProcessor.prototype.getMainContainer = function(){return this.main_container;}
EcBatchProcessor.renderTagID = undefined;
EcBatchProcessor.postUrl = undefined;
EcBatchProcessor.prototype.getPostUrl = function(){return this.postUrl}
EcBatchProcessor.dataContainer = undefined;
EcBatchProcessor.prototype.getDataContainer = function(){return this.dataContainer}
EcBatchProcessor.prototype.setDataContainer = function(dataContainer){this.dataContainer = dataContainer}


EcBatchProcessor.keyRefers = undefined;
EcBatchProcessor.prototype.startRedner = function(){
    this.initialContainer();
    this.keyRefers = ['ID','TEXT','STATUS'];
    if(this.getMainContainer()) {
        var tableContainer = document.createElement('table');
        tableContainer.setAttribute('class','table table-striped');
        this.getMainContainer().appendChild(tableContainer);
    
        
        if(true){
           var tableHeader = document.createElement('thead');
           tableContainer.appendChild(tableHeader);
           var e_tr = document.createElement("tr");
           tableHeader.appendChild(e_tr);
           for(var i in this.keyRefers){
               var td = document.createElement('th');
               e_tr.appendChild(td);
               td.innerHTML = this.keyRefers[i];
           }
           var functionColTd = document.createElement('th');
           e_tr.appendChild(functionColTd);
        }
        
        if(true){
           var dataContainer = document.createElement('tbody');
           this.setDataContainer(dataContainer);
           tableContainer.appendChild(dataContainer);
           var td = document.createElement('td');
           td.setAttribute('colspan',this.keyRefers.length + 1); 
           td.setAttribute('style','text-align:center;');
           dataContainer.appendChild(td);
           td.innerHTML = "No Data";
        }
    }
}


EcBatchProcessor.batchDatas = undefined;
EcBatchProcessor.prototype.getBatchDatas = function(){return this.batchDatas}
EcBatchProcessor.prototype.setBatchDatas = function(batchDatas){this.batchDatas = batchDatas}


EcBatchProcessor.prototype.addRowData = function(rowData){
    var container = this.getDataContainer();
    if(!this.getBatchDatas()) {
        container.innerHTML = "";
        this.setBatchDatas(new Array());
    }
    if(rowData['TEXT']) {
        rowData['ID'] = this.getBatchDatas().length + 1;
        this.getBatchDatas().push(rowData);
        var e_tr = document.createElement("tr");
        this.getDataContainer().appendChild(e_tr);
        rowData['RowHtmlComponent'] = {};
        rowData['RowHtmlComponent']['RowTr'] = e_tr;
        for(var i = 0 ; i < this.keyRefers.length ; i++){
            var td = document.createElement('td');
            e_tr.appendChild(td);
            if(i == 0)  rowData['RowHtmlComponent']['IDText'] = td;
            if(rowData[this.keyRefers[i]]) 
                td.innerHTML = rowData[this.keyRefers[i]];
            else 
                td.innerHTML = "-";
            if(i == this.keyRefers.length - 1) {
                rowData['RowHtmlComponent']['StatusTd'] = td;
            }
        }
        if(true){
           var functionColTd = document.createElement('th');
           e_tr.appendChild(functionColTd);
           var button = document.createElement('button');
           button.setAttribute('class','btn btn-default'); 
           button.setAttribute('removeID',rowData['ID']);
           button.innerHTML = "移除";
           functionColTd.appendChild(button);   
           rowData['RowHtmlComponent']['RowRmButton'] = button;
           var self = this;
           button.onclick = function(){
               var cachDatas = self.getBatchDatas();
                if(cachDatas){
                    var removeIndex = this.getAttribute('removeID');
                    if(cachDatas.length > removeIndex - 1) {
                        self.removeRowData(cachDatas[removeIndex - 1]);
                    }
                }
           }
        }
    } else {
        alert('validate fail for batch processor!!');
    }
}


EcBatchProcessor.prototype.removeRowData = function(rowData){
    this.getDataContainer().removeChild(rowData['RowHtmlComponent']['RowTr']);
    this.getBatchDatas().splice(rowData['ID'] - 1,1);
    this.resortRowID();
}

EcBatchProcessor.prototype.resortRowID = function(){
    var cachDatas = this.getBatchDatas();
    if(cachDatas && cachDatas.length > 0){
       for(var i in cachDatas){
           cachDatas[i]['ID'] = parseInt(i) + 1;
           var rmBtn = cachDatas[i]['RowHtmlComponent']['RowRmButton'];
            cachDatas[i]['RowHtmlComponent']['IDText'].innerHTML =  cachDatas[i]['ID'];
           rmBtn.setAttribute('removeID',cachDatas[i]['ID']);
       }
    }
}


EcBatchProcessor.batchProcessCursor = 0;
EcBatchProcessor.prototype.getBatchProcessCursor = function(){return this.batchProcessCursor}
EcBatchProcessor.prototype.setBatchProcessCursor = function(batchProcessCursor){this.batchProcessCursor = batchProcessCursor}
EcBatchProcessor.prototype.processBatch = function(){
    if(!this.getBatchProcessCursor()) this.setBatchProcessCursor(0);
    if(this.getBatchDatas()) {
        this.showBackdrop();
        this.postNext();
    }
}

EcBatchProcessor.prototype.postNext = function(){
    var rowData = this.getBatchDatas()[this.getBatchProcessCursor()];
    if(rowData){
        var para = "";
        for(var i in rowData){
           if(i == 'RowHtmlComponent') continue;
           var code = i;
           var value = encodeURIComponent(rowData[i]);
           para += code + "=" + value + "&";
        }
        para += "action=BatchProcess";

        var ajaxer = new Ajaxer();
        
        var self = this;
        ajaxer.postJsonRes(this.getPostUrl(),para,function(ResJsonObj){
           self.markRowTaskDone();
        },function(status){
            alert('Connection break,please retry later!!');
            this.hideBackdrop();
        });
    } else {
       this.hideBackdrop();
    }
}

EcBatchProcessor.prototype.markRowTaskDone = function(){
     var rowData = this.getBatchDatas()[this.getBatchProcessCursor()];
     var statusTD = rowData['RowHtmlComponent']['StatusTd'];
     statusTD.innerHTML = "OK";
     this.setBatchProcessCursor(this.getBatchProcessCursor() + 1);
     this.postNext();
}



EcBatchProcessor.prototype.initialContainer = function(){
   var pageContainer = document.getElementById(this.renderTagID);
   if(pageContainer) {
      this.main_container = document.createElement("div");
      pageContainer.appendChild(this.main_container);
   }
}

//Table Loading Block Object
EcBatchProcessor.loadingBlock = undefined;
EcBatchProcessor.prototype.setloadingBlock = function(loadingBlock){this.loadingBlock = loadingBlock;}
EcBatchProcessor.prototype.getloadingBlock = function(){return this.loadingBlock;}
EcBatchProcessor.prototype.showBackdrop = function(){
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

EcBatchProcessor.prototype.hideBackdrop = function(){
    if(this.getloadingBlock()) {
        this.getloadingBlock().smain.setAttribute('class','hide');
        this.getloadingBlock().backDrop.setAttribute('class','hide');
    }
}   

