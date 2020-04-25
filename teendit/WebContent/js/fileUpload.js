(function() {
    function init() {
        checkFileUploadingToken();
    }

    function checkFileUploadingToken(){
    	const queryString = window.location.search;
    	const urlParams = new URLSearchParams(queryString);
    	const token = urlParams.get('token')
    	if (token === null) {
    		alert("Invalid token");
    	}
    }
   
    init();
})();