(function() {
	var valid = false;
	var token = null;
    function init() {
        checkFileUploadingToken();
        document.querySelector('#file-upload-btn').addEventListener('click', upload);

    }

    function checkFileUploadingToken(){
    	const queryString = window.location.search;
    	const urlParams = new URLSearchParams(queryString);
    	token = urlParams.get('token');
    	if (token === null) {
    		alert("Invalid token");
    	} else{
    		valid = true;
    	}
    }
    
    function upload(){
    	if(valid ==false){
    		alert("Invalid token!");
    	} else{
    		var file = document.getElementById('fileInput').files[0];
    		if(file!=null){
    			var extension = file.name.split('.').pop();
    			var reader = new FileReader();
    		    reader.readAsDataURL(file);
    		    reader.onload = function (e) { 
    		    	fileString = this.result.split(',')[1];  	
    		    	
    		    	var url = './upload';
    		        var req = JSON.stringify({
    		        	token: token,
    		        	base64_file: fileString,
    		        	password:"111",
    		        	format:extension,
    		        	time_to_view: 77
    		        });

    		        console.log(req);
    		        fetch(url, {
    		                method: 'POST',
    		                body: req
    		            })
    		            .then(res => {
    		                return res.json()
    		            }).then(res => {
    		            	if(res.status === 'OK'){
    		            		alert("Uploaded!");
    		            	} else{
    		            		alert("Something is wrong!");
    		            	}
    		            })   		    	
    			}   		    
    		} else{
    			alert("Choose a file!");
    		}
    	}
    }   
   
    init();
})();