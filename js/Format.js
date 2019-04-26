function Format(){

}


Format.prototype.textNotEmpty = function(text) {
	return text && text.length > 0;
};


Format.prototype.textCastIntOK = function(text) {
	return parseInt(text);
};

Format.prototype.compare = function(text1,text2) {
	if(text1 && text2) return text1.toUpperCase() == text2.toUpperCase();
	else return (!text1 && !text2)
};
