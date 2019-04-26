$(document).ready(function(){
    appendOnProcessing();
    
    if(location.href.indexOf('?') >= 0) {
        bindParameterToPageValue();
    }
    
    if(start) start();
    if( document.getElementById('gonext')){
       loadPageSwitch();
    }
    
    
    if( document.getElementById('deleteitem')){
        if(getPageBindValue('itemNo')) {
            document.getElementById('deleteitem').removeAttribute('disabled');
            document.getElementById('deleteitem').onclick = function(){
                deleteItemOnItemEdit();
            }
        }
    }
    
    
});

function appendOnProcessing(){
    var body = document.getElementsByTagName("body")[0];
    var loadingContainer = document.createElement('div');
    loadingContainer.innerHTML = "text";
    loadingContainer.setAttribute('id','LoadingBx');
    loadingContainer.setAttribute('data-backdrop','static');
    loadingContainer.setAttribute('data-keyboard','false ');
    loadingContainer.setAttribute('class','modal fade bs-example-modal-lg');
    var text = "";
    text += "<div class='modal-dialog'>";
        text += "<div class='modal-content'>";
            text += "<div class='modal-header centeralign'>";
                text += "<h4 class='modal-title' id='opType'>資料處理中,請稍候</h4>";
            text += "</div>";
        text += "</div>";
    text += "</div>";
    loadingContainer.innerHTML = text;
    body.appendChild(loadingContainer);
}

function switchToLoadingStatus(){
    $("#LoadingBx").modal('show');
}

function switchToNoLoadingStatus(){
    $("#LoadingBx").modal('hide');
}


function getUrlParameter(key){
    if(location.href.indexOf(key + '=') >= 0){
		try {
            var data = location.href.substring(location.href.indexOf(key + '=') + (key + '=').length);
	    	return data;
		} catch(err) {}
    }
}


function bindParameterToPageValue(){
    var paraText = location.href.substring(location.href.indexOf('?') + 1);
    var paras = paraText.split("&");
    var body = document.getElementsByTagName("BODY")[0];
    for (var i in paras){
        var t = paras[i];
        if(t.indexOf("=") > 0) {
            var k = t.split("=");
            var hidden = document.createElement('input');
            hidden.setAttribute('type','hidden');
            hidden.setAttribute('id','pagev_' + k[0]);
            hidden.value = k[1];
            body.appendChild(hidden);
        }
    }
}



function getPageBindValue(key){
    var id = 'pagev_' + key;
    var input = document.getElementById(id);
    if(input) {
        return input.value;
    }
}




function urlBookCode(){
    if(location.href.indexOf('bcode=') >= 0){
			try {
				var bcode = location.href.substring(location.href.indexOf('bcode=') + 'bcode='.length);
	    		if(bcode){
	    			document.getElementById("bookCode").value = bcode;
	    			return bcode;
	    		}
			} catch(err) {}
    }
    return undefined;
}




function loadPageSwitch(){
             if(!getPageBindValue('itemNo')) return;
             var ajaxer = new Ajaxer();
             var para = "pno=" + getPageBindValue('pno') + "&itemNo=" + getPageBindValue('itemNo');
             ajaxer.postJsonRes("PageOperationHandler.loadBackNextInfo",para,function(ResJsonObj){
                 if(ResJsonObj.result && ResJsonObj.result == 'ok') {
                     if(ResJsonObj.data) {
                         for(var i in ResJsonObj.data){
                             var itemType = ResJsonObj.data[i].ITEM_TYPE;
                             var page = itemType == 'IMAGE' ? "imgitemeditor.html" : "textitemeditor.html";
                             var alink = undefined;
                             var switchPara = "pno=" + getPageBindValue('pno') + "&itemNo=" + ResJsonObj.data[i].ITEM_ID;
                             if(ResJsonObj.data[i].TYPE == 'NEXT') {
                                  alink = document.getElementById('gonext');
                             } else if(ResJsonObj.data[i].TYPE == 'LAST') {
                                 alink = document.getElementById('golast');
                             }
                             if(alink){
                                alink.setAttribute('href',page + "?" + switchPara);
                                alink.removeAttribute('disabled');
                             }
                         }
                     }
                 }
             });
        }

function deleteItemOnItemEdit(){
    var itemNo = getPageBindValue('itemNo');
    var pno = getPageBindValue('pno') ;
    if(pno && itemNo) {
        if(confirm("是否確定刪除元素?")){
           var para = "PID=" + pno + "&ITEM_ID=" + itemNo + "&action=commitDelete";
            var ajaxer = new Ajaxer();
            ajaxer.postJsonRes("StoreProject.xml",para,function(ResJsonObj){
                    if(ResJsonObj.result && ResJsonObj.result == 'ok') {
                                alert("刪除成功!!");
                                var nextLink = document.getElementById('gonext');
                                if(nextLink && !nextLink.hasAttribute('disabled')){
                                    nextLink.click();
                                } else {
                                    location.href = 'projectviewer.html?pno='+pno;
                                }
                        } else {
                                alert("刪除失敗!!");
                        }
                 });
        }
        
    }
}