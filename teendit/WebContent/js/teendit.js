(function() {
    //id for the current user
    var currentUser;
    var secondsLeft;
    var secondsPast = 0;
    var timer;

    //call the init function when the page is loaded
    init();
    /**
     * Initialize major event handlers
     */
    function init() {
        //add button listeners
        document.querySelector('#login-btn').addEventListener('click', login);
        document.querySelector('#parent-login-btn').addEventListener('click', parentLogin);
        document.querySelector('#new-post').addEventListener('click', showPostingPage);
        document.querySelector('#new-post-btn').addEventListener('click', sendNewPost);
        document.querySelector('#return-btn').addEventListener('click', validateSession);
        document.querySelector('#return-btn-signup').addEventListener('click', validateSession);
        document.querySelector('#logout-btn').addEventListener('click', logout);
        document.querySelector('#register-form-btn').addEventListener('click', showRegisteringPage);
        document.querySelector('#btn-signup').addEventListener('click', register);
        document.querySelector('#data-notice-btn').addEventListener('click', policy);
        //check the login status and show the components needs to be shown.
        validateSession();
    }

    function validateSession() {
        showLoginPage();
        // The request parameters
        var url = './login';
        var req = JSON.stringify({});
        fetch(url, {
                method: 'GET'
            })
            .then(res => {
                return res.json();
            })
            .then(res => {
                if (res.status === 'OK') {
                    //status = ok means logged in
                    onSessionValid(res);
                }
            }).catch(function() {})
    }

    function onSessionValid(res) {
        //setup the page for the logged in user
        currentUser = res.user_id;
        //show the global stream posts only
        showGlobalPage();
        loadItems();
    }

    function showGlobalPage() {
        hideElement(document.querySelector('#login-content'));
        hideElement(document.querySelector('#newpost-content'));
        hideElement(document.querySelector('#signup-content'));
        showElement(document.querySelector('#logout-btn'));
        showElement(document.querySelector('#globalstream-content'));
        hideElement(document.querySelector('#upload-file-content'));

    }

    function showLoginPage() {
        //if the session is invalid, go to the login page
        hideElement(document.querySelector('#signup-content'));
        hideElement(document.querySelector('#globalstream-content'));
        hideElement(document.querySelector('#newpost-content'));
        hideElement(document.querySelector('#logout-btn'));
        showElement(document.querySelector('#login-content'));
        hideElement(document.querySelector('#upload-file-content'));
    }

    function showPostingPage() {
        //set up the page to new post editing page
        hideElement(document.querySelector('#signup-content'));
        hideElement(document.querySelector('#login-content'));
        hideElement(document.querySelector('#globalstream-content'));
        showElement(document.querySelector('#logout-btn'));
        showElement(document.querySelector('#newpost-content'));
        hideElement(document.querySelector('#upload-file-content'));
    }

    function showRegisteringPage() {
        //set up the page to new post editing page
        showElement(document.querySelector('#signup-content'));
        hideElement(document.querySelector('#login-content'));
        hideElement(document.querySelector('#globalstream-content'));
        hideElement(document.querySelector('#logout-btn'));
        hideElement(document.querySelector('#newpost-content'));
        hideElement(document.querySelector('#upload-file-content'));
    }

    //tool functions for hide and show elements in the page
    function hideElement(element) {
        element.style.display = 'none';
    }

    function showElement(element, style) {
        var displayStyle = style ? style : 'block';
        element.style.display = displayStyle;
    }

    // show the policy information
    function policy() {
        alert("For teenage users, by creating this account, we will store users’ ID (user name), password, birthday, browsing time and email address. Your user name, password, and birthday is used to maintain your account, post history, and posts comments. All the information would be securely encrypted and used only for sending account reset information. The birthday data would only be used to determine whether you are the teenage group. For teenage parents, we will store parents’ correlation with their teenage children just to maintain the account. We will temporarily store the ID documents for manually ID verification and will delete them immediately after the verification process. We understand that ID documents are extremely important, we would encrypt them while we are storing them and transferring them. Not all the employees in Teendit can open the ID documents in the database, private keys would be needed. Your data will not be shared with any third party or used for any other purposes that are not directly related to Teendit.");
    }


    function sendNewPost() {
        //read the content of the post inputs
        var head = document.querySelector('#txtnewpost-headline').value;
        var content = document.querySelector('#txtnewpost-content').value;
        if (head == '' || content == '') {
            //make sure input is valid
            showPostingPage();
            alert("write something!");
        } else {
            //send post
            var url = './main';
            var req = JSON.stringify({
                user_id: currentUser,
                name: head,
                content: content,
                category: "default",
            });
            console.log(req);
            fetch(url, {
                method: 'POST',
                body: req
            })
            validateSession();
        }
    }

    function login() {
        //get the user name & password and hash the content
        var username = document.querySelector('#txtUserName').value;
        var password = document.querySelector('#txtPassword').value;
        password = md5(username + md5(password));
        // The request parameters
        var url = './login';
        var req = JSON.stringify({
            user_id: username,
            password: password,
        });

        console.log(req);
        fetch(url, {
                method: 'POST',
                body: req
            })
            .then(res => {
                //status = 401 if the password does not match
                if (res.status === 401) {
                    console.log(res);
                    console.log("RETURN VALUE NOT 200!");
                    throw new Error("not 200");
                }
                return res.json()
            })
            .then(res => {
                if (res.status === 'OK') {
                    secondsLeft = 60 * (res.time - res.time_viewed);
                    alert("You have " + (res.time - res.time_viewed) + " minutes left today!\nRemember to use logout button, else your timer won't stop!");
                    //login successfully

                    timer = setInterval(function() {
                        secondsLeft--;
                        secondsPast++;
                        s = "<h4>%s minutes</h><h4>%s seconds</h4><h4>left today!</h4>";
                        s = s.format(parseInt(secondsLeft / 60), secondsLeft % 60);
                        var timerDiv = document.getElementById('timer');
                        timerDiv.innerHTML = s;
                        if (secondsLeft == 0) {
                            alert("You have run out of today's time budget!");
                            logout();
                        } else if (secondsLeft == 60) {
                            alert("You have one minute left!");
                        }
                    }, 100);

                    onSessionValid(res);
                }
            })
            .catch(err => {
                alert("Login error! Wrong email/password or account not activated!");
            })
        //clear the input boxes
        document.getElementById('txtUserName').value = "";
        document.getElementById('txtPassword').value = "";
    }
    
    function parentLogin(){
    	//get the user name & password and hash the content
        var username = document.querySelector('#txtUserName').value;
        var password = document.querySelector('#txtPassword').value;
//        password = md5(username + md5(password));
        // The request parameters
        var url = './login';
        var req = JSON.stringify({
        	parent_email: username,
            password: password,
        });

        console.log(req);
        fetch(url, {
                method: 'POST',
                body: req
            })
            .then(res => {
                //status = 401 if the password does not match
                if (res.status === 401) {
                    console.log(res);
                    console.log("RETURN VALUE NOT 200!");
                    throw new Error("not 200");
                }
                return res.json()
            })
            .then(res => {
                if (res.status === 'OK') {
                   alert("Parent in!");
                }
            })
            .catch(err => {
                alert("Login error! Wrong email/password or account not activated!");
            })
        //clear the input boxes
        document.getElementById('txtUserName').value = "";
        document.getElementById('txtPassword').value = "";
    }
    
    function sar(){
    	var url = './sar';
        fetch(url, {
                method: 'GET',
            }).then(res => {
                console.log(res);
                if (res.status === 200) {
                    alert("SAR get successfully!");
                }
                return res.json();
            }).then(res => {
            	    var a = document.createElement("a");
            	    var file = new Blob([JSON.stringify(res)], {type: 'text/plain'});
            	    a.href = URL.createObjectURL(file);
            	    a.download = "SAR.txt";
            	    a.click();
            })
            .catch(err => {
                console.error(err);
            })
    }

    function logout() {
        //logout and get the index page from server
        var url = './logout';
        clearInterval(timer);
        minutes = Math.ceil(secondsPast / 60);
        var req = JSON.stringify({
            time_viewed: minutes
        });
        fetch(url, {
            method: 'POST',
            body: req
        }).then(res => {
            if (res.status === 200) {
                validateSession();
            }
        })
    }

    function register() {
        //get the user name & password and hash the content
        var username = document.querySelector('#txtUserName-su').value;
        var email = document.querySelector('#txtEmail-u').value;
        var parentemail = document.querySelector('#txtEmail-p').value;
        var password = document.querySelector('#txtRepeatPass').value;
        document.querySelector('#btn-signup').disabled = true;
        alert("Sending request! This might take up to one minute!");

        password = md5(username + md5(password));
        // The request parameters
        var url = './register';
        var req = JSON.stringify({
            user_id: username,
            password: password,
            email: email,
            parent_email: parentemail
        });
        console.log(req);
        fetch(url, {
                method: 'POST',
                body: req
            })
            .then(res => {
                return res.json();
            })
            .then(res => {
                if (res.status === 'User or Parent Already Exists') {
                    alert("Account already exist!");
                    document.querySelector('#btn-signup').disabled = false;
                } else if (res.status === 'OK') {
                    alert("Signup request sent!");
                }
            })
            .catch(err => {
                alert("signup error!");
            })
        //clear the input boxes
        document.getElementById('txtRepeatPass').value = "";
    }
    
    function removeAccount(){  	
    	var url = './deleteaccount';
        fetch(url, {
                method: 'GET',
            }).then(res => {
                console.log(res);
                if (res.status === 200) {
                    alert("Successfully!");
                    validateSession();
                }
                return res.json();
            })
            .catch(err => {
                console.error(err);
            })
    }

    //function for list all the posts in the global stream
    function loadItems() {
        var url = './main';
        var data = null;
        // get the JSON array from the server.
        fetch(url, {
                method: 'GET',
            })
            .then(res => {
                return res.json();
            })
            .then(items => {
                console.log(items);
                // record the item and print to the web page
                if (!items || items.length === 0) {} else {
                    listItems(items);
                }
            })
            .catch(err => {
                console.error(err); // print error
            })
    }

    /**
     * render all the items on the web page.
     */
    function listItems(items) {
        //get the area for posts
        var itemList = document.querySelector('#item-list');
        itemList.innerHTML = ''; // clear current results

        for (var i = 0; i < items.length; i++) {
            addItem(itemList, items[i]); // List all the content
        }
    }

    /**
     * render one item on the web page.
     */
    function addItem(itemList, item) {

        //add the post content by adding html elements
        var s = "<div id=\"post-%s\" class=\"news-list-item clearfix\"style=\"padding-bottom: 20px; border-bottom: 1px solid #eee\"><div class\=\"row\"><div class=\"col-xs-8\"><div><a href=\"#\" class=\"title\"style=\"display: block; color: #444; font-size: 18px; font-weight: bold; margin-bottom: 5px; line-height: 1.5\">%s</a></div><p>%s</p><div class=\"info\"><a>Author: %s</a><span>%s</span></div></div></div></div>"
        s = s.format(item.item_id, item.name, item.content, item.user_id, "");
        var child = document.createElement('div');
        child.innerHTML = s;
        itemList.appendChild(child);

        //add the comment bar
        var child2 = document.createElement('div');
        child2.innerHTML = "<h4>Comments</h4>";
        itemList.appendChild(child2);

        //add comments for the post, if exist
        if (item.hasOwnProperty('comments')) {
            for (var i = 0; i < item.comments.length; i++) {
                var child3 = document.createElement('div');
                var s2 = "<li id='comment_%s'><blockquote class='blockquote'><h6 class='mb-0'>%s </h6><footer class='blockquote-footer'>by <a href='#'>%s</a>&nbsp·&nbsp<span>%s</span></footer></blockquote></li>";
                s2 = s2.format(item.comments[i].comment_id, item.comments[i].content, item.comments[i].user_id, "");
                child3.innerHTML = s2;
                itemList.appendChild(child3);
            }
        }

        //add the comment input boxes for this post
        var s4 = " <div class=\"row\"><div class=\"col-xs-10\"><input type=\"text\" class=\"form-control\" name=\"comment_content\"id=\"comment-input-%s\" placeholder=\"Please comment here\" required></div><div class=\"col-xs-1\"><button id=\"post-comment-btn-%s\" type=\"submit\" class=\"btn btn-primary\">Comment</button></div><div class=\"col-xs-1\"></div><br><br><br><br>";
        var child4 = document.createElement('div');
        s4 = s4.format(item.item_id, item.item_id);
        child4.innerHTML = s4;
        itemList.appendChild(child4);
        //set unique id for the comment button according to the post button
        var comment_txt = "#comment-input-" + item.item_id;
        var comment_btn = "#post-comment-btn-%s";
        comment_btn = comment_btn.format(item.item_id);
        //add listener for posting comments
        document.querySelector(comment_btn).addEventListener('click', function() {
            console.log(comment_txt);
            console.log(item.item_id);
            var comment = document.querySelector(comment_txt).value;
            if (comment === '') {
                //do nothing if no text inputed
            } else {
                //post the commnet to the server
                var url = './main';
                var req = JSON.stringify({
                    user_id: currentUser,
                    item_id: item.item_id,
                    content: comment,
                });

                console.log(req);
                fetch(url, {
                        method: 'POST',
                        body: req
                    }).then(res => {
                        console.log(res);
                        if (res.status === 200) {
                            alert("Comment successfully!");
                            //validate session and refresh the page
                            validateSession();
                        }
                        return res.json();
                    })
                    .catch(err => {
                        console.error(err); // print error
                    })
            }
        });
    }


    //tool function for string formatting
    String.prototype.format = function() {
        var args = Array.prototype.slice.call(arguments);
        var count = 0;
        return this.replace(/%s/g, function(s, i) {
            return args[count++];
        });
    }
})();