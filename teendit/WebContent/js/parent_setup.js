(function() {
    var valid = false;
    var token = null;

    function init() {
        checkFileUploadingToken();
        document.querySelector('#submit').addEventListener('click', submit);

    }

    function checkFileUploadingToken() {
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
            var password1 = document.querySelector('#password1').value;
            var password2 = document.querySelector('#password2').value;
            document.getElementById('password1').value = "";
            document.getElementById('password2').value = "";
            var minutes = document.querySelector('#timepicker').value;
            if (password1 == "") {
                alert("Password should not be empty!");
            } else if (password1 != password2) {
                alert("Password not match!");
            } else {
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
                        alert(fileString);
                        console.log(req);
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