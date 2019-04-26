function Ajaxer() {
	
	
}


Ajaxer.prototype.get = function(url,callBackMethodOnOK,callBackMethodOnFail) {
	var xhttp = this.generateXMLRequest(callBackMethodOnOK,callBackMethodOnFail);
	xhttp.open("GET", url, true);
	xhttp.send();
};

Ajaxer.prototype.getJSONRes = function(url,callBackMethodOnOK,callBackMethodOnFail) {
	var xhttp = this.generateJsonResXMLRequest(callBackMethodOnOK,callBackMethodOnFail);
	xhttp.open("GET", url, true);
	xhttp.send();
};


Ajaxer.prototype.post = function(url,parameterText,callBackMethodOnOK,callBackMethodOnFail) {
	var xhttp = this.generateXMLRequest(callBackMethodOnOK,callBackMethodOnFail);
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	if(parameterText) xhttp.send(parameterText);
	else xhttp.send();
};

Ajaxer.prototype.postJsonRes = function(url,parameterText,callBackMethodOnOK,callBackMethodOnFail) {
	var xhttp = this.generateJsonResXMLRequest(callBackMethodOnOK,callBackMethodOnFail);
	xhttp.open("POST", url, true);
	xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	if(parameterText) xhttp.send(parameterText);
	else xhttp.send();
};


Ajaxer.prototype.generateXMLRequest = function(callBackMethodOnOK,callBackMethodOnFail) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
   		  if (this.readyState == 4) {
	   		 if(this.status == 200) {
	   		   	callBackMethodOnOK(this.responseText);	
	   		 } else {
	   		   	if(callBackMethodOnFail) {
	   		   		callBackMethodOnFail(this.status);
	   		   	}      	
	   		 }
	   	} 
   	};
   	return xhttp;
};

Ajaxer.prototype.generateJsonResXMLRequest = function(callBackMethodOnOK,callBackMethodOnFail) {
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
   		  if (this.readyState == 4) {
	   		 if(this.status == 200) {
	   		   	callBackMethodOnOK(JSON.parse(this.responseText));	
	   		 } else {
	   		   	if(callBackMethodOnFail) {
	   		   		callBackMethodOnFail(JSON.parse('{"status":"'+this.status+'"}')    );
	   		   	}      	
	   		 }
	   	} 
   	};
   	return xhttp;
};

