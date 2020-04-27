// the script used for the parent account initialization page
(function() {
    var valid = false;
    var token = null;

    function init() {
    	//get the token
        checkFileUploadingToken();
        document.querySelector('#submit').addEventListener('click', submit);

    }

    function checkFileUploadingToken() {
    	//get token from the link
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        token = urlParams.get('token');
        if (token === null) {
            alert("Invalid token");
        } else {
            valid = true;
        }
    }

    function submit() {
        if (valid == false) {
            alert("Invalid token!");
        } else {
        	//get the new passwords
            var password1 = document.querySelector('#password1').value;
            var password2 = document.querySelector('#password2').value;
            document.getElementById('password1').value = "";
            document.getElementById('password2').value = "";
            //get the time limit
            var minutes = document.querySelector('#timepicker').value;
            if (password1 == "") {
                alert("Password should not be empty!");
            } else if (password1 != password2) {
                alert("Password not match!");
            } else {
            	//get file
                var file = document.getElementById('fileInput').files[0];
                if (file != null) {
                    var extension = file.name.split('.').pop();
                    var reader = new FileReader();
                    reader.readAsDataURL(file);
                    reader.onload = function(e) {
                        fileString = this.result.split(',')[1];
                        var url = './upload';
                        var req = JSON.stringify({
                            token: token,
                            base64_file: fileString,
                            password: md5(password1),
                            format: extension,
                            time_to_view: minutes
                        });
                        console.log(req);
                        //file uploading
                        fetch(url, {
                                method: 'POST',
                                body: req
                            })
                            .then(res => {
                                return res.json()
                            }).then(res => {
                                if (res.status === 'OK') {
                                    alert("Uploaded!");
                                } else {
                                    alert("Something is wrong!");
                                }
                            })
                    }
                } else {
                    alert("Choose a file!");
                }
            }
        }
    }

    init();
})();