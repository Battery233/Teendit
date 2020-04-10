(function() {
	var currentUser;
    /**
	 * Initialize major event handlers
	 */
    function init() {
        document.querySelector('#login-btn').addEventListener('click', login);
        document.querySelector('#new-post').addEventListener('click', newpost);
        document.querySelector('#new-post-btn').addEventListener('click', sendNewPost);
        document.querySelector('#return-btn').addEventListener('click', validateSession);
        validateSession();
    }

    function validateSession() {
        onSessionInvalid();
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
                    onSessionValid(res);
                }
            }).catch(function() {})
    }

    function onSessionValid(res) {
        currentUser = res.user_id;
        var loginContent = document.querySelector('#login-content');
        var globalContent = document.querySelector('#globalstream-content');
        var newpostContent = document.querySelector('#newpost-content');
        hideElement(loginContent);
        hideElement(newpostContent);
        showElement(globalContent);
        loadItems();
    }

    function onSessionInvalid() {
        var loginContent = document.querySelector('#login-content');
        var globalContent = document.querySelector('#globalstream-content');
        var newpostContent = document.querySelector('#newpost-content');
        hideElement(globalContent);
        hideElement(newpostContent);
        showElement(loginContent);
    }

    function hideElement(element) {
        element.style.display = 'none';
    }

    function showElement(element, style) {
        var displayStyle = style ? style : 'block';
        element.style.display = displayStyle;
    }
    
    function newpost(){
    	hideElement(document.querySelector('#login-content'));
    	hideElement(document.querySelector('#globalstream-content'));
    	showElement(document.querySelector('#newpost-content'));
    }
    
    function sendNewPost(){
    	var head = document.querySelector('#txtnewpost-headline').value;
        var content = document.querySelector('#txtnewpost-content').value;
        if(head==''||content==''){
        	hideElement(document.querySelector('#login-content'));
        	hideElement(document.querySelector('#globalstream-content'));
        	showElement(document.querySelector('#newpost-content'));
        	alert("write something!");
        } else{
        	var url = './main';
            var req = JSON.stringify({
                user_id: currentUser,
                name: head,
                content: content,
            });

            console.log(req);
            fetch(url, {
                    method: 'POST',
                    body: req
                }).then(res => {
                    console.log(res);
                })                
         }
    }

    function login() {
        var username = document.querySelector('#txtUserName').value;
        var password = document.querySelector('#txtPassword').value;
        // password = md5(username + md5(password));

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
                if (res.status === 401) {
                    console.log(res);
                    console.log("RETURN VALUE NOT 200!");
                    throw new Error("not 200");
                } else {
                    console.log(res);
                    console.log("Status 200!");
                }
                return res.json()
            })
            .then(res => {
                if (res.status === 'OK') {
                    onSessionValid(res);
                }
            })
            .catch(err => {
                alert("Login error!");
                console.log("not 200 error!");
            })
    }

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
                // record the item and print to the webpage
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
        var s = "<div id=\"post-%s\" class=\"news-list-item clearfix\"style=\"padding-bottom: 20px; border-bottom: 1px solid #eee\"><div class\=\"row\"><div class=\"col-xs-8\"><div><a href=\"#\" class=\"title\"style=\"display: block; color: #444; font-size: 18px; font-weight: bold; margin-bottom: 5px; line-height: 1.5\">%s</a></div><p>%s</p><div class=\"info\"><a>%s</a><span>%s</span></div></div></div></div>"
        s = s.format(item.item_id, "Post" + item.item_id + "Title", item.content, item.user_id," time");

        var child = document.createElement('div');
        child.innerHTML = s;
        itemList.appendChild(child);
        var child2 = document.createElement('div');
        child2.innerHTML = "<h4>Comments</h4>";
        itemList.appendChild(child2);
        if (item.hasOwnProperty('comments')) {
            for (var i = 0; i < item.comments.length; i++) {
                var child3 = document.createElement('div');
                var s2 = "<li id='comment_%s'><blockquote class='blockquote'><h6 class='mb-0'>%s </h6><footer class='blockquote-footer'>by <a href='#'>%s</a>&nbsp·&nbsp<span>%s</span></footer></blockquote></li>";
                s2 = s2.format(item.comments[i].comment_id, item.comments[i].content, item.comments[i].user_id," time");
                child3.innerHTML = s2;
                itemList.appendChild(child3);
            }
        }


        var s4 = " <div class=\"row\"><div class=\"col-xs-10\"><input type=\"text\" class=\"form-control\" name=\"comment_content\"id=\"comment_input\" placeholder=\"Please comment here\" required></div><div class=\"col-xs-1\"><button type=\"submit\" class=\"btn btn-primary\">Comment</button></div><div class=\"col-xs-1\"><button type=\"submit\" class=\"btn btn-warning\">BullyReport</button></div><br><br><br><br>";
        var child4 = document.createElement('div');
        child4.innerHTML = s4;
        itemList.appendChild(child4);
    }

    String.prototype.format = function() {
        var args = Array.prototype.slice.call(arguments);
        var count = 0;
        return this.replace(/%s/g, function(s, i) {
            return args[count++];
        });
    }
    init();
})();